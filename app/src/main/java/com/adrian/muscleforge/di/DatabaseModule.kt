package com.adrian.muscleforge.di

import android.content.Context
import androidx.room.Room
import com.adrian.muscleforge.data.AppDatabase
import com.adrian.muscleforge.exercise.dao.ExerciseDao
import com.adrian.muscleforge.routines.dao.RoutineDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "muscle_forge_db"
        ).build()
    }

    @Provides
    fun provideRoutineDao(db: AppDatabase): RoutineDao {
        return db.routineDao()
    }

    @Provides
    fun provideExerciseDao(db: AppDatabase): ExerciseDao{
        return db.exerciseDao()
    }
}
