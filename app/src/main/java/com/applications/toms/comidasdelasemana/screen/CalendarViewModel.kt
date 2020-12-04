package com.applications.toms.comidasdelasemana.screen

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applications.toms.comidasdelasemana.R
import com.applications.toms.comidasdelasemana.database.DailyMeals
import com.applications.toms.comidasdelasemana.database.DatabaseDao
import com.applications.toms.comidasdelasemana.showKeyboard
import kotlinx.coroutines.*

class CalendarViewModel(application: Application, val database: DatabaseDao) : AndroidViewModel(application) {

    //Define viewModelJob and assign it an instance of Job.
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        //Para que todas las coroutines se cancelen al clear la viewmodel
        viewModelJob.cancel()
    }

    //Define a uiScope for the coroutines:
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private val _weeklyMeals = MutableLiveData<List<DailyMeals>>()
    val weeklyMeals: LiveData<List<DailyMeals>>
        get() = _weeklyMeals

    private val initialData = MutableLiveData<List<DailyMeals>>()

    enum class EditingStatus { INITIAL, EDITING, ERROR, DONE }
    //reset EditText Event
    private val _status = MutableLiveData<EditingStatus>()
    val status : LiveData<EditingStatus>
        get() = _status

    init {
        initializeData()
        _status.value = EditingStatus.INITIAL
        initialData.value = listOf(
            DailyMeals(0, application.getString(R.string.monday),"Almuerzo de Lunes","Cena de Lunes"),
            DailyMeals(1, application.getString(R.string.tuesday),"Almuerzo de Martes","Cena de Martes"),
            DailyMeals(2, application.getString(R.string.wednesday),"Almuerzo de Miercoles","Cena de Miercoles"),
            DailyMeals(3, application.getString(R.string.thursday),"Almuerzo de Jueves","Cena de Jueves"),
            DailyMeals(4, application.getString(R.string.friday),"Almuerzo de Viernes","Cena de Viernes"),
            DailyMeals(5, application.getString(R.string.saturday),"Almuerzo de Sabado","Cena de Sabado"),
            DailyMeals(6, application.getString(R.string.sunday),"Almuerzo de Domingo","Cena de Domingo")
        )
    }

    private fun initializeData() {
        //Usamos una corutina para obtener la data de la BD para no bloquear la UI mientras esperamos los rdos
        uiScope.launch {
            _weeklyMeals.value = getDataFromDataBase()
            Log.d(TAG, "updateDiner: ${weeklyMeals}")
            if (weeklyMeals.value!!.isEmpty()){
                for (dailyMeal in initialData.value!!){
                    database.insert(DailyMeals(day = dailyMeal.day,lunch = dailyMeal.lunch,diner = dailyMeal.diner))
                }
            }
        }
    }

    //La marcamos como suspend porque la llamamos desde un corutina
    private suspend fun getDataFromDataBase(): List<DailyMeals>? {
        return withContext(Dispatchers.IO){
            var data = database.getAll()
            data
        }
    }

    fun updateDiner(onDay:String,onMeal:String){
        uiScope.launch {
            database.updateDinerMeal(onDay,onMeal)
            _weeklyMeals.value = getDataFromDataBase()
            Log.d(TAG, "updateDiner: ${weeklyMeals}")
            _status.value = EditingStatus.DONE
        }
    }

    fun updateLunch(onDay:String,onMeal:String){
        uiScope.launch {
            database.updateLunchMeal(onDay,onMeal)
            _weeklyMeals.value = getDataFromDataBase()
            Log.d(TAG, "updateDiner: ${weeklyMeals}")
            _status.value = EditingStatus.DONE
        }
    }

    fun onStartEditing(day: String,meal:String,mealToChange:String) {
        _status.value = EditingStatus.EDITING
        if (meal == "diner"){
            updateDiner(day,mealToChange)
        }else{
            updateLunch(day,mealToChange)
        }
    }

    companion object{
        private const val TAG = "TMV-CalendarFragmentViewModel"
    }
}