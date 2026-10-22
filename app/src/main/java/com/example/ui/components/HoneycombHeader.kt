package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.HoneyGoldPrimary

@Composable
fun HoneycombHeader(
  isAdminMode: Boolean,
  cartItemCount: Int,
  unreadNotificationCount: Int,
  onToggleRole: () -> Unit,
  onCartClicked: () -> Unit,
  onNotificationsClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(
        Brush.horizontalGradient(
          colors = listOf(
            Color(0xFFFFFDF8),
            Color(0xFFFFF6EE),
            Color(0xFFFAF3FF)
          )
        )
      )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Brand logo & title with dreamy sparkle touch
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .shadow(4.dp, CircleShape)
              .clip(CircleShape)
              .background(Color(0xFFFEF3C7))
              .border(2.dp, Color(0xFFF59E0B), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.honeycomb_logo),
              contentDescription = "Honeycomb Stitches Logo",
              modifier = Modifier.fillMaxWidth(),
              contentScale = ContentScale.Crop
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Honeycomb Stitches",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color(0xFF381E0D)
              )
              Text(
                text = " ✨",
                fontSize = 14.sp
              )
            }
            Text(
              text = if (isAdminMode) "Workshop Admin Portal 🍯" else "handmade with love 💛",
              fontSize = 11.sp,
              color = if (isAdminMode) HoneyGoldPrimary else Color(0xFF8C6F5A),
              fontWeight = if (isAdminMode) FontWeight.Bold else FontWeight.Medium
            )
          }
        }

        // Action icons with cute soft surfaces
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Admin notification bell
          BadgedBox(
            badge = {
              if (unreadNotificationCount > 0) {
                Badge(
                  containerColor = Color(0xFFE11D48),
                  contentColor = Color.White
                ) {
                  Text(text = "$unreadNotificationCount", fontSize = 10.sp)
                }
              }
            }
          ) {
            Surface(
              shape = CircleShape,
              color = Color.White.copy(alpha = 0.85f),
              shadowElevation = 1.dp
            ) {
              IconButton(
                onClick = onNotificationsClicked,
                modifier = Modifier
                  .size(38.dp)
                  .testTag("admin_notifications_button")
              ) {
                Icon(
                  imageVector = Icons.Default.Notifications,
                  contentDescription = "Admin Notifications",
                  tint = if (unreadNotificationCount > 0) Color(0xFFE11D48) else Color(0xFF78716C),
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }

          if (!isAdminMode) {
            Spacer(modifier = Modifier.width(6.dp))

            // Cart Icon with badge
            BadgedBox(
              badge = {
                if (cartItemCount > 0) {
                  Badge(
                    containerColor = HoneyGoldPrimary,
                    contentColor = Color.White
                  ) {
                    Text(text = "$cartItemCount", fontSize = 10.sp)
                  }
                }
              }
            ) {
              Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.85f),
                shadowElevation = 1.dp
              ) {
                IconButton(
                  onClick = onCartClicked,
                  modifier = Modifier
                    .size(38.dp)
                    .testTag("cart_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Shopping Cart",
                    tint = Color(0xFF381E0D),
                    modifier = Modifier.size(19.dp)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Cute Role Switch pill button
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isAdminMode) Color(0xFFFEF3C7) else Color(0xFFFFECE5),
            shadowElevation = 2.dp,
            modifier = Modifier
              .clickable(onClick = onToggleRole)
              .testTag("toggle_role_button")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = if (isAdminMode) Icons.Default.AdminPanelSettings else Icons.Default.Storefront,
                contentDescription = if (isAdminMode) "Switch to Customer" else "Switch to Admin",
                tint = if (isAdminMode) Color(0xFFB45309) else Color(0xFFE11D48),
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isAdminMode) "Admin" else "Store",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAdminMode) Color(0xFFB45309) else Color(0xFFE11D48)
              )
            }
          }
        }
      }
    }
  }
}
