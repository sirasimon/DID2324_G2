package com.example.shoppinglist

import androidx.compose.ui.graphics.Color

enum class ItemCategory {
    NONE, BEVANDE, CURA_PERSONA, CURA_CASA, FORNO, GASTRONOMIA, ORTOFRUTTA, SURGELATI,
    //FRESCHI
}

data class ItemUI(val bg: Color, val txt:Color)

val catColors = mapOf(
    ItemCategory.NONE to ItemUI(Color(0xFFDDDDDD), Color(0xFF868686)),
    ItemCategory.BEVANDE to ItemUI(Color(0xFFC7CBFD), Color(0xFF2B2294)),
    ItemCategory.CURA_PERSONA to ItemUI(Color(0xFFFDC2DD), Color(0xFF98296C)),
    ItemCategory.CURA_CASA to ItemUI(Color(0xFFEEC6A1), Color(0xFFC34825)),
    ItemCategory.FORNO to ItemUI(Color(0xFFFAF367), Color(0xFFF77C00)),
    //ItemCategory.FRESCHI to ItemUI(Color.Blue, Color.Black),
    ItemCategory.GASTRONOMIA to ItemUI(Color(0xFFFFCD81), Color(0xFFE65502)),
    ItemCategory.ORTOFRUTTA to ItemUI(Color(0xFFACFC8D), Color(0xFF009616)),
    ItemCategory.SURGELATI to ItemUI(Color(0xFFB3EBFE), Color(0xFF0065A9)),
)

val catImgs = mapOf(
    ItemCategory.NONE to R.drawable.resource_null,
    ItemCategory.BEVANDE to R.drawable.bevande,
    ItemCategory.CURA_PERSONA to R.drawable.cura_persona,
    ItemCategory.CURA_CASA to R.drawable.cura_casa,
    ItemCategory.FORNO to R.drawable.forno,
    ItemCategory.GASTRONOMIA to R.drawable.gastronomia,
    ItemCategory.ORTOFRUTTA to R.drawable.ortofrutta,
    ItemCategory.SURGELATI to R.drawable.surgelati,
)

val catIcons = mapOf(
    ItemCategory.NONE to R.drawable.null_icon,
    ItemCategory.BEVANDE to R.drawable.bevande_icon,
    ItemCategory.CURA_PERSONA to R.drawable.cura_persona_icon,
    ItemCategory.CURA_CASA to R.drawable.cura_casa_icon,
    ItemCategory.FORNO to R.drawable.forno_icon,
    ItemCategory.GASTRONOMIA to R.drawable.gastronomia_icon,
    ItemCategory.ORTOFRUTTA to R.drawable.ortofrutta_icon,
    ItemCategory.SURGELATI to R.drawable.surgelati_icon,
)