package com.adrian.muscleforge.data

import android.content.Context
import android.provider.SyncStateContract.Helpers.insert
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.adrian.muscleforge.exercise.*
import com.adrian.muscleforge.exercise.dao.ExerciseDao
import com.adrian.muscleforge.routines.Routine
import com.adrian.muscleforge.routines.dao.RoutineDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Routine::class, Exercise::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,
                    "muscle_forge_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // Poblar la base de datos al crearla
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.exerciseDao()?.apply {
                                    ExerciseDataSeeder.getInitialExercises().forEach {
                                        addExercise(it)
                                    }
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }

        }
    }
}

