package com.pro.venta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pro.venta.R
import com.pro.venta.model.Questions
import kotlinx.android.synthetic.main.row_post.view.*

class PostAdapter (val posts: ArrayList<Questions>, clickListenera: (Questions,Int) -> Unit, clickListenerB: (Questions, Int) -> Unit, clickListenerC: (Questions, Int) -> Unit , clickListenerD: (Questions, Int) -> Unit) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(){

    var courseClickListenerA = clickListenera
    var courseClickListenerB = clickListenerB
    var courseClickListenerC = clickListenerC
    var courseClickListenerD = clickListenerD

    //se declaran las variables que se van a utlizar para mostrar los datos en el recyclerView
    class PostViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val pr: TextView = itemview.NumeroPregunta
        val username: TextView = itemview.username
        val op1: RadioButton = itemview.Opcion1
        val op2: RadioButton = itemview.Opcion2
        val op3: RadioButton = itemview.Opcion3
        val op4: RadioButton = itemview.Opcion4

        fun bind(part: Questions, pos: Int, clickListenerA: (Questions, Int) -> Unit,clickListenerB: (Questions, Int) -> Unit,clickListenerC: (Questions, Int) -> Unit,clickListenerD: (Questions, Int) -> Unit){

            op1.setOnClickListener {
                clickListenerA(part,pos)
            }
            op2.setOnClickListener {
                clickListenerB(part,pos)
            }
            op3.setOnClickListener {
                clickListenerC(part,pos)
            }
            op4.setOnClickListener {
                clickListenerD(part,pos)
            }
        }
    }

    //se muestra el elemento con todos su datos en el recyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder{
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_post, parent, false)
        return PostViewHolder (view)
    }

    //obtenemos el tamaño del elemento, en este caso el arraylist
    override fun getItemCount(): Int {
        return posts.size
    }

    //en este metodo se pinta la informacion en los elementos del layout
    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
        val np: Int = position+1
        holder.pr.text = "Pregunta "+np.toString()+":"
        holder.username.text = posts[position].question
        holder.op1.text = posts[position].options[0].opcion
        holder.op2.text = posts[position].options[1].opcion
        holder.op3.text = posts[position].options[2].opcion
        holder.op4.text = posts[position].options[3].opcion

        holder.bind(posts[position],position, courseClickListenerA,courseClickListenerB,courseClickListenerC,courseClickListenerD)

        when (posts[position].selected){
            0 -> {
                holder.op1.isChecked = true
                holder.op2.isChecked = false
                holder.op3.isChecked = false
                holder.op4.isChecked = false
            }
            1 -> {
                holder.op1.isChecked = false
                holder.op2.isChecked = true
                holder.op3.isChecked = false
                holder.op4.isChecked = false
            }
            2 -> {
                holder.op1.isChecked = false
                holder.op2.isChecked = false
                holder.op3.isChecked = true
                holder.op4.isChecked = false
            }
            3 -> {
                holder.op1.isChecked = false
                holder.op2.isChecked = false
                holder.op3.isChecked = false
                holder.op4.isChecked = true
            }
            else -> {

            }
        }
    }

}