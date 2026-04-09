package com.example.sagareduapp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sagareduapp1.database.AppRepository
import com.example.sagareduapp1.database.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(private val repository: AppRepository) : ViewModel() {
    private val _level = MutableStateFlow("1")
    val level: StateFlow<String> = _level.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    val allUsers = repository.allUsers

    fun setUsername(username: String) {
        _currentUser.value = User(username = username)
    }

    fun setLevel(newLevel: String) {
        _level.value = newLevel
    }

    fun updateScore(points: Int) {
        _score.value += points
    }

    fun resetScore() {
        _score.value = 0
    }

    fun saveGameResult() {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                val resultUser = user.copy(
                    level = _level.value,
                    score = _score.value,
                    date = System.currentTimeMillis()
                )
                repository.insertUser(resultUser)
            }
        }
    }

    fun clearUsers() {
        viewModelScope.launch {
            repository.deleteAllUsers()
        }
    }
}
