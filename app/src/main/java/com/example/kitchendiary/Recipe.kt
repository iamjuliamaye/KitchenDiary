package com.example.kitchendiary

data class Recipe(
    val id: Int,
    val title: String,
    val category: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val isFavorite: Boolean = false
)

object SampleData {
    val categories = listOf("Breakfast", "Main Course", "Dessert", "Snack")

    val recipes = emptyList<Recipe>()

    fun findById(id: Int): Recipe? = recipes.find { it.id == id }
}
