package com.example.spataroapp.presentation.order_screen

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.spataroapp.R
import com.example.spataroapp.databinding.FragmentOrderBinding
import com.example.spataroapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class Order : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private val viewModel: OrderViewModel by viewModels()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        //activity back stack
        val act = activity as MainActivity
        act?.allowNavigationUP()
        //listeners
        observers()
        listeners()

        return binding.root
    }

    //update list autocomplete text
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun updateList(list: MutableList<String>) {
        val adaptCO = context?.let { ArrayAdapter(it, R.layout.option_item, list.toTypedArray()) }
        if (adaptCO != null && !adaptCO.isEmpty) {
            binding.inputClientId.setText(viewModel.clientID.value, true)
            binding.inputClientId.setAdapter(adaptCO)
        }
    }

    //observers
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun observers(){
        //list
        viewModel.clientList.observe(viewLifecycleOwner, Observer{value ->
            updateList(value)
        })
    }

    //listeners
    fun listeners() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //select date
        binding.selectDate.setOnClickListener{
            val dpd =
                context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                            val mes = mMonth + 1
                            var m = "" + mes
                            var d = "" + mDay
                            if (m.length == 1) {
                                m = "0" + m
                            }
                            if (d.length == 1) {
                                d = "0" + d
                            }
                            binding.selectDate.text = "" + mYear + "-" + m + "-" + d
                        },
                        year,
                        month,
                        day
                    )
                }
            if (dpd != null) {
                dpd.show()
            }
        }

        //clientID listener
        binding.inputClientId.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i("BEFORE", "")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString()!= ""){
                    val arr = p0.toString().split(".")
                    if(arr.isNotEmpty()){
                        viewModel.docType.value = viewModel.clients.value?.get(arr[0].toInt())?.id_tipo_doc
                        viewModel.docNum.value = viewModel.clients.value?.get(arr[0].toInt())?.id_cliente.toString()
                        viewModel.name.value = viewModel.clients.value?.get(arr[0].toInt())?.nombre
                        viewModel.email.value = viewModel.clients.value?.get(arr[0].toInt())?.correo
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i("AFTER", "")
            }

        })
    }

    //menu options
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.users_menu, menu)
        val search = menu?.findItem(R.id.nav_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Buscar Cliente"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    viewModel.searchClientByName(p0.toString())
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