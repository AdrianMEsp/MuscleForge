package com.adrian.muscleforge.routines.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.relation.RoutineExerciseCrossRef
import com.adrian.muscleforge.relation.RoutineWithExercises
import com.adrian.muscleforge.routines.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert
    suspend fun insertRoutine(routine: Routine)

    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<Routine>>

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Delete
    suspend fun delete(routine: Routine)

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Insert
    suspend fun insertRoutineExerciseCrossRef(crossRef: RoutineExerciseCrossRef)

    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = :id")
    suspend fun getRoutineWithExercises(id: Long): RoutineWithExercises

}