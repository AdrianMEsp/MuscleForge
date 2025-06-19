package com.adrian.muscleforge.exercise

object ExerciseDataSeeder {

    //to poblate de db
    fun getInitialExercises(): List<Exercise> {
        return listOf(
            Exercise(name = "Biceps 1y1", series = 0, repetitions = 0, weight = 0.0),
            Exercise(name = "Hip Trust", series = 0, repetitions = 0, weight = 0.0)
        )
    }
}