package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Product

@Composable
fun ProductArtThumbnail(
  product: Product,
  modifier: Modifier = Modifier,
  badgeText: String? = null
) {
  val baseColor = try {
    Color(android.graphics.Color.parseColor(product.colorHex))
  } catch (e: Exception) {
    Color(0xFFD97706)
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(
        Brush.linearGradient(
          colors = listOf(
            baseColor.copy(alpha = 0.15f),
            baseColor.copy(alpha = 0.05f),
            Color(0xFFFFF9ED)
          )
        )
      ),
    contentAlignment = Alignment.Center
  ) {
    // If it's the signature Bee plushie or logo product, show the official Honeycomb Stitches brand logo!
    if (product.iconType == "BEE" || product.id == "prod-1") {
      Image(
        painter = painterResource(id = R.drawable.honeycomb_logo),
        contentDescription = product.title,
        modifier = Modifier
          .fillMaxSize()
          .padding(8.dp)
          .clip(CircleShape),
        contentScale = ContentScale.Crop
      )
    } else {
      // Artistic handmade representation with category icon & yarn-inspired badge
      Box(
        modifier = Modifier
          .size(76.dp)
          .clip(CircleShape)
          .background(baseColor.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        val icon = when (product.iconType) {
          "BLANKET" -> Icons.Default.ColorLens
          "SWEATER" -> Icons.Default.Checkroom
          "BEANIE" -> Icons.Default.Park
          "KIT" -> Icons.Default.Handyman
          "BAG" -> Icons.Default.ShoppingBag
          else -> Icons.Default.LocalMall
        }

        Icon(
          imageVector = icon,
          contentDescription = product.category,
          tint = baseColor,
          modifier = Modifier.size(40.dp)
        )
      }
    }

    // Out of Stock Overlay
    if (product.isOutOfStock) {
      Surface(
        modifier = Modifier
          .align(Alignment.Center)
          .padding(8.dp),
        color = Color(0xDD1F1712),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(
          text = "OUT OF STOCK",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
      }
    }

    // Badge overlay
    if (!badgeText.isNullOrBlank() && !product.isOutOfStock) {
      Surface(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(8.dp),
        color = Color(0xFFE11D48),
        shape = RoundedCornerShape(6.dp)
      ) {
        Text(
          text = badgeText,
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 10.sp,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
      }
    }
  }
}
