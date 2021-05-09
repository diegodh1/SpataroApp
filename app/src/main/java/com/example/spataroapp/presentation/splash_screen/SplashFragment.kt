package com.example.spataroapp.presentation.splash_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.spataroapp.R
import com.example.spataroapp.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //observers
        viewModel.message.observe(viewLifecycleOwner, Observer { value ->
            when {
                value == "success" -> {
                    val action = SplashFragmentDirections.actionSplashFragmentToHome2()
                    view?.findNavController()!!.navigate(action)
                }
                value != "" -> {
                    val action = SplashFragmentDirections.actionSplashFragmentToLogin()
                    view?.findNavController()!!.navigate(action)
                }
            }
        })
        return binding.root
    }
}