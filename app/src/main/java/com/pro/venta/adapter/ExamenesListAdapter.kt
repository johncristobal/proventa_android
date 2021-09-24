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
import kotlinx.android.synthetic.main.row_examlist.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ExamenesListAdapter (val examenes: ArrayList<ExamenesD>) : RecyclerView.Adapter<ExamenesListAdapter.ExamenesViewHolder>(){

    override fun getItemCount(): Int {
        return examenes.size
    }

    override fun onBindViewHolder(holder: ExamenesListAdapter.ExamenesViewHolder, position: Int) {
        holder.tipoExamen.text = examenes[position].name
        if (examenes[position].score.toInt() < 14){
            holder.tipoExamen.setTextColor(Color.parseColor("#E52B50"))
        }else{
            holder.tipoExamen.setTextColor(Color.parseColor("#4D881E"))
        }
        holder.estatusExamen.text = "Aciertos: ${examenes[position].score}"

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        try {
            val d = sdf.parse(examenes[position].created_at)
            sdf.applyPattern("dd/MM/yyyy");
            holder.FechaExamen.text = "Fecha: ${sdf.format(d)}"
        } catch (ex: ParseException) {
            Log.v("Exception", ex.getLocalizedMessage())
            holder.FechaExamen.text = "Fecha: ${examenes[position].created_at}"
        }

        holder.IDExamen.text = "ID: ${examenes[position].id}"
    }

    class ExamenesViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val tipoExamen: TextView = itemview.tipoExamen
        val estatusExamen: TextView = itemview.estatusExamen
        val FechaExamen: TextView = itemview.FechaExamen
        val IDExamen: TextView = itemview.IDExamen
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamenesViewHolder{
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_examlist, parent, false)
        return ExamenesViewHolder (view)
    }
}