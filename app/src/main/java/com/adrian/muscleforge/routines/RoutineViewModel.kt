package com.adrian.muscleforge.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.routines.dao.RoutineDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(private val routineDao: RoutineDao
) : ViewModel() {

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines: StateFlow<List<Routine>> = _routines.asStateFlow()

    private val _routineExercises = MutableStateFlow<List<Exercise>>(emptyList())
    val routineExercises: StateFlow<List<Exercise>> = _routineExercises.asStateFlow()

    init {
        loadRoutines()
    }

    fun loadExercisesForRoutine(routineId: Long) {
        viewModelScope.launch {
            val routineWithExercises = routineDao.getRoutineWithExercises(routineId)
            _routineExercises.value = routineWithExercises.exercises
        }
    }

    //This things connect to de daoRoutine
    private fun loadRoutines() {
        viewModelScope.launch {
            routineDao.getAllRoutines()
                .collect { list ->
                    _routines.value = list
                }
        }
    }

    fun updateRoutine(routine: Routine){
        viewModelScope.launch {
            routineDao.updateRoutine(routine)
        }
    }

    fun deleteRoutine(routine: Routine){
        viewModelScope.launch {
            routineDao.delete(routine)
        }
    }

    fun addRoutine(name: String) {
        viewModelScope.launch {
            val newRoutine = Routine(name = name)
            routineDao.insertRoutine(newRoutine)
            // No hace falta volver a cargar manualmente: el Flow se actualiza solo
        }
    }
}

