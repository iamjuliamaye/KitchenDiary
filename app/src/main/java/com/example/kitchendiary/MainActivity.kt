package com.example.kitchendiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kitchendiary.ui.theme.KitchenDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KitchenDiaryTheme {
                KitchenDiaryApp()
            }
        }
    }
}