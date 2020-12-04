package com.applications.toms.comidasdelasemana.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DatabaseDao {
    //CRUD
    //Insertar / Create
    @Insert
    suspend fun insert(item: DailyMeals)

    //Obtener / Read
    @Query("SELECT * FROM daily_meals ORDER BY dailyMealId ASC")
    fun getAll(): List<DailyMeals>

    //Actualizar / Update
    @Query("UPDATE daily_meals SET lunch = :newLunch WHERE day = :key")
    suspend fun updateLunchMeal(key: String, newLunch: String)

    @Query("UPDATE daily_meals SET diner = :newDiner WHERE day = :key")
    suspend fun updateDinerMeal(key: String, newDiner: String)

    //Borrar / Delete
    @Query("DELETE FROM daily_meals WHERE dailyMealId = :key")
    fun deleteMeal(key: Long)
}