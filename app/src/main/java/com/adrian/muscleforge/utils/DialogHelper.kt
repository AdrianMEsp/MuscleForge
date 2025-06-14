package com.adrian.muscleforge.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.adrian.muscleforge.R
import com.adrian.muscleforge.exercise.Exercise

object DialogHelper {

    fun showDialogConfirm(context: Context, message: String, onResult: (Boolean) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val tvMessage: TextView = dialogView.findViewById(R.id.etHintDeleteMessage)
        val btnAccept: Button = dialogView.findViewById(R.id.btnAccept)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)

        tvMessage.text = message

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

}
