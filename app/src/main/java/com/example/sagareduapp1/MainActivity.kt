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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SagarEduApp1Theme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = koinViewModel()

    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") { LandingScreen(navController, viewModel) }
        composable("game") { GameScreen(navController, viewModel) }
        composable("setting") { SettingScreen(navController, viewModel) }
        composable(
            route = "score/{totalQuestions}",
            arguments = listOf(navArgument("totalQuestions") { type = NavType.IntType })
        ) { backStackEntry ->
            val totalQuestions = backStackEntry.arguments?.getInt("totalQuestions") ?: 0
            ScoreScreen(navController, viewModel, totalQuestions)
        }
        composable("testDB") { TestDBScreen(viewModel) }
    }
}
