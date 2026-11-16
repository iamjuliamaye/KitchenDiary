package com.example.kitchendiary

import android.content.Context
import android.net.Uri
import com.example.kitchendiary.ui.theme.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File
import java.io.FileOutputStream

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val fileName = "recipe_photo_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    recipeId: Int,
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val existing = RecipeRepository.findById(recipeId)
    val isEditing = existing != null


    var title by remember { mutableStateOf(existing?.title ?: "") }
    var category by remember { mutableStateOf(existing?.category ?: RecipeRepository.categories.first()) }
    var ingredients by remember { mutableStateOf(existing?.ingredients?.joinToString("\n") ?: "") }
    var steps by remember { mutableStateOf(existing?.steps?.joinToString("\n") ?: "") }
    var imageUri by remember { mutableStateOf(existing?.imageUri) }
    var titleError by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val permanentPath = saveImageToInternalStorage(context, uri)
            if (permanentPath != null) {
                imageUri = permanentPath
            }
        }
    }

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

            Text("Recipe Photo 📷", style = MaterialTheme.typography.titleMedium)
            if (!imageUri.isNullOrEmpty()) {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Recipe Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(26.dp))
                        )
                        IconButton(
                            onClick = { imageUri = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove photo", tint = Color.White)
                        }
                    }
                }
            } else {
                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = DeepRose)
                    Spacer(Modifier.height(8.dp))
                    Text("Select Photo from Gallery 🖼️", color = DeepRose)
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
                                steps = stepList,
                                imageUri = imageUri
                            )
                            onShowSnackbar("Recipe '$title' updated successfully! ✏️")
                        } else {
                            RecipeRepository.addRecipe(
                                title = title.trim(),
                                category = category,
                                ingredients = ingredientList,
                                steps = stepList,
                                imageUri = imageUri
                            )
                            onShowSnackbar("Recipe '$title' added successfully! 🍓")
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
