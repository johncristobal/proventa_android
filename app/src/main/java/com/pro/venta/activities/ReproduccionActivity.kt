package com.pro.venta.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pro.venta.R
import com.pro.venta.ws.singleton.settings
import kotlinx.android.synthetic.main.activity_reproduccion.*

class ReproduccionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reproduccion)

        val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val datos = prefs.getString("settings","")
        var sett = settings()
        if(datos != "") {
            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            sett = gson.fromJson(datos, settings::class.java) //aqui la cadena se convierte en un objeto.
            switchReproducirVideoAuto.isChecked = sett.autoPlayVideo

        }else{
            sett = settings()
            switchReproducirVideoAuto.isChecked = sett.autoPlayVideo
        }

        ReturnRVButton.setOnClickListener{
            finish()
        }

        switchReproducirVideoAuto.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

                sett.autoPlayVideo = p1

                val gson = GsonBuilder().create()
                val stringData = gson.toJson(sett)

                //Aqui se guarda settings con sus datos.
                prefs.edit().putString("settings",stringData).apply()
            }
        })
    }
}
