package com.example.kitchendiary

object Routes {
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val DETAIL = "detail/{recipeId}"
    const val ADD_EDIT = "add_edit?recipeId={recipeId}"

    fun detail(recipeId: Int) = "detail/$recipeId"
    fun addEdit(recipeId: Int = -1) = "add_edit?recipeId=$recipeId"
}