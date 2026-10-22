package com.example.ui.customer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HoneycombRepository
import com.example.model.Product
import com.example.model.Review
import com.example.ui.components.ProductArtThumbnail
import com.example.ui.components.StarRatingBar
import com.example.ui.theme.HoneyGoldPrimary

@Composable
fun ProductDetailScreen(
  product: Product,
  reviews: List<Review>,
  isWishlisted: Boolean,
  onBackClicked: () -> Unit,
  onAddToCartClicked: (Product, Int) -> Unit,
  onToggleWishlist: (Product) -> Unit,
  onSubmitReview: (rating: Int, author: String, title: String, comment: String) -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBackClicked() }

  var quantity by remember { mutableIntStateOf(1) }
  var showWriteReviewDialog by remember { mutableStateOf(false) }

  val productReviews = reviews.filter { it.productId == product.id }

  if (showWriteReviewDialog) {
    WriteReviewDialog(
      product = product,
      onDismiss = { showWriteReviewDialog = false },
      onSubmit = { rating, author, title, comment ->
        onSubmitReview(rating, author, title, comment)
        showWriteReviewDialog = false
      }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFFFFBF5))
  ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onBackClicked,
        modifier = Modifier.testTag("detail_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back to store",
          tint = Color(0xFF381E0D)
        )
      }

      Text(
        text = "Craft Details ✨",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF381E0D)
      )

      IconButton(
        onClick = { onToggleWishlist(product) },
        modifier = Modifier.testTag("detail_wishlist_button")
      ) {
        Icon(
          imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
          contentDescription = "Wishlist",
          tint = if (isWishlisted) Color(0xFFE11D48) else Color(0xFF78716C)
        )
      }
    }

    // Scrollable content
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp)
    ) {
      // Large Product Visual with dreamy rounded container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = Color(0x33D97706))
          .clip(RoundedCornerShape(24.dp))
          .border(1.5.dp, Color(0xFFFDE68A), RoundedCornerShape(24.dp))
      ) {
        ProductArtThumbnail(
          product = product,
          badgeText = product.badge,
          modifier = Modifier.matchParentSize()
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Category and Stock Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color(0xFFFEF3C7)
        ) {
          Text(
            text = "${product.category.uppercase()} ✨",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB45309),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }

        if (product.isOutOfStock) {
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFEE2E2)
          ) {
            Text(
              text = "Sold Out 🌸",
              color = Color(0xFFDC2626),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        } else {
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFDCFCE7)
          ) {
            Text(
              text = "In Stock (${product.stockQuantity} left) 💛",
              color = Color(0xFF15803D),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Product Title
      Text(
        text = product.title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF26140A),
        lineHeight = 28.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Rating summary
      Row(verticalAlignment = Alignment.CenterVertically) {
        StarRatingBar(
          rating = product.rating,
          reviewCount = productReviews.size.coerceAtLeast(product.reviewCount),
          starSize = 16.dp
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Price Tag in Indian Rupees (₹)
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = HoneycombRepository.formatInr(product.price),
          fontSize = 26.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF26140A)
        )
        if (product.originalPrice != null && product.originalPrice > product.price) {
          Text(
            text = " ${HoneycombRepository.formatInr(product.originalPrice)}",
            fontSize = 16.sp,
            color = Color(0xFF9CA3AF),
            textDecoration = TextDecoration.LineThrough,
            modifier = Modifier.padding(start = 8.dp)
          )
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFFE4E6),
            modifier = Modifier.padding(start = 8.dp)
          ) {
            Text(
              text = "SAVE ${HoneycombRepository.formatInr(product.originalPrice - product.price)} ✨",
              color = Color(0xFFE11D48),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Description
      Text(
        text = "About this craft",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF381E0D)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = product.description,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        color = Color(0xFF57483F)
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Dreamy Craftsmanship Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFED7AA))
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = HoneyGoldPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Materials: ${product.materials}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF381E0D))
          }
          Text(text = "Craft Time: ${product.estimatedCraftTime}", fontSize = 12.sp, color = Color(0xFF57483F))
          Text(text = "Delivery: Free craft delivery across India on orders over ₹999 🇮🇳", fontSize = 12.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Medium)
          Text(text = "Care: Gentle hand wash with mild baby shampoo, dry flat in shade.", fontSize = 12.sp, color = Color(0xFF78716C))
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
      HorizontalDivider(color = Color(0xFFF0E3D3))
      Spacer(modifier = Modifier.height(16.dp))

      // Customer Reviews Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Customer Reviews",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF26140A)
          )
          Text(
            text = "${productReviews.size} authentic patron reviews",
            fontSize = 12.sp,
            color = Color(0xFF78716C)
          )
        }

        OutlinedButton(
          onClick = { showWriteReviewDialog = true },
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier.testTag("write_review_button")
        ) {
          Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp), tint = HoneyGoldPrimary)
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Write Review ✨", fontSize = 12.sp, color = HoneyGoldPrimary, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Reviews List
      if (productReviews.isEmpty()) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5ED))
        ) {
          Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
            Text(
              text = "No reviews yet for this craft. Be the first to share your experience! 💛",
              fontSize = 13.sp,
              color = Color(0xFF78716C),
              fontWeight = FontWeight.Medium
            )
          }
        }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          productReviews.forEach { review ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                      shape = CircleShape,
                      color = Color(0xFFFEF3C7),
                      modifier = Modifier.size(30.dp)
                    ) {
                      Box(contentAlignment = Alignment.Center) {
                        Text(
                          text = review.author.take(1).uppercase(),
                          fontWeight = FontWeight.Bold,
                          fontSize = 13.sp,
                          color = Color(0xFFB45309)
                        )
                      }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                      Text(
                        text = review.author,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF26140A)
                      )
                      if (review.verifiedPurchase) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                          Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Verified Purchase",
                            tint = Color(0xFF15803D),
                            modifier = Modifier.size(11.dp)
                          )
                          Spacer(modifier = Modifier.width(2.dp))
                          Text(text = "Verified Buyer", fontSize = 10.sp, color = Color(0xFF15803D), fontWeight = FontWeight.SemiBold)
                        }
                      }
                    }
                  }

                  Text(text = review.date, fontSize = 11.sp, color = Color(0xFF9CA3AF))
                }

                Spacer(modifier = Modifier.height(6.dp))

                StarRatingBar(rating = review.rating.toFloat(), showLabel = false, starSize = 14.dp)

                if (review.title.isNotBlank()) {
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = review.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF381E0D)
                  )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = review.comment,
                  fontSize = 13.sp,
                  lineHeight = 18.sp,
                  color = Color(0xFF57483F)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }

    // Bottom Action Bar: Quantity & Add to Cart
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = Color.White,
      shadowElevation = 8.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Quantity Selector
        if (!product.isOutOfStock) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .background(Color(0xFFF3ECE4), RoundedCornerShape(12.dp))
              .padding(horizontal = 4.dp, vertical = 2.dp)
          ) {
            IconButton(
              onClick = { if (quantity > 1) quantity-- },
              modifier = Modifier.size(32.dp).testTag("decrease_qty_button")
            ) {
              Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color(0xFF451A03), modifier = Modifier.size(16.dp))
            }
            Text(
              text = "$quantity",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFF451A03),
              modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
              onClick = { if (quantity < product.stockQuantity) quantity++ },
              modifier = Modifier.size(32.dp).testTag("increase_qty_button")
            ) {
              Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color(0xFF451A03), modifier = Modifier.size(16.dp))
            }
          }
        }

        // Add to Cart Button in Indian Rupees (₹)
        Button(
          onClick = { onAddToCartClicked(product, quantity) },
          enabled = !product.isOutOfStock,
          modifier = Modifier
            .weight(1f)
            .height(46.dp)
            .testTag("detail_add_to_cart_button"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = HoneyGoldPrimary,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFE5E7EB),
            disabledContentColor = Color(0xFF9CA3AF)
          )
        ) {
          Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (product.isOutOfStock) "Item Out of Stock" else "Add $quantity to Basket · ${HoneycombRepository.formatInr(product.price * quantity)} ✨",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}
