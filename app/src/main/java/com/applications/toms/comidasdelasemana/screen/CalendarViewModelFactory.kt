package com.applications.toms.comidasdelasemana.screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applications.toms.comidasdelasemana.database.DatabaseDao

class CalendarViewModelFactory(private val dataSource: DatabaseDao,
                             private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            return CalendarViewModel(application,dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}