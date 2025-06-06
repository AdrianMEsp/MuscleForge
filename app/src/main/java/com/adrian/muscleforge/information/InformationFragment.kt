package com.adrian.muscleforge.information

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import com.adrian.muscleforge.R
import com.adrian.muscleforge.databinding.FragmentInformationBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformationFragment : Fragment() {
    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!

    private lateinit var chronometer: Chronometer
    private var running = false
    private var pauseOffset: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chronometer(view)
    }

    private fun chronometer(view: View){
        chronometer = view.findViewById(R.id.chronometer)
        val startButton = view.findViewById<FloatingActionButton>(R.id.startButton)
        val stopButton = view.findViewById<FloatingActionButton>(R.id.stopButton)
        val pauseButton = view.findViewById<FloatingActionButton>(R.id.pauseButton)

        startButton.setOnClickListener {
            if (!running) {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                running = true
            }
        }

        pauseButton.setOnClickListener {
            if (running) {
                chronometer.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                running = false
            }
        }

        stopButton.setOnClickListener {
            chronometer.stop()
            chronometer.base = SystemClock.elapsedRealtime()
            pauseOffset = 0L
            running = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
}