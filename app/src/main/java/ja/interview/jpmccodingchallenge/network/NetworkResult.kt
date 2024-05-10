package ja.interview.jpmccodingchallenge.network

import retrofit2.HttpException
import retrofit2.Response


sealed class NetworkResult<T>{
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val code: Int, val message: String) : NetworkResult<T>()
    data class Exception<T>(val ex: Throwable) : NetworkResult<T>()
}

suspend fun <T : Any> fetch(networkCall: suspend () -> Response<T>): NetworkResult<T> {
    return try {
        val response = networkCall()
        val body = response.body()
        if (response.isSuccessful && body != null) NetworkResult.Success(body)
        else NetworkResult.Error(response.code(), response.errorBody().toString())
    } catch (ex: HttpException) {
        NetworkResult.Error(ex.code(), ex.localizedMessage)
    } catch (ex: Throwable) {
        NetworkResult.Exception(ex)
    }
}

fun <T : Any, R> NetworkResult<T>.onSuccessCopy(
    onSuccess: (T) -> NetworkResult<R>,
): NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> onSuccess(data)
        is NetworkResult.Error -> NetworkResult.Error(this.code,this.message)
        is NetworkResult.Exception -> NetworkResult.Exception(this.ex)
    }
}