package com.example.spataroapp.presentation.admin_user_screen

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.spataroapp.R
import com.example.spataroapp.databinding.FragmentUsersBinding
import com.example.spataroapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Users : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        //Observers
        observers()
        val act = activity as MainActivity
        act?.allowNavigationUP()
        //return the view
        binding.btnImage.setOnClickListener {
            openImageChooser()
        }
        return binding.root
    }

    //open image
    private fun openImageChooser(){
        Intent(Intent.ACTION_PICK).also {
            it.type ="image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, 100)
        }
    }
    //process the image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                100 -> {
                    viewModel.photo.value = data?.data
                    if (data != null) {
                        binding.pickedPhoto.setImageURI(data.data!!)
                    }
                }
            }
        }
    }

    //build all the observers
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun observers(){
        viewModel.documents.observe(viewLifecycleOwner, Observer { value ->
            if(value.isNotEmpty()){
                updateList(value)
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun updateList(list: MutableList<String>){
        val adaptCO = context?.let { ArrayAdapter(it, R.layout.option_item,list.toTypedArray()) }
        if (adaptCO != null && !adaptCO.isEmpty) {
           binding.typeDocInput.setText(viewModel.typeID.value, false)
            binding.typeDocInput.setAdapter(adaptCO)
        }
    }

}