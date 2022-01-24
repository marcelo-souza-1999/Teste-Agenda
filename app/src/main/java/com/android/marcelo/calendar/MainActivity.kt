package com.android.marcelo.calendar

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        saveAgenda()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
                ), CALENDAR_REQUESTS_CODE
            )
        }

    }

    private fun saveAgenda() {

        val calID: Long = 3
        val startMillis: Long = Calendar.getInstance().run {
            set(2022, 1, 24, 13, 30)
            timeInMillis
        }
        val endMillis: Long = Calendar.getInstance().run {
            set(2022, 2, 14, 23, 45)
            timeInMillis
        }

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, "Evento do Marcelo")
            put(CalendarContract.Events.DESCRIPTION, "Evento de teste até fevereiro")
            put(CalendarContract.Events.CALENDAR_ID, calID)
            put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles")
        }
        val uri: Uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)!!

// get the event ID that is the last element in the Uri
        val eventID: Long = uri.lastPathSegment!!.toLong()

        Toast.makeText(this, "Evento salvo na agenda, seu id é: ${eventID}", Toast.LENGTH_LONG)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALENDAR_REQUESTS_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    saveAgenda()
                }
            }
        }
    }

    companion object {
        const val CALENDAR_REQUESTS_CODE = 1000
    }
}