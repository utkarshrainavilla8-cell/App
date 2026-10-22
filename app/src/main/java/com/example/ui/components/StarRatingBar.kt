package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HoneyYellowAccent

@Composable
fun StarRatingBar(
  rating: Float,
  modifier: Modifier = Modifier,
  starSize: Dp = 16.dp,
  showLabel: Boolean = true,
  reviewCount: Int? = null
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 1..5) {
      val icon = when {
        rating >= i -> Icons.Default.Star
        rating >= i - 0.5f -> Icons.Default.StarHalf
        else -> Icons.Default.StarBorder
      }
      Icon(
        imageVector = icon,
        contentDescription = "Star $i",
        tint = HoneyYellowAccent,
        modifier = Modifier.size(starSize)
      )
    }

    if (showLabel) {
      Text(
        text = String.format("%.1f", rating),
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        modifier = Modifier.padding(start = 4.dp),
        color = Color(0xFF451A03)
      )
      if (reviewCount != null) {
        Text(
          text = "($reviewCount)",
          fontSize = 12.sp,
          color = Color(0xFF78716C),
          modifier = Modifier.padding(start = 2.dp)
        )
      }
    }
  }
}

@Composable
fun InteractiveRatingPicker(
  rating: Int,
  onRatingChanged: (Int) -> Unit,
  modifier: Modifier = Modifier,
  starSize: Dp = 32.dp
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 1..5) {
      Icon(
        imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
        contentDescription = "Rate $i stars",
        tint = if (i <= rating) HoneyYellowAccent else Color(0xFFD6D3D1),
        modifier = Modifier
          .size(starSize)
          .padding(2.dp)
          .clickable { onRatingChanged(i) }
      )
    }
  }
}
