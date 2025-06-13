package com.adrian.muscleforge.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.routines.dao.RoutineDao
import com.adrian.muscleforge.routines.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(private val repository: RoutineRepository
) : ViewModel() {

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines: StateFlow<List<Routine>> = _routines.asStateFlow()

    private val _exercisesInRoutine = MutableStateFlow<List<Exercise>>(emptyList())
    val exercisesInRoutine: StateFlow<List<Exercise>> = _exercisesInRoutine.asStateFlow()

    init {
        loadRoutines()
    }

    fun loadExercisesForRoutine(routineId: Long) {
        viewModelScope.launch {
            val routineWithExercises = repository.getRoutineWithExercises(routineId)
            _exercisesInRoutine.value = routineWithExercises.exercises
        }
    }

    //This things connect to de daoRoutine
    private fun loadRoutines() {
        viewModelScope.launch {
            repository.getAllRoutines()
                .collect { list ->
                    _routines.value = list
                }
        }
    }

    fun updateRoutine(routine: Routine){
        viewModelScope.launch {
            repository.updateRoutine(routine)
        }
    }

    fun deleteRoutine(routine: Routine){
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }

    fun addRoutine(name: String) {
        viewModelScope.launch {
            val newRoutine = Routine(name = name)
            repository.addRoutine(newRoutine)
            // No hace falta volver a cargar manualmente: el Flow se actualiza solo
        }
    }

//    this delete only from the routine
    fun deleteExerciseFromRoutine(exerciseId: Long, routineId: Long){
        viewModelScope.launch {
            repository.removeExerciseFromRoutine(exerciseId,routineId)
            loadExercisesForRoutine(routineId)
        }
    }


    //    this edit that exercise whatever he is
    fun editExercise(exercise: Exercise, id:Long){
        viewModelScope.launch {
            repository.updateExercise(exercise)
            loadExercisesForRoutine(id)


//            this 2 lines updates the list at edit time
//            _exercisesInRoutine.value = _exercisesInRoutine.value.map {
//                if (it.exerciseId == exercise.exerciseId ) exercise else it
//            }
        }
    }
}

