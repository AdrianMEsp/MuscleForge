package com.adrian.muscleforge.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adrian.muscleforge.routines.Routine
import com.adrian.muscleforge.routines.RoutineDao

@Database(entities = [Routine::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
