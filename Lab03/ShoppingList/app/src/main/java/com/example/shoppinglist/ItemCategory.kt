package com.example.shoppinglist

import androidx.compose.ui.graphics.Color

enum class ItemCategory {
    NONE, BEVANDE, CURA_PERSONA, CURA_CASA, FORNO, FRESCHI, GASTRONOMIA, ORTOFRUTTA, SURGELATI
}

data class ItemUI(val bg: Color, val txt:Color)

val catColors = mapOf(
    ItemCategory.NONE to ItemUI(Color.LightGray, Color.Black),
    ItemCategory.BEVANDE to ItemUI(Color.Blue, Color.Black),
    ItemCategory.CURA_PERSONA to ItemUI(Color.Blue, Color.Black),
    ItemCategory.CURA_CASA to ItemUI(Color.LightGray, Color.Black),
    ItemCategory.FORNO to ItemUI(Color(249, 243, 125), Color(231,131,49)),
    ItemCategory.FRESCHI to ItemUI(Color.Blue, Color.Black),
    ItemCategory.GASTRONOMIA to ItemUI(Color.Red, Color.Black),
    ItemCategory.ORTOFRUTTA to ItemUI(Color.Green, Color.Black),
    ItemCategory.SURGELATI to ItemUI(Color(191, 233, 252), Color(42, 99, 164)),
)