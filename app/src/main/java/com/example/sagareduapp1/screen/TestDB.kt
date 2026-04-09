package com.example.sagareduapp1.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sagareduapp1.viewmodel.AppViewModel

@Composable
fun TestDBScreen(viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val users by viewModel.allUsers.collectAsStateWithLifecycle(initialValue = emptyList())
    var name by remember { mutableStateOf("") }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Database Debugging", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = {
                if (name.isNotBlank()) {
                    viewModel.setUsername(name)
                    viewModel.saveGameResult()
                    name = ""
                }
            }) {
                Text("Insert Dummy User")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                viewModel.clearUsers()
            }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("Clear All")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Record Count: ${users.size}")

        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
            items(users) { user ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "ID: ${user.id} | Name: ${user.username}")
                        Text(text = "Score: ${user.score} | Level: ${user.level}")
                        Text(text = "Date: ${user.date}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
