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
import com.pro.venta.model.Course
import kotlinx.android.synthetic.main.course_item.view.*

class CourseAdapter(contexto: Context, capitulos: ArrayList<Course>, clickListener: (Course,Int) -> Unit, clickListenerDown: (Course, Int) -> Unit) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    var context = contexto
    var chapters = capitulos
    var courseClickListener = clickListener
    var courseClickListenerDown = clickListenerDown
    var row_index = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.course_item, parent, false))
        return view
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {

        holder.textDescriptionCapitulo.text = chapters[position].description
        holder.textNameCapitulo.text = chapters[position].nameTemp
        val duration = chapters[position].duration
        holder.textViewDuracion.text = "Duración: $duration min"

        /*holder.clickItem.setOnClickListener {
            row_index=position
            Log.w("index","$row_index")
            notifyDataSetChanged()
        }*/

        if(row_index == position){
            //val color = context.resources.getColor(R.color.verdeoscuro,) //.getColor(R.color.verdeoscuro)
            holder.backItem.setBackgroundColor(Color.parseColor("#2b4818"))
        }else{
            //val color = context.resources.getColor(R.color.negroback)
            holder.backItem.setBackgroundColor(Color.parseColor("#FF121311"))
        }

        holder.bind(chapters[position],position, courseClickListener,courseClickListenerDown)

        if(chapters[position].download == 0){
            holder.imageViewDownload.setImageResource(R.drawable.downloadicon)
            holder.descargandoId.alpha = 0.0F
            holder.imageViewDownload.alpha = 1.0F
        }else if(chapters[position].download == 1){
            holder.imageViewDownload.setImageResource(R.drawable.trashicon)
            holder.descargandoId.alpha = 0.0F
            holder.imageViewDownload.alpha = 1.0F
        }else if(chapters[position].download == 2){
            //descargando
            holder.descargandoId.alpha = 1.0F
            holder.imageViewDownload.alpha = 0.0F
        }

        val prefs = context.getSharedPreferences(context.getString(R.string.preferences), android.content.Context.MODE_PRIVATE)

        val sesion = prefs.getString("sesion","null")
        if (sesion!!.equals("null")){
            holder.imageViewDownload.visibility = View.INVISIBLE
        }
    }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textNameCapitulo = itemView.CapitulotextView
        val textDescriptionCapitulo = itemView.DescriptiontextView
        val imageViewDownload = itemView.imageViewDownload
        val textViewDuracion = itemView.textViewDuracion
        val backItem = itemView.backItem
        val clickItem = itemView.clickVideo
        val descargandoId = itemView.descargandoId

        fun bind(part: Course, pos: Int, clickListener: (Course, Int) -> Unit,clickListenerDown: (Course, Int) -> Unit){

            clickItem.setOnClickListener {
                clickListener(part,pos)
            }
            imageViewDownload.setOnClickListener {
                clickListenerDown(part,pos)
            }
        }
    }
}
