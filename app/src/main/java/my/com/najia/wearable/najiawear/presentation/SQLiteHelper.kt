package my.com.najia.wearable.najiawear.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "prayer_times.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "prayer_times"
        private const val ADDITIONAL_TABLE_NAME = "additional_data"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                hijri TEXT,
                date TEXT,
                day TEXT,
                imsak TEXT,
                fajr TEXT,
                syuruk TEXT,
                dhuhr TEXT,
                asr TEXT,
                maghrib TEXT,
                isha TEXT
            )
        """.trimIndent()

        val createAdditionalTableSQL = """
            CREATE TABLE IF NOT EXISTS $ADDITIONAL_TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                status TEXT,
                serverTime TEXT,
                periodType TEXT,
                lang TEXT,
                zone TEXT,
                bearing TEXT
            )
        """.trimIndent()

        db.execSQL(createTableSQL)
        db.execSQL(createAdditionalTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $ADDITIONAL_TABLE_NAME")
        onCreate(db)
    }

    fun deleteAllData() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.execSQL("DELETE FROM $ADDITIONAL_TABLE_NAME")
    }

    fun insertAdditionalData(
        status: String,
        serverTime: String,
        periodType: String,
        lang: String,
        zone: String,
        bearing: String
    ) {
        val db = this.writableDatabase
        val insertSQL = """
            INSERT INTO $ADDITIONAL_TABLE_NAME
            (status, serverTime, periodType, lang, zone, bearing)
            VALUES (?, ?, ?, ?, ?, ?)
        """.trimIndent()

        db.execSQL(
            insertSQL,
            arrayOf(status, serverTime, periodType, lang, zone, bearing)
        )
    }

    @SuppressLint("Range")
    fun getZone(): String? {
        val db = this.readableDatabase
        val query = "SELECT zone FROM $ADDITIONAL_TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        return if (cursor.moveToFirst()) {
            val zone = cursor.getString(cursor.getColumnIndex("zone"))
            cursor.close()
            zone
        } else {
            null
        }
    }
}
