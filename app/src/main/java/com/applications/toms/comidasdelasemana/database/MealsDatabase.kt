package com.applications.toms.comidasdelasemana.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DailyMeals::class], version = 1, exportSchema = false)
abstract class MealsDatabase: RoomDatabase(){

    abstract val listDatabaseDao: DatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: MealsDatabase? = null

        fun getInstance(context: Context): MealsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MealsDatabase::class.java,
                        "daily_meals"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}