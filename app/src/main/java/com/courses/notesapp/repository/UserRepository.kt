package com.courses.notesapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.courses.notesapp.api.UserApi
import com.courses.notesapp.models.UserRequest
import com.courses.notesapp.models.UserResponse
import com.courses.notesapp.util.ErrorHandling
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _userResponseLiveData = MutableLiveData<ErrorHandling<UserResponse>>()
    val userResponseLiveData: LiveData<ErrorHandling<UserResponse>>
        get() = _userResponseLiveData


    suspend fun registerUser(userRequest: UserRequest) {
        //todo loading
        _userResponseLiveData.postValue(ErrorHandling.Loading())

        val response = userApi.signup(userRequest)
        handledResponse(response)
    }


    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(ErrorHandling.Loading())

        val response = userApi.signin(userRequest)
        handledResponse(response)
    }

    private fun handledResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(ErrorHandling.Success(response.body()!!))

        } else if (response.errorBody() != null) {

            val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(
                ErrorHandling.Error(errorObject.getString("message"))
            )

        } else {
            _userResponseLiveData.postValue(ErrorHandling.Error("Something went wrong"))
        }
    }
}