package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.HoneycombRepository
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.ui.admin.AddEditProductDialog
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.admin.AdminLoginDialog
import com.example.ui.admin.AdminNotificationsDialog
import com.example.ui.admin.QuickChangePriceDialog
import com.example.ui.components.HoneycombHeader
import com.example.ui.customer.CartScreen
import com.example.ui.customer.CustomerCatalogScreen
import com.example.ui.customer.OrderTrackingScreen
import com.example.ui.customer.ProductDetailScreen
import com.example.ui.theme.HoneyGoldPrimary
import kotlinx.coroutines.launch

enum class CustomerScreen {
  CATALOG,
  PRODUCT_DETAIL,
  CART,
  MY_ORDERS
}

@Composable
fun HoneycombMainScreen() {
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  // Reactive Data Flows
  val products by HoneycombRepository.products.collectAsStateWithLifecycle()
  val reviews by HoneycombRepository.reviews.collectAsStateWithLifecycle()
  val orders by HoneycombRepository.orders.collectAsStateWithLifecycle()
  val cart by HoneycombRepository.cart.collectAsStateWithLifecycle()
  val wishlist by HoneycombRepository.wishlist.collectAsStateWithLifecycle()
  val notifications by HoneycombRepository.notifications.collectAsStateWithLifecycle()

  // App Navigation States
  var isAdminMode by remember { mutableStateOf(false) }
  var customerScreen by remember { mutableStateOf(CustomerScreen.CATALOG) }
  var selectedProduct by remember { mutableStateOf<Product?>(null) }

  // Modals / Dialogs
  var showLoginDialog by remember { mutableStateOf(false) }
  var showNotificationsDialog by remember { mutableStateOf(false) }
  var showAddEditProductDialog by remember { mutableStateOf(false) }
  var productToEdit by remember { mutableStateOf<Product?>(null) }
  var productForPriceChange by remember { mutableStateOf<Product?>(null) }

  // Login Dialog
  if (showLoginDialog) {
    AdminLoginDialog(
      onDismiss = { showLoginDialog = false },
      onLoginSuccess = {
        showLoginDialog = false
        isAdminMode = true
        scope.launch {
          snackbarHostState.showSnackbar("Welcome to Admin Workshop Portal! 🐝")
        }
      }
    )
  }

  // Admin Notifications Dialog
  if (showNotificationsDialog) {
    AdminNotificationsDialog(
      notifications = notifications,
      onDismiss = { showNotificationsDialog = false },
      onNotificationClicked = { notif ->
        showNotificationsDialog = false
        isAdminMode = true
        HoneycombRepository.markNotificationAsRead(notif.id)
      },
      onMarkAllRead = {
        HoneycombRepository.markAllNotificationsAsRead()
      }
    )
  }

  // Add / Edit Product Dialog
  if (showAddEditProductDialog) {
    AddEditProductDialog(
      productToEdit = productToEdit,
      onDismiss = {
        showAddEditProductDialog = false
        productToEdit = null
      },
      onSaveProduct = { saved ->
        if (productToEdit != null) {
          HoneycombRepository.updateProduct(saved)
          scope.launch { snackbarHostState.showSnackbar("Updated ${saved.title}!") }
        } else {
          HoneycombRepository.addProduct(saved)
          scope.launch { snackbarHostState.showSnackbar("Added ${saved.title} to store! ✨") }
        }
        showAddEditProductDialog = false
        productToEdit = null
      }
    )
  }

  // Quick Change Price Dialog
  if (productForPriceChange != null) {
    QuickChangePriceDialog(
      product = productForPriceChange!!,
      onDismiss = { productForPriceChange = null },
      onConfirmPrice = { newPrice ->
        HoneycombRepository.updateProductPrice(productForPriceChange!!.id, newPrice)
        scope.launch {
          snackbarHostState.showSnackbar("Updated price of ${productForPriceChange!!.title} to ${HoneycombRepository.formatInr(newPrice)} ✨")
        }
        productForPriceChange = null
      }
    )
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      HoneycombHeader(
        isAdminMode = isAdminMode,
        cartItemCount = cart.sumOf { it.quantity },
        unreadNotificationCount = notifications.count { !it.isRead },
        onToggleRole = {
          if (isAdminMode) {
            isAdminMode = false
          } else {
            showLoginDialog = true
          }
        },
        onCartClicked = {
          customerScreen = CustomerScreen.CART
        },
        onNotificationsClicked = {
          showNotificationsDialog = true
        }
      )
    },
    bottomBar = {
      if (!isAdminMode) {
        NavigationBar(
          containerColor = Color.White,
          tonalElevation = 6.dp,
          modifier = Modifier.navigationBarsPadding()
        ) {
          NavigationBarItem(
            selected = customerScreen == CustomerScreen.CATALOG,
            onClick = { customerScreen = CustomerScreen.CATALOG },
            icon = {
              Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = "Shop",
                tint = if (customerScreen == CustomerScreen.CATALOG) HoneyGoldPrimary else Color(0xFF78716C)
              )
            },
            label = {
              Text(
                text = "Crafts",
                fontWeight = if (customerScreen == CustomerScreen.CATALOG) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = HoneyGoldPrimary,
              selectedTextColor = HoneyGoldPrimary,
              indicatorColor = Color(0xFFFEF3C7)
            ),
            modifier = Modifier.testTag("nav_crafts")
          )

          NavigationBarItem(
            selected = customerScreen == CustomerScreen.CART,
            onClick = { customerScreen = CustomerScreen.CART },
            icon = {
              Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = "Basket",
                tint = if (customerScreen == CustomerScreen.CART) HoneyGoldPrimary else Color(0xFF78716C)
              )
            },
            label = {
              Text(
                text = "Basket (${cart.sumOf { it.quantity }})",
                fontWeight = if (customerScreen == CustomerScreen.CART) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = HoneyGoldPrimary,
              selectedTextColor = HoneyGoldPrimary,
              indicatorColor = Color(0xFFFEF3C7)
            ),
            modifier = Modifier.testTag("nav_cart")
          )

          NavigationBarItem(
            selected = customerScreen == CustomerScreen.MY_ORDERS,
            onClick = { customerScreen = CustomerScreen.MY_ORDERS },
            icon = {
              Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = "Orders",
                tint = if (customerScreen == CustomerScreen.MY_ORDERS) HoneyGoldPrimary else Color(0xFF78716C)
              )
            },
            label = {
              Text(
                text = "Orders (${orders.size})",
                fontWeight = if (customerScreen == CustomerScreen.MY_ORDERS) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = HoneyGoldPrimary,
              selectedTextColor = HoneyGoldPrimary,
              indicatorColor = Color(0xFFFEF3C7)
            ),
            modifier = Modifier.testTag("nav_orders")
          )

          NavigationBarItem(
            selected = false,
            onClick = { showLoginDialog = true },
            icon = {
              Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = "Admin Portal",
                tint = Color(0xFF78716C)
              )
            },
            label = {
              Text(
                text = "Admin",
                fontSize = 11.sp
              )
            },
            colors = NavigationBarItemDefaults.colors(
              indicatorColor = Color(0xFFFEF3C7)
            ),
            modifier = Modifier.testTag("nav_admin_login")
          )
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      if (isAdminMode) {
        // ADMIN PORTAL SCREEN
        AdminDashboardScreen(
          products = products,
          orders = orders,
          notifications = notifications,
          onAddProduct = {
            productToEdit = null
            showAddEditProductDialog = true
          },
          onEditProduct = { prod ->
            productToEdit = prod
            showAddEditProductDialog = true
          },
          onDeleteProduct = { prodId ->
            HoneycombRepository.deleteProduct(prodId)
            scope.launch { snackbarHostState.showSnackbar("Product removed from catalog") }
          },
          onChangePrice = { prod ->
            productForPriceChange = prod
          },
          onToggleOutOfStock = { prodId, isOut ->
            HoneycombRepository.toggleOutOfStock(prodId, isOut)
            scope.launch {
              val stateLabel = if (isOut) "Marked Out of Stock" else "Marked In Stock"
              snackbarHostState.showSnackbar(stateLabel)
            }
          },
          onUpdateStock = { prodId, newStock ->
            HoneycombRepository.updateProductStock(prodId, newStock)
          },
          onUpdateOrderStatus = { orderId, newStatus ->
            HoneycombRepository.updateOrderStatus(orderId, newStatus)
            scope.launch {
              snackbarHostState.showSnackbar("Order #$orderId updated to ${newStatus.displayName}")
            }
          }
        )
      } else {
        // CUSTOMER SCREENS
        when (customerScreen) {
          CustomerScreen.CATALOG -> {
            CustomerCatalogScreen(
              products = products,
              wishlist = wishlist,
              onProductClicked = { prod ->
                selectedProduct = prod
                customerScreen = CustomerScreen.PRODUCT_DETAIL
              },
              onAddToCartClicked = { prod ->
                HoneycombRepository.addToCart(prod, 1)
                scope.launch {
                  snackbarHostState.showSnackbar("Added ${prod.title} to your basket! 🧶")
                }
              },
              onToggleWishlist = { prod ->
                HoneycombRepository.toggleWishlist(prod.id)
              }
            )
          }

          CustomerScreen.PRODUCT_DETAIL -> {
            val activeProduct = products.find { it.id == selectedProduct?.id } ?: selectedProduct
            if (activeProduct != null) {
              ProductDetailScreen(
                product = activeProduct,
                reviews = reviews,
                isWishlisted = wishlist.contains(activeProduct.id),
                onBackClicked = { customerScreen = CustomerScreen.CATALOG },
                onAddToCartClicked = { prod, qty ->
                  HoneycombRepository.addToCart(prod, qty)
                  scope.launch {
                    snackbarHostState.showSnackbar("Added $qty item(s) to basket!")
                  }
                },
                onToggleWishlist = { prod ->
                  HoneycombRepository.toggleWishlist(prod.id)
                },
                onSubmitReview = { rating, author, title, comment ->
                  HoneycombRepository.addReview(activeProduct.id, author, rating, title, comment)
                  scope.launch {
                    snackbarHostState.showSnackbar("Thank you for reviewing! ⭐")
                  }
                }
              )
            } else {
              customerScreen = CustomerScreen.CATALOG
            }
          }

          CustomerScreen.CART -> {
            CartScreen(
              cartItems = cart,
              onBackClicked = { customerScreen = CustomerScreen.CATALOG },
              onUpdateQuantity = { prodId, qty ->
                HoneycombRepository.updateCartQuantity(prodId, qty)
              },
              onRemoveItem = { prodId ->
                HoneycombRepository.removeFromCart(prodId)
              },
              onPlaceOrder = { name, email, addr, payMethod, discount ->
                val newOrder = HoneycombRepository.placeOrder(
                  customerName = name,
                  customerEmail = email,
                  customerAddress = addr,
                  paymentMethod = payMethod,
                  discountAmount = discount
                )
                newOrder
              },
              onViewOrders = {
                customerScreen = CustomerScreen.MY_ORDERS
              }
            )
          }

          CustomerScreen.MY_ORDERS -> {
            OrderTrackingScreen(
              orders = orders,
              onBackClicked = { customerScreen = CustomerScreen.CATALOG },
              onCancelOrder = { orderId ->
                HoneycombRepository.cancelOrder(orderId)
                scope.launch {
                  snackbarHostState.showSnackbar("Order #$orderId has been cancelled")
                }
              }
            )
          }
        }
      }
    }
  }
}
