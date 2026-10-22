package com.example.model

data class Product(
  val id: String,
  val title: String,
  val category: String,
  val price: Double,
  val originalPrice: Double? = null,
  val rating: Float = 5.0f,
  val reviewCount: Int = 0,
  val isOutOfStock: Boolean = false,
  val stockQuantity: Int = 10,
  val description: String = "",
  val materials: String = "100% Organic Cotton Yarn",
  val estimatedCraftTime: String = "Handmade in 2–3 days",
  val badge: String? = null,
  val colorHex: String = "#F59E0B",
  val iconType: String = "BEE",
  val salesCount: Int = 0
)
