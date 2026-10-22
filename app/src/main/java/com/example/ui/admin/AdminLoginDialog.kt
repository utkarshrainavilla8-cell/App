package com.example.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HoneyGoldPrimary

@Composable
fun AdminLoginDialog(
  onDismiss: () -> Unit,
  onLoginSuccess: () -> Unit
) {
  var password by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("admin@honeycomb.com") }
  var errorText by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        Icons.Default.AdminPanelSettings,
        contentDescription = null,
        tint = HoneyGoldPrimary
      )
    },
    title = {
      Text(
        text = "Honeycomb Admin Login",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color(0xFF26140A)
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "Access workshop inventory, change prices, manage out-of-stock items, and inspect sales analytics.",
          fontSize = 12.sp,
          color = Color(0xFF57483F)
        )

        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("Admin Email") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("admin_email_input"),
          shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
          value = password,
          onValueChange = {
            password = it
            errorText = null
          },
          label = { Text("Admin Password (or PIN)") },
          placeholder = { Text("1234") },
          visualTransformation = PasswordVisualTransformation(),
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("admin_password_input"),
          shape = RoundedCornerShape(10.dp)
        )

        if (errorText != null) {
          Text(
            text = errorText!!,
            color = Color(0xFFDC2626),
            fontSize = 12.sp
          )
        }

        // Quick demo access button
        OutlinedButton(
          onClick = {
            onLoginSuccess()
          },
          modifier = Modifier.fillMaxWidth().testTag("quick_admin_demo_button"),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(Icons.Default.Key, contentDescription = null, tint = HoneyGoldPrimary)
          Spacer(modifier = Modifier.height(4.dp))
          Text("Quick 1-Tap Admin Access", color = HoneyGoldPrimary, fontWeight = FontWeight.Bold)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          // Accept 1234 or honey or any standard password
          if (password == "1234" || password.equals("honey", ignoreCase = true) || password.isNotBlank()) {
            onLoginSuccess()
          } else {
            errorText = "Please enter password (e.g. 1234)"
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = HoneyGoldPrimary),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("submit_admin_login_button")
      ) {
        Text("Sign In")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = Color(0xFF78716C))
      }
    },
    shape = RoundedCornerShape(20.dp),
    containerColor = Color.White
  )
}
