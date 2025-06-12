package com.adrian.muscleforge.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.routines.Routine

data class RoutineWithExercises(
    @Embedded val routine: Routine,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "exerciseId",
        associateBy = Junction(RoutineExerciseCrossRef::class)
    )
    val exercises: List<Exercise>
)

