package com.example.model

enum class OrderStatus(val displayName: String, val colorHex: String) {
  PENDING("Order Received", "#D97706"),
  CRAFTING("Crafting with Love", "#2563EB"),
  SHIPPED("Shipped & En Route", "#7C3AED"),
  DELIVERED("Delivered", "#15803D"),
  CANCELLED("Cancelled", "#DC2626")
}

data class OrderItem(
  val productId: String,
  val productTitle: String,
  val quantity: Int,
  val unitPrice: Double,
  val category: String,
  val iconType: String = "BEE"
)

data class Order(
  val id: String,
  val customerName: String,
  val customerEmail: String,
  val customerAddress: String,
  val items: List<OrderItem>,
  val subtotal: Double,
  val discount: Double = 0.0,
  val shipping: Double = 0.0,
  val total: Double,
  val status: OrderStatus = OrderStatus.PENDING,
  val placedAtTimestamp: Long = System.currentTimeMillis(),
  val formattedDate: String,
  val paymentMethod: String = "Credit Card",
  val notes: String = ""
)
