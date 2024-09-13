package com.example.myweather.data.service

import com.example.myweather.data.model.Resource
import retrofit2.HttpException
import java.io.IOException

/**
 * A common error handler for network requests.
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return try {

        // Attempt the API call
        val result = apiCall()
        Resource.Success(result)
    } catch (e: HttpException) {
        val errorMessage = when (e.code()) {
            400 -> "Bad Request: The server couldn't understand your request."
            401 -> "Unauthorized: Invalid API key."
            404 -> "Resource not found. Please try again."
            500 -> "Server Error: Please try again later."
            else -> "HTTP Error: ${e.message()}"
        }
        Resource.Error(errorMessage, e)
    } catch (e: IOException) {
        // Handle network failure like no internet
        Resource.Error("Network error: Please check your internet connection.", e)
    } catch (e: Exception) {
        // Handle general unexpected errors
        Resource.Error("An unexpected error occurred: ${e.localizedMessage}", e)
    }
}
