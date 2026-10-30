package com.example.kitchendiary

import com.example.kitchendiary.ui.theme.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    recipeId: Int,          // -1 = adding a new recipe, otherwise editing
    onBack: () -> Unit
) {
    val existing = RecipeRepository.findById(recipeId)
    val isEditing = existing != null

    // Pre-fill the form when editing; empty when adding
    var title by remember { mutableStateOf(existing?.title ?: "") }
    var category by remember { mutableStateOf(existing?.category ?: RecipeRepository.categories.first()) }
    var ingredients by remember { mutableStateOf(existing?.ingredients?.joinToString("\n") ?: "") }
    var steps by remember { mutableStateOf(existing?.steps?.joinToString("\n") ?: "") }
    var titleError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Edit Recipe ✏️" else "New Recipe 🍓",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepRose
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepRose
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (it.isNotBlank()) titleError = false
                },
                label = { Text("🍽️ Recipe name") },
                isError = titleError,
                supportingText = {
                    if (titleError) {
                        Text("Please enter a recipe title 💕", color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                colors = cuteFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Category", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RecipeRepository.categories.forEach { c ->
                    CuteChip(
                        label = "${categoryEmoji(c)} $c",
                        selected = category == c,
                        selectedColor = categoryColor(c)
                    ) { category = c }
                }
            }

            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text("🥕 Ingredients (one per line)") },
                minLines = 4,
                shape = RoundedCornerShape(22.dp),
                colors = cuteFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = steps,
                onValueChange = { steps = it },
                label = { Text("👩‍🍳 Steps (one per line)") },
                minLines = 4,
                shape = RoundedCornerShape(22.dp),
                colors = cuteFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        val ingredientList = ingredients.lines().map { it.trim() }.filter { it.isNotEmpty() }
                        val stepList = steps.lines().map { it.trim() }.filter { it.isNotEmpty() }

                        if (isEditing) {
                            RecipeRepository.updateRecipe(
                                id = recipeId,
                                title = title.trim(),
                                category = category,
                                ingredients = ingredientList,
                                steps = stepList
                            )
                        } else {
                            RecipeRepository.addRecipe(
                                title = title.trim(),
                                category = category,
                                ingredients = ingredientList,
                                steps = stepList
                            )
                        }
                        onBack()
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = DeepRose),
                modifier = Modifier.fillMaxWidth().height(54.dp)
            ) {
                Text(
                    if (isEditing) "Update Recipe 💕" else "Save Recipe 💕",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
