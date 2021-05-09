package com.example.spataroapp.presentation.login_screen

import android.content.res.ColorStateList
import android.graphics.Color
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
import com.example.spataroapp.R
import com.example.spataroapp.databinding.FragmentLoginBinding
import com.example.spataroapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : Fragment() {
    //variables
    lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //implementation binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //hide toolbar
        val act = activity as MainActivity
        act?.hideDrawer()

        //call the observers
        observers()

        //init
        return binding.root
    }

    //Observers
    private fun observers() {
        //user observer
        viewModel.errorUser.observe(viewLifecycleOwner, Observer { value ->
            if (value) {
                binding.userLayout.setErrorTextColor(ColorStateList.valueOf(Color.WHITE))
                binding.userLayout.error = "Por favor ingrese un usuario válido, este campo solo puede tener números"
            } else {
                binding.userLayout.error = ""
            }
        })
        //password observer
        viewModel.errorPassword.observe(viewLifecycleOwner, Observer { value ->
            if (value) {
                binding.passwordLayout.setErrorTextColor(ColorStateList.valueOf(Color.WHITE))
                binding.passwordLayout.error = "Por favor ingrese una contraseña"
            } else {
                binding.passwordLayout.error = ""
            }
        })
        //message observer
        viewModel.message.observe(viewLifecycleOwner, Observer { value ->
            when{
                value == "success" ->{
                    val action = LoginDirections.actionLoginToHome()
                    view?.findNavController()!!.navigate(action)
                }
                value != "" -> Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
            }
        })

    }

}