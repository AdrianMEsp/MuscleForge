package com.adrian.muscleforge.routines.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.adrian.muscleforge.R
import com.adrian.muscleforge.routines.Routine

class RoutineAdapter(
    private var routines: List<Routine>,
    private val onCheckedChanged: (Routine) -> Unit,
    private val onRoutineClick: (Routine) -> Unit,
    private val onDeleteClick: (Routine) -> Unit,
    private val onAddExercisesClick: (Routine) -> Unit
) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvRoutineName)
//        val textView2: TextView = itemView.findViewById(R.id.tvRoutineNameInRoutine)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkboxRoutine)
        val card: CardView = itemView.findViewById(R.id.cardViewRoutine)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnAddExerToRoutine)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteRoutine)

        fun bind(routine: Routine) {
            textView.text = routine.name
//            textView2.text = routine.name

            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = routine.isChecked
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChanged(routine.copy(isChecked = isChecked))
            }

            card.setOnClickListener {
                onRoutineClick(routine) // Abre vista de ejercicios asignados a esta rutina
            }

            btnEdit.setOnClickListener {
                onAddExercisesClick(routine) // Abre selección de ejercicios para agregar
            }

            btnDelete.setOnClickListener {
                onDeleteClick(routine)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        holder.bind(routines[position])
    }

    override fun getItemCount(): Int = routines.size

    fun updateList(newList: List<Routine>) {
        routines = newList
        notifyDataSetChanged()
    }
}
