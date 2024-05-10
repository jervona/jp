package ja.interview.jpmccodingchallenge.screens.weather

import ja.interview.jpmccodingchallenge.extensions.convertUnixToTime
import ja.interview.jpmccodingchallenge.network.models.CurrentWeatherData
import ja.interview.jpmccodingchallenge.repo.LocationAutoComplete
import kotlin.math.roundToInt

data class CurrentWeatherState(
    val isLoading: Boolean,
    val isLocationGranted: Boolean,
    val weatherData: CurrentWeatherReport?
) {
    companion object {
        val INIT_STATE = CurrentWeatherState(false, false,null)
    }
}

data class CurrentWeatherReport(
    val locationTitle:String,
    val imageUrl: String,
    val currentTemp: Int,
    val weatherDescription: String,
    val tempHigh:Int,
    val feelsLike:Int,
    val tempLow:Int,
    val windSpeed:Int,
    val sunset:String,
    val sunrise:String,
    val humidity:Int
)


data class SearchState(
    val isSearching: Boolean,
    val searchSuggestions: List<LocationAutoComplete>
) {
    companion object {
        val INIT_STATE = SearchState(false, emptyList())
    }
}

fun CurrentWeatherData.toCurrentWeatherReport(): CurrentWeatherReport {
    return CurrentWeatherReport(
        locationTitle = "$name, ${sys.country}",
        imageUrl = "https://openweathermap.org/img/wn/${weather.first().icon}@2x.png",
        currentTemp = main.temp.roundToInt(),
        weatherDescription = weather.first().description.replaceFirstChar { it.uppercase() },
        tempHigh = main.temp_max.roundToInt(),
        tempLow = main.temp_min.roundToInt(),
        feelsLike = main.feels_like.roundToInt(),
        windSpeed = wind.speed.roundToInt(),
        sunset = sys.sunset.convertUnixToTime(),
        sunrise = sys.sunrise.convertUnixToTime(),
        humidity = main.humidity
    )
}