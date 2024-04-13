import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.OutlinedChip
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Dialog
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.android.horologist.compose.material.Button
import com.google.android.horologist.compose.material.Confirmation
import com.google.android.horologist.compose.material.ToggleChip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import my.com.najia.wearable.najiawear.R
import my.com.najia.wearable.najiawear.data.PrayerTimeValue
import my.com.najia.wearable.najiawear.presentation.MainActivity
import my.com.najia.wearable.najiawear.presentation.SQLiteHelper
import my.com.najia.wearable.najiawear.presentation.ui.watchlist.WatchListViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject


@Composable
fun Dialogs(
    columnState: ScalingLazyColumnState,
    showVignette: Boolean,
    onClickVignetteToggle: (Boolean) -> Unit,
    onClickWatch: (Int) -> Unit
) {
    val viewModel: WatchListViewModel = viewModel(
        factory = WatchListViewModel.Factory
    )
    val watches by viewModel.watches
    Dialogs(
        watches = watches,
        columnState = columnState,
        showVignette = showVignette,
        onClickVignetteToggle = onClickVignetteToggle,
        onClickWatch = onClickWatch
    )
}

/**
 * Displays a list of watches plus a [ToggleChip] at the top to display/hide the Vignette around
 * the screen. The list is powered using a [ScalingLazyColumn].
 */
@Composable
fun Dialogs(
    watches: List<PrayerTimeValue>,
    columnState: ScalingLazyColumnState,
    showVignette: Boolean,
    onClickVignetteToggle: (Boolean) -> Unit,
    onClickWatch: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmationShowDialog by remember { mutableStateOf(false) }
    var confirmationStatus by remember { mutableStateOf("") }
    var alertShowDialog by remember { mutableStateOf(false) }
    var alertStatus by remember { mutableStateOf("") }
    var apiResponse by remember { mutableStateOf("") }

    val dialogDismissed = stringResource(R.string.dialog_dismissed)
    val dialogTimedOut = stringResource(R.string.dialog_timed_out)
    val dialogNo = stringResource(R.string.confirmation_dialog_no)
    val dialogYes = stringResource(R.string.alert_dialog_yes)

    val statesOfMalaysia = mapOf(
        "Johor" to listOf(
            mapOf("code" to "JHR01", "name" to "Pulau Aur dan Pulau Pemanggil"),
            mapOf("code" to "JHR02", "name" to "Johor Bharu, Kota Tinggi, Mersing"),
            mapOf("code" to "JHR03", "name" to "Kluang, Pontian"),
            mapOf("code" to "JHR04", "name" to "Batu Pahat, Muar, Segamat, Gemas Johor")
        ),
        "Kedah" to listOf(
            mapOf("code" to "KDH01", "name" to "Kota Setar, Kubang Pasu .."),
            mapOf("code" to "KDH02", "name" to "Kuala Muda, Yan, Pendang"),
            mapOf("code" to "KDH03", "name" to "Padang Terap, Sik"),
            mapOf("code" to "KDH04", "name" to "Baling"),
            mapOf("code" to "KDH05", "name" to "Bandar Baharu, Kulim"),
            mapOf("code" to "KDH06", "name" to "Langkawi"),
            mapOf("code" to "KDH07", "name" to "Gunung Jerai")
        ),
        "Kelantan" to listOf(
            mapOf("code" to "KTN01", "name" to "Kota Bharu, Pasir Mas, Tanah Merah, Tumpat .."),
            mapOf("code" to "KTN03", "name" to "Gua Musang (Daerah Galas Dan Bertam), Jeli")
        ),
        "Melaka" to listOf(
            mapOf("code" to "MLK01", "name" to "Seluruh Negeri Melaka")
        ),
        "Negeri Sembilan" to listOf(
            mapOf("code" to "NGS01", "name" to "Tampin, Jempol"),
            mapOf("code" to "NGS02", "name" to "Jelebu, Kuala Pilah, Port Dickson, Seremban ..")
        ),
        "Pahang" to listOf(
            mapOf("code" to "PHG01", "name" to "Pulau Tioman"),
            mapOf("code" to "PHG02", "name" to "Kuantan, Pekan, Rompin, Muadzam Shah"),
            mapOf("code" to "PHG03", "name" to "Jerantut, Temerloh, Maran, Bera, Chenor, Jengka"),
            mapOf("code" to "PHG04", "name" to "Bentong, Lipis, Raub"),
            mapOf("code" to "PHG05", "name" to "Genting Sempah, Janda Baik, Bukit Tinggi"),
            mapOf("code" to "PHG06", "name" to "Cameron Highlands, Genting Highlands, Bukit Fraser")
        ),
        "Perlis" to listOf(
            mapOf("code" to "PLS01", "name" to "Kangar, Padang Besar, Arau")
        ),
        "Pulau Pinang" to listOf(
            mapOf("code" to "PNG01", "name" to "Seluruh Negeri Pulau Pinang")
        ),
        "Perak" to listOf(
            mapOf("code" to "PRK01", "name" to "Tapah, Slim River, Tanjung Malim"),
            mapOf("code" to "PRK02", "name" to "Kuala Kangsar, Ipoh, Batu Gajah, Kampar .."),
            mapOf("code" to "PRK03", "name" to "Lenggong, Pengkalan Hulu, Grik"),
            mapOf("code" to "PRK04", "name" to "Temengor, Belum"),
            mapOf("code" to "PRK05", "name" to "Kg Gajah, Teluk Intan, Bagan Datuk .."),
            mapOf("code" to "PRK06", "name" to "Selama, Taiping, Bagan Serai, Parit Buntar"),
            mapOf("code" to "PRK07", "name" to "Bukit Larut")
        ),
        "Sabah" to listOf(
            mapOf("code" to "SBH01", "name" to "Bahagian Sandakan (Timur), Bukit Garam .."),
            mapOf("code" to "SBH02", "name" to "Beluran, Telupid, Pinangah, Terusan .."),
            mapOf("code" to "SBH03", "name" to "Lahad Datu, Silabukan, Kunak, Semporna .."),
            mapOf("code" to "SBH04", "name" to "Bandar Tawau, Balong, Merotai .."),
            mapOf("code" to "SBH05", "name" to "Kudat, Kota Marudu, Pitas, Pulau Banggi .."),
            mapOf("code" to "SBH06", "name" to "Gunung Kinabalu"),
            mapOf("code" to "SBH07", "name" to "Kota Kinabalu, Ranau, Kota Belud .."),
            mapOf("code" to "SBH08", "name" to "Pensiangan, Keningau, Tambunan, Nabawan .."),
            mapOf("code" to "SBH09", "name" to "Beaufort, Kuala Penyu, Sipitang, Tenom ..")
        ),
        "Selangor" to listOf(
            mapOf("code" to "SGR01", "name" to "Gombak, Petaling, Sepang, Shah Alam.."),
            mapOf("code" to "SGR02", "name" to "Kuala Selangor, Sabak Bernam"),
            mapOf("code" to "SGR03", "name" to "Klang, Kuala Langat")
        ),
        "Sarawak" to listOf(
            mapOf("code" to "SWK01", "name" to "Limbang, Lawas, Sundar, Trusan"),
            mapOf("code" to "SWK02", "name" to "Miri, Niah, Bekenu, Sibuti, Marudi"),
            mapOf("code" to "SWK03", "name" to "Pandan, Belaga, Suai, Tatau, Sebauh, Bintulu"),
            mapOf("code" to "SWK04", "name" to "Sibu, Mukah, Dalat, Song, Igan, Oya, Kapit .."),
            mapOf("code" to "SWK05", "name" to "Sarikei, Matu, Julau, Rajang, Daro, Bintangor .."),
            mapOf("code" to "SWK06", "name" to "Lubok Antu, Sri Aman, Roban, Debak, Kabong .."),
            mapOf("code" to "SWK07", "name" to "Serian, Simunjan, Samarahan, Sebuyau, Meludam"),
            mapOf("code" to "SWK08", "name" to "Kuching, Bau, Lundu, Sematan"),
            mapOf("code" to "SWK09", "name" to "Zon Khas (Kampung Patarikan)")
        ),
        "Terengganu" to listOf(
            mapOf("code" to "TRG01", "name" to "Kuala Terengganu, Marang, Kuala Nerus"),
            mapOf("code" to "TRG02", "name" to "Besut, Setiu"),
            mapOf("code" to "TRG03", "name" to "Hulu Terengganu"),
            mapOf("code" to "TRG04", "name" to "Dungun, Kemaman")
        ),
        "Wilayah Persekutuan" to listOf(
            mapOf("code" to "WLY01", "name" to "Kuala Lumpur, Putrajaya"),
            mapOf("code" to "WLY02", "name" to "Labuan")
        )
    )

    ScalingLazyColumn(
        modifier = modifier.fillMaxSize(),
        columnState = columnState
    ) {
        statesOfMalaysia.forEach { (stateName, areas) ->
            item {
                OutlinedChip(
                    onClick = {
                        alertStatus = ""
                        alertShowDialog = false
                    },
                    label = { Text(stateName) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end= 25.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            areas.forEach { area ->
                item {
                    Text(
                        text = "${area["code"]} - ${area["name"] ?: ""}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end= 25.dp)
                            .clickable {
                                alertStatus = "${area["code"]}"
                                alertShowDialog = true
                            },
                        textAlign = TextAlign.Start
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    Dialog(
        showDialog = confirmationShowDialog,
        onDismissRequest = {
            if (confirmationStatus.isEmpty()) confirmationStatus = dialogDismissed
            confirmationShowDialog = false
        }
    ) {
        Confirmation(
            onTimeout = {
                confirmationStatus = dialogTimedOut
                confirmationShowDialog = false
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.confirmation_dialog_tick),
                    modifier = Modifier.size(48.dp)
                )
            }
        ) {
            Text(
                text = "Confirmation: $alertStatus", // Display code and name
                textAlign = TextAlign.Center
            )
        }
    }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScalingLazyListState()
    val toastContext = LocalContext.current

    Dialog(
        showDialog = alertShowDialog,
        onDismissRequest = {
            if (alertStatus.isEmpty()) alertStatus = dialogDismissed
            alertShowDialog = false
        },
        scrollState = scrollState
    ) {
        Alert(
            title = {
                Text(
                    text = stringResource(R.string.confirmation_dialog_power_off),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground
                )
            },
            negativeButton = {
                Button(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = dialogNo,
                    onClick = {
                        alertStatus = dialogNo
                        alertShowDialog = false
                    },
                    colors = ButtonDefaults.secondaryButtonColors()
                )
            },
            positiveButton = {
                Button(
                    imageVector = Icons.Filled.Check,
                    contentDescription = dialogYes,
                    onClick = {
                        alertShowDialog = false

                        val apiUrl = "https://www.e-solat.gov.my/index.php?r=esolatApi/takwimsolat&period=year&zone=$alertStatus"

                        // Use a coroutine to make the API call
                        scope.launch {
                            try {
                                // Make the API call in a background thread
                                val response = makeApiCall(apiUrl, toastContext)
                            } catch (e: Exception) {
                                apiResponse = "API call failed"
                            }
                        }

                        val toastMessage = "$alertStatus has been selected"
                        Toast.makeText(
                            toastContext,
                            toastMessage,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (toastContext is ComponentActivity) {
                            val intent = Intent(toastContext, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            toastContext.startActivity(intent)
                            toastContext.finish()
                        }
                    },
                    colors = ButtonDefaults.primaryButtonColors()
                )
            },
            scrollState = scrollState
        ) {
            Text(
                text = "$alertStatus", // Display code and name
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )
        }
    }

    // Display the API response
    Text(
        text = apiResponse,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}
private suspend fun makeApiCall(apiUrl: String, context: Context) {

    val TABLE_NAME = "prayer_times"

    // Create an OkHttpClient instance
    val client = OkHttpClient()

    // Create a GET request with the URL
    val request = Request.Builder()
        .url(apiUrl)
        .build()

    // Use Kotlin coroutine to perform the network operation asynchronously
    GlobalScope.launch(Dispatchers.IO) {
        val dbHelper = SQLiteHelper(context)
        val db = dbHelper.writableDatabase

        try {
            // Execute the request and get the response
            val response: Response = client.newCall(request).execute()

            // Check if the request was successful (HTTP status code 200)
            if (response.isSuccessful) {
                // Get the response body as a string
                val responseBodyString = response.body?.string()

                // Log the API response
                Log.d("API Url", "API Url: $apiUrl") // Log the API URL
                Log.d("API Response", "API Response: $responseBodyString") // Log the entire API response

                // Clear the existing data in the database
                dbHelper.deleteAllData() // Call the deleteAllData function

                // Parse the JSON data and insert it into the database
                if (responseBodyString != null) {
                    try {
                        val jsonResponse = JSONObject(responseBodyString)
                        val prayerTimesArray = jsonResponse.getJSONArray("prayerTime")

                        // Insert new data into the "prayer_times" table
                        for (i in 0 until prayerTimesArray.length()) {
                            val jsonObject = prayerTimesArray.getJSONObject(i)
                            val hijri = jsonObject.optString("hijri")
                            val date = jsonObject.optString("date")
                            val day = jsonObject.optString("day")
                            val imsak = jsonObject.optString("imsak")
                            val fajr = jsonObject.optString("fajr")
                            val syuruk = jsonObject.optString("syuruk")
                            val dhuhr = jsonObject.optString("dhuhr")
                            val asr = jsonObject.optString("asr")
                            val maghrib = jsonObject.optString("maghrib")
                            val isha = jsonObject.optString("isha")

                            val insertSQL = """
                                INSERT INTO $TABLE_NAME
                                (hijri, date, day, imsak, fajr, syuruk, dhuhr, asr, maghrib, isha)
                                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                            """.trimIndent()

                            db.execSQL(
                                insertSQL,
                                arrayOf(hijri, date, day, imsak, fajr, syuruk, dhuhr, asr, maghrib, isha)
                            )
                        }

                        // Insert additional data into the "additional_data" table
                        val status = jsonResponse.optString("status")
                        val serverTime = jsonResponse.optString("serverTime")
                        val periodType = jsonResponse.optString("periodType")
                        val lang = jsonResponse.optString("lang")
                        val zone = jsonResponse.optString("zone")
                        val bearing = jsonResponse.optString("bearing")

                        dbHelper.insertAdditionalData(status, serverTime, periodType, lang, zone, bearing)
                        db.close() // Close the database connection in the finally block
                    } catch (e: JSONException) {
                        Log.e("JSON Parsing Error", "Error parsing JSON: ${e.message}")
                    }
                }

            } else {
                Log.d("Request failed with HTTP status code:", "Request failed with HTTP status code: ${response.code}") // Log the API response
                println("Request failed with HTTP status code: ${response.code}")
            }
        } catch (e: Exception) {
            Log.e("Error", "Error: $e")
        }
    }
}




