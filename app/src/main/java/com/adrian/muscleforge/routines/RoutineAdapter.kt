package com.adrian.muscleforge.routines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adrian.muscleforge.databinding.ItemRoutineBinding

class RoutineAdapter : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    private val routines = mutableListOf<String>()

    fun submitList(list: List<String>) {
        routines.clear()
        routines.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val binding = ItemRoutineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoutineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        holder.bind(routines[position])
    }

    override fun getItemCount(): Int = routines.size

    inner class RoutineViewHolder(private val binding: ItemRoutineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String) {
            binding.routineTitle.text = name
        }
    }
}
