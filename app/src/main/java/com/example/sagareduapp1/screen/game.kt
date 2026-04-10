package com.example.sagareduapp1.screen

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.sagareduapp1.R
import com.example.sagareduapp1.helper.getAnswerFromFileName
import com.example.sagareduapp1.helper.getBitmapFromAssetsByIndex
import com.example.sagareduapp1.helper.getTotalImagesInLevel
import com.example.sagareduapp1.ui.theme.EduBlue
import com.example.sagareduapp1.ui.theme.EduGreen
import com.example.sagareduapp1.ui.theme.EduOrange
import com.example.sagareduapp1.ui.theme.EduTextPrimary
import com.example.sagareduapp1.ui.theme.EduTextSecondary
import com.example.sagareduapp1.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(navController: NavHostController, viewModel: AppViewModel) {
    val context = LocalContext.current
    val level by viewModel.level.collectAsStateWithLifecycle()
    val score by viewModel.score.collectAsStateWithLifecycle()

    var currentImageIndex by remember { mutableIntStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var showNextButton by remember { mutableStateOf(false) }
    var showQuitDialog by remember { mutableStateOf(false) }
    var showInvalidInputDialog by remember { mutableStateOf(false) }

    // Shuffle the question order every time a level starts
    val shuffledIndices = remember(level) {
        val total = getTotalImagesInLevel(context, level)
        (0 until total).shuffled()
    }
    val totalImages = shuffledIndices.size
    
    // Get the actual index from our shuffled list
    val actualIndex = remember(currentImageIndex, shuffledIndices) {
        if (shuffledIndices.isNotEmpty() && currentImageIndex < shuffledIndices.size) 
            shuffledIndices[currentImageIndex] 
        else 0
    }

    val correctAnswer = remember(level, actualIndex) { getAnswerFromFileName(context, level, actualIndex) }
    val imageBitmap = remember(level, actualIndex) { getBitmapFromAssetsByIndex(context, level, actualIndex) }

    // Helper function to play sound
    val playSound = { resId: Int ->
        val mp = MediaPlayer.create(context, resId)
        mp?.apply {
            setOnCompletionListener { it.release() }
            start()
        }
    }

    // Level-based Themes:
    val (backgroundColors, textColor, cardAlpha, btnColor) = remember(level) {
        when (level) {
            "1" -> Quadruple(
                listOf(Color(0xFFF5F7FA), Color(0xFFE3F2FD), Color(0xFFE8F5E9)),
                EduTextPrimary, 0.95f, EduBlue
            )
            "2" -> Quadruple(
                listOf(Color(0xFFB0BEC5), Color(0xFF455A64), Color(0xFF263238)),
                Color.White, 0.7f, EduOrange
            )
            "3" -> Quadruple(
                listOf(Color(0xFF311B92), Color(0xFFB71C1C), Color(0xFF000000)),
                Color.White, 0.5f, Color(0xFFD32F2F)
            )
            else -> Quadruple(listOf(Color.White, Color.Gray), Color.Black, 1f, Color.DarkGray)
        }
    }

    if (showQuitDialog) {
        AlertDialog(
            onDismissRequest = { showQuitDialog = false },
            title = { Text("Quit Mission?") },
            text = { Text("Your current score will be saved. Do you want to exit to the results screen?") },
            confirmButton = {
                TextButton(onClick = {
                    showQuitDialog = false
                    viewModel.saveGameResult()
                    navController.navigate("score/$totalImages") {
                        popUpTo("game") { inclusive = true }
                    }
                }) {
                    Text("YES, QUIT")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuitDialog = false }) {
                    Text("CANCEL")
                }
            }
        )
    }

    if (showInvalidInputDialog) {
        AlertDialog(
            onDismissRequest = { showInvalidInputDialog = false },
            title = { Text("Numbers Only") },
            text = { Text("This challenge requires a numeric answer. Please enter digits only.") },
            confirmButton = {
                TextButton(onClick = { showInvalidInputDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(backgroundColors))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Mission: Level $level - Q${currentImageIndex + 1}/$totalImages",
                            style = MaterialTheme.typography.headlineMedium,
                            color = textColor
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = cardAlpha)),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CURRENT SCORE",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (level == "1") EduTextSecondary else Color.DarkGray
                            )
                            Text(
                                text = "$score",
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                                color = btnColor
                            )
                        }
                        if (feedbackMessage.isNotEmpty()) {
                            Text(
                                text = feedbackMessage,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = if (feedbackMessage.startsWith("Excellent")) EduGreen else Color(0xFFC62828)
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    border = BorderStroke(2.dp, btnColor.copy(alpha = 0.5f))
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Puzzle Image",
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = userAnswer,
                        onValueChange = { 
                            if (!showNextButton) {
                                if (it.all { char -> char.isDigit() }) {
                                    userAnswer = it
                                } else {
                                    showInvalidInputDialog = true
                                }
                            }
                        },
                        label = { Text("Enter Answer", color = textColor.copy(alpha = 0.8f)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        enabled = !showNextButton,
                        shape = RoundedCornerShape(16.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = if (showNextButton) Color.Gray else textColor),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = btnColor,
                            unfocusedBorderColor = btnColor.copy(alpha = 0.4f),
                            focusedLabelColor = btnColor
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (!showNextButton) {
                        Button(
                            onClick = {
                                if (userAnswer.trim() == correctAnswer) {
                                    viewModel.updateScore(10)
                                    feedbackMessage = "Excellent! +10"
                                    playSound(R.raw.success)
                                } else {
                                    feedbackMessage = "Try Again! Ans: $correctAnswer"
                                    playSound(R.raw.failure)
                                }
                                showNextButton = true
                            },
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            enabled = userAnswer.isNotBlank(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = btnColor)
                        ) {
                            Text("SUBMIT ANSWER", style = MaterialTheme.typography.labelLarge, fontSize = 18.sp, color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = {
                                if (currentImageIndex < totalImages - 1) {
                                    currentImageIndex++
                                    userAnswer = ""
                                    feedbackMessage = ""
                                    showNextButton = false
                                } else {
                                    viewModel.saveGameResult()
                                    navController.navigate("score/$totalImages") {
                                        popUpTo("game") { inclusive = true }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = btnColor)
                        ) {
                            Text(
                                if (currentImageIndex < totalImages - 1) "NEXT CHALLENGE" else "FINISH MISSION",
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { showQuitDialog = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, btnColor.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = btnColor)
                    ) {
                        Text("QUIT MISSION", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
