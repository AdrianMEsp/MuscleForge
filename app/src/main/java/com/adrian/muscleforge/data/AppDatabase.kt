package com.adrian.muscleforge.data

import android.content.Context
import android.util.Log
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

@Database(entities = [Routine::class, Exercise::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "muscle_forge_db"
                )
                    .fallbackToDestructiveMigration() // 🔧 Borra DB vieja si cambia versión
                    .addCallback(DatabaseCallback())   // ⬅️ Attach callback para seeding
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.i("DB", "onCreate triggered, seeding initial data")
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.exerciseDao()?.let { dao ->
                        ExerciseDataSeeder.getInitialExercises().forEach {
                            dao.addExercise(it)
                            Log.i("DB", "Inserted exercise: $it")
                        }
                    }
                }
            }
        }
    }
}
