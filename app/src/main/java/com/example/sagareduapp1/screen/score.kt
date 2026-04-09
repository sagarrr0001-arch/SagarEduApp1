package com.example.sagareduapp1.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sagareduapp1.viewmodel.AppViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sagareduapp1.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(navController: NavHostController, viewModel: AppViewModel, totalQuestions: Int) {
    val users by viewModel.allUsers.collectAsStateWithLifecycle(initialValue = emptyList())
    val currentScore by viewModel.score.collectAsStateWithLifecycle()

    val maxPossibleScore = totalQuestions * 10
    val isFullScore = currentScore >= maxPossibleScore && maxPossibleScore > 0

    val themeGradient = remember(isFullScore) {
        if (isFullScore) {
            listOf(Color(0xFFFFD700), Color(0xFFFFF176), Color(0xFF81C784))
        } else {
            listOf(Color(0xFF90A4AE), Color(0xFFCFD8DC), Color(0xFF546E7A))
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MISSION RESULTS", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White.copy(alpha = 0.3f))
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(themeGradient))
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isFullScore) "UNBELIEVABLE! PERFECT!" else "GOOD EFFORT!",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                    color = if (isFullScore) Color(0xFF2E7D32) else Color(0xFF37474F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("FINAL SCORE", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                        Text(
                            "$currentScore",
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp),
                            color = if (isFullScore) EduOrange else EduBlue
                        )
                        Text("out of $maxPossibleScore", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Button(
                    onClick = {
                        viewModel.resetScore()
                        navController.navigate("setting") {
                            popUpTo("landing") { inclusive = false }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFullScore) EduGreen else EduBlue
                    )
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("PLAY AGAIN", style = MaterialTheme.typography.labelLarge)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "TOP AGENTS",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                ) {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(users.sortedByDescending { it.score }.take(10)) { user ->
                            ListItem(
                                headlineContent = { Text(user.username, fontWeight = FontWeight.Bold) },
                                supportingContent = { Text("Lvl ${user.level} • Score: ${user.score}") },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        }
                    }
                }
            }
        }
    }
}
