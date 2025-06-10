package com.adrian.muscleforge.routines.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrian.muscleforge.R
import com.adrian.muscleforge.routines.Routine

class RoutineAdapter(private var routines: List<Routine>)
    : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.tvRoutineName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        holder.nameText.text = routines[position].name
    }

    override fun getItemCount(): Int = routines.size

    fun updateList(newList: List<Routine>) {
        routines = newList
        notifyDataSetChanged()
    }
}