package com.example.thriftstore.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.thriftstore.data.model.Admin
import com.example.thriftstore.data.model.User
import com.example.thriftstore.data.repository.AuthRepository
import com.example.thriftstore.utils.SessionManager
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager // Optional if managing session in Fragment, but VM is cleaner? 
    // Actually SessionManager is android specific, ok to use here or just return success and let Fragment handle session.
    // Let's return success and let Fragment handle session to keep VM cleaner of Android Context if possible.
    // But passing Context to VM is common in AndroidViewModel. I passed SessionManager.
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult

    fun login(emailOrUsername: String, password: String, isAdmin: Boolean) {
        viewModelScope.launch {
            if (isAdmin) {
                val admin = repository.loginAdmin(emailOrUsername)
                if (admin == null) {
                    _loginResult.value = LoginResult.Error("Akun admin tidak ditemukan")
                } else if (admin.password != password) {
                    _loginResult.value = LoginResult.Error("Password admin salah")
                } else {
                    _loginResult.value = LoginResult.SuccessAdmin(admin)
                }
            } else {
                val user = repository.loginUser(emailOrUsername)
                if (user == null) {
                    _loginResult.value = LoginResult.Error("Akun tidak ditemukan")
                } else if (user.password != password) {
                    _loginResult.value = LoginResult.Error("Password salah")
                } else {
                    _loginResult.value = LoginResult.SuccessUser(user)
                }
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            try {
                repository.registerUser(user)
                _registerResult.value = true
            } catch (e: Exception) {
                _registerResult.value = false
            }
        }
    }
}

sealed class LoginResult {
    data class SuccessUser(val user: User) : LoginResult()
    data class SuccessAdmin(val admin: Admin) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class AuthViewModelFactory(private val repository: AuthRepository, private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
