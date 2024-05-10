package ja.interview.jpmccodingchallenge.repo

import androidx.core.text.isDigitsOnly
import com.google.android.gms.maps.model.LatLng
import ja.interview.jpmccodingchallenge.data.SharedPref
import ja.interview.jpmccodingchallenge.network.GeoService
import ja.interview.jpmccodingchallenge.network.NetworkResult
import ja.interview.jpmccodingchallenge.network.WeatherService
import ja.interview.jpmccodingchallenge.network.fetch
import ja.interview.jpmccodingchallenge.network.models.CurrentWeatherData
import ja.interview.jpmccodingchallenge.network.onSuccessCopy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

interface WeatherRepo {
    fun lastCitySearched():LatLng?
    fun isLocationGranted():Boolean
    suspend fun getLocationName(userInput: String): Flow<NetworkResult<List<LocationAutoComplete>>>
    suspend fun getWeatherReport(latLng: LatLng): NetworkResult<CurrentWeatherData>
}

class WeatherRepoImpl(
    private val geoService: GeoService,
    private val weatherService: WeatherService,
    private val dispatcher: CoroutineDispatcher,
    private val sharedPref: SharedPref
) : WeatherRepo {
    override fun lastCitySearched(): LatLng? = sharedPref.getLastCitySearch()
    override fun isLocationGranted(): Boolean = sharedPref.isLocationGranted()

    override suspend fun getLocationName(userInput: String): Flow<NetworkResult<List<LocationAutoComplete>>> =
        flowOf(userInput)
            .map {
                if (it.isDigitsOnly()) {
                    getLocationByZipCode(userInput)
                } else {
                    getLocationString(userInput)
                }
            }.flattenConcat()
            .flowOn(dispatcher)


    override suspend fun getWeatherReport(latLng: LatLng) = withContext(dispatcher) {
        sharedPref.lastCitySearch(latLng)
        fetch {
            weatherService.getCurrentWeatherReport(latLng.latitude, latLng.longitude)
        }
    }


    private fun getLocationByZipCode(userInput: String): Flow<NetworkResult<List<LocationAutoComplete>>> {
        return flow {
            emit(
                fetch {
                    geoService.getLocationByZip(userInput)
                }
            )
        }.transform {
            val itemToEmit = it.onSuccessCopy { data ->
                val locationList: List<LocationAutoComplete> = listOf(
                    LocationAutoComplete(
                        "${data.name}, ${data.country}",
                        LatLng(data.lat, data.lon)
                    )
                )
                NetworkResult.Success(locationList)
            }
            emit(itemToEmit)
        }
    }


    private fun getLocationString(userInput: String): Flow<NetworkResult<List<LocationAutoComplete>>> {
        return flow {
            emit(
                fetch {
                    geoService.getLocationByName(userInput)
                }
            )
        }.transform {
            val itemToEmit = it.onSuccessCopy { list ->
                val newList = list.map { item ->
                    LocationAutoComplete(
                        "${item.name}, ${item.country}",
                        LatLng(item.lat, item.lon)
                    )
                }.distinctBy { item -> item.locationDisplayText }
                NetworkResult.Success(newList)
            }
            emit(itemToEmit)
        }
    }
}

data class LocationAutoComplete(
    val locationDisplayText:String,
    val lat: LatLng,
)