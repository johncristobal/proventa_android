package com.pro.venta.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pro.venta.R
import com.pro.venta.model.ExamenesD
import com.pro.venta.model.Ordenes
import kotlinx.android.synthetic.main.row_examlist.view.*
import kotlinx.android.synthetic.main.row_ordenes.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrdenesAdapter (val ordenes: ArrayList<Ordenes>) : RecyclerView.Adapter<OrdenesAdapter.ExamenesViewHolder>(){

    override fun getItemCount(): Int {
        return ordenes.size
    }

    override fun onBindViewHolder(holder: OrdenesAdapter.ExamenesViewHolder, position: Int) {
        holder.tipoOrden.text = ordenes[position].description
        holder.estatusOrden.text = "Aciertos: ${ordenes[position].status}"

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        try {
            val d = sdf.parse(ordenes[position].created_at)
            sdf.applyPattern("dd/MM/yyyy");
            holder.FechaOrden.text = "Fecha: ${sdf.format(d)}"
        } catch (ex: ParseException) {
            Log.v("Exception", ex.getLocalizedMessage())
            holder.FechaOrden.text = "Fecha: ${ordenes[position].created_at}"
        }

        holder.IDOrden.text = "ID: ${ordenes[position].id}"
    }

    class ExamenesViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val tipoOrden: TextView = itemview.tipoOrden
        val estatusOrden: TextView = itemview.estatusOrden
        val FechaOrden: TextView = itemview.FechaOrden
        val IDOrden: TextView = itemview.IDOrden
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamenesViewHolder{
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_ordenes, parent, false)
        return ExamenesViewHolder (view)
    }
}