package com.courses.notesapp.models

data class UserResponse(
    val token: String,
    val user: User
)