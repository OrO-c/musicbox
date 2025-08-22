package com.example.universalvoicebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.universalvoicebox.ui.screen.ImportWizardScreen
import com.example.universalvoicebox.ui.screen.VoiceLibraryScreen
import com.example.universalvoicebox.ui.theme.UniversalVoiceBoxTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 主Activity
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniversalVoiceBoxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UniversalVoiceBoxApp()
                }
            }
        }
    }
}

/**
 * 应用主界面
 */
@Composable
fun UniversalVoiceBoxApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "voice_library"
    ) {
        composable("voice_library") {
            VoiceLibraryScreen(
                onNavigateToImport = {
                    navController.navigate("import_wizard")
                }
            )
        }
        
        composable("import_wizard") {
            ImportWizardScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}