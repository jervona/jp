package ja.interview.jpmccodingchallenge

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

interface LocationService {
    fun hasLocationPermission():Boolean
    suspend fun getCurrentLocation():Location?
}

class LocationServiceImpl constructor(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient,
    private val dispatcher: CoroutineDispatcher
) : LocationService {

    override fun hasLocationPermission(): Boolean = context.hasLocationPermission()

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? = withContext(dispatcher) {
        if (hasLocationPermission()) locationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token,
        ).await()
        else null
    }

    private fun Context.hasLocationPermission() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}