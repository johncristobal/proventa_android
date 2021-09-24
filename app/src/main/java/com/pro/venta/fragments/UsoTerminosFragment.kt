package com.pro.venta.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson

import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.fragment_uso_terminos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 */
class UsoTerminosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uso_terminos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingAvisoUso.visibility = View.VISIBLE
        avisoViewUso.visibility = View.INVISIBLE
        val prefs= activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val datos= prefs.getString("user","")

        //Creamos una variable gson para volverla a formato objeto.
        val gson = Gson()
        val us = gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

        doAsync {

            val params = CalltaskUser().aviso_terminosTask("uso", us.access_token)
            //UserLoginTask(MainActivity.this, this).execute()

            //a partir de aqui se le informa al usuario la respuesta del server...
            uiThread {
                loadingAvisoUso.visibility = View.INVISIBLE
                avisoViewUso.visibility = View.VISIBLE

                //cast para convertir Any a Boolean
                if (params["response"] as Boolean){

                    //cast para pasara a objeto User
                    val html = params["html"] as String
                    avisoViewUso.settings.javaScriptEnabled = true
                    avisoViewUso.loadData(html,"text/html", "UTF-8")

                }else{
                    val msg = params["msg"].toString()
                    Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
                    //textView.text = "Error"
                }
            }
        }
    }


}
