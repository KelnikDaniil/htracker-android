package com.kelnik.htracker.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.kelnik.htracker.R

val typography = Typography(
    brand = TextStyle(
        fontFamily = FontFamily(Font(R.font.oregano_regular)),
        fontSize = 84.sp,
        letterSpacing = 2.5.sp,
    ),
    iconHint = TextStyle(
        fontFamily = FontFamily(Font(R.font.raleway_bold)),
        fontSize = 14.sp,
    ),
    titleTopBar = TextStyle(
        fontFamily = FontFamily(Font(R.font.raleway_bold)),
        fontSize = 20.sp,
    ),
    subtitleTopBar = TextStyle(
        fontFamily = FontFamily(Font(R.font.raleway_medium)),
        fontSize = 14.sp,
    ),

)

data class Typography(
    val brand: TextStyle,
    val iconHint: TextStyle,
    val titleTopBar: TextStyle,
    val subtitleTopBar: TextStyle,
)