package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.model.AdminNotification
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.ui.components.AnalyticsOverview
import com.example.ui.theme.HoneyGoldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
  products: List<Product>,
  orders: List<Order>,
  notifications: List<AdminNotification>,
  onAddProduct: () -> Unit,
  onEditProduct: (Product) -> Unit,
  onDeleteProduct: (String) -> Unit,
  onChangePrice: (Product) -> Unit,
  onToggleOutOfStock: (String, Boolean) -> Unit,
  onUpdateStock: (String, Int) -> Unit,
  onUpdateOrderStatus: (String, OrderStatus) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Sales Analytics", "Manage Products", "Order Status")

  val unreadNotifications = notifications.filter { !it.isRead }
  val latestUnread = unreadNotifications.firstOrNull()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFFFFDF9))
  ) {
    // New Order Alert Banner (if unread incoming order exists!)
    if (latestUnread != null) {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
          .clickable { selectedTab = 2 }
          .testTag("admin_new_order_banner"),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFEF3C7),
        shadowElevation = 2.dp
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.NotificationsActive,
            contentDescription = null,
            tint = Color(0xFFD97706),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = latestUnread.title,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFF78350F)
            )
            Text(
              text = latestUnread.message,
              fontSize = 11.sp,
              color = Color(0xFF92400E)
            )
          }
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFD97706)
          ) {
            Text(
              text = "VIEW",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    // Tabs Navigation
    PrimaryTabRow(
      selectedTabIndex = selectedTab,
      containerColor = Color.White,
      contentColor = HoneyGoldPrimary,
      indicator = {
        TabRowDefaults.PrimaryIndicator(
          modifier = Modifier.tabIndicatorOffset(selectedTab),
          color = HoneyGoldPrimary
        )
      }
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
              fontSize = 13.sp,
              color = if (selectedTab == index) HoneyGoldPrimary else Color(0xFF78716C)
            )
          },
          icon = {
            val icon = when (index) {
              0 -> Icons.Default.Analytics
              1 -> Icons.Default.Inventory2
              else -> Icons.Default.ReceiptLong
            }
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = if (selectedTab == index) HoneyGoldPrimary else Color(0xFF78716C),
              modifier = Modifier.size(18.dp)
            )
          },
          modifier = Modifier.testTag("admin_tab_$index")
        )
      }
    }

    // Tab Content
    when (selectedTab) {
      0 -> {
        // Sales Analytics View
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
        ) {
          AnalyticsOverview(orders = orders, products = products)
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
      1 -> {
        // Manage Products View (Add, change price, toggle out of stock)
        AdminProductManagementScreen(
          products = products,
          onAddProduct = onAddProduct,
          onEditProduct = onEditProduct,
          onDeleteProduct = onDeleteProduct,
          onChangePrice = onChangePrice,
          onToggleOutOfStock = onToggleOutOfStock,
          onUpdateStock = onUpdateStock
        )
      }
      2 -> {
        // Orders & Order Status View
        AdminOrderManagementScreen(
          orders = orders,
          onUpdateOrderStatus = onUpdateOrderStatus
        )
      }
    }
  }
}
