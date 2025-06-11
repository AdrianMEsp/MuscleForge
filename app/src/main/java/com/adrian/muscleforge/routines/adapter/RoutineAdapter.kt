package com.adrian.muscleforge.routines.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrian.muscleforge.R
import com.adrian.muscleforge.routines.Routine

class RoutineAdapter(private var routines: List<Routine>,
    private val onCheckedChanged: (Routine) -> Unit )
    : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvRoutineName)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkboxRoutine)

        fun bind(routine: Routine){
            textView.text = routine.name
            checkbox.setOnCheckedChangeListener(null) //prevents unnecessary shoots
            checkbox.isChecked = routine.isChecked
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                val update = routine.copy(isChecked = isChecked)
                onCheckedChanged(update)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routines[position]
        holder.bind(routine)

    }

    override fun getItemCount(): Int = routines.size

    fun updateList(newList: List<Routine>) {
        routines = newList
        notifyDataSetChanged()
    }
}