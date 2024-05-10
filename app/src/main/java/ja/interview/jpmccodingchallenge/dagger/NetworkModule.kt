package ja.interview.jpmccodingchallenge.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ja.interview.jpmccodingchallenge.Constants.API_KEY
import ja.interview.jpmccodingchallenge.Constants.BASE_URL
import ja.interview.jpmccodingchallenge.network.GeoService
import ja.interview.jpmccodingchallenge.network.WeatherService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(Interceptor { chain ->
                var originalRequest = chain.request()
                val url = originalRequest.url.newBuilder().apply {
                    addQueryParameter("appid", API_KEY).build()
                }.build()
                originalRequest = originalRequest.newBuilder().url(url).build()
                chain.proceed(originalRequest)
            }
            )
        }.build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): WeatherService = retrofit.create(WeatherService::class.java)

@Provides
    @Singleton
    fun provideGeoService(retrofit: Retrofit): GeoService = retrofit.create(GeoService::class.java)
}