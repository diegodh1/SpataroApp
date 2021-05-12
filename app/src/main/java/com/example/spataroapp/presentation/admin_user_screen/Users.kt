package com.example.spataroapp.presentation.admin_user_screen

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.spataroapp.R
import com.example.spataroapp.databinding.FragmentUsersBinding
import com.example.spataroapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.infomation.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Base64.getEncoder


@AndroidEntryPoint
class Users : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private val viewModel: UsersViewModel by viewModels()
    private lateinit var mAlertDialog: AlertDialog

    //alert


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.loading, null)
        val mBuilder = AlertDialog.Builder(context).setView(mDialogView).setTitle("")
        mAlertDialog = mBuilder.show()
        mAlertDialog.dismiss()
        //activity
        val act = activity as MainActivity
        act?.allowNavigationUP()
        //listeners
        listeners()
        //Observers
        observers()
        //return the view

        return binding.root
    }

    fun convertToBase64(): String {
        val bmp = (binding.pickedPhoto.getDrawable() as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        val base64 = Base64.encodeToString(image, Base64.NO_WRAP)
        return base64
    }

    //listeners
    fun listeners() {
        //submitListener btn_finish
        binding.btnFinish.setOnClickListener {
            val ok = viewModel.checkFields()
            if(ok){
                when(viewModel.edit.value){
                    false -> {
                        viewModel.loading.value = true
                        lifecycleScope.launch {
                            val res = withContext(Dispatchers.IO) {
                                val photo = convertToBase64()
                                viewModel.insertUser(photo)
                            }
                            mAlertDialog.dismiss()
                            viewModel.cleanFields()
                            viewModel.loading.value = false
                            viewModel.message.value = res
                        }
                    }
                    else ->{
                        viewModel.loading.value = true
                        lifecycleScope.launch {
                            val res = withContext(Dispatchers.IO) {
                                val photo = convertToBase64()
                                viewModel.editUser(photo)
                            }
                            mAlertDialog.dismiss()
                            viewModel.cleanFields()
                            viewModel.loading.value = false
                            viewModel.message.value = res
                        }
                    }
                }

            }

        }
        //btnImage Listener
        binding.btnImage.setOnClickListener {
            openImageChooser()
        }
    }

    //open image
    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, 100)
        }
    }

    //process the image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
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
    fun observers() {
        //list documents observer
        viewModel.documents.observe(viewLifecycleOwner, Observer { value ->
            if (value.isNotEmpty()) {
                updateList(value)
            }
        })

        //photoBase64 observer
        viewModel.photoBase64.observe(viewLifecycleOwner, Observer { value ->
            val imageBytes = Base64.decode(value, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            binding.pickedPhoto.setImageBitmap(decodedImage)
        })

        //docTypeID observer
        viewModel.typeID.observe(viewLifecycleOwner, Observer { value ->
            if (value != "") {
                binding.typeDocInput.setText(value, false)
            }
        })

        //is edit user
        viewModel.edit.observe(viewLifecycleOwner, Observer { value ->
            binding.inputPassword.isEnabled = !value
            binding.inputUserId.isEnabled = !value
        })

        //message observer
        viewModel.message.observe(viewLifecycleOwner, Observer { value ->
            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.infomation, null)
            val mBuilder = AlertDialog.Builder(context).setView(mDialogView).setTitle("")
            when{
                value == "Registro Realizado" || value == "Registro Actualizado" -> {
                    mDialogView.content.text = value
                    mDialogView.aceptar.isVisible = true
                    mDialogView.cerrar.isVisible = false
                }
                value != "" ->{
                    mDialogView.content.text = value
                    mDialogView.cerrar.isVisible = true
                    mDialogView.aceptar.isVisible = false
                }
            }

            val alertDialog = mBuilder.show()
            mDialogView.aceptar.setOnClickListener {
                alertDialog.dismiss()
            }
            mDialogView.cerrar.setOnClickListener {
                alertDialog.dismiss()
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { value ->
            if(!value){
                mAlertDialog.dismiss()
            }
            else{
                mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mAlertDialog.setCanceledOnTouchOutside(false);
                mAlertDialog.show()
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun updateList(list: MutableList<String>) {
        val adaptCO = context?.let { ArrayAdapter(it, R.layout.option_item, list.toTypedArray()) }
        if (adaptCO != null && !adaptCO.isEmpty) {
            binding.typeDocInput.setText(viewModel.typeID.value, false)
            binding.typeDocInput.setAdapter(adaptCO)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.users_menu, menu)
        val search = menu?.findItem(R.id.nav_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Buscar Usuario"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.i("SUBMIT", p0.toString())
                if (p0 != null && !p0.isDigitsOnly()) {
                    Toast.makeText(
                        context,
                        "El ID de usuario debe ser numérico",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.searchUser(p0!!.toInt())
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}