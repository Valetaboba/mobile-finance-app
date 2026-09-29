package com.example.myapplication.wardrobe

import com.example.myapplication.R

enum class ClothingSlot(val title: String) { TOP("Верх"), PANTS("Брюки и шорты"), SHOES("Обувь") }

/** Coordinates are in the 520 × 800 body canvas; crop removes each PNG's transparent margins. */
data class LayerRect(val x: Int, val y: Int, val width: Int, val height: Int)
data class ClothingItem(
    val productId: String, val slot: ClothingSlot, val imageRes: Int,
    val crop: LayerRect, val placement: LayerRect,
)

object ClothingCatalog {
    val shirtPlacement = LayerRect(82, 394, 286, 210)
    val pantsPlacement = LayerRect(96, 559, 269, 159)
    val shoesPlacement = LayerRect(92, 687, 283, 61)
    val items = listOf(
        ClothingItem("top_tshirt", ClothingSlot.TOP, R.drawable.clothes_shirt_1, LayerRect(18,44,259,210), shirtPlacement),
        ClothingItem("jacket_wind", ClothingSlot.TOP, R.drawable.clothes_shirt_2, LayerRect(27,50,258,209), shirtPlacement),
        ClothingItem("jacket_denim", ClothingSlot.TOP, R.drawable.clothes_shirt_3, LayerRect(28,49,263,207), shirtPlacement),
        ClothingItem("jacket_winter", ClothingSlot.TOP, R.drawable.clothes_shirt_5, LayerRect(9,34,260,205), shirtPlacement),
        ClothingItem("shorts_sand", ClothingSlot.PANTS, R.drawable.clothes_pants_1, LayerRect(24,117,425,326), pantsPlacement),
        ClothingItem("shorts_sport", ClothingSlot.PANTS, R.drawable.clothes_pants_2, LayerRect(30,105,426,326), pantsPlacement),
        ClothingItem("pants_jeans", ClothingSlot.PANTS, R.drawable.clothes_pants_1, LayerRect(24,117,425,326), pantsPlacement),
        ClothingItem("pants_cargo", ClothingSlot.PANTS, R.drawable.clothes_pants_2, LayerRect(30,105,426,326), pantsPlacement),
        ClothingItem("shoes_canvas", ClothingSlot.SHOES, R.drawable.clothes_shoes_1, LayerRect(12,24,281,57), shoesPlacement),
        ClothingItem("shoes_running", ClothingSlot.SHOES, R.drawable.clothes_shoes_2, LayerRect(7,10,280,57), shoesPlacement),
        ClothingItem("shoes_boots", ClothingSlot.SHOES, R.drawable.clothes_shoes_3, LayerRect(11,14,280,56), shoesPlacement),
    )
    fun item(id: String) = items.firstOrNull { it.productId == id }
    fun canonicalId(id: String): String = when (id) {
        "top_jacket_wind" -> "jacket_wind"
        "top_jacket_denim" -> "jacket_denim"
        "top_jacket_winter" -> "jacket_winter"
        "pants_shorts_sand" -> "shorts_sand"
        "pants_shorts_sport" -> "shorts_sport"
        else -> id
    }
}
