package com.adrian.muscleforge.di

import android.content.Context
import androidx.room.Room
import com.adrian.muscleforge.data.AppDatabase
import com.adrian.muscleforge.routines.RoutineDao
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
            "routine_database"
        ).build()
    }

    @Provides
    fun provideRoutineDao(db: AppDatabase): RoutineDao {
        return db.routineDao()
    }
}
