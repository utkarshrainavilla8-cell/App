package com.example.ui.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.Product
import com.example.ui.components.InteractiveRatingPicker
import com.example.ui.theme.HoneyGoldPrimary

@Composable
fun WriteReviewDialog(
  product: Product,
  onDismiss: () -> Unit,
  onSubmit: (rating: Int, author: String, title: String, comment: String) -> Unit
) {
  var rating by remember { mutableIntStateOf(5) }
  var author by remember { mutableStateOf("") }
  var title by remember { mutableStateOf("") }
  var comment by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = {
          if (comment.isNotBlank()) {
            onSubmit(rating, author, title, comment)
          }
        },
        enabled = comment.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = HoneyGoldPrimary),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("submit_review_button")
      ) {
        Text("Submit Review")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = Color(0xFF78716C))
      }
    },
    title = {
      Column {
        Text(
          text = "Review Handmade Craft",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = Color(0xFF26140A)
        )
        Text(
          text = product.title,
          fontSize = 13.sp,
          color = Color(0xFF78716C)
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "Your Rating:",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color(0xFF381E0D)
        )

        InteractiveRatingPicker(
          rating = rating,
          onRatingChanged = { rating = it },
          starSize = 34.dp
        )

        OutlinedTextField(
          value = author,
          onValueChange = { author = it },
          label = { Text("Your Name (Optional)") },
          placeholder = { Text("e.g. Emily") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("review_author_input"),
          shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Review Headline") },
          placeholder = { Text("e.g. In love with this bee!") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("review_title_input"),
          shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
          value = comment,
          onValueChange = { comment = it },
          label = { Text("Your Feedback & Craft Experience *") },
          placeholder = { Text("Tell us about stitch quality, yarn feel, and craft...") },
          minLines = 3,
          maxLines = 5,
          modifier = Modifier.fillMaxWidth().testTag("review_comment_input"),
          shape = RoundedCornerShape(10.dp)
        )
      }
    },
    shape = RoundedCornerShape(20.dp),
    containerColor = Color.White
  )
}
