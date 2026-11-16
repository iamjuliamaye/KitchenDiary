package com.example.kitchendiary.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CuteColors = lightColorScheme(
    primary = DeepRose,
    onPrimary = Color.White,
    primaryContainer = Blush,
    onPrimaryContainer = Cocoa,
    secondary = RosePink,
    onSecondary = Color.White,
    secondaryContainer = Blush,
    onSecondaryContainer = Cocoa,
    background = Cream,
    onBackground = Cocoa,
    surface = Color.White,
    onSurface = Cocoa,
    surfaceVariant = Blush,
    onSurfaceVariant = CocoaSoft,
    outline = PinkBorder
)

private val DarkCuteColors = darkColorScheme(
    primary = RosePink,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4A2834),
    onPrimaryContainer = Blush,
    secondary = BubblegumPink,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF3D232A),
    onSecondaryContainer = Blush,
    background = Color(0xFF1E1719),
    onBackground = Color(0xFFFDE8EF),
    surface = Color(0xFF2B2125),
    onSurface = Color(0xFFFDE8EF),
    surfaceVariant = Color(0xFF38292E),
    onSurfaceVariant = Color(0xFFD8B6C1),
    outline = Color(0xFF5A3E48)
)

private val CuteTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 26.sp
    ),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)
)

private val CuteShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun KitchenDiaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkCuteColors else CuteColors
    MaterialTheme(
        colorScheme = colors,
        typography = CuteTypography,
        shapes = CuteShapes,
        content = content
    )
}
