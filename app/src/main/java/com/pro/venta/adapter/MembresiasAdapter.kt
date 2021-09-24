package com.pro.venta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pro.venta.R
import com.pro.venta.model.Membership
import kotlinx.android.synthetic.main.row_membership.view.*
import kotlinx.android.synthetic.main.row_post.view.*

class MembresiasAdapter (val membresias: ArrayList<Membership>) : RecyclerView.Adapter<MembresiasAdapter.MembershipViewHolder>(){

    override fun getItemCount(): Int {
        return membresias.size
    }

    override fun onBindViewHolder(holder: MembresiasAdapter.MembershipViewHolder, position: Int) {
        holder.tipoCuenta.text = membresias[position].name
        holder.estatusCuenta.text = "Estatus: ${membresias[position].status}"
        holder.IDCuenta.text = "ID: ${membresias[position].id.toString()}"
    }

    class MembershipViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val tipoCuenta: TextView = itemview.tipoCuenta
        val estatusCuenta: TextView = itemview.estatusCuenta
        val IDCuenta: TextView = itemview.IDCuenta
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipViewHolder{
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_membership, parent, false)
        return MembershipViewHolder (view)
    }
}