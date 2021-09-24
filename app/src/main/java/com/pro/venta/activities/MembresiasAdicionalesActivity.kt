package com.pro.venta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.pro.venta.R
import com.pro.venta.adapter.MembresiasAdapter
import com.pro.venta.model.Membership
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_membresias_adicionales.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject

class MembresiasAdicionalesActivity : AppCompatActivity() {

    lateinit var cuentas : ArrayList<Membership>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membresias_adicionales)

        ReturnButton.setOnClickListener(){
            finish()
        }

        button2.setOnClickListener {
            startActivity(Intent(this, BuyCountsActivity::class.java))
        }

        doAsync {

            val prefs= getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            val datos= prefs.getString("user","")

            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            val us=gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

            val params = CalltaskUser().getCuentas(us.access_token)

            uiThread {

                if (params["response"] as Boolean) {

                    cuentas = ArrayList<Membership>()

                    val data = params["data"] as String
                    val dataTemp = JSONObject(data)["data"]
                    val datosCuentas = JSONArray(dataTemp.toString())

                    for (i in 0 until datosCuentas.length()){
                        val temp = (datosCuentas.getJSONObject(i))
                        val id = temp.getInt("id")
                        val email_send = temp.getString("email_send")
                        val name = temp.getString("name")
                        val status = temp.getString("status")

                        val cuenta = Membership(id,email_send,name,status)
                        cuentas.add(cuenta)
                    }

                    membresiaslista.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                    membresiaslista.adapter = MembresiasAdapter(cuentas)


                }else {
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    //dialog.dismiss()
                }
            }
        }
    }
}
