package com.adrian.muscleforge.exercise

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.muscleforge.exercise.dao.ExerciseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor( private val exerciseDao: ExerciseDao)
    : ViewModel(){

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            exerciseDao.getAllExercises().collect {
                list -> _exercises.value = list
            }
        }
    }

    fun addExercise(name: String,series: Int, repeats: Int, weight: Double){
        viewModelScope.launch {
            val newExercise = Exercise(name = name, series = series, repetitions = repeats, weight = weight)
            exerciseDao.addExercise(newExercise)
        }
    }

    fun deleteExercise(exercise: Exercise){
        viewModelScope.launch {
            exerciseDao.delete(exercise)
        }
    }

}