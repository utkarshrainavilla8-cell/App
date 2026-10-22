package com.example.ui.admin

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AdminNotification
import com.example.ui.theme.HoneyGoldPrimary

@Composable
fun AdminNotificationsDialog(
  notifications: List<AdminNotification>,
  onDismiss: () -> Unit,
  onNotificationClicked: (AdminNotification) -> Unit,
  onMarkAllRead: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Notifications, contentDescription = null, tint = HoneyGoldPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Order Notifications",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF26140A)
          )
        }

        if (notifications.any { !it.isRead }) {
          TextButton(onClick = onMarkAllRead, modifier = Modifier.testTag("mark_all_read_button")) {
            Text("Mark all read", fontSize = 11.sp, color = HoneyGoldPrimary)
          }
        }
      }
    },
    text = {
      if (notifications.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Text("No notifications yet.", color = Color(0xFF78716C), fontSize = 13.sp)
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(notifications, key = { it.id }) { notif ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onNotificationClicked(notif) }
                .testTag("notification_item_${notif.id}"),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (!notif.isRead) Color(0xFFFEF3C7) else Color(0xFFFAF5ED)
              )
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  shape = CircleShape,
                  color = if (!notif.isRead) HoneyGoldPrimary else Color(0xFFE5D5C2),
                  modifier = Modifier.size(36.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(
                      imageVector = Icons.Default.ShoppingBag,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = notif.title,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = Color(0xFF26140A)
                    )
                    if (!notif.isRead) {
                      Box(
                        modifier = Modifier
                          .size(8.dp)
                          .clip(CircleShape)
                          .background(Color(0xFFE11D48))
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(2.dp))

                  Text(
                    text = notif.message,
                    fontSize = 12.sp,
                    color = Color(0xFF57483F)
                  )

                  Spacer(modifier = Modifier.height(2.dp))

                  Text(
                    text = notif.formattedTime,
                    fontSize = 10.sp,
                    color = Color(0xFF78716C)
                  )
                }
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = HoneyGoldPrimary),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Close")
      }
    },
    shape = RoundedCornerShape(20.dp),
    containerColor = Color.White
  )
}
