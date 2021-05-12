package com.example.spataroapp.presentation.reference_screen

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spataroapp.R
import com.example.spataroapp.databinding.FragmentReferenceBinding
import com.example.spataroapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.infomation.view.*
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class Reference : Fragment() {
    private lateinit var binding: FragmentReferenceBinding
    val viewModel: ReferenceViewModel by viewModels()
    private lateinit var mAlertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reference, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        //call activity up
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.loading, null)
        val mBuilder = AlertDialog.Builder(context).setView(mDialogView).setTitle("")
        mAlertDialog = mBuilder.show()
        mAlertDialog.dismiss()
        //activity
        val act = activity as MainActivity
        act?.allowNavigationUP()
        //update list
        updateList()
        observers()
        listeners()
        return binding.root
    }

    fun observers(){
        //loading observer
        viewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer { value ->
            if(!value){
                mAlertDialog.dismiss()
            }
            else{
                mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mAlertDialog.setCanceledOnTouchOutside(false);
                mAlertDialog.show()
            }
        })

        viewModel.message.observe(viewLifecycleOwner, androidx.lifecycle.Observer { value ->
            if(value!=""){
                val mDialogView = LayoutInflater.from(context).inflate(R.layout.infomation, null)
                val mBuilder = AlertDialog.Builder(context).setView(mDialogView).setTitle("")
                mDialogView.content.text = value
                mDialogView.aceptar.isVisible = true
                mDialogView.cerrar.isVisible = false
                val alertDialog = mBuilder.show()
                mDialogView.aceptar.setOnClickListener {
                    alertDialog.dismiss()
                }
            }
        })
    }

    //update list autocomplete text
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun updateList() {
        val list: MutableList<String> = mutableListOf()
        list.add("UND")
        list.add("MTS")
        val adaptCO = context?.let { ArrayAdapter(it, R.layout.option_item, list.toTypedArray()) }
        if (adaptCO != null && !adaptCO.isEmpty) {
            binding.inputFileType.setText(viewModel.fileType.value, false)
            binding.inputFileType.setAdapter(adaptCO)
        }
    }

    //listeners
    private fun listeners(){
        //select file listener
        binding.selectFile.setOnClickListener {
            openFileChooser()
        }
    }

    //select excel
    private fun openFileChooser() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "*/*"
            val mimeTypes = arrayOf("application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, 100)
        }
    }

    //process the file
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    viewModel.excelFile.value = data?.data
                    var inputStream = data?.data?.let {
                        context?.contentResolver!!.openInputStream(
                            it
                        )
                    }
                    var byteArray = inputStream?.readBytes()
                    if(byteArray!= null) {
                        lifecycleScope.launch {
                            val encodedString: String = Base64.getEncoder().encodeToString(byteArray)
                            viewModel.fileBase64.value = encodedString
                        }
                    }
                }
            }
        }
    }
}