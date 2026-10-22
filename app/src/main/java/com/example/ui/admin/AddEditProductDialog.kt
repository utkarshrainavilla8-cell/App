package com.example.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HoneycombRepository
import com.example.model.Product
import com.example.ui.theme.HoneyGoldPrimary
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductDialog(
  productToEdit: Product? = null,
  onDismiss: () -> Unit,
  onSaveProduct: (Product) -> Unit
) {
  val isEditing = productToEdit != null

  var title by remember { mutableStateOf(productToEdit?.title ?: "") }
  var category by remember { mutableStateOf(productToEdit?.category ?: "Amigurumi") }
  var priceString by remember { mutableStateOf(productToEdit?.price?.toInt()?.toString() ?: "899") }
  var stockQuantityString by remember { mutableStateOf(productToEdit?.stockQuantity?.toString() ?: "10") }
  var isOutOfStock by remember { mutableStateOf(productToEdit?.isOutOfStock ?: false) }
  var description by remember { mutableStateOf(productToEdit?.description ?: "") }
  var materials by remember { mutableStateOf(productToEdit?.materials ?: "100% Cotton Yarn") }
  var badge by remember { mutableStateOf(productToEdit?.badge ?: "") }
  var iconType by remember { mutableStateOf(productToEdit?.iconType ?: "BEE") }

  var categoryDropdownExpanded by remember { mutableStateOf(false) }
  val categoryOptions = listOf("Amigurumi", "Sweaters", "Blankets", "Accessories", "Kits & Yarn")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (isEditing) "Edit Handmade Craft ✨" else "Add New Handmade Craft ✨",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color(0xFF26140A)
      )
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Product Title *") },
          placeholder = { Text("e.g. Cozy Honeycomb Cardigan") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("product_title_input"),
          shape = RoundedCornerShape(12.dp)
        )

        // Category dropdown
        ExposedDropdownMenuBox(
          expanded = categoryDropdownExpanded,
          onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
        ) {
          OutlinedTextField(
            value = category,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
            modifier = Modifier
              .menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )
          ExposedDropdownMenu(
            expanded = categoryDropdownExpanded,
            onDismissRequest = { categoryDropdownExpanded = false }
          ) {
            categoryOptions.forEach { opt ->
              DropdownMenuItem(
                text = { Text(opt) },
                onClick = {
                  category = opt
                  categoryDropdownExpanded = false
                  iconType = when (opt) {
                    "Amigurumi" -> "BEE"
                    "Sweaters" -> "SWEATER"
                    "Blankets" -> "BLANKET"
                    "Accessories" -> "BEANIE"
                    "Kits & Yarn" -> "KIT"
                    else -> "BEE"
                  }
                }
              )
            }
          }
        }

        // Price in Indian Rupees and Stock
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = priceString,
            onValueChange = { priceString = it },
            label = { Text("Price (₹) *") },
            placeholder = { Text("899") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f).testTag("product_price_input"),
            shape = RoundedCornerShape(12.dp)
          )

          OutlinedTextField(
            value = stockQuantityString,
            onValueChange = { stockQuantityString = it },
            label = { Text("Stock Qty *") },
            placeholder = { Text("10") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f).testTag("product_stock_input"),
            shape = RoundedCornerShape(12.dp)
          )
        }

        // Out of Stock Switch
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Mark as Out of Stock",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFF381E0D)
            )
            Text(
              text = "Hides purchase option from customers",
              fontSize = 11.sp,
              color = Color(0xFF78716C)
            )
          }

          Switch(
            checked = isOutOfStock,
            onCheckedChange = { isOutOfStock = it },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = Color(0xFFDC2626)
            ),
            modifier = Modifier.testTag("out_of_stock_switch")
          )
        }

        OutlinedTextField(
          value = badge,
          onValueChange = { badge = it },
          label = { Text("Badge Label (Optional)") },
          placeholder = { Text("e.g. Bestseller ✨, New 🌸") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
          value = materials,
          onValueChange = { materials = it },
          label = { Text("Materials") },
          placeholder = { Text("100% Organic Cotton Yarn") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description") },
          placeholder = { Text("Handcrafted details, stitching notes...") },
          minLines = 2,
          maxLines = 4,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val parsedPrice = priceString.toDoubleOrNull() ?: 899.0
          val parsedStock = stockQuantityString.toIntOrNull() ?: 10

          val product = if (isEditing) {
            productToEdit!!.copy(
              title = title.ifBlank { "Handmade Craft" },
              category = category,
              price = parsedPrice,
              stockQuantity = parsedStock,
              isOutOfStock = isOutOfStock || parsedStock <= 0,
              badge = badge.ifBlank { null },
              materials = materials.ifBlank { "100% Cotton Yarn" },
              description = description.ifBlank { "Handmade with love by our workshop." },
              iconType = iconType
            )
          } else {
            Product(
              id = "prod-${UUID.randomUUID().toString().take(6)}",
              title = title.ifBlank { "Handmade Craft" },
              category = category,
              price = parsedPrice,
              stockQuantity = parsedStock,
              isOutOfStock = isOutOfStock || parsedStock <= 0,
              badge = badge.ifBlank { null },
              materials = materials.ifBlank { "100% Cotton Yarn" },
              description = description.ifBlank { "Handmade with love by our workshop." },
              iconType = iconType,
              rating = 5.0f,
              reviewCount = 0,
              salesCount = 0
            )
          }

          onSaveProduct(product)
        },
        enabled = title.isNotBlank() && priceString.toDoubleOrNull() != null,
        colors = ButtonDefaults.buttonColors(containerColor = HoneyGoldPrimary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("save_product_button")
      ) {
        Text(if (isEditing) "Save Changes" else "Add Craft ✨")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = Color(0xFF78716C))
      }
    },
    shape = RoundedCornerShape(22.dp),
    containerColor = Color.White
  )
}

@Composable
fun QuickChangePriceDialog(
  product: Product,
  onDismiss: () -> Unit,
  onConfirmPrice: (Double) -> Unit
) {
  var newPriceString by remember { mutableStateOf(product.price.toInt().toString()) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Change Price in Rupees (₹)",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color(0xFF26140A)
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = product.title,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          color = Color(0xFF57483F)
        )
        Text(
          text = "Current price: ${HoneycombRepository.formatInr(product.price)}",
          fontSize = 12.sp,
          color = Color(0xFF78716C)
        )

        OutlinedTextField(
          value = newPriceString,
          onValueChange = { newPriceString = it },
          label = { Text("New Price (₹)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("quick_price_input"),
          shape = RoundedCornerShape(12.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val price = newPriceString.toDoubleOrNull()
          if (price != null && price > 0) {
            onConfirmPrice(price)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = HoneyGoldPrimary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("confirm_price_button")
      ) {
        Text("Update Price ✨")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = Color(0xFF78716C))
      }
    },
    shape = RoundedCornerShape(22.dp),
    containerColor = Color.White
  )
}
