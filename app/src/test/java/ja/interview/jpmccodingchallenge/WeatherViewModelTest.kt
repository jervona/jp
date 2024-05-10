package ja.interview.jpmccodingchallenge

import com.google.android.gms.maps.model.LatLng
import ja.interview.jpmccodingchallenge.repo.WeatherRepo
import org.junit.Rule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import ja.interview.jpmccodingchallenge.network.NetworkResult
import ja.interview.jpmccodingchallenge.repo.LocationAutoComplete
import ja.interview.jpmccodingchallenge.screens.weather.WeatherViewModel
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class WeatherViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    val mockRepo: WeatherRepo = mock()
    val mockLocationService: LocationService = mock()


    @Test
    fun testSIsSearchingStatusIsUpdated() = runBlocking {
        val viewModel = WeatherViewModel(mockRepo,mockLocationService)
        val expectedList = listOf(
            LocationAutoComplete("New York,NY", LatLng(0.toDouble(), 0.toDouble())),
            LocationAutoComplete("Queens,NY", LatLng(0.toDouble(), 0.toDouble()))
        )

        whenever(mockRepo.getLocationName(anyString())).thenReturn(
            flowOf(NetworkResult.Success(expectedList))
        )


        viewModel.onSearchQuery("blahblach")
        assertEquals(false, viewModel.searchListState.value.isSearching)
    }
}