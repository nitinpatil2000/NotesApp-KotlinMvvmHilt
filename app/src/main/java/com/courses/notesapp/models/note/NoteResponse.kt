package com.courses.notesapp.models.note

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class NoteResponse(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val title: String,
    val updatedAt: String,
    val userId: String
) : Parcelable
