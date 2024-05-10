package ja.interview.jpmccodingchallenge.network

import ja.interview.jpmccodingchallenge.network.models.LocationResponseItem
import ja.interview.jpmccodingchallenge.network.models.ZipCodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoService {
    @GET("geo/1.0/direct")
    suspend fun getLocationByName(
        @Query("q") params: String,
        @Query("limit") limit: Int = 5
    ): Response<List<LocationResponseItem>>


    @GET("/geo/1.0/zip")
    suspend fun getLocationByZip(
        @Query("zip") params: String,
        @Query("limit") limit: Int = 5
    ): Response<ZipCodeResponse>
}