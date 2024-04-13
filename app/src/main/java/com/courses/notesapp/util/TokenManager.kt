package com.courses.notesapp.util

import android.content.Context
import com.courses.notesapp.util.Constant.SHARED_PREFS
import com.courses.notesapp.util.Constant.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {

    private var preference = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    //todo save token
    fun saveToken(token: String) {
        val editor = preference.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    //todo get token
    fun getToken(): String? {
        return preference.getString(USER_TOKEN, null)
    }

}