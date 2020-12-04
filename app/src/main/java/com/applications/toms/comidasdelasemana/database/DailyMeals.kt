package com.applications.toms.comidasdelasemana.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "daily_meals")
data class DailyMeals(

    @PrimaryKey(autoGenerate = true)
    val dailyMealId: Long = 0L,

    @ColumnInfo(name = "day")
    val day: String,

    @ColumnInfo(name = "lunch")
    val lunch: String,

    @ColumnInfo(name = "diner")
    val diner:String
)