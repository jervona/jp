package ja.interview.jpmccodingchallenge

import android.text.TextUtils
import app.cash.turbine.test
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import ja.interview.jpmccodingchallenge.data.SharedPref
import ja.interview.jpmccodingchallenge.network.GeoService
import ja.interview.jpmccodingchallenge.network.NetworkResult
import ja.interview.jpmccodingchallenge.network.WeatherService
import ja.interview.jpmccodingchallenge.network.models.ZipCodeResponse
import ja.interview.jpmccodingchallenge.repo.LocationAutoComplete
import ja.interview.jpmccodingchallenge.repo.WeatherRepo
import ja.interview.jpmccodingchallenge.repo.WeatherRepoImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyChar
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import retrofit2.Response

@RunWith(JUnit4::class)
class WeatherRepoTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    //I planned to write test however isDigitsOnly() is not supporting in mocking
    // trying to figure this out lead to rabbit hole. Which i thought was too much for this project
}