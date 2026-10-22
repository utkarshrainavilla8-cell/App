package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HoneycombRepository
import com.example.model.Product

@Composable
fun ProductCard(
  product: Product,
  isWishlisted: Boolean,
  onProductClicked: (Product) -> Unit,
  onAddToCartClicked: (Product) -> Unit,
  onToggleWishlist: (Product) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = Color(0x33D97706))
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, Color(0xFFF3E8DB), RoundedCornerShape(20.dp))
      .clickable { onProductClicked(product) }
      .testTag("product_card_${product.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    Column {
      // Art Thumbnail with Wishlist button
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(160.dp)
      ) {
        ProductArtThumbnail(
          product = product,
          badgeText = product.badge,
          modifier = Modifier.matchParentSize()
        )

        // Cute Wishlist button
        Surface(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(8.dp),
          shape = CircleShape,
          color = Color.White.copy(alpha = 0.92f),
          shadowElevation = 3.dp
        ) {
          IconButton(
            onClick = { onToggleWishlist(product) },
            modifier = Modifier.size(32.dp).testTag("wishlist_button_${product.id}")
          ) {
            Icon(
              imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Wishlist",
              tint = if (isWishlisted) Color(0xFFE11D48) else Color(0xFF9CA3AF),
              modifier = Modifier.size(17.dp)
            )
          }
        }
      }

      // Details
      Column(modifier = Modifier.padding(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = product.category.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB45309),
            letterSpacing = 0.5.sp
          )

          // Cute Stock pill
          if (product.isOutOfStock) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFFEE2E2)
            ) {
              Text(
                text = "Sold Out",
                color = Color(0xFFDC2626),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          } else {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFDCFCE7)
            ) {
              Text(
                text = "${product.stockQuantity} left",
                color = Color(0xFF15803D),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
          text = product.title,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = Color(0xFF26140A),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Rating
        StarRatingBar(
          rating = product.rating,
          reviewCount = product.reviewCount,
          starSize = 13.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Price row in Indian Rupees (₹)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = HoneycombRepository.formatInr(product.price),
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = Color(0xFF26140A)
            )
            if (product.originalPrice != null && product.originalPrice > product.price) {
              Text(
                text = " ${HoneycombRepository.formatInr(product.originalPrice)}",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                textDecoration = TextDecoration.LineThrough,
                modifier = Modifier.padding(start = 4.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Dreamy Glowing Add to Cart Button
        Button(
          onClick = { onAddToCartClicked(product) },
          enabled = !product.isOutOfStock,
          modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .testTag("add_to_cart_${product.id}"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD97706),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFF3F4F6),
            disabledContentColor = Color(0xFF9CA3AF)
          )
        ) {
          Icon(
            imageVector = Icons.Default.AddShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(15.dp)
          )
          Text(
            text = if (product.isOutOfStock) "Out of Stock" else "Add to Basket ✨",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 6.dp)
          )
        }
      }
    }
  }
}
