package com.example.ui.admin

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HoneycombRepository
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.components.OrderStatusBadge
import com.example.ui.theme.HoneyGoldPrimary

@Composable
fun AdminOrderManagementScreen(
  orders: List<Order>,
  onUpdateOrderStatus: (String, OrderStatus) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedFilter by remember { mutableStateOf("All") }

  val filterOptions = listOf("All", "Pending", "Crafting", "Shipped", "Delivered")

  val filteredOrders = orders.filter { order ->
    when (selectedFilter) {
      "Pending" -> order.status == OrderStatus.PENDING
      "Crafting" -> order.status == OrderStatus.CRAFTING
      "Shipped" -> order.status == OrderStatus.SHIPPED
      "Delivered" -> order.status == OrderStatus.DELIVERED
      else -> true
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFFFFBF5))
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "Workshop Order Fulfillment ✨",
      fontWeight = FontWeight.Bold,
      fontSize = 18.sp,
      color = Color(0xFF26140A)
    )
    Text(
      text = "Update order statuses to notify customers in real-time",
      fontSize = 12.sp,
      color = Color(0xFF78716C)
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Filter Chips
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(filterOptions, key = { it }) { opt ->
        val isSelected = selectedFilter == opt
        FilterChip(
          selected = isSelected,
          onClick = { selectedFilter = opt },
          label = {
            Text(
              text = opt,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = HoneyGoldPrimary,
            selectedLabelColor = Color.White,
            containerColor = Color.White,
            labelColor = Color(0xFF451A03)
          ),
          shape = RoundedCornerShape(16.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (filteredOrders.isEmpty()) {
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
            modifier = Modifier.size(56.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "No orders in this status",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF57483F)
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(filteredOrders, key = { it.id }) { order ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("admin_order_card_${order.id}"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              // Order ID, Date, and Status Badge
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
                    fontSize = 11.sp,
                    color = Color(0xFF78716C)
                  )
                }

                OrderStatusBadge(status = order.status)
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Customer Info
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFAF5ED),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Text(
                    text = "Customer: ${order.customerName} (${order.customerEmail})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF381E0D)
                  )
                  Text(
                    text = "Shipping: ${order.customerAddress}",
                    fontSize = 11.sp,
                    color = Color(0xFF78716C)
                  )
                  Text(
                    text = "Payment: ${order.paymentMethod}",
                    fontSize = 11.sp,
                    color = Color(0xFF78716C)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Ordered items in Indian Rupees (₹)
              order.items.forEach { item ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = "${item.quantity}x ${item.productTitle}",
                    fontSize = 12.sp,
                    color = Color(0xFF57483F)
                  )
                  Text(
                    text = HoneycombRepository.formatInr(item.unitPrice * item.quantity),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF26140A)
                  )
                }
              }

              Spacer(modifier = Modifier.height(6.dp))
              HorizontalDivider(color = Color(0xFFF0E3D3))
              Spacer(modifier = Modifier.height(6.dp))

              // Total Amount in INR
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Order Total",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFF26140A)
                )
                Text(
                  text = HoneycombRepository.formatInr(order.total),
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = HoneyGoldPrimary
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Status Actions: Update buttons
              Text(
                text = "Update Order Progress:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF451A03)
              )

              Spacer(modifier = Modifier.height(6.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                // Crafting button
                OutlinedButton(
                  onClick = { onUpdateOrderStatus(order.id, OrderStatus.CRAFTING) },
                  enabled = order.status != OrderStatus.CRAFTING,
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.weight(1f).testTag("status_crafting_${order.id}"),
                  colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF1D4ED8)
                  )
                ) {
                  Text("Crafting 🧶", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Shipped button
                OutlinedButton(
                  onClick = { onUpdateOrderStatus(order.id, OrderStatus.SHIPPED) },
                  enabled = order.status != OrderStatus.SHIPPED,
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.weight(1f).testTag("status_shipped_${order.id}"),
                  colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF6B21A8)
                  )
                ) {
                  Text("Shipped 🚚", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Delivered button
                Button(
                  onClick = { onUpdateOrderStatus(order.id, OrderStatus.DELIVERED) },
                  enabled = order.status != OrderStatus.DELIVERED,
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.weight(1f).testTag("status_delivered_${order.id}"),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF15803D),
                    contentColor = Color.White
                  )
                ) {
                  Text("Delivered 🌸", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
