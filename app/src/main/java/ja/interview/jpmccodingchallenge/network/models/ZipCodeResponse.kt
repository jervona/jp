package ja.interview.jpmccodingchallenge.network.models

data class ZipCodeResponse(
    val country: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val zip: String
)