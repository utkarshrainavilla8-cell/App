package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HoneycombRepository
import com.example.model.Product
import com.example.ui.components.ProductArtThumbnail
import com.example.ui.theme.HoneyGoldPrimary

@Composable
fun AdminProductManagementScreen(
  products: List<Product>,
  onAddProduct: () -> Unit,
  onEditProduct: (Product) -> Unit,
  onDeleteProduct: (String) -> Unit,
  onChangePrice: (Product) -> Unit,
  onToggleOutOfStock: (String, Boolean) -> Unit,
  onUpdateStock: (String, Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var stockFilter by remember { mutableStateOf("All") }

  val filteredProducts = products.filter { prod ->
    val matchesSearch = searchQuery.isBlank() || prod.title.contains(searchQuery, ignoreCase = true) || prod.category.contains(searchQuery, ignoreCase = true)
    val matchesFilter = when (stockFilter) {
      "In Stock" -> !prod.isOutOfStock
      "Out of Stock" -> prod.isOutOfStock
      else -> true
    }
    matchesSearch && matchesFilter
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFFFFBF5))
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Top action bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Inventory & Pricing (₹)",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = Color(0xFF26140A)
        )
        Text(
          text = "${products.size} crafts in workshop catalog",
          fontSize = 12.sp,
          color = Color(0xFF78716C)
        )
      }

      Button(
        onClick = onAddProduct,
        colors = ButtonDefaults.buttonColors(containerColor = HoneyGoldPrimary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("admin_add_product_button")
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Add Craft ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Search bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = { Text("Filter products by name or category...") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = HoneyGoldPrimary) },
      singleLine = true,
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("admin_search_product_input")
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Stock Filter chips
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      listOf("All", "In Stock", "Out of Stock").forEach { opt ->
        val isSelected = stockFilter == opt
        FilterChip(
          selected = isSelected,
          onClick = { stockFilter = opt },
          label = { Text(opt, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = HoneyGoldPrimary,
            selectedLabelColor = Color.White,
            containerColor = Color.White,
            labelColor = Color(0xFF451A03)
          ),
          shape = RoundedCornerShape(16.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Products List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(filteredProducts, key = { it.id }) { product ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_product_card_${product.id}"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Header row: Thumbnail, Title, Price, and Edit/Delete
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(RoundedCornerShape(14.dp))
              ) {
                ProductArtThumbnail(product = product, modifier = Modifier.matchParentSize())
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = product.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFF26140A)
                )
                Text(
                  text = "${product.category} · ${product.salesCount} sold",
                  fontSize = 11.sp,
                  color = Color(0xFF78716C)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                  text = HoneycombRepository.formatInr(product.price),
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Color(0xFFD97706)
                )
              }

              Row {
                IconButton(
                  onClick = { onEditProduct(product) },
                  modifier = Modifier.size(32.dp).testTag("edit_product_${product.id}")
                ) {
                  Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
                }
                IconButton(
                  onClick = { onDeleteProduct(product.id) },
                  modifier = Modifier.size(32.dp).testTag("delete_product_${product.id}")
                ) {
                  Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF0E3D3))
            Spacer(modifier = Modifier.height(10.dp))

            // Action row 1: Out of Stock Switch & Stock Counter
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Out of Stock toggle switch
              Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                  checked = product.isOutOfStock,
                  onCheckedChange = { isOut -> onToggleOutOfStock(product.id, isOut) },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFDC2626),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFF15803D)
                  ),
                  modifier = Modifier.testTag("admin_out_of_stock_toggle_${product.id}")
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (product.isOutOfStock) "Out of Stock 🌸" else "In Stock ✨",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (product.isOutOfStock) Color(0xFFDC2626) else Color(0xFF15803D)
                )
              }

              // Stock stepper
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .background(Color(0xFFF3ECE4), RoundedCornerShape(10.dp))
                  .padding(horizontal = 4.dp, vertical = 2.dp)
              ) {
                IconButton(
                  onClick = { onUpdateStock(product.id, (product.stockQuantity - 1).coerceAtLeast(0)) },
                  modifier = Modifier.size(26.dp)
                ) {
                  Icon(Icons.Default.Remove, contentDescription = "Decrease Stock", modifier = Modifier.size(14.dp))
                }
                Text(
                  text = "Stock: ${product.stockQuantity}",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF451A03),
                  modifier = Modifier.padding(horizontal = 6.dp)
                )
                IconButton(
                  onClick = { onUpdateStock(product.id, product.stockQuantity + 1) },
                  modifier = Modifier.size(26.dp)
                ) {
                  Icon(Icons.Default.Add, contentDescription = "Increase Stock", modifier = Modifier.size(14.dp))
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action row 2: Change Price Button in INR
            OutlinedButton(
              onClick = { onChangePrice(product) },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .testTag("change_price_button_${product.id}"),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB45309))
            ) {
              Icon(Icons.Default.PriceChange, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Change Price (Current: ${HoneycombRepository.formatInr(product.price)}) ✨", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
