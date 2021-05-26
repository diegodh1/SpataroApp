package com.example.spataroapp.presentation.order_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spataroapp.data.entities.ApiItemOrder
import com.example.spataroapp.R
import kotlinx.android.synthetic.main.item_order.view.*

class RecyclerAdapterOrder(private val context: Context, private val itemClickListener:onItemClickListener):
ListAdapter<ApiItemOrder, RecyclerAdapterOrder.OrderViewHolder>(OrderDiffUtil()){
    interface onItemClickListener{
        fun onItemClick(id:Int, quatity:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.referece.text = getItem(position).referencia
        holder.color.text = getItem(position).color
        holder.size.text = getItem(position).id_talla
        holder.price.text = getItem(position).precio.toString()
        holder.quatity.text = "${getItem(position).unidades.toString()} Unidad(es)"
    }

    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var referece: TextView
        var color: TextView
        var size: TextView
        var price: TextView
        var quatity: TextView
        init{
            referece = itemView.reference_id
            color = itemView.color_text
            size = itemView.size_text
            price = itemView.price_text
            quatity = itemView.quatity_id
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