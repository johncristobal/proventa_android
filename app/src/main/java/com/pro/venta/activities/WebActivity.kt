package com.pro.venta.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.gson.Gson
import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_web.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class WebActivity : AppCompatActivity() {

    private lateinit var runnable:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val intentUrl = intent.getStringExtra("url")

        webcontainer.webViewClient = WebViewClient()
        webcontainer.settings.setSupportZoom(true)
        webcontainer.settings.javaScriptEnabled = true
        //val url = "https://github.github.com/training-kit/downloads/github-git-cheat-sheet.pdf"
        //webcontainer.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=$intentUrl")
        webcontainer.loadUrl(intentUrl)

        back.setOnClickListener {
            finish()
        }

//        checkaPay()

        ListoA.setOnClickListener {

            val alert = AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
            alert.setMessage("¿Seguro que ya realizó el pago?")
            alert.setPositiveButton("SI", DialogInterface.OnClickListener{ dialog,
                                                                           id ->  startActivity(Intent(this, GraciasCompraActivity::class.java))})//Toast.makeText(activity!!, "funciona", Toast.LENGTH_SHORT).show()})

            alert.setNegativeButton("NO", DialogInterface.OnClickListener{ dialog,
                                                                           id -> dialog.dismiss()})//Toast.makeText(activity!!, "no funciona", Toast.LENGTH_SHORT).show()})

            val al= alert.create()
            al.setTitle("Realización de pago")
            al.show()

        }
    }

    fun checkaPay(){

        val handler = Handler()

        runnable = Runnable {

            //consumir ws user get y checar usuario
            doAsync {

                val prefs= getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
                val datos= prefs.getString("user","")

                //Creamos una variable gson para volverla a formato objeto.
                val gson = Gson()
                val us = gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

                val params = CalltaskUser().userPagoCheckTask("${us.token_type} ${us.access_token}")
                //UserLoginTask(MainActivity.this, this).execute()

                uiThread {

                    //cast para convertir Any a Boolean
                    if (params["response"] as Boolean) {

                        //cast paar pasar a User
                        /*
                        val webVi = Intent(activity!!,WebActivity::class.java)
                        webVi.putExtra("url","http://xatsaautopartes.xyz/moodle/images/ready.pdf")
                        activity!!.startActivity(webVi)
                        */

                        val userType = params["userType"] as String
                        if(userType.equals("Premium")){

                            prefs.edit().putString("pago","1").apply()

                            handler.removeCallbacks(runnable)
                        }

                    } else {
                        //val msg = params["msg"].toString()
                        //Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                        //textView.text = "Error"
                    }
                }
            }

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}
