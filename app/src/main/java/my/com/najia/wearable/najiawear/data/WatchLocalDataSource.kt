package my.com.najia.wearable.najiawear.data

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import my.com.najia.wearable.najiawear.R
import my.com.najia.wearable.najiawear.presentation.SQLiteHelper

data class PrayerTime(
    val hijri: String,
    val date: String,
    val day: String,
    val imsak: String,
    val fajr: String,
    val syuruk: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)

// Function to retrieve data by date
@SuppressLint("Range")
fun getDataByDate(context: Context, date: String): List<PrayerTime> {
    val dbHelper = SQLiteHelper(context)
    val db = dbHelper.readableDatabase

    val prayerTimes = mutableListOf<PrayerTime>()

    // Define the columns you want to retrieve
    val columns = arrayOf(
        "hijri",
        "date",
        "day",
        "imsak",
        "fajr",
        "syuruk",
        "dhuhr",
        "asr",
        "maghrib",
        "isha"
    )

    // Define the WHERE clause to filter by date
    val selection = "date = ?"

    // Define the values for the WHERE clause
    val selectionArgs = arrayOf(date)

    // Query the database
    val cursor: Cursor? = db.query(
        "prayer_times",
        columns,
        selection,
        selectionArgs,
        null,
        null,
        null
    )

    // Iterate through the cursor and add data to the list
    cursor?.use { cursor ->
        while (cursor.moveToNext()) {
            val hijri = cursor.getString(cursor.getColumnIndex("hijri"))
            val date = cursor.getString(cursor.getColumnIndex("date"))
            val day = cursor.getString(cursor.getColumnIndex("day"))
            val imsak = cursor.getString(cursor.getColumnIndex("imsak"))
            val fajr = cursor.getString(cursor.getColumnIndex("fajr"))
            val syuruk = cursor.getString(cursor.getColumnIndex("syuruk"))
            val dhuhr = cursor.getString(cursor.getColumnIndex("dhuhr"))
            val asr = cursor.getString(cursor.getColumnIndex("asr"))
            val maghrib = cursor.getString(cursor.getColumnIndex("maghrib"))
            val isha = cursor.getString(cursor.getColumnIndex("isha"))

            val prayerTime = PrayerTime(
                hijri,
                date,
                day,
                imsak,
                fajr,
                syuruk,
                dhuhr,
                asr,
                maghrib,
                isha
            )
            prayerTimes.add(prayerTime)
        }
    }

    // Close the database
    db.close()

    return prayerTimes
}

fun logPrayerTimesByDate(context: Context, date: String) {
    val prayerTimes = getDataByDate(context, date)
    for (prayerTime in prayerTimes) {
        Log.d("PrayerTime", "Date: ${prayerTime.date}, Isha: ${prayerTime.isha}")
    }
}

class WatchLocalDataSource(context: Context) {

    val todayDate: String
        get() {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            return dateFormat.format(calendar.time)
        }
    val watches = listOf(
        PrayerTimeValue(
            modelId = 1,
            name = "Imsak",
            time = getDataByDate(context, todayDate).firstOrNull()?.imsak ?: "",
            description = ( getDataByDate(
                context,
                todayDate
            ).firstOrNull()?.imsak) ?: "",
            icon = R.drawable.icon_subuh
        ),
        PrayerTimeValue(
            modelId = 2,
            name = "Subuh",
            time = getDataByDate(context, todayDate).firstOrNull()?.fajr ?: "",
            description = ( getDataByDate(
                context,
                todayDate
            ).firstOrNull()?.fajr) ?: "",
            icon = R.drawable.icon_asar
        ),
        PrayerTimeValue(
            modelId = 3,
            name = "Syuruk",
            time = getDataByDate(context, todayDate).firstOrNull()?.syuruk ?: "",
            description = (getDataByDate(
                context,
                todayDate
            ).firstOrNull()?.syuruk) ?: "",
            icon = R.drawable.icon_duha
        ),
        PrayerTimeValue(
            modelId = 4,
            name = "Zohor",
            time = getDataByDate(context, todayDate).firstOrNull()?.dhuhr ?: "",
            description = (getDataByDate(
                context,
                todayDate
            ).firstOrNull()?.dhuhr) ?: "",
            icon = R.drawable.icon_zohor
        ),
        PrayerTimeValue(
            modelId = 5,
            name = "Asar",
            time = getDataByDate(context, todayDate).firstOrNull()?.asr ?: "",
            description = (getDataByDate(
                context,
                todayDate
            ).firstOrNull()?.asr) ?: "",
            icon = R.drawable.icon_asar
        ),
        PrayerTimeValue(
            modelId = 6,
            name = "Maghrib",
            time = getDataByDate(context, todayDate).firstOrNull()?.maghrib ?: "",
            description = ( getDataByDate(
                context,
                todayDate
            ).firstOrNull()?.maghrib) ?: "",
            icon = R.drawable.icon_maghrib
        ),
        PrayerTimeValue(
            modelId = 7,
            name = "Isyak",
            time = getDataByDate(context, todayDate).firstOrNull()?.isha ?: "",
            description = (getDataByDate(
                context,
                todayDate
            ).firstOrNull()?.isha) ?: "",
            icon = R.drawable.icon_isyak
        )
    )
}
