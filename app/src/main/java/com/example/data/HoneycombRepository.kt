package com.example.data

import com.example.model.AdminNotification
import com.example.model.CartItem
import com.example.model.Order
import com.example.model.OrderItem
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object HoneycombRepository {

  private val _products = MutableStateFlow<List<Product>>(initialProducts)
  val products: StateFlow<List<Product>> = _products.asStateFlow()

  private val _reviews = MutableStateFlow<List<Review>>(initialReviews)
  val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

  private val _orders = MutableStateFlow<List<Order>>(initialOrders)
  val orders: StateFlow<List<Order>> = _orders.asStateFlow()

  private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
  val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

  private val _wishlist = MutableStateFlow<Set<String>>(setOf("prod-1", "prod-3"))
  val wishlist: StateFlow<Set<String>> = _wishlist.asStateFlow()

  private val _notifications = MutableStateFlow<List<AdminNotification>>(initialNotifications)
  val notifications: StateFlow<List<AdminNotification>> = _notifications.asStateFlow()

  // Format INR nicely, e.g. ₹849 or ₹3,499
  fun formatInr(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = 0
    return "₹${formatter.format(amount)}"
  }

  // --- Customer Actions ---

  fun addToCart(product: Product, quantity: Int = 1) {
    if (product.isOutOfStock) return
    val current = _cart.value.toMutableList()
    val index = current.indexOfFirst { it.product.id == product.id }
    if (index >= 0) {
      val existing = current[index]
      current[index] = existing.copy(quantity = existing.quantity + quantity)
    } else {
      current.add(CartItem(product = product, quantity = quantity))
    }
    _cart.value = current
  }

  fun updateCartQuantity(productId: String, newQuantity: Int) {
    val current = _cart.value.toMutableList()
    if (newQuantity <= 0) {
      current.removeAll { it.product.id == productId }
    } else {
      val index = current.indexOfFirst { it.product.id == productId }
      if (index >= 0) {
        current[index] = current[index].copy(quantity = newQuantity)
      }
    }
    _cart.value = current
  }

  fun removeFromCart(productId: String) {
    _cart.value = _cart.value.filter { it.product.id != productId }
  }

  fun clearCart() {
    _cart.value = emptyList()
  }

  fun toggleWishlist(productId: String) {
    val current = _wishlist.value.toMutableSet()
    if (current.contains(productId)) {
      current.remove(productId)
    } else {
      current.add(productId)
    }
    _wishlist.value = current
  }

  fun addReview(productId: String, author: String, rating: Int, title: String, comment: String) {
    val newReview = Review(
      id = "rev-${UUID.randomUUID().toString().take(6)}",
      productId = productId,
      author = author.ifBlank { "Crochet Lover" },
      rating = rating.coerceIn(1, 5),
      date = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date()),
      title = title,
      comment = comment,
      verifiedPurchase = true,
      helpfulCount = 0
    )
    _reviews.value = listOf(newReview) + _reviews.value

    // Update product rating and review count
    val prodReviews = _reviews.value.filter { it.productId == productId }
    val avgRating = prodReviews.map { it.rating }.average().toFloat()
    val updatedProducts = _products.value.map { prod ->
      if (prod.id == productId) {
        prod.copy(rating = (Math.round(avgRating * 10) / 10.0).toFloat(), reviewCount = prodReviews.size)
      } else prod
    }
    _products.value = updatedProducts
  }

  fun placeOrder(
    customerName: String,
    customerEmail: String,
    customerAddress: String,
    paymentMethod: String,
    discountAmount: Double = 0.0
  ): Order? {
    val currentCart = _cart.value
    if (currentCart.isEmpty()) return null

    val subtotal = currentCart.sumOf { it.product.price * it.quantity }
    val shipping = if (subtotal > 999.0) 0.0 else 99.0
    val total = (subtotal - discountAmount + shipping).coerceAtLeast(0.0)

    val randomNum = (1000..9999).random()
    val orderId = "HS-$randomNum"
    val formattedDate = SimpleDateFormat("dd MMM, yyyy · h:mm a", Locale.getDefault()).format(Date())

    val orderItems = currentCart.map { item ->
      OrderItem(
        productId = item.product.id,
        productTitle = item.product.title,
        quantity = item.quantity,
        unitPrice = item.product.price,
        category = item.product.category,
        iconType = item.product.iconType
      )
    }

    val newOrder = Order(
      id = orderId,
      customerName = customerName.ifBlank { "Aarav Sharma" },
      customerEmail = customerEmail.ifBlank { "aarav@example.in" },
      customerAddress = customerAddress.ifBlank { "Flat 402, Lotus Bloom Enclave, Indiranagar, Bengaluru" },
      items = orderItems,
      subtotal = subtotal,
      discount = discountAmount,
      shipping = shipping,
      total = total,
      status = OrderStatus.PENDING,
      placedAtTimestamp = System.currentTimeMillis(),
      formattedDate = formattedDate,
      paymentMethod = paymentMethod
    )

    // Add order to orders list
    _orders.value = listOf(newOrder) + _orders.value

    // Decrement stock and update sales count
    val updatedProducts = _products.value.map { prod ->
      val orderedItem = currentCart.find { it.product.id == prod.id }
      if (orderedItem != null) {
        val newStock = (prod.stockQuantity - orderedItem.quantity).coerceAtLeast(0)
        prod.copy(
          stockQuantity = newStock,
          isOutOfStock = newStock == 0,
          salesCount = prod.salesCount + orderedItem.quantity
        )
      } else prod
    }
    _products.value = updatedProducts

    // Trigger Admin Notification
    val formattedTotal = formatInr(total)
    val notification = AdminNotification(
      id = "notif-${UUID.randomUUID().toString().take(6)}",
      orderId = orderId,
      title = "New Order #$orderId Received! ✨🍯",
      message = "${newOrder.customerName} ordered ${orderItems.sumOf { it.quantity }} item(s) for $formattedTotal via $paymentMethod",
      timestamp = System.currentTimeMillis(),
      formattedTime = "Just now",
      isRead = false,
      amount = total
    )
    _notifications.value = listOf(notification) + _notifications.value

    // Clear cart
    _cart.value = emptyList()

    return newOrder
  }

  fun cancelOrder(orderId: String) {
    _orders.value = _orders.value.map { order ->
      if (order.id == orderId && (order.status == OrderStatus.PENDING || order.status == OrderStatus.CRAFTING)) {
        order.copy(status = OrderStatus.CANCELLED)
      } else order
    }
  }

  // --- Admin Actions ---

  fun updateProductPrice(productId: String, newPrice: Double) {
    if (newPrice <= 0) return
    _products.value = _products.value.map { prod ->
      if (prod.id == productId) {
        prod.copy(price = newPrice)
      } else prod
    }
  }

  fun toggleOutOfStock(productId: String, isOutOfStock: Boolean) {
    _products.value = _products.value.map { prod ->
      if (prod.id == productId) {
        prod.copy(
          isOutOfStock = isOutOfStock,
          stockQuantity = if (isOutOfStock) 0 else (if (prod.stockQuantity == 0) 5 else prod.stockQuantity)
        )
      } else prod
    }
  }

  fun updateProductStock(productId: String, newStock: Int) {
    _products.value = _products.value.map { prod ->
      if (prod.id == productId) {
        prod.copy(
          stockQuantity = newStock.coerceAtLeast(0),
          isOutOfStock = newStock <= 0
        )
      } else prod
    }
  }

  fun addProduct(product: Product) {
    _products.value = listOf(product) + _products.value
  }

  fun updateProduct(product: Product) {
    _products.value = _products.value.map {
      if (it.id == product.id) product else it
    }
  }

  fun deleteProduct(productId: String) {
    _products.value = _products.value.filter { it.id != productId }
  }

  fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
    _orders.value = _orders.value.map { order ->
      if (order.id == orderId) {
        order.copy(status = newStatus)
      } else order
    }
  }

  fun markNotificationAsRead(notificationId: String) {
    _notifications.value = _notifications.value.map { notif ->
      if (notif.id == notificationId) notif.copy(isRead = true) else notif
    }
  }

  fun markAllNotificationsAsRead() {
    _notifications.value = _notifications.value.map { it.copy(isRead = true) }
  }
}

// Default Seed Data with Indian Rupee (₹) Pricing
private val initialProducts = listOf(
  Product(
    id = "prod-1",
    title = "Bumblebee Amigurumi Plushie",
    category = "Amigurumi",
    price = 849.00,
    originalPrice = 999.00,
    rating = 4.9f,
    reviewCount = 28,
    isOutOfStock = false,
    stockQuantity = 12,
    description = "Our signature honey bumblebee! Handcrafted with ultra-soft organic velvet yarn, cute embroidered smile, and tiny gossamer wings. Filled with hypoallergenic cloud stuffing.",
    materials = "100% Cotton & Velvet Yarn, Safety Eyes, Hypoallergenic stuffing",
    estimatedCraftTime = "Ready to ship · Handcrafted in 2 days",
    badge = "Bestseller ✨",
    colorHex = "#F59E0B",
    iconType = "BEE",
    salesCount = 142
  ),
  Product(
    id = "prod-2",
    title = "Honeycomb Hexagon Blanket",
    category = "Blankets",
    price = 3499.00,
    originalPrice = 3999.00,
    rating = 4.9f,
    reviewCount = 15,
    isOutOfStock = false,
    stockQuantity = 4,
    description = "Intricate interlocking honey-golden hexagons stitched together with dreamy cream border. Ultra-soft and cozy for dreamy evenings or afternoon chai.",
    materials = "Merino Wool Blend, Hand-stitched hex motifs (50x60 inches)",
    estimatedCraftTime = "Made to order · 4 days craft time",
    badge = "Staff Pick 💛",
    colorHex = "#D97706",
    iconType = "BLANKET",
    salesCount = 64
  ),
  Product(
    id = "prod-3",
    title = "Sunny Meadow Crochet Cardigan",
    category = "Sweaters",
    price = 2899.00,
    originalPrice = 3499.00,
    rating = 4.8f,
    reviewCount = 19,
    isOutOfStock = false,
    stockQuantity = 6,
    description = "Lightweight yet warm balloon-sleeve cardigan with daisy granny squares and carved wooden buttons. Dreamy oversized fit that drapes beautifully.",
    materials = "Soft Bamboo & Cotton Yarn, Carved Wooden Buttons",
    estimatedCraftTime = "Handmade · 3 days craft time",
    badge = "Trending 🌸",
    colorHex = "#E59500",
    iconType = "SWEATER",
    salesCount = 52
  ),
  Product(
    id = "prod-4",
    title = "Golden Honeycomb Beanie & Mitts",
    category = "Accessories",
    price = 1199.00,
    rating = 4.9f,
    reviewCount = 11,
    isOutOfStock = false,
    stockQuantity = 9,
    description = "Textured waffle and honeycomb knit stitch beanie paired with fingerless mittens. Provides soothing warmth and easy touchscreen use.",
    materials = "Alpaca & Wool Blend, Stretchy Ribbed Brim",
    estimatedCraftTime = "In Stock · Ships within 24h",
    badge = "Cozy Cloud ☁️",
    colorHex = "#FBBF24",
    iconType = "BEANIE",
    salesCount = 38
  ),
  Product(
    id = "prod-5",
    title = "Beginner Bee & Daisy DIY Kit",
    category = "Kits & Yarn",
    price = 899.00,
    rating = 5.0f,
    reviewCount = 34,
    isOutOfStock = false,
    stockQuantity = 18,
    description = "Everything you need to craft your very own baby bee! Includes premium golden yarn, bamboo 4.0mm hook, ergonomic stitch markers, stuffing, and step-by-step illustrated video guide QR code.",
    materials = "3 Skeins Cotton Yarn, Bamboo Hook, Video Tutorial Included",
    estimatedCraftTime = "Ready to ship",
    badge = "Best for Beginners 🐝",
    colorHex = "#EA580C",
    iconType = "KIT",
    salesCount = 210
  ),
  Product(
    id = "prod-6",
    title = "Granny Square Boho Market Tote",
    category = "Accessories",
    price = 1499.00,
    rating = 4.7f,
    reviewCount = 14,
    isOutOfStock = true,
    stockQuantity = 0,
    description = "Bohemian crochet shoulder bag made with 16 distinct vintage granny squares. Fully lined with natural linen fabric and reinforced sturdy handles.",
    materials = "Recycled Cotton Cord, Linen Interior Lining",
    estimatedCraftTime = "Restocking soon",
    badge = "Sold Out",
    colorHex = "#B45309",
    iconType = "BAG",
    salesCount = 77
  )
)

private val initialReviews = listOf(
  Review(
    id = "rev-1",
    productId = "prod-1",
    author = "Ananya Singhania",
    rating = 5,
    date = "18 Oct, 2026",
    title = "Cutest little bee ever! 🐝💛",
    comment = "The bumblebee is even cuter in person! The stitches are tight, velvety soft, and so dreamy. My daughter takes it everywhere. Definitely ordering the honeycomb blanket next!",
    verifiedPurchase = true,
    helpfulCount = 14
  ),
  Review(
    id = "rev-2",
    productId = "prod-1",
    author = "Pooja Reddy",
    rating = 5,
    date = "14 Oct, 2026",
    title = "Exquisite handmade craft ✨",
    comment = "You can feel the warmth and care poured into every single loop. The packaging had dried flower petals and a lovely handwritten card. 10/10 recommend Honeycomb Stitches!",
    verifiedPurchase = true,
    helpfulCount = 8
  ),
  Review(
    id = "rev-3",
    productId = "prod-2",
    author = "Rohan Malhotra",
    rating = 5,
    date = "09 Oct, 2026",
    title = "Dreamy aesthetic and so warm",
    comment = "Heavy enough to feel comforting like a weighted blanket, but very breathable. The warm honey and cream colors look divine on our sofa.",
    verifiedPurchase = true,
    helpfulCount = 22
  ),
  Review(
    id = "rev-4",
    productId = "prod-3",
    author = "Isha Kapoor",
    rating = 5,
    date = "28 Sep, 2026",
    title = "Compliments wherever I go! 🌸",
    comment = "Wore this cardigan to an art cafe in Bandra and three people came up to ask where I got it. So soft, cozy, and perfectly oversized.",
    verifiedPurchase = true,
    helpfulCount = 19
  ),
  Review(
    id = "rev-5",
    productId = "prod-5",
    author = "Tanmay Joshi",
    rating = 5,
    date = "02 Oct, 2026",
    title = "Made my first crochet bee! 🧶",
    comment = "I had never held a crochet hook before in my life. The video guide included in the kit made it so effortless. Took me about 4 hours and now my cute bee sits on my desk!",
    verifiedPurchase = true,
    helpfulCount = 31
  )
)

private val initialOrders = listOf(
  Order(
    id = "HS-7842",
    customerName = "Meera Nambiar",
    customerEmail = "meera.n@example.in",
    customerAddress = "12/4 Orchid Heights, Koramangala, Bengaluru, Karnataka",
    items = listOf(
      OrderItem("prod-1", "Bumblebee Amigurumi Plushie", 1, 849.00, "Amigurumi", "BEE"),
      OrderItem("prod-4", "Golden Honeycomb Beanie & Mitts", 1, 1199.00, "Accessories", "BEANIE")
    ),
    subtotal = 2048.00,
    discount = 200.00,
    shipping = 0.0,
    total = 1848.00,
    status = OrderStatus.DELIVERED,
    placedAtTimestamp = System.currentTimeMillis() - 86400000 * 3,
    formattedDate = "19 Oct, 2026 · 2:15 PM",
    paymentMethod = "UPI (Google Pay)"
  ),
  Order(
    id = "HS-8091",
    customerName = "Devendra Joshi",
    customerEmail = "dev.joshi@example.in",
    customerAddress = "Flat 501, Silver Birch Towers, Powai, Mumbai, Maharashtra",
    items = listOf(
      OrderItem("prod-2", "Honeycomb Hexagon Blanket", 1, 3499.00, "Blankets", "BLANKET")
    ),
    subtotal = 3499.00,
    discount = 0.0,
    shipping = 0.0,
    total = 3499.00,
    status = OrderStatus.SHIPPED,
    placedAtTimestamp = System.currentTimeMillis() - 86400000 * 1,
    formattedDate = "21 Oct, 2026 · 11:42 AM",
    paymentMethod = "HDFC Credit Card ending in 4921"
  ),
  Order(
    id = "HS-8219",
    customerName = "Shreya Deshmukh",
    customerEmail = "shreya.d@example.in",
    customerAddress = "88 Sunshine Park, Koregaon Park, Pune, Maharashtra",
    items = listOf(
      OrderItem("prod-5", "Beginner Bee & Daisy DIY Kit", 2, 899.00, "Kits & Yarn", "KIT")
    ),
    subtotal = 1798.00,
    discount = 0.0,
    shipping = 0.0,
    total = 1798.00,
    status = OrderStatus.CRAFTING,
    placedAtTimestamp = System.currentTimeMillis() - 3600000 * 5,
    formattedDate = "Today · 9:30 AM",
    paymentMethod = "UPI (PhonePe)"
  )
)

private val initialNotifications = listOf(
  AdminNotification(
    id = "notif-1",
    orderId = "HS-8219",
    title = "New Order #HS-8219 Received! ✨🐝",
    message = "Shreya Deshmukh ordered 2x Beginner DIY Kits for ₹1,798 via PhonePe",
    timestamp = System.currentTimeMillis() - 3600000 * 5,
    formattedTime = "5 hours ago",
    isRead = false,
    amount = 1798.00
  ),
  AdminNotification(
    id = "notif-2",
    orderId = "HS-8091",
    title = "Order #HS-8091 Dispatched 📦💛",
    message = "Devendra Joshi ordered Honeycomb Hexagon Blanket for ₹3,499",
    timestamp = System.currentTimeMillis() - 86400000 * 1,
    formattedTime = "Yesterday",
    isRead = true,
    amount = 3499.00
  )
)
