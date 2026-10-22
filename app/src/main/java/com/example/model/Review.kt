package com.example.model

data class Review(
  val id: String,
  val productId: String,
  val author: String,
  val rating: Int,
  val date: String,
  val title: String,
  val comment: String,
  val verifiedPurchase: Boolean = true,
  val helpfulCount: Int = 0
)
