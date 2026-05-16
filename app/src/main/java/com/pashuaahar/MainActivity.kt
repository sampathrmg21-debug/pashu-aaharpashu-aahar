package com.pashuaahar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pashuaahar.ui.PashuAaharApp
import com.pashuaahar.ui.theme.PashuAaharTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PashuAaharTheme {
                PashuAaharApp()
            }
        }
    }
}
