package com.courses.notesapp.viewmodel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courses.notesapp.models.UserRequest
import com.courses.notesapp.models.UserResponse
import com.courses.notesapp.repository.UserRepository
import com.courses.notesapp.util.ErrorHandling
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<ErrorHandling<UserResponse>>
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

    //todo for validation
    fun validateCredentials(
        username: String,
        emailAddress: String,
        password: String,
        isLogin:Boolean
    ): Pair<Boolean, String> {
        //todo first is true
        var result = Pair(true, "")

        //todo second is false and set the message.
        if (!isLogin && TextUtils.isEmpty(username) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please enter the valid email format")
        } else if (password.length <= 5) {
            result = Pair(false, "Password length should be 6 characters")
        }

        return result
    }
}