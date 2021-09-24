package com.pro.venta.activities

/*
**** pago debe regresar a app
* reproducir video en descargados
*
* */

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pro.venta.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_DELAY: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val task = object : TimerTask() {
            override fun run() {

                val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                val sesion = prefs.getString("sesion","null")

                if (sesion!!.equals("1")){
                    val intent = Intent(applicationContext,DashboardActivity::class.java)
                    startActivity(intent)
                }else{
                    //val intent = Intent(applicationContext,TutorialActivity::class.java)
                    //val intent = Intent(applicationContext,LoginActivity::class.java)
                    val intent = Intent(applicationContext,DashboardActivity::class.java)
                    startActivity(intent)
                }

                //cuenta de prueba por eeror en ws
                /*val user = User()
                user.access_token = ""
                user.refresh_token = ""
                user.token_type = ""
                user.expires_in = 0
                user.email = "mail"
                user.name = "prueba"

                val gson = GsonBuilder().create()
                val stringData = gson.toJson(user)

                val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                prefs.edit().putString("sesion","1").apply()

                //Aqui se guarda el usuario con sus datos.
                prefs.edit().putString("user",stringData).apply()
                val intent = Intent(applicationContext,DashboardActivity::class.java)
                startActivity(intent)*/
            }
        }

        val timer = Timer()
        timer.schedule(task, SPLASH_SCREEN_DELAY)
    }
}
