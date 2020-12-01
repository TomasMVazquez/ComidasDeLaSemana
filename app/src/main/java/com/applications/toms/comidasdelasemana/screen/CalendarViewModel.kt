package com.applications.toms.comidasdelasemana.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val _weeklyMeals = MutableLiveData<List<Pair<String,List<Pair<String,String>>>>>()
    val weklyMeals: LiveData<List<Pair<String,List<Pair<String,String>>>>>
        get() = _weeklyMeals

    init {
        _weeklyMeals.value = listOf(
            "Lunes"     to listOf("Almuerzo" to "L1", "Cena" to "L2"),
            "Martes"    to listOf("Almuerzo" to "M1", "Cena" to "M2"),
            "Miercoles" to listOf("Almuerzo" to "M1", "Cena" to "M2"),
            "Jueves"    to listOf("Almuerzo" to "J1", "Cena" to "J2"),
            "Viernes"   to listOf("Almuerzo" to "V1", "Cena" to "V2"),
            "Sabado"    to listOf("Almuerzo" to "S1", "Cena" to "S2"),
            "Domingo"   to listOf("Almuerzo" to "D1", "Cena" to "D2")
        )
    }
}