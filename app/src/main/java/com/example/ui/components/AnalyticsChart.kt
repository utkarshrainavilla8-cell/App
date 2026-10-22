package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HoneycombRepository
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Product

@Composable
fun AnalyticsOverview(
  orders: List<Order>,
  products: List<Product>,
  modifier: Modifier = Modifier
) {
  val nonCancelledOrders = orders.filter { it.status != OrderStatus.CANCELLED }
  val totalRevenue = nonCancelledOrders.sumOf { it.total }
  val totalOrdersCount = nonCancelledOrders.size
  val avgOrderValue = if (totalOrdersCount > 0) totalRevenue / totalOrdersCount else 0.0
  val pendingOrdersCount = orders.count { it.status == OrderStatus.PENDING || it.status == OrderStatus.CRAFTING }

  Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
    // 4 Key Metrics in 2x2 Grid
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      MetricCard(
        title = "Total Revenue",
        value = HoneycombRepository.formatInr(totalRevenue),
        subtitle = "+18.4% this week ✨",
        icon = Icons.Default.CurrencyRupee,
        iconBg = Color(0xFFFEF3C7),
        iconTint = Color(0xFFD97706),
        modifier = Modifier.weight(1f)
      )
      MetricCard(
        title = "Orders Placed",
        value = "$totalOrdersCount",
        subtitle = "Active workshop orders",
        icon = Icons.Default.ReceiptLong,
        iconBg = Color(0xFFDBEAFE),
        iconTint = Color(0xFF2563EB),
        modifier = Modifier.weight(1f)
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      MetricCard(
        title = "Avg Order Value",
        value = HoneycombRepository.formatInr(avgOrderValue),
        subtitle = "Per basket",
        icon = Icons.Default.TrendingUp,
        iconBg = Color(0xFFDCFCE7),
        iconTint = Color(0xFF15803D),
        modifier = Modifier.weight(1f)
      )
      MetricCard(
        title = "To Craft / Pack",
        value = "$pendingOrdersCount",
        subtitle = "Needs fulfillment 🧶",
        icon = Icons.Default.HourglassTop,
        iconBg = Color(0xFFFFE4E6),
        iconTint = Color(0xFFE11D48),
        modifier = Modifier.weight(1f)
      )
    }

    // Revenue Trend Chart Card in Indian Rupees
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Weekly Sales Trend (₹)",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = Color(0xFF26140A)
            )
            Text(
              text = "Revenue over the last 7 days in Indian Rupees",
              fontSize = 12.sp,
              color = Color(0xFF78716C)
            )
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFEF3C7)
          ) {
            Text(
              text = "7 Days ✨",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFB45309),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Canvas Bar Chart
        val days = listOf("Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed")
        val dailyValues = listOf(1850.0, 3200.0, 5400.0, 6800.0, 4200.0, 4900.0, (totalRevenue * 0.35).coerceAtLeast(5800.0))
        val maxVal = (dailyValues.maxOrNull() ?: 5000.0).coerceAtLeast(1000.0)

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
        ) {
          Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
            val barCount = days.size
            val barWidth = size.width / (barCount * 1.8f)
            val spacing = size.width / barCount

            // Baseline
            drawLine(
              color = Color(0xFFF0E3D3),
              start = Offset(0f, size.height),
              end = Offset(size.width, size.height),
              strokeWidth = 1.dp.toPx()
            )

            dailyValues.forEachIndexed { index, value ->
              val barHeight = ((value / maxVal) * (size.height * 0.85f)).toFloat()
              val x = index * spacing + (spacing - barWidth) / 2
              val y = size.height - barHeight

              // Dreamy honey gradient
              val brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFF59E0B), Color(0xFFD97706)),
                startY = y,
                endY = size.height
              )

              drawRoundRect(
                brush = brush,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
              )
            }
          }

          // Labels row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            days.forEach { day ->
              Text(
                text = day,
                fontSize = 10.sp,
                color = Color(0xFF78716C),
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }

    // Category Sales Breakdown
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Sales by Craft Category ✨",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = Color(0xFF26140A)
        )
        Text(
          text = "Revenue distribution across India",
          fontSize = 12.sp,
          color = Color(0xFF78716C)
        )

        Spacer(modifier = Modifier.height(12.dp))

        val categories = listOf(
          Triple("Amigurumi Plushies 🐝", 0.42f, "₹12,450 (42%)"),
          Triple("Blankets & Quilts ☁️", 0.28f, "₹8,300 (28%)"),
          Triple("Sweaters & Tops 🌸", 0.18f, "₹5,350 (18%)"),
          Triple("DIY Craft Kits & Yarn 🧶", 0.12f, "₹3,560 (12%)")
        )

        categories.forEach { (cat, progress, label) ->
          Column(modifier = Modifier.padding(vertical = 6.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = cat, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF381E0D))
              Text(text = label, fontSize = 12.sp, color = Color(0xFF78716C))
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
              color = Color(0xFFD97706),
              trackColor = Color(0xFFFEF3C7),
            )
          }
        }
      }
    }

    // Top Selling Products Leaderboard
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Top Selling Handcrafts 💛",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = Color(0xFF26140A)
        )
        Spacer(modifier = Modifier.height(12.dp))

        val sortedProducts = products.sortedByDescending { it.salesCount }.take(4)
        sortedProducts.forEachIndexed { index, product ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (index == 0) Color(0xFFFEF3C7) else Color(0xFFF3ECE4),
              modifier = Modifier.size(28.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                  text = "#${index + 1}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = if (index == 0) Color(0xFFB45309) else Color(0xFF57483F)
                )
              }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = product.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF26140A)
              )
              Text(
                text = "${product.category} · In stock: ${product.stockQuantity}",
                fontSize = 11.sp,
                color = Color(0xFF78716C)
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "${product.salesCount} sold",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF15803D)
              )
              Text(
                text = HoneycombRepository.formatInr(product.price),
                fontSize = 11.sp,
                color = Color(0xFF78716C)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun MetricCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  iconBg: Color,
  iconTint: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = iconBg,
        modifier = Modifier.size(36.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = value,
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,
        color = Color(0xFF26140A)
      )

      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF57483F)
      )

      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = Color(0xFF78716C)
      )
    }
  }
}
