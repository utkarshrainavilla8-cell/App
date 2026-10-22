package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HoneycombRepository
import com.example.model.OrderStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Honeycomb", appName)
  }

  @Test
  fun `repository places order and triggers admin notification`() {
    val initialNotificationsCount = HoneycombRepository.notifications.value.size
    val product = HoneycombRepository.products.value.first()

    HoneycombRepository.addToCart(product, 2)
    val order = HoneycombRepository.placeOrder(
      customerName = "Anna Joy",
      customerEmail = "anna@example.com",
      customerAddress = "123 Honey Lane",
      paymentMethod = "Credit Card",
      discountAmount = 0.0
    )

    assertNotNull(order)
    assertEquals(OrderStatus.PENDING, order?.status)
    assertEquals(initialNotificationsCount + 1, HoneycombRepository.notifications.value.size)
  }

  @Test
  fun `admin can update product price and stock status`() {
    val product = HoneycombRepository.products.value.first()
    val testPrice = 1299.00

    HoneycombRepository.updateProductPrice(product.id, testPrice)
    val updated = HoneycombRepository.products.value.find { it.id == product.id }
    assertEquals(testPrice, updated?.price ?: 0.0, 0.01)

    HoneycombRepository.toggleOutOfStock(product.id, true)
    val outOfStockProduct = HoneycombRepository.products.value.find { it.id == product.id }
    assertTrue(outOfStockProduct?.isOutOfStock == true)
  }
}
