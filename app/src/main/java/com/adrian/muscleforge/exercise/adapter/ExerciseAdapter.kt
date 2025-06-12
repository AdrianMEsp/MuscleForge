package com.adrian.muscleforge.exercise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrian.muscleforge.R
import com.adrian.muscleforge.exercise.Exercise

class ExerciseAdapter(
    private var exercises: List<Exercise>,
    private val onEditClick: (Exercise) -> Unit,
    private val onDeleteClick: (Exercise) -> Unit )
    : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>(){

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameText: TextView = itemView.findViewById(R.id.tvExerciseName)
        val series: TextView = itemView.findViewById(R.id.tvSeries)
        val reps: TextView = itemView.findViewById(R.id.tvRepetitions)
        val weight: TextView = itemView.findViewById(R.id.tvWeight)

        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEditExercise)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteExercise)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false )
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ExerciseViewHolder,
        position: Int
    ) {
        holder.nameText.text = exercises[position].name
        holder.series.text = exercises[position].series.toString()
        holder.reps.text = exercises[position].repetitions.toString()
        holder.weight.text = exercises[position].weight.toString()

        val exercise = exercises[position]
        holder.btnEdit.setOnClickListener {
            onEditClick(exercise)
        }
        holder.btnDelete.setOnClickListener {
            onDeleteClick(exercise)
        }
    }

    override fun getItemCount(): Int = exercises.size

    fun updateList(newList: List<Exercise>){
        exercises = newList
        notifyDataSetChanged()
    }

}