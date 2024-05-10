package ja.interview.jpmccodingchallenge.data

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import ja.interview.jpmccodingchallenge.Constants.PREF_FILE_KEY


interface SharedPref {
    fun isLocationGranted(): Boolean
    fun updateLocationStatus(boolean: Boolean)
    fun lastCitySearch(latLng: LatLng)
    fun getLastCitySearch():LatLng?
}


class SharedPrefImpl constructor(
    context: Context
) : SharedPref {
    private val sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    companion object {
        private val LOCATION_KEY = "is_location_granted"
        private val LOCATION_LAT_KEY = "location_lat"
        private val LOCATION_LNG_KEY = "location_lng"
    }


    override fun isLocationGranted(): Boolean = sharedPref.getBoolean(LOCATION_KEY, false)


    override fun updateLocationStatus(boolean: Boolean) {
        editor.putBoolean(LOCATION_KEY, boolean).apply()
    }

    override fun lastCitySearch(latLng: LatLng) {
        editor.apply{
            putFloat(LOCATION_LAT_KEY, latLng.latitude.toFloat())
            putFloat(LOCATION_LNG_KEY, latLng.longitude.toFloat())
        }.apply()
    }

    override fun getLastCitySearch(): LatLng? {
        val lat = sharedPref.getFloat(LOCATION_LAT_KEY, 0f)
        val lng = sharedPref.getFloat(LOCATION_LNG_KEY, 0f)
        return if (lat == 0f && lng == 0f) null else LatLng(lat.toDouble(), lng.toDouble())
    }
}