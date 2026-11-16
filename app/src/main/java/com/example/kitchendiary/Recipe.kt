package com.example.kitchendiary

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

enum class SortOption(val label: String) {
    TITLE_ASC("A–Z 🔤"),
    TITLE_DESC("Z–A 🔤"),
    DATE_NEWEST("Newest 📅"),
    DATE_OLDEST("Oldest 📅"),
    CATEGORY("Category 🏷️")
}

data class Recipe(
    val id: Int,
    val title: String,
    val category: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val isFavorite: Boolean = false,
    val imageUri: String? = null,
    val dateAdded: Long = System.currentTimeMillis()
)

object RecipeRepository {
    val categories = listOf("Breakfast", "Main Course", "Dessert", "Snack")

    private val _recipes = mutableStateListOf<Recipe>()
    private var prefs: SharedPreferences? = null
    private val gson = Gson()
    private const val PREFS_NAME = "kitchen_diary_prefs"
    private const val KEY_RECIPES = "saved_recipes"

    fun init(context: Context) {
        if (prefs != null) return
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs?.getString(KEY_RECIPES, null)

        if (!json.isNullOrEmpty()) {
            try {
                val type = object : TypeToken<List<Recipe>>() {}.type
                val loadedList: List<Recipe> = gson.fromJson(json, type)
                _recipes.clear()
                _recipes.addAll(loadedList)
            } catch (e: Exception) {
                loadInitialDefaults()
            }
        } else {
            loadInitialDefaults()
        }
    }

    private fun loadInitialDefaults() {
        _recipes.clear()
        _recipes.addAll(
            listOf(
                Recipe(
                    id = 1,
                    title = "Strawberry Pancakes 🥞",
                    category = "Breakfast",
                    ingredients = listOf(
                        "1 cup Flour",
                        "1 Egg",
                        "1/2 cup Milk",
                        "1 tbsp Sugar",
                        "Fresh Strawberries",
                        "Maple Syrup"
                    ),
                    steps = listOf(
                        "Mix flour, egg, milk, and sugar in a bowl until smooth.",
                        "Heat a non-stick pan over medium heat and pour pancake batter.",
                        "Cook until bubbles form on top, flip and cook until golden brown.",
                        "Top with fresh strawberries and maple syrup, then enjoy!"
                    ),
                    isFavorite = true,
                    dateAdded = System.currentTimeMillis() - 3000000
                ),
                Recipe(
                    id = 2,
                    title = "Creamy Carbonara 🍝",
                    category = "Main Course",
                    ingredients = listOf(
                        "200g Spaghetti",
                        "2 Fresh Eggs",
                        "50g Parmesan Cheese",
                        "100g Pancetta or Bacon",
                        "Black Pepper"
                    ),
                    steps = listOf(
                        "Boil spaghetti in salted water until al dente.",
                        "Fry pancetta in a pan until crispy and golden.",
                        "Whisk eggs and parmesan together in a small bowl.",
                        "Combine hot pasta, pancetta, and egg mixture off the heat, tossing quickly to create a creamy sauce."
                    ),
                    isFavorite = false,
                    dateAdded = System.currentTimeMillis() - 2000000
                ),
                Recipe(
                    id = 3,
                    title = "Matcha Cheesecake 🍰",
                    category = "Dessert",
                    ingredients = listOf(
                        "200g Cream Cheese",
                        "1 tbsp Matcha Powder",
                        "100g Sugar",
                        "2 Eggs",
                        "200ml Heavy Cream"
                    ),
                    steps = listOf(
                        "Beat cream cheese and sugar together until smooth.",
                        "Sift in matcha powder and mix thoroughly.",
                        "Add eggs one at a time, then pour in heavy cream.",
                        "Bake at 160°C for 45 minutes and chill in the fridge before serving."
                    ),
                    isFavorite = true,
                    dateAdded = System.currentTimeMillis() - 1000000
                )
            )
        )
        saveToPrefs()
    }

    private fun saveToPrefs() {
        val json = gson.toJson(_recipes.toList())
        prefs?.edit()?.putString(KEY_RECIPES, json)?.apply()
    }

    val recipes: List<Recipe> get() = _recipes

    fun findById(id: Int): Recipe? = _recipes.find { it.id == id }

    fun addRecipe(
        title: String,
        category: String,
        ingredients: List<String>,
        steps: List<String>,
        imageUri: String? = null
    ) {
        val newId = (_recipes.maxOfOrNull { it.id } ?: 0) + 1
        _recipes.add(
            Recipe(
                id = newId,
                title = title.ifBlank { "Untitled Recipe" },
                category = category,
                ingredients = ingredients,
                steps = steps,
                isFavorite = false,
                imageUri = imageUri,
                dateAdded = System.currentTimeMillis()
            )
        )
        saveToPrefs()
    }

    fun updateRecipe(
        id: Int,
        title: String,
        category: String,
        ingredients: List<String>,
        steps: List<String>,
        imageUri: String? = null
    ) {
        val index = _recipes.indexOfFirst { it.id == id }
        if (index != -1) {
            val old = _recipes[index]
            _recipes[index] = old.copy(
                title = title.ifBlank { "Untitled Recipe" },
                category = category,
                ingredients = ingredients,
                steps = steps,
                imageUri = imageUri
            )
            saveToPrefs()
        }
    }

    fun deleteRecipe(id: Int) {
        _recipes.removeAll { it.id == id }
        saveToPrefs()
    }

    fun toggleFavorite(id: Int): Boolean {
        val index = _recipes.indexOfFirst { it.id == id }
        var isFav = false
        if (index != -1) {
            val old = _recipes[index]
            isFav = !old.isFavorite
            _recipes[index] = old.copy(isFavorite = isFav)
            saveToPrefs()
        }
        return isFav
    }

    fun getSortedFilteredRecipes(
        query: String,
        selectedCategory: String,
        sortOption: SortOption
    ): List<Recipe> {
        val filtered = _recipes.filter { recipe ->
            (selectedCategory == "All" || recipe.category == selectedCategory) &&
            (query.isBlank() || recipe.title.contains(query, ignoreCase = true))
        }

        return when (sortOption) {
            SortOption.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
            SortOption.TITLE_DESC -> filtered.sortedByDescending { it.title.lowercase() }
            SortOption.DATE_NEWEST -> filtered.sortedByDescending { it.dateAdded }
            SortOption.DATE_OLDEST -> filtered.sortedBy { it.dateAdded }
            SortOption.CATEGORY -> filtered.sortedBy { it.category }
        }
    }
}

object SampleData {
    val categories get() = RecipeRepository.categories
    val recipes get() = RecipeRepository.recipes
    fun findById(id: Int): Recipe? = RecipeRepository.findById(id)
}
