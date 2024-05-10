package ja.interview.jpmccodingchallenge.screens.weather

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.interview.jpmccodingchallenge.LocationService
import ja.interview.jpmccodingchallenge.extensions.launch
import ja.interview.jpmccodingchallenge.network.NetworkResult
import ja.interview.jpmccodingchallenge.repo.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repoImpl: WeatherRepo,
    private val locationService: LocationService
) : ViewModel() {

    private val _queryText = MutableStateFlow("")
     //We could contain SearchState and CurrentWeatherState in one Large UI state data object
    private val _searchListState = MutableStateFlow(SearchState.INIT_STATE)
    val searchListState = _searchListState.asStateFlow()

    private val _weatherState = MutableStateFlow(
        CurrentWeatherState.INIT_STATE.copy(isLocationGranted = locationService.hasLocationPermission())
    )
    val weatherState = _weatherState.asStateFlow()

    init {
        initSearchFlow()
        //If we have Location Permission display current weather else display last search city.
        if (locationService.hasLocationPermission()) getWeatherForCurrentLocation()
        else repoImpl.lastCitySearched()?.let { getWeatherReport(it) }
    }

    private fun initSearchFlow() {
        launch {
            _queryText
                .debounce(500L)
                .distinctUntilChanged()
                .onEach { _searchListState.update { it.copy(isSearching = true) } }
                .flatMapLatest { repoImpl.getLocationName(it) }
                .onCompletion { _searchListState.update { it.copy(isSearching = false) } }
                .collectLatest { result ->
                    when (result) {
                        is NetworkResult.Error -> println(result.code)  //handle errors for example 404 errors
                        is NetworkResult.Exception -> println(result.ex)   //Log or track Exception maybe display dialog
                        is NetworkResult.Success -> {
                            _searchListState.update {
                                it.copy(searchSuggestions = result.data)
                            }
                        }
                    }
                }
        }
    }

    private fun getWeatherForCurrentLocation() {
        launch {
            locationService.getCurrentLocation()?.let {
                _weatherState.update { it.copy(isLocationGranted = true) }
                getWeatherReport(LatLng(it.latitude, it.longitude))
            }
        }
    }

    fun onSearchQuery(text: String) {
        _queryText.value = text
    }

    fun getWeatherReport(latLng: LatLng) {
        launch {
            _weatherState.update { it.copy(isLoading = true) }
            when (val result = repoImpl.getWeatherReport(latLng)) {
                is NetworkResult.Error -> {
                    //handle errors for example 404 errors
                }
                is NetworkResult.Exception -> {
                    //Log or track Exception maybe display dialog
                }
                is NetworkResult.Success -> {
                    _weatherState.update {
                        it.copy(
                            isLoading = false,
                            weatherData = result.data.toCurrentWeatherReport()
                        )
                    }
                }
            }
        }
    }

    fun updateLocationPermission(permissionGranted: Boolean) {
        if (permissionGranted) {
            getWeatherForCurrentLocation()
        } else {
            // We can display a dialog informing user we need location permission
        }
    }
}