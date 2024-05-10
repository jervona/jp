package ja.interview.jpmccodingchallenge.dagger

import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ja.interview.jpmccodingchallenge.LocationService
import ja.interview.jpmccodingchallenge.LocationServiceImpl
import ja.interview.jpmccodingchallenge.data.SharedPref
import ja.interview.jpmccodingchallenge.network.GeoService
import ja.interview.jpmccodingchallenge.network.WeatherService
import ja.interview.jpmccodingchallenge.repo.WeatherRepo
import ja.interview.jpmccodingchallenge.repo.WeatherRepoImpl
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RepoModule {
    @Singleton
    @Provides
    fun provideLocationService(
        @IoDispatcher dispatcher: CoroutineDispatcher,
        @ApplicationContext context:Context,
    ): LocationService {
        return LocationServiceImpl(
            context,
            LocationServices.getFusedLocationProviderClient(context),
            dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideMovieRepo(
        geoService: GeoService,
        weatherService: WeatherService,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        sharedPref: SharedPref
    ): WeatherRepo = WeatherRepoImpl(
        geoService,
        weatherService,
        dispatcher,
        sharedPref
    )
}