package ja.interview.jpmccodingchallenge.extensions

import java.text.SimpleDateFormat
import java.util.Locale


fun Long.convertUnixToTime(): String {
    val sdf = SimpleDateFormat("HH:mm aa" , Locale.getDefault(Locale.Category.FORMAT))
    val date = java.util.Date(this * 1000)
    return sdf.format(date)
}