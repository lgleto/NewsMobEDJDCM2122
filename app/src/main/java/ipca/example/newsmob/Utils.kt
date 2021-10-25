package ipca.example.newsmob

import java.text.SimpleDateFormat
import java.util.*

fun Date.longDateString() : String  {
    val dateFormater = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault())
    return dateFormater.format(this)
}

fun String.toDate() : Date{
    val dateFormater = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault())
    val date = dateFormater.parse(this)
    return date?:Date()
}