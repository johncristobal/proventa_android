package com.pro.venta.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pro.venta.R
import com.pro.venta.model.User
import kotlinx.android.synthetic.main.activity_membresia.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class MembresiaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membresia)

        val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        val datos= prefs.getString("user","")

        //Creamos una variable gson para volverla a formato objeto.
        val gson = Gson()
        val us = gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

        if (us.account) {
            tvtipoMembresia.text = us.nameAccount
            tvtipoDescr.text = us.accountDescription
            tvstatusType.text = us.statusAccount
            tvDatosFecha.text = us.consume_date
            tvDatosFechaV.text = us.expire_date
        }else{
            tvMembresiamas.visibility = View.VISIBLE
            editarButton.text = "Adquirir membresía"

            tvMembresia.visibility = View.INVISIBLE
            tvtipoMembresia.visibility = View.INVISIBLE
            tvdescription.visibility = View.INVISIBLE
            tvtipoDescr.visibility = View.INVISIBLE
            tvStatus.visibility = View.INVISIBLE
            tvstatusType.visibility = View.INVISIBLE

            tvfecha.visibility = View.INVISIBLE
            tvDatosFecha.visibility = View.INVISIBLE

            tvFechaV.visibility = View.INVISIBLE
            tvDatosFechaV.visibility = View.INVISIBLE
        }
        ReturnButton.setOnClickListener(){
            finish()
        }

        editarButton.setOnClickListener {
            if (us.account) {
                alert("Tu membresía actual todavía no ha vencido.") {
                    yesButton { }
                }.show()
            }else {
                startActivity(Intent(this, BuyCountsActivity::class.java))

            }
        }
    }
}
