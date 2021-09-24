package com.pro.venta.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.fragment_terminos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TerminosFragment: Fragment(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_terminos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingTerminos.visibility = View.VISIBLE
        terminosView.visibility = View.INVISIBLE
        //val prefs= activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        //val datos= prefs.getString("user","")

        //Creamos una variable gson para volverla a formato objeto.
        //val gson = Gson()
        //val us = gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

        doAsync {

            val params = CalltaskUser().aviso_terminosTask("terminos", "")
            //UserLoginTask(MainActivity.this, this).execute()

            //a partir de aqui se le informa al usuario la respuesta del server...
            uiThread {
                loadingTerminos.visibility = View.INVISIBLE
                terminosView.visibility = View.VISIBLE

                //cast para convertir Any a Boolean
                if (params["response"] as Boolean){

                    //cast para pasara a objeto User
                    val html = params["html"] as String
                    terminosView.settings.javaScriptEnabled = true
                    terminosView.loadData(html,"text/html", "UTF-8")

                }else{
                    val msg = params["msg"].toString()
                    Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
                    //textView.text = "Error"
                }
            }
        }
    }
}