package com.luckhost.data.network.retrofit

import com.luckhost.data.network.models.Either
import com.luckhost.data.network.models.IsBodyWasEmpty
import com.luckhost.data.network.models.NetworkError
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun <T> makeRequest(
    responseCall: suspend () -> Response<T>,
    errorStringToGet: String,
): Flow<Either<NetworkError, T>> = flow {
    try {
        val response = responseCall()

        if (response.isSuccessful && response.body() != null) {
            emit(Either.Right(response.body()!!))
        } else {
            response.errorBody()?.let {
                val errorMessage =
                    mapOf(
                        errorStringToGet to
                                JSONObject(it.string()).getString(errorStringToGet)
                    )
                emit(Either.Left(NetworkError.Api(error = errorMessage)))
            } ?: emit(Either.Left(NetworkError.Api(error = mapOf(
                "detail" to "errorBody is empty"))))
        }
    } catch (e: SocketTimeoutException) {
        emit(Either.Left(NetworkError.Unexpected(
            "Timeout: Failed to connect to the server.")))
    } catch (e: IOException) {
        emit(Either.Left(NetworkError.Unexpected(
            "Network error: Please check your connection.")))
    } catch (e: HttpException) {
        emit(Either.Left(NetworkError.Unexpected(
            "HTTP error: ${e.message()}")))
    } catch (e: Exception) {
        emit(Either.Left(NetworkError.Unexpected(
            "An unexpected error occurred: ${e.localizedMessage}")))
    }
}.catch { e ->
    emit(Either.Left(NetworkError.Unexpected(
        "An error occurred: ${e.localizedMessage}")
        )
    )
}