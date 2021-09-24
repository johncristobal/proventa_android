package com.pro.venta.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pro.venta.R
import com.pro.venta.ws.singleton.quality
import com.pro.venta.ws.singleton.settings
import kotlinx.android.synthetic.main.activity_opciones_descarga.*
import org.jetbrains.anko.selector

class OpcionesDescargaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opciones_descarga)

        val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val datos = prefs.getString("settings","")
        var sett = settings()
        if(datos != "") {
            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            sett = gson.fromJson(datos, settings::class.java) //aqui la cadena se convierte en un objeto.
            switchWiFi.isChecked = sett.wifiDownload

            when (sett.qualityVideoDownload){
                quality.alta -> {
                    calidadD.text = quality.alta.tipo
                }
                quality.media -> {
                    calidadD.text = quality.media.tipo
                }
                else -> {
                    calidadD.text = quality.baja.tipo
                }
            }

        }else{
            sett = settings()
            switchWiFi.isChecked = sett.wifiDownload
            calidadD.text = quality.alta.tipo
        }

        ReturnODButton.setOnClickListener{
            finish()
        }

        switchWiFi.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

                sett.wifiDownload = p1

                val gson = GsonBuilder().create()
                val stringData = gson.toJson(sett)

                //Aqui se guarda settings con sus datos.
                prefs.edit().putString("settings",stringData).apply()
            }
        })

        calidadD.setOnClickListener {
            //define que calidad es para descargar
            val calidad = listOf("Alta", "Media", "Baja")
            selector("Selecciona calidad de descarga", calidad) {dialogInterface, i ->
                when (i){
                    0 -> {
                        calidadD.text = quality.alta.tipo
                        sett.qualityVideoDownload = quality.alta
                    }
                    1 -> {
                        calidadD.text = quality.media.tipo
                        sett.qualityVideoDownload = quality.media
                    }
                    else -> {
                        calidadD.text = quality.baja.tipo
                        sett.qualityVideoDownload = quality.baja
                    }
                }

                val gson = GsonBuilder().create()
                val stringData = gson.toJson(sett)

                //Aqui se guarda settings con sus datos.
                prefs.edit().putString("settings",stringData).apply()
            }
        }
    }
}
