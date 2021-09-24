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
import kotlinx.android.synthetic.main.download_item.view.*

class DownloadAdapter(contexto: Context, capitulos: ArrayList<Course>, clickListener: (Course, Int) -> Unit, clickListenerDown: (Course, Int) -> Unit) : RecyclerView.Adapter<DownloadAdapter.CourseViewHolder>() {

    var context = contexto
    var chapters = capitulos
    var courseClickListener = clickListener
    var courseClickListenerDown = clickListenerDown
    var row_index = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.download_item, parent, false))
        return view
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {

        holder.NombreCap.text = chapters[position].nameTemp
        holder.MegasCap.text = chapters[position].size

        /*holder.clickItem.setOnClickListener {
            row_index=position
            Log.w("index","$row_index")
            notifyDataSetChanged()
        }*/

        holder.bind(chapters[position],position, courseClickListener,courseClickListenerDown)

    }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val NombreCap = itemView.NombreCap
        val MegasCap = itemView.MegasCap
        val EliminaVideoButton = itemView.EliminaVideoButton

        fun bind(part: Course, pos: Int, clickListener: (Course, Int) -> Unit,clickListenerDown: (Course, Int) -> Unit){

            NombreCap.setOnClickListener {
                clickListener(part,pos)
            }

            EliminaVideoButton.setOnClickListener {
                clickListenerDown(part,pos)
            }
        }
    }
}
