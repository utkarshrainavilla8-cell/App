package com.example.model

data class AdminNotification(
  val id: String,
  val orderId: String,
  val title: String,
  val message: String,
  val timestamp: Long = System.currentTimeMillis(),
  val formattedTime: String,
  val isRead: Boolean = false,
  val amount: Double
)
