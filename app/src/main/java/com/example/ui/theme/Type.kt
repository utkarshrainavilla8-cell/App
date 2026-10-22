package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val QuicksandFontFamily = FontFamily(
  Font(R.font.quicksand, FontWeight.Normal)
)

val HoneycombTypography = Typography(
  displayLarge = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 40.sp,
    letterSpacing = 0.sp
  ),
  displayMedium = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 36.sp,
    letterSpacing = 0.sp
  ),
  headlineLarge = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    letterSpacing = 0.sp
  ),
  headlineMedium = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.15.sp
  ),
  titleLarge = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.15.sp
  ),
  titleMedium = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    letterSpacing = 0.15.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    lineHeight = 22.sp,
    letterSpacing = 0.25.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.25.sp
  ),
  labelLarge = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp
  ),
  labelMedium = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
  ),
  labelSmall = TextStyle(
    fontFamily = QuicksandFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 10.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.5.sp
  )
)
