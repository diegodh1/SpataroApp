package com.example.spataroapp.presentation.admin_client_screen

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.spataroapp.R
import com.example.spataroapp.databinding.FragmentClientBinding
import com.example.spataroapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.infomation.view.*

@AndroidEntryPoint
class Client : Fragment() {

    private lateinit var binding:FragmentClientBinding
    private val viewModel:ClientViewModel by viewModels()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val act = activity as MainActivity
        act?.allowNavigationUP()
        //observers
        observers()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.users_menu, menu)
        val search = menu?.findItem(R.id.nav_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Buscar Cliente"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null && !p0.isDigitsOnly()) {
                    Toast.makeText(
                        context,
                        "El documento del cliente debe ser numérico",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.searchClient(p0!!.toInt())
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    //observers
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun observers() {
        //list documents observer
        viewModel.documents.observe(viewLifecycleOwner, Observer { value ->
            if (value.isNotEmpty()) {
                updateList(value)
            }
        })

        //message observer
        viewModel.message.observe(viewLifecycleOwner, Observer { value ->
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.infomation, null)
            val mBuilder = AlertDialog.Builder(context).setView(mDialogView).setTitle("")
            if(value != ""){
                mDialogView.content.text = value
                val alertDialog = mBuilder.show()
                mDialogView.aceptar.setOnClickListener {
                    alertDialog.dismiss()
                }
                mDialogView.cerrar.setOnClickListener {
                    alertDialog.dismiss()
                }
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
}