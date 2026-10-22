package com.example.ui.customer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HoneycombRepository
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.components.OrderStatusBadge

@Composable
fun OrderTrackingScreen(
  orders: List<Order>,
  onBackClicked: () -> Unit,
  onCancelOrder: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBackClicked() }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFFFFBF5))
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBackClicked, modifier = Modifier.testTag("orders_back_button")) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF381E0D))
      }
      Text(
        text = "Your Handcrafted Orders ✨",
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF381E0D)
      )
    }

    if (orders.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = null,
            tint = Color(0xFFD6C7BC),
            modifier = Modifier.size(64.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No orders found yet",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF381E0D)
          )
          Text(
            text = "When you purchase artisanal crafts, track their progress here! 💛",
            fontSize = 13.sp,
            color = Color(0xFF78716C)
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(orders, key = { it.id }) { order ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("order_card_${order.id}"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              // Order Top Bar
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "Order #${order.id} ✨",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF26140A)
                  )
                  Text(
                    text = order.formattedDate,
                    fontSize = 12.sp,
                    color = Color(0xFF78716C)
                  )
                }

                OrderStatusBadge(status = order.status)
              }

              Spacer(modifier = Modifier.height(16.dp))

              // Step Progress Tracker
              if (order.status != OrderStatus.CANCELLED) {
                CuteOrderTimelineTracker(currentStatus = order.status)
              } else {
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = Color(0xFFFEE2E2),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "This craft order was cancelled and refunded.",
                    color = Color(0xFFDC2626),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(10.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))
              HorizontalDivider(color = Color(0xFFF0E3D3))
              Spacer(modifier = Modifier.height(12.dp))

              // Items breakdown in Indian Rupees (₹)
              Text(
                text = "Crafted Items:",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF381E0D)
              )
              Spacer(modifier = Modifier.height(6.dp))

              order.items.forEach { item ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = "${item.quantity}x ${item.productTitle}",
                    fontSize = 13.sp,
                    color = Color(0xFF57483F),
                    modifier = Modifier.weight(1f)
                  )
                  Text(
                    text = HoneycombRepository.formatInr(item.unitPrice * item.quantity),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF26140A)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Total & Delivery info
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "Delivery to:",
                    fontSize = 11.sp,
                    color = Color(0xFF78716C)
                  )
                  Text(
                    text = order.customerAddress,
                    fontSize = 12.sp,
                    color = Color(0xFF381E0D),
                    fontWeight = FontWeight.Medium
                  )
                }

                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = "Total Paid",
                    fontSize = 11.sp,
                    color = Color(0xFF78716C)
                  )
                  Text(
                    text = HoneycombRepository.formatInr(order.total),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFFD97706)
                  )
                }
              }

              // Cancel button if still pending or crafting
              if (order.status == OrderStatus.PENDING || order.status == OrderStatus.CRAFTING) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                  onClick = { onCancelOrder(order.id) },
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("cancel_order_button_${order.id}"),
                  colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
                ) {
                  Text("Cancel Order", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

@Composable
private fun CuteOrderTimelineTracker(currentStatus: OrderStatus) {
  val steps = listOf(
    OrderStatus.PENDING to "Received 🍯",
    OrderStatus.CRAFTING to "Crafting 🧶",
    OrderStatus.SHIPPED to "Shipped 🚚",
    OrderStatus.DELIVERED to "Delivered 🌸"
  )

  val currentIndex = when (currentStatus) {
    OrderStatus.PENDING -> 0
    OrderStatus.CRAFTING -> 1
    OrderStatus.SHIPPED -> 2
    OrderStatus.DELIVERED -> 3
    OrderStatus.CANCELLED -> -1
  }

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    steps.forEachIndexed { index, (status, label) ->
      val isCompleted = index <= currentIndex
      val isCurrent = index == currentIndex

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(
              when {
                isCurrent -> Color(0xFFD97706)
                isCompleted -> Color(0xFF15803D)
                else -> Color(0xFFE5E7EB)
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          if (isCompleted && !isCurrent) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
          } else {
            Text(
              text = "${index + 1}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (isCompleted) Color.White else Color(0xFF9CA3AF)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = label,
          fontSize = 10.sp,
          fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
          color = when {
            isCurrent -> Color(0xFFD97706)
            isCompleted -> Color(0xFF15803D)
            else -> Color(0xFF9CA3AF)
          }
        )
      }
    }
  }
}
