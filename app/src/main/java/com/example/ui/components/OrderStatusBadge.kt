package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderStatus

@Composable
fun OrderStatusBadge(
  status: OrderStatus,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor) = when (status) {
    OrderStatus.PENDING -> Color(0xFFFEF3C7) to Color(0xFFB45309)
    OrderStatus.CRAFTING -> Color(0xFFDBEAFE) to Color(0xFF1D4ED8)
    OrderStatus.SHIPPED -> Color(0xFFF3E8FF) to Color(0xFF6B21A8)
    OrderStatus.DELIVERED -> Color(0xFFDCFCE7) to Color(0xFF15803D)
    OrderStatus.CANCELLED -> Color(0xFFFEE2E2) to Color(0xFFB91C1C)
  }

  Box(
    modifier = modifier
      .background(bgColor, RoundedCornerShape(8.dp))
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Text(
      text = status.displayName,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold
    )
  }
}
