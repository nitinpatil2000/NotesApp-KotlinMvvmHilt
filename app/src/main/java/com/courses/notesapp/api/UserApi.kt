package com.courses.notesapp.api

import com.courses.notesapp.models.UserRequest
import com.courses.notesapp.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/users/signup")
    suspend fun userSignUp(
        @Body userRequest: UserRequest
    ): Response<UserResponse>

    @POST("/users/signin")
    suspend fun userSignIn(
        @Body userRequest: UserRequest
    ): Response<UserResponse>
}