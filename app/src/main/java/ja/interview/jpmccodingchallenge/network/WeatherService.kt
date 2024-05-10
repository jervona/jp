package ja.interview.jpmccodingchallenge.network

import ja.interview.jpmccodingchallenge.network.models.CurrentWeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherReport(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String = "imperial",
    ): Response<CurrentWeatherData>
}