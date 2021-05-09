package com.example.spataroapp.presentation.home_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.spataroapp.R
import com.example.spataroapp.databinding.FragmentHomeBinding
import com.example.spataroapp.databinding.FragmentSplashBinding
import com.example.spataroapp.presentation.MainActivity
import com.example.spataroapp.presentation.splash_screen.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // settings binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val act = activity as MainActivity
        act?.createToolbar()
        viewModel.user.observe(viewLifecycleOwner, Observer { value ->
            if(value!= null){
                act?.setInfoDrawer(value.nombre+" "+value.apellido, value.correo.toString(), value.foto.toString())
            }
        })
        return binding.root
    }

}