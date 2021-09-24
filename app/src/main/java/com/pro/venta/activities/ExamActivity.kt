package com.pro.venta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.gson.Gson
import com.pro.venta.R
import com.pro.venta.adapter.PostAdapter
import com.pro.venta.model.Questions
import com.pro.venta.model.User
import com.pro.venta.ws.task.CallTaskExam
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_exam.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class ExamActivity : AppCompatActivity() {

    var preguntas: ArrayList<Questions> = ArrayList()
    var respuestas: HashMap<String,String> = HashMap<String,String>()
    var position: Int = 0
    var postAdapter : PostAdapter? = null
    var idExam = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_exam)

        ReturnButton.setOnClickListener(){
            finish()
        }

        previousButton.setOnClickListener(){

            if(position==0){
                recyclerView.scrollToPosition(0)
            }
            else{
                position--
                recyclerView.scrollToPosition(position)
                numeroP.text =(position+1).toString()+" de 20"
            }

            Log.w("valor de position", position.toString())
        }

        nextButton.setOnClickListener(){

            if(position==preguntas.size-1){
                recyclerView.scrollToPosition( preguntas.size)
            }
            else{
                position++
                recyclerView.scrollToPosition(position)
                numeroP.text =(position+1).toString()+" de 20"
            }
            Log.w("valor de position", position.toString())
            //recyclerView.scrollToPosition(1)
        }

        SendButton.setOnClickListener{
            //send examen
            val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            val datos = prefs.getString("user","")
            //Creamos una variable gson para volverla a formato objeto.
            val gson1 = Gson()
            val us = gson1.fromJson(datos, User::class.java)
            val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
            dialog.show()
            doAsync {
                val params = CallTaskExam().userSendExamTask(us.access_token, idExam, respuestas) //recuperamos el token del usuario

                uiThread {
                    dialog.dismiss()//Eliminamos el dialogo

                    if(params ["response"] as Boolean){

                        val msg = params["msg"].toString()
                        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        val msg = params["msg"].toString()
                        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        muestraPreguntas()
    }

    fun muestraPreguntas(){
        //dialog para decirle al usuario que su peticion esta en proceso
        val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
        dialog.show()
        val context = this

        val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val datos = prefs.getString("user","")
        //Creamos una variable gson para volverla a formato objeto.
        val gson1 = Gson()
        val us = gson1.fromJson(datos, User::class.java)

        doAsync {
            val params = CallTaskExam().userGetExamTask(us.access_token) //recuperamos el token del usuario

            uiThread {
                dialog.dismiss()//Eliminamos el dialogo

                if(params ["response"] as Boolean){

                    preguntas = params["preg"] as ArrayList<Questions>
                    idExam = (params["idExam"] as String).toInt()

                    for (i in 0 until preguntas.size) {
                        Log.w("pregunta $i:", preguntas.get(i).question)
                        respuestas.put(preguntas.get(i).id.toString(), preguntas.get(i).options[0].id.toString())
                    }

                    recyclerView.layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)

                    postAdapter = PostAdapter(preguntas, { question, pos ->
                        //radio a
                        //respuestas.put(question.id.toString(), preguntas[pos].options[0].id.toString())
                        Log.w("respuesta 0",question.id.toString()+" - " +preguntas[pos].options[0].id.toString())
                        respuestas[question.id.toString()] = preguntas[pos].options[0].id.toString()

                        preguntas[pos].selected = 0
                        postAdapter!!.notifyDataSetChanged()
                    },{ question, pos ->
                        //radio b
                        //respuestas.put(question.id.toString(), preguntas[pos].options[1].id.toString())
                        Log.w("respuesta 1",question.id.toString()+" - " +preguntas[pos].options[1].id.toString())
                        respuestas[question.id.toString()] = preguntas[pos].options[1].id.toString()

                        preguntas[pos].selected = 1
                        postAdapter!!.notifyDataSetChanged()
                    },{ question, pos ->
                        //radio c
                        //respuestas.put(question.id.toString(), preguntas[pos].options[2].id.toString())
                        Log.w("respuesta 2",question.id.toString()+" - " +preguntas[pos].options[2].id.toString())
                        respuestas[question.id.toString()] = preguntas[pos].options[2].id.toString()

                        preguntas[pos].selected = 2
                        postAdapter!!.notifyDataSetChanged()
                    },{ question, pos ->
                        //radio d
                        //respuestas.put(question.id.toString(), preguntas[pos].options[3].id.toString())
                        Log.w("respuesta 3",question.id.toString()+" - " +preguntas[pos].options[3].id.toString())
                        respuestas[question.id.toString()] = preguntas[pos].options[3].id.toString()

                        preguntas[pos].selected = 3
                        postAdapter!!.notifyDataSetChanged()
                    })

                    recyclerView.adapter = postAdapter
                    //mandamos el array que obtenemos del ws para mostrarlo en pantalla
                    //recyclerView.isLayoutFrozen = true
                    recyclerView.setOnTouchListener(object : View.OnTouchListener{
                        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                            return true
                        }
                    })

                    val msg = "mnda preguntas desde ws"//params["msg"].toString()
                    //Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    Log.w("tamaño del arraylist", preguntas.size.toString())
                    Log.w("tamaño del arraylist", preguntas.get(0).options.size.toString())
                }
                else{
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
