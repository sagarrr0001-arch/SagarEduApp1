package com.example.sagareduapp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sagareduapp1.screen.GameScreen
import com.example.sagareduapp1.screen.LandingScreen
import com.example.sagareduapp1.screen.ScoreScreen
import com.example.sagareduapp1.screen.SettingScreen
import com.example.sagareduapp1.screen.TestDBScreen
import com.example.sagareduapp1.ui.theme.SagarEduApp1Theme
import com.example.sagareduapp1.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main entry point of the application.
 * This activity hosts all the Jetpack Compose screens using a single-activity architecture.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge support for modern Android look
        enableEdgeToEdge()
        setContent {
            // Apply the custom educational theme to the whole app
            SagarEduApp1Theme {
                AppNav()
            }
        }
    }
}

/**
 * Handles all navigation logic within the app using Navigation Compose.
 */
@Composable
fun AppNav() {
    val navController = rememberNavController()
    // Inject the ViewModel using Koin (Dependency Injection)
    val viewModel: AppViewModel = koinViewModel()

    // NavHost defines the "map" of our application screens
    NavHost(navController = navController, startDestination = "landing") {
        
        // 1. Landing Screen: Where user enters their name
        composable("landing") { LandingScreen(navController, viewModel) }
        
        // 2. Game Screen: Where the actual mission/puzzle happens
        composable("game") { GameScreen(navController, viewModel) }
        
        // 3. Setting Screen: Level selection and user dashboard
        composable("setting") { SettingScreen(navController, viewModel) }
        
        // 4. Score Screen: Displays final results (Passes totalQuestions as an argument)
        composable(
            route = "score/{totalQuestions}",
            arguments = listOf(navArgument("totalQuestions") { type = NavType.IntType })
        ) { backStackEntry ->
            val totalQuestions = backStackEntry.arguments?.getInt("totalQuestions") ?: 0
            ScoreScreen(navController, viewModel, totalQuestions)
        }
        
        // 5. Test Database Screen: For debugging Room DB records
        composable("testDB") { TestDBScreen(viewModel) }
    }
}
