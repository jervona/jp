package ja.interview.jpmccodingchallenge.screens.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ja.interview.jpmccodingchallenge.R
import ja.interview.jpmccodingchallenge.ui.theme.DarkBlue


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeatherCard(
    state: CurrentWeatherState,
    modifier: Modifier = Modifier
) {
    state.weatherData?.let { data->
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkBlue),
            shape = RoundedCornerShape(10.dp),
            modifier = modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.locationTitle,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                ImageWithLoader(
                    imageUrl = data.imageUrl,
                    modifier = Modifier.width(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${ data.currentTemp }°F",
                    fontSize = 50.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = data.weatherDescription,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                maxItemsInEachRow = 3
            ) {
                WeatherDataDisplay(
                    title = "Low:" ,
                    value = "${data.tempLow}°F"
                )
                WeatherDataDisplay(
                    title = "High:" ,
                    value = "${data.tempHigh}°F"
                )
                WeatherDataDisplay(
                    title = "Feels Like:" ,
                    value = "${data.feelsLike}°F"
                )
                WeatherDataDisplay(
                    title = "Humidity" ,
                    value = "${data.humidity} MPH"
                )
                WeatherDataDisplay(
                    title = "Wind Speed" ,
                    value = "${data.windSpeed} MPH"
                )
                WeatherDataDisplay(
                    title = "Sunrise" ,
                    value = data.sunrise
                )
                WeatherDataDisplay(
                    title = "Sunset" ,
                    value = data.sunset
                )
            }
        }
    }
}

@Composable
fun WeatherDataDisplay(
    title: String,
    value:String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(color = Color.White)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = TextStyle(color = Color.White)
        )
    }
}

@Composable
fun ImageWithLoader(imageUrl: String, modifier: Modifier = Modifier) {
    val imageIsLoading = remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            onLoading = { imageIsLoading.value = true },
            onSuccess = { imageIsLoading.value = false },
            onError = { imageIsLoading.value = false },
            error = painterResource(R.drawable.error),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier
        )
        if (imageIsLoading.value) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}