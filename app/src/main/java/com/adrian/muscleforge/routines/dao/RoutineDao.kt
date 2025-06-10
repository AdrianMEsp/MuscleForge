package com.adrian.muscleforge.routines.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.adrian.muscleforge.routines.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert
    suspend fun insertRoutine(routine: Routine)

    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<Routine>>
}