package com.adrian.muscleforge.routines
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert
    suspend fun insertRoutine(routine: Routine)

    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<Routine>>
}