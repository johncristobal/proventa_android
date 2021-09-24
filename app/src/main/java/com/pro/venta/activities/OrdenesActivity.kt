package com.pro.venta.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.pro.venta.R
import com.pro.venta.adapter.OrdenesAdapter
import com.pro.venta.model.Ordenes
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_ordenes.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject

class OrdenesActivity : AppCompatActivity() {

    lateinit var ordenes : ArrayList<Ordenes>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordenes)

        ReturnButton.setOnClickListener(){
            finish()
        }

        doAsync {

            val prefs= getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            val datos= prefs.getString("user","")

            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            val us=gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

            val params = CalltaskUser().getOrdenes(us.access_token)

            uiThread {

                if (params["response"] as Boolean) {

                    ordenes = ArrayList<Ordenes>()

                    val data = params["data"] as String
                    val dataTemp = JSONObject(data)["data"]
                    val datosCuentas = JSONArray(dataTemp.toString())

                    for (i in 0 until datosCuentas.length()){
                        val temp = (datosCuentas.getJSONObject(i))
                        val id = temp.getInt("id")
                        val description = temp.getString("description")
                        val created_at = temp.getString("created_at")
                        val status = temp.getString("status")

                        val cuenta = Ordenes(id,created_at,description,status)
                        ordenes.add(cuenta)
                    }

                    ordenesList.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                    ordenesList.adapter = OrdenesAdapter(ordenes)


                }else {
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    //dialog.dismiss()
                }
            }
        }
    }
}
