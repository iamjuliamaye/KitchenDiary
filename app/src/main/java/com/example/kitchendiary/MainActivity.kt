package com.example.kitchendiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.kitchendiary.ui.theme.KitchenDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RecipeRepository.init(applicationContext)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            KitchenDiaryTheme(darkTheme = isDarkTheme) {
                KitchenDiaryApp(
                    isDarkTheme = isDarkTheme,
                    onToggleDarkTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}
