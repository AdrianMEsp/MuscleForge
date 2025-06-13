package com.adrian.muscleforge.exercise

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.muscleforge.exercise.dao.ExerciseDao
import com.adrian.muscleforge.relation.RoutineExerciseCrossRef
import com.adrian.muscleforge.routines.dao.RoutineDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor( private val exerciseDao: ExerciseDao,
                                             private val routineDao: RoutineDao)
    : ViewModel(){

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    init {
        loadExercises()
    }

    suspend fun getUnassignedExercises(routineId: Long): List<Exercise> {
        return exerciseDao.getUnassignedExercises(routineId)
    }

    fun insertRoutineExerciseCrossRef(crossRef: RoutineExerciseCrossRef) {
        viewModelScope.launch {
            routineDao.insertRoutineExerciseCrossRef(crossRef)
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            exerciseDao.getAllExercises().collect {
                list -> _exercises.value = list
            }
        }
    }

    fun updateExercise(exercise: Exercise){
        viewModelScope.launch {
            exerciseDao.updateExercise(exercise)
        }
    }

    fun addExercise(name: String,series: Int, repeats: Int, weight: Double){
        viewModelScope.launch {
            val newExercise = Exercise(name = name, series = series, repetitions = repeats, weight = weight)
            exerciseDao.addExercise(newExercise)
            loadExercises()
        }
    }

    fun deleteExercise(exercise: Exercise){
        viewModelScope.launch {
            exerciseDao.delete(exercise)
        }
    }

}