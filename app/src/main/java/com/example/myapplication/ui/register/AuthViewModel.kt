package com.example.myapplication.ui.register

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.UserRequest
import com.example.myapplication.models.UserResponse
import com.example.myapplication.repository.UserRepository
import com.example.myapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateUserInput(email: String, password: String, userName: String, isLogin: Boolean): Pair<Boolean, String> {
        var result = Pair(true, "")
        if(TextUtils.isEmpty(email) || (!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please provide the credentials")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = Pair(false, "Invalid Email")
        } else if (password.length < 6) {
            result = Pair(false, "Password must be greater than 6")
        }
        return result
    }
}