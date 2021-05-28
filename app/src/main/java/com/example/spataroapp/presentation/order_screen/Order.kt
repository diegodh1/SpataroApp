package com.example.spataroapp.presentation.order_screen

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AlertDialogLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spataroapp.R
import com.example.spataroapp.data.entities.ApiItemOrder
import com.example.spataroapp.databinding.FragmentOrderBinding
import com.example.spataroapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.infomation.view.*
import kotlinx.android.synthetic.main.order_recycler.view.*
import java.util.*


@AndroidEntryPoint
class Order : Fragment(), RecyclerAdapterOrder.onItemClickListener {

    private lateinit var binding: FragmentOrderBinding
    private val viewModel: OrderViewModel by viewModels()
    private lateinit var mDialogView:View
    private lateinit var mBuilder:AlertDialog.Builder
    private lateinit var mAlertDialog: AlertDialog
    private lateinit var adapter:RecyclerAdapterOrder
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
        //initialize
        mDialogView = LayoutInflater.from(context).inflate(R.layout.order_recycler, null)
        mBuilder = AlertDialog.Builder(context).setView(mDialogView)
        mDialogView.recyclerView.adapter = context?.let { RecyclerAdapterOrder(it, this) }
        adapter = mDialogView.recyclerView.adapter as RecyclerAdapterOrder
        mDialogView.recyclerView.recyclerView.layoutManager = LinearLayoutManager(context)
        mDialogView.recyclerView.recyclerView.addItemDecoration(
            DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL)
        )
        mAlertDialog = mBuilder.show()
        mAlertDialog.window?.setLayout(resources.displayMetrics.widthPixels,resources.displayMetrics.heightPixels)
        mAlertDialog.dismiss()
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
    //update list references
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun updateListReferences(list: MutableList<String>) {
        val adaptCO = context?.let { ArrayAdapter(it, R.layout.option_item, list.toTypedArray()) }
        if (adaptCO != null && !adaptCO.isEmpty) {
            binding.inputRef.setText(viewModel.referenceID.value, true)
            binding.inputRef.setAdapter(adaptCO)
        }
    }

    //update list colors
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun updateListColor(list: MutableList<String>) {
        val adaptCO = context?.let { ArrayAdapter(it, R.layout.option_item, list.toTypedArray()) }
        if (adaptCO != null && !adaptCO.isEmpty) {
            binding.inputColor.setText(viewModel.colorID.value, true)
            binding.inputColor.setAdapter(adaptCO)
        }
    }

    //update list colors
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun updateListSize(list: MutableList<String>) {
        val adaptCO = context?.let { ArrayAdapter(it, R.layout.option_item, list.toTypedArray()) }
        if (adaptCO != null && !adaptCO.isEmpty) {
            binding.inputSize.setText(viewModel.tallaID.value, true)
            binding.inputSize.setAdapter(adaptCO)
        }
    }
    //show list
    fun showList(list: MutableList<ApiItemOrder>){
        mAlertDialog.show()
        //close
        mDialogView.btn_close.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    //observers
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun observers(){
        //list
        viewModel.clientList.observe(viewLifecycleOwner, Observer{value ->
            updateList(value)
        })
        //message observer
        viewModel.message.observe(viewLifecycleOwner, Observer { value ->
            if(value != "") {
                val mDialogView = LayoutInflater.from(context).inflate(R.layout.infomation, null)
                val mBuilder = AlertDialog.Builder(context).setView(mDialogView).setTitle("")
                val alertDialog = mBuilder.show()
                mDialogView.content.text = value
                when{
                    value == "Registro Realizado"->{
                        mDialogView.cerrar.isVisible = false
                    }
                    value == "Nueva Referencia Agregada" -> {
                        mDialogView.cerrar.isVisible = false
                        viewModel.getItemsOrder(viewModel.orderID.value!!)
                    }
                }
                mDialogView.aceptar.setOnClickListener {
                    alertDialog.dismiss()
                }
                mDialogView.cerrar.setOnClickListener {
                    alertDialog.dismiss()
                }
            }
        })
        //references observer
        viewModel.references.observe(viewLifecycleOwner, Observer {value ->
            if(value.isNotEmpty()){
                updateListReferences(value)
            }
        })

        viewModel.referenceID.observe(viewLifecycleOwner, Observer{value ->
            if(value != ""){
                viewModel.searchColorByReference(value)
            }
        })

        viewModel.colorsAux.observe(viewLifecycleOwner, Observer{value ->
            if(value.isNotEmpty()){
                updateListColor(value)
            }
        })


        viewModel.tallasAux.observe(viewLifecycleOwner, Observer{value ->
            if(value.isNotEmpty()){
                updateListSize(value)
            }
        })

        viewModel.colorID.observe(viewLifecycleOwner, Observer{value ->
            if(value != ""){
                val temp = value.split(".")
                val pos = temp[0].toInt()
                viewModel.colors.value?.get(pos)?.let { viewModel.searchSizeByColorReference(it?.value) }
            }
        })

        viewModel.items.observe(viewLifecycleOwner, Observer{value ->
            if(adapter!=null) {
                adapter.submitList(value.toMutableList())
            }
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
                            viewModel.date.value = "$mYear-$m-$d"
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

        //on change references input listener
        binding.inputSearchRef.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i("BEFORE", "")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchReferences(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i("AFTER", "")
            }

        })

        //show items
        binding.showItems.setOnClickListener {
            showList(viewModel.items.value!!)
        }

        binding.makeSignature.setOnClickListener{
            val navController = this.findNavController()
            val action = OrderDirections.actionOrderToCanvas(viewModel.orderID.value!!)
            navController?.navigate(action)
        }
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

    override fun onItemClick(id: Int) {
        viewModel.deleteItemOrder(id)
    }
}