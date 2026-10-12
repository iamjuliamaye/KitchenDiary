package com.example.kitchendiary


import com.example.kitchendiary.ui.theme.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun KitchenDiaryApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Bottom bar only appears on the two top-level screens
    val showBottomBar = currentRoute == Routes.HOME || currentRoute == Routes.FAVORITES

    val navColors = NavigationBarItemDefaults.colors(
        selectedIconColor = DeepRose,
        selectedTextColor = DeepRose,
        indicatorColor = Blush,
        unselectedIconColor = CocoaSoft,
        unselectedTextColor = CocoaSoft
    )

    Scaffold(
        // Inner screens have their own Scaffold/TopAppBar, so avoid double insets
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = currentRoute == Routes.HOME,
                        onClick = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Recipes") },
                        label = { Text("Recipes") },
                        colors = navColors
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.FAVORITES,
                        onClick = {
                            navController.navigate(Routes.FAVORITES) {
                                popUpTo(Routes.HOME)
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                        label = { Text("Favorites") },
                        colors = navColors
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                RecipeListScreen(
                    onRecipeClick = { id -> navController.navigate(Routes.detail(id)) },
                    onAddClick = { navController.navigate(Routes.addEdit()) }
                )
            }

            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    onRecipeClick = { id -> navController.navigate(Routes.detail(id)) }
                )
            }

            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("recipeId") ?: -1
                RecipeDetailScreen(
                    recipeId = id,
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate(Routes.addEdit(id)) },
                    onDeleted = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.ADD_EDIT,
                arguments = listOf(navArgument("recipeId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { entry ->
                val id = entry.arguments?.getInt("recipeId") ?: -1
                AddEditRecipeScreen(
                    recipeId = id,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}