package com.example.kitchendiary

import com.example.kitchendiary.ui.theme.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import java.util.Locale

fun scaleIngredient(ingredient: String, multiplier: Int): String {
    if (multiplier == 1) return ingredient

    val fractionRegex = Regex("""^(\d+)/(\d+)""")
    val numberRegex = Regex("""^(\d+(?:\.\d+)?)""")

    val fractionMatch = fractionRegex.find(ingredient)
    if (fractionMatch != null) {
        val num = fractionMatch.groupValues[1].toDoubleOrNull() ?: 1.0
        val den = fractionMatch.groupValues[2].toDoubleOrNull() ?: 1.0
        val scaledVal = (num / den) * multiplier
        val scaledStr = if (scaledVal % 1.0 == 0.0) scaledVal.toInt().toString() else String.format(Locale.US, "%.1f", scaledVal)
        return ingredient.replaceFirst(fractionRegex, scaledStr)
    }

    val numMatch = numberRegex.find(ingredient)
    if (numMatch != null) {
        val valNum = numMatch.groupValues[1].toDoubleOrNull() ?: return ingredient
        val scaledVal = valNum * multiplier
        val scaledStr = if (scaledVal % 1.0 == 0.0) scaledVal.toInt().toString() else String.format(Locale.US, "%.1f", scaledVal)
        return ingredient.replaceFirst(numberRegex, scaledStr)
    }

    return ingredient
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleted: () -> Unit
) {
    val recipe = RecipeRepository.findById(recipeId)
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Feature 2: Serving Size Adjuster state
    var servingMultiplier by remember { mutableIntStateOf(1) }

    // Feature 3: Cooking Timer state
    var timerSeconds by remember { mutableIntStateOf(300) } // 5 minutes default
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerRunning, timerSeconds) {
        if (isTimerRunning && timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        } else if (timerSeconds == 0) {
            isTimerRunning = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepRose
                        )
                    }
                },
                actions = {
                    if (recipe != null) {
                        IconButton(onClick = { RecipeRepository.toggleFavorite(recipe.id) }) {
                            Icon(
                                if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Toggle favorite",
                                tint = DeepRose
                            )
                        }
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DeepRose)
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DeepRose)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (recipe == null) {
            Text(
                "Recipe not found 🥲",
                modifier = Modifier.padding(padding).padding(20.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Feature 1: Photo or Banner Card
                Card(
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = categoryColor(recipe.category)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (!recipe.imageUri.isNullOrEmpty()) {
                            AsyncImage(
                                model = recipe.imageUri,
                                contentDescription = recipe.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(24.dp))
                            )
                            Spacer(Modifier.height(12.dp))
                        } else {
                            EmojiBadge(categoryEmoji(recipe.category), size = 84.dp)
                            Spacer(Modifier.height(12.dp))
                        }

                        Text(
                            recipe.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Cocoa,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        CategoryPill(recipe.category)
                    }
                }

                // Feature 2: Serving Size Adjuster Card
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🍽️ Serving Size", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${servingMultiplier}x",
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepRose,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(1, 2, 3, 4).forEach { mult ->
                                CuteChip(
                                    label = "${mult}x",
                                    selected = servingMultiplier == mult,
                                    onClick = { servingMultiplier = mult }
                                )
                            }
                        }
                    }
                }

                // Ingredients
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("🛒 Ingredients", style = MaterialTheme.typography.titleLarge)
                            if (servingMultiplier > 1) {
                                Text(
                                    "Scaled for ${servingMultiplier}x",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DeepRose
                                )
                            }
                        }

                        recipe.ingredients.forEach { item ->
                            val scaledItem = scaleIngredient(item, servingMultiplier)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier.size(8.dp).background(RosePink, CircleShape)
                                )
                                Text(scaledItem, modifier = Modifier.padding(start = 12.dp))
                            }
                        }
                    }
                }

                // Steps
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("👩‍🍳 Steps", style = MaterialTheme.typography.titleLarge)
                        recipe.steps.forEachIndexed { i, step ->
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier.size(28.dp).background(DeepRose, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${i + 1}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                                Text(step, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
                            }
                        }
                    }
                }

                // Feature 3: Cooking Timer Card
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("⏱️ Cooking Timer", style = MaterialTheme.typography.titleLarge)

                        val minutes = timerSeconds / 60
                        val seconds = timerSeconds % 60
                        val timerText = String.format(Locale.US, "%02d:%02d", minutes, seconds)

                        Text(
                            text = if (timerSeconds == 0) "⏰ Time's up! 🍲" else timerText,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepRose
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { timerSeconds += 60 },
                                shape = RoundedCornerShape(50)
                            ) { Text("+1m") }
                            OutlinedButton(
                                onClick = { timerSeconds += 300 },
                                shape = RoundedCornerShape(50)
                            ) { Text("+5m") }
                            OutlinedButton(
                                onClick = { timerSeconds += 600 },
                                shape = RoundedCornerShape(50)
                            ) { Text("+10m") }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { isTimerRunning = !isTimerRunning },
                                colors = ButtonDefaults.buttonColors(containerColor = DeepRose),
                                shape = RoundedCornerShape(50)
                            ) {
                                Icon(
                                    imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(if (isTimerRunning) "Pause" else "Start")
                            }

                            OutlinedButton(
                                onClick = {
                                    isTimerRunning = false
                                    timerSeconds = 300
                                },
                                shape = RoundedCornerShape(50)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Reset")
                                Spacer(Modifier.width(4.dp))
                                Text("Reset")
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }

    if (showDeleteDialog && recipe != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            shape = RoundedCornerShape(28.dp),
            containerColor = Color.White,
            title = { Text("Delete this recipe? 🥺") },
            text = { Text("This will permanently remove '${recipe.title}' from your diary.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    RecipeRepository.deleteRecipe(recipe.id)
                    onDeleted()
                }) { Text("Delete", color = DeepRose) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Keep it", color = CocoaSoft)
                }
            }
        )
    }
}
