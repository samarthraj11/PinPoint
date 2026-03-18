package com.pinpoint.design

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.pinpoint.R


object LocalTypography {
    val navRegular10: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_regular)),
        fontSize = 10.sp,
        lineHeight = 16.sp
    )
    val navHeavy10: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_medium)),
        fontSize = 10.sp,
        lineHeight = 16.sp
    )
    val bodyEmphasized12: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_semibold)),
        fontSize = 12.sp,
        lineHeight = 20.sp
    )
    val bodySmall12: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_regular)),
        fontSize = 12.sp,
        lineHeight = 20.sp
    )
    val bodyMedium14: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_regular)),
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    val headingSmall14: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_medium)),
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    val headingMedium16: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_semibold)),
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    val headingLarge20: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_semibold)),
        fontSize = 20.sp,
        lineHeight = 32.sp
    )
    val heading28: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.lora_semibold)),
        fontSize = 28.sp,
        lineHeight = 32.sp
    )
    val headingLarge32: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_semibold)),
        fontSize = 32.sp,
        lineHeight = 40.sp
    )
    val serifHeadingMedium16: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.lora_medium)),
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    val serifHeadingLarge20: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.lora_medium)),
        fontSize = 20.sp,
        lineHeight = 32.sp
    )
    val buttonDefault14: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_medium)),
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    val h1: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.lora_semibold))
    )
    val h2: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.lora_regular))
    )
    val h3: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.lora_medium))
    )
    val h4: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.lora_medium))
    )
    val labelRegular12: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_regular)),
        fontSize = 12.sp,
        lineHeight = 20.sp
    )
    val labelHeavy12: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_semibold)),
        fontSize = 12.sp,
        lineHeight = 20.sp
    )
    val buttonTertiary14UL: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_regular)),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        textDecoration = TextDecoration.Underline
    )
    val buttonTertiary12UL: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_regular)),
        fontSize = 12.sp,
        lineHeight = 20.sp,
        textDecoration = TextDecoration.Underline
    )
    val numberSmall: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.spacegrotesk_medium)),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    )
    val numberDefault: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.spacegrotesk_medium)),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    )
    val numberMedium: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.spacegrotesk_medium)),
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    )
    val numberLarge: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.spacegrotesk_medium)),
        fontWeight = FontWeight.Medium,
        lineHeight = 62.sp,
        fontSize = 48.sp
    )
}
