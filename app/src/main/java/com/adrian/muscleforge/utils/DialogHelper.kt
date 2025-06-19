package com.adrian.muscleforge.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.adrian.muscleforge.R
import com.adrian.muscleforge.exercise.Exercise
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object DialogHelper {

    fun showDialogConfirmDeleteExercise(context: Context, onResult: (Boolean) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_exercise, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAccept: Button = dialogView.findViewById(R.id.btnAccept)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)

        btnAccept.setOnClickListener {
            onResult(true)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            onResult(false)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDialogCreateRoutine(context: Context, onResult: (String?) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_routine, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnAccept: Button = dialogView.findViewById(R.id.btnAccept)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)
        val etNewRoutineName: EditText = dialogView.findViewById(R.id.etNewRoutineName)

        btnAccept.setOnClickListener {
            val name =
                etNewRoutineName.text.toString().trim().replaceFirstChar { it.uppercaseChar() }
            if (name.isNotEmpty()) {
                onResult(name)
                dialog.dismiss()
            } else {
                etNewRoutineName.error = "Name Required"
            }

        }
        btnCancel.setOnClickListener {
            onResult(null)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDialogCreateExercise(context: Context, onResult: (Exercise?) -> Unit) {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_exercise, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAccept: Button = dialogView.findViewById(R.id.btnAccept)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)
        val exerciseName: EditText = dialogView.findViewById(R.id.exerciseName)
        val exerciseSeries: EditText = dialogView.findViewById(R.id.exerciseSeries)
        val exerciseRepetitions: EditText = dialogView.findViewById(R.id.exerciseRepetitions)
        val exerciseWeight: EditText = dialogView.findViewById(R.id.exerciseWeight)

        btnAccept.setOnClickListener {
            if (exerciseName.text.toString().isNotBlank()) {

                val series = exerciseSeries.text.toString().toIntOrNull() ?: 0
                val repetitions = exerciseRepetitions.text.toString().toIntOrNull() ?: 0
                val weight = exerciseWeight.text.toString().toDoubleOrNull() ?: 0.0

                val newExercise = Exercise(
                    name = exerciseName.text.toString().replaceFirstChar { it.uppercaseChar() },
                    series = series,
                    repetitions = repetitions,
                    weight = weight
                )
                onResult(newExercise)
                dialog.dismiss()

            } else {
                exerciseName.error = "Name Required"
            }
        }
        btnCancel.setOnClickListener {
            onResult(null)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDialogEditRoutine(context: Context, exercise: Exercise, onResult: (Exercise) -> Unit){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_exercise,null)

        val btnAccept: Button = dialogView.findViewById(R.id.btnAccept)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)
        val exerciseName: EditText = dialogView.findViewById(R.id.exerciseName)
        val exerciseSeries: EditText = dialogView.findViewById(R.id.exerciseSeries)
        val exerciseRepetitions: EditText = dialogView.findViewById(R.id.exerciseRepetitions)
        val exerciseWeight: EditText = dialogView.findViewById(R.id.exerciseWeight)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        exerciseName.setText(exercise.name)
        exerciseSeries.setText(exercise.series.toString())
        exerciseRepetitions.setText(exercise.repetitions.toString())
        exerciseWeight.setText(exercise.weight.toString())

        btnAccept.setOnClickListener {
            val updatedExercise = exercise.copy(
                name = exerciseName.text.toString(),
                series = exerciseSeries.text.toString().toIntOrNull() ?: 0,
                repetitions = exerciseRepetitions.text.toString().toIntOrNull() ?: 0,
                weight = exerciseWeight.text.toString().toDoubleOrNull() ?: 0.0
            )
            onResult(updatedExercise)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    fun showDialogConfirmDeleteRoutine(context: Context, onResult: (Boolean) -> Unit){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_routine, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAccept: Button = dialogView.findViewById(R.id.btnAccept)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)

        btnAccept.setOnClickListener {
            onResult(true)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            onResult(false)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDialogExercisesAdded(context: Context, lifecycleOwner: LifecycleOwner){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_exercises_added,null)


        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        lifecycleOwner.lifecycleScope.launch {
            delay(1000)
            dialog.dismiss()
        }
    }
}
