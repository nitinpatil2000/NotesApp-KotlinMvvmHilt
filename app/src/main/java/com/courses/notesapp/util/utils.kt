package com.courses.notesapp.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class utils {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): ErrorHandling<T> {
        return withContext(dispatcher) {
            try {
                ErrorHandling.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ErrorHandling.Error("IO Exception")
                    is HttpException -> {
                        val errorObj =
                            JSONObject(throwable.response()!!.errorBody()!!.charStream().readText())
                        ErrorHandling.Error(errorObj.getString("message"))
                    }

                    else ->
                        ErrorHandling.Error("something went wrong", null)

                }
            }
        }
    }
}