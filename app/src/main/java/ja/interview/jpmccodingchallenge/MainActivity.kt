package ja.interview.jpmccodingchallenge

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ja.interview.jpmccodingchallenge.screens.LocationSearchBar
import ja.interview.jpmccodingchallenge.screens.weather.WeatherCard
import ja.interview.jpmccodingchallenge.screens.weather.WeatherViewModel
import ja.interview.jpmccodingchallenge.ui.theme.JPMCCodingChallengeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            val permissionGranted = it[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                    it[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            viewModel.updateLocationPermission(permissionGranted)
        }

        setContent {
            JPMCCodingChallengeTheme {

                val searchListState = viewModel.searchListState.collectAsStateWithLifecycle().value
                val weatherState = viewModel.weatherState.collectAsStateWithLifecycle().value

                if (!weatherState.isLocationGranted) requestLocationPermission()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        LocationSearchBar(
                            searchListState,
                            viewModel::onSearchQuery,
                            viewModel::getWeatherReport,
                            Modifier.align(Alignment.TopCenter)
                        )
                        WeatherCard(
                            weatherState,
                            Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }

    private fun requestLocationPermission() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JPMCCodingChallengeTheme {
    }
}