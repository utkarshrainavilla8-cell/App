package com.example.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.HoneyGoldPrimary

@Composable
fun CustomerCatalogScreen(
  products: List<Product>,
  wishlist: Set<String>,
  onProductClicked: (Product) -> Unit,
  onAddToCartClicked: (Product) -> Unit,
  onToggleWishlist: (Product) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }
  var inStockOnly by remember { mutableStateOf(false) }

  val categories = listOf("All", "Amigurumi", "Sweaters", "Blankets", "Accessories", "Kits & Yarn")

  val filteredProducts = products.filter { product ->
    val matchesCategory = selectedCategory == "All" || product.category.equals(selectedCategory, ignoreCase = true)
    val matchesSearch = searchQuery.isBlank() || product.title.contains(searchQuery, ignoreCase = true) || product.description.contains(searchQuery, ignoreCase = true)
    val matchesStock = !inStockOnly || !product.isOutOfStock
    matchesCategory && matchesSearch && matchesStock
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFFFFFBF5),
            Color(0xFFFFF7EF),
            Color(0xFFFAF4FF)
          )
        )
      )
  ) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Cute & Dreamy Hero Banner
      item(span = { GridItemSpan(2) }) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = Color(0x33D97706))
            .clip(RoundedCornerShape(24.dp)),
          colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  colors = listOf(
                    Color(0xFFFEF3C7),
                    Color(0xFFFFEBE3),
                    Color(0xFFF9F0FF)
                  )
                )
              )
              .border(1.5.dp, Color(0xFFFDE68A), RoundedCornerShape(24.dp))
              .padding(18.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Surface(
                  shape = RoundedCornerShape(20.dp),
                  color = Color(0xFFD97706),
                  shadowElevation = 2.dp
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "HANDMADE IN INDIA ✨",
                      color = Color.White,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold,
                      letterSpacing = 0.5.sp
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = "Warm. Handmade.\nDreamy & Cozy.",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  lineHeight = 25.sp,
                  color = Color(0xFF381E0D)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = "Use code HONEY10 for 10% off! Free craft delivery over ₹999 💛",
                  fontSize = 11.sp,
                  color = Color(0xFF78350F),
                  fontWeight = FontWeight.Medium
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              // Dreamy Logo Badge with gentle glow
              Box(
                modifier = Modifier
                  .size(80.dp)
                  .shadow(6.dp, CircleShape, spotColor = Color(0x44F59E0B))
                  .clip(CircleShape)
                  .border(2.5.dp, Color(0xFFF59E0B), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.honeycomb_logo),
                  contentDescription = "Handmade honey bee emblem",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
              }
            }
          }
        }
      }

      // Search & Cute Filter Bar
      item(span = { GridItemSpan(2) }) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search plushies, cardigans, kits...") },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFFB45309))
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF78716C))
                }
              }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("catalog_search_input")
          )

          // Dreamy Category filter chips
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
          ) {
            items(categories, key = { it }) { category ->
              val isSelected = selectedCategory == category
              val categoryLabel = when (category) {
                "All" -> "All ✨"
                "Amigurumi" -> "Amigurumi 🐝"
                "Sweaters" -> "Sweaters 🌸"
                "Blankets" -> "Blankets ☁️"
                "Accessories" -> "Accessories 🧶"
                "Kits & Yarn" -> "DIY Kits 🎨"
                else -> category
              }

              FilterChip(
                selected = isSelected,
                onClick = { selectedCategory = category },
                label = {
                  Text(
                    text = categoryLabel,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp
                  )
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = HoneyGoldPrimary,
                  selectedLabelColor = Color.White,
                  containerColor = Color.White,
                  labelColor = Color(0xFF451A03)
                ),
                shape = RoundedCornerShape(20.dp),
                border = FilterChipDefaults.filterChipBorder(
                  enabled = true,
                  selected = isSelected,
                  borderColor = Color(0xFFF0E3D3)
                ),
                modifier = Modifier.testTag("category_chip_$category")
              )
            }

            item {
              FilterChip(
                selected = inStockOnly,
                onClick = { inStockOnly = !inStockOnly },
                label = {
                  Text(
                    text = if (inStockOnly) "In Stock Only ✓" else "In Stock Only",
                    fontSize = 12.sp,
                    fontWeight = if (inStockOnly) FontWeight.Bold else FontWeight.Medium
                  )
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = Color(0xFFDCFCE7),
                  selectedLabelColor = Color(0xFF15803D),
                  containerColor = Color.White,
                  labelColor = Color(0xFF57483F)
                ),
                shape = RoundedCornerShape(20.dp),
                border = FilterChipDefaults.filterChipBorder(
                  enabled = true,
                  selected = inStockOnly,
                  borderColor = Color(0xFFD1FAE5)
                ),
                modifier = Modifier.testTag("in_stock_filter_chip")
              )
            }
          }
        }
      }

      // Products Header count
      item(span = { GridItemSpan(2) }) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (selectedCategory == "All") "Dreamy Handcrafted Collection ✨" else "$selectedCategory Collection",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF26140A)
          )

          Text(
            text = "${filteredProducts.size} crafts",
            fontSize = 12.sp,
            color = Color(0xFF78716C),
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Product Cards Grid
      if (filteredProducts.isEmpty()) {
        item(span = { GridItemSpan(2) }) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = Color(0xFFD6C7BC),
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "No handcrafted crafts found",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF57483F)
              )
              Text(
                text = "Try clearing filters or search keywords",
                fontSize = 12.sp,
                color = Color(0xFF78716C)
              )
            }
          }
        }
      } else {
        items(filteredProducts, key = { it.id }) { product ->
          ProductCard(
            product = product,
            isWishlisted = wishlist.contains(product.id),
            onProductClicked = onProductClicked,
            onAddToCartClicked = onAddToCartClicked,
            onToggleWishlist = onToggleWishlist
          )
        }
      }

      item(span = { GridItemSpan(2) }) {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
