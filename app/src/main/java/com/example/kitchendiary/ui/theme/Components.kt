package com.example.kitchendiary.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun categoryEmoji(category: String): String = when (category) {
    "Breakfast" -> "🍳"
    "Main Course" -> "🍲"
    "Dessert" -> "🍰"
    "Snack" -> "🍪"
    else -> "🍴"
}

fun categoryColor(category: String): Color = when (category) {
    "Breakfast" -> Butter
    "Main Course" -> Peach
    "Dessert" -> Blush
    "Snack" -> Mint
    else -> Lavender
}

@Composable
fun CuteHeader(title: String, subtitle: String? = null) {
    Column(Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        if (subtitle != null) {
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CategoryPill(category: String) {
    Surface(shape = RoundedCornerShape(50), color = Color.White.copy(alpha = 0.8f)) {
        Text(
            "${categoryEmoji(category)} $category",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun EmojiBadge(emoji: String, size: Dp = 52.dp) {
    Box(
        modifier = Modifier.size(size).clip(CircleShape).background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = (size.value * 0.5f).sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuteChip(
    label: String,
    selected: Boolean,
    selectedColor: Color = BubblegumPink,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        shape = RoundedCornerShape(50),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = Cocoa,
            selectedContainerColor = selectedColor,
            selectedLabelColor = Cocoa
        )
    )
}

@Composable
fun cuteFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedBorderColor = RosePink,
    unfocusedBorderColor = PinkBorder,
    focusedLabelColor = DeepRose,
    cursorColor = DeepRose
)