package com.example.spataroapp.presentation.order_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spataroapp.data.entities.ApiItemOrder
import com.example.spataroapp.R
import com.example.spataroapp.presentation.config.BaseViewHolder
import kotlinx.android.synthetic.main.item_order.view.*

class RecyclerAdapterOrder(private val context: Context, private val itemClickListener:onItemClickListener):
ListAdapter<ApiItemOrder, RecyclerAdapterOrder.OrderViewHolder>(OrderDiffUtil()){
    interface onItemClickListener{
        fun onItemClick(id:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class OrderViewHolder(itemView: View): BaseViewHolder<ApiItemOrder>(itemView){

        override fun bind(item: ApiItemOrder, position: Int) {
            itemView.reference_id.text = item.referencia
            itemView.color_text.text = item.color
            itemView.size_text.text = item.id_talla
            itemView.price_text.text = item.precio.toString()
            itemView.quatity_id.text = "${item.unidades} Unidad(es)"
            itemView.btn_delete.setOnClickListener {
                itemClickListener.onItemClick(item.id_consecutivo)
            }
        }
    }
}

class OrderDiffUtil: DiffUtil.ItemCallback<ApiItemOrder>(){
    override fun areItemsTheSame(oldItem: ApiItemOrder, newItem: ApiItemOrder): Boolean {
        return oldItem.id_consecutivo == newItem.id_consecutivo
    }

    override fun areContentsTheSame(oldItem: ApiItemOrder, newItem: ApiItemOrder): Boolean {
        return oldItem == newItem
    }

}