package com.example.spataroapp.presentation.canvas_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.spataroapp.R
import com.example.spataroapp.databinding.FragmentCanvasBinding


class Canvas : Fragment() {
    private lateinit var binding: FragmentCanvasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_canvas, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }
}