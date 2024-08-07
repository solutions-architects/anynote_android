package com.luckhost.data.network.retrofit

import com.luckhost.data.network.models.Either
import com.luckhost.data.network.models.NetworkError
import org.json.JSONObject
import retrofit2.Response

fun <T> getBodyOrNetworkError(
    response: Response<T>,
    errorStringToGet: String,
): Either<NetworkError, T> {

    if (response.isSuccessful && response.body() != null) {
        return Either.Right(response.body()!!)
    } else {
        response.errorBody()?.let {
            val errorMessage =
                mapOf(errorStringToGet to
                        response
                            .errorBody()
                            ?.string()
                            ?.let {
                                JSONObject(it).getString(errorStringToGet)
                            }.toString()
                )
            return Either.Left(NetworkError.Api(error = errorMessage))
        }
        return Either.Left(NetworkError.Api(error = mapOf("detail" to "errorBody is empty")))
    }
}