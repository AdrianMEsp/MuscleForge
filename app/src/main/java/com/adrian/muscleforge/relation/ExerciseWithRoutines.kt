package com.adrian.muscleforge.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.routines.Routine

data class ExerciseWithRoutines(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "routineId",
        associateBy = Junction(RoutineExerciseCrossRef::class)
    )
    val routines: List<Routine>
)