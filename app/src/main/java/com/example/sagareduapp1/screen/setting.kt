package com.example.sagareduapp1.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
fun SettingScreen(navController: NavHostController, viewModel: AppViewModel) {
    val currentLevel by viewModel.level.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allScores by viewModel.allUsers.collectAsStateWithLifecycle(initialValue = emptyList())

    val levels = listOf("1", "2", "3")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("GAME LOBBY", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = EduBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome, ${currentUser?.username ?: "Player"}!",
                style = MaterialTheme.typography.titleLarge,
                color = EduBlue
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "CHOOSE YOUR CHALLENGE",
                style = MaterialTheme.typography.labelLarge,
                color = EduTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                levels.forEach { level ->
                    val isSelected = level == currentLevel
                    Card(
                        modifier = Modifier
                            .size(90.dp)
                            .clickable { viewModel.setLevel(level) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) EduBlue else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = level,
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = 32.sp,
                                    color = if (isSelected) Color.White else EduBlue
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("game") },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EduGreen)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("START MISSION", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "YOUR DASHBOARD",
                style = MaterialTheme.typography.labelLarge,
                color = EduTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                if (allScores.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No missions completed yet!", color = Color.Gray)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        items(allScores.filter { it.username == currentUser?.username }.sortedByDescending { it.date }) { scoreRecord ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Level ${scoreRecord.level}", fontWeight = FontWeight.Bold)
                                    Text(
                                        java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault()).format(java.util.Date(scoreRecord.date)),
                                        fontSize = 12.sp, color = Color.Gray
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = EduOrange, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("${scoreRecord.score}", style = MaterialTheme.typography.titleLarge, color = EduBlue)
                                }
                            }
                            HorizontalDivider(modifier = Modifier.alpha(0.5f))
                        }
                    }
                }
            }
        }
    }
}
