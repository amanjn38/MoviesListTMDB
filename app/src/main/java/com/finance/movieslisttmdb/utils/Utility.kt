package com.finance.movieslisttmdb.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.finance.movieslisttmdb.R
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

fun formatTime(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60

    return if (hours > 0) {
        if (remainingMinutes > 0) {
            "${if (hours == 1) "1 hr" else "$hours hrs"} ${if (remainingMinutes == 1) "1 min" else "$remainingMinutes mins"}"
        } else {
            if (hours == 1) "1 hr" else "$hours hrs"
        }
    } else {
        if (remainingMinutes == 1) "1 min" else "$remainingMinutes mins"
    }
}

fun convertRating(ratingStr: String): Float {
    // Parse the rating from string to float
    val rating = ratingStr.toFloatOrNull() ?: 0f

    // Convert to 5-point scale
    val convertedRating = rating / 2

    return (convertedRating * 2).roundToInt() / 2.0f
}

fun createHeaders(): Map<String, String> {
    return mapOf(
        "accept" to "application/json",
        "Authorization" to "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZGMwZWQ5ZTFmZTEwZTJiMDQ5NTQwMDEwOTZjNmY3ZSIsInN1YiI6IjY1Y2U0NDY1ZDhhZjY3MDE2NDhmOTZiMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.boE326nsE3kXtrUOoXIi_QXlaNa3XFngRGBnRySYMuk"
    )
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun showNoInternetDialog(context: Context) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle(context.getString(R.string.no_internet_title))
        .setMessage(context.getString(R.string.no_internet_message))
        .setPositiveButton(context.getString(R.string.ok)) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }

    val alertDialog = alertDialogBuilder.create()
    alertDialog.show()
}