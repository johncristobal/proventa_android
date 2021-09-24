package com.pro.venta.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_payment.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)


        val objInt: Intent = intent
        val precioF= objInt.getStringExtra("total")
        val idpay= objInt.getStringExtra("idpay")
        precioFinal.text = "Importe: $"+precioF+" MXN"

        closeButton.setOnClickListener(){
            finish()
        }

        PagoTarjeta.setOnClickListener(){

            lanzarPago("1", idpay)
        }

        PagoTiendas.setOnClickListener(){
            lanzarPago("2", idpay)
        }

        PagoTranferencia.setOnClickListener(){
            lanzarPago("3", idpay)
        }
    }

    private fun lanzarPago(method: String, idPay: String) {
        val dialog = indeterminateProgressDialog("Recuperando liga de pago…", "Atención")
        dialog.show()

        //tarea asincrona para llamar servidor
        doAsync {

            val prefs = getSharedPreferences(
                getString(R.string.preferences),
                android.content.Context.MODE_PRIVATE
            )
            val datos = prefs.getString("user", "")

            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            val us =gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

            val params = CalltaskUser().realizaPagoTask("${us.access_token}",method, idPay)
            //UserLoginTask(MainActivity.this, this).execute()
            uiThread {
                dialog.dismiss()

                //cast para convertir Any a Boolean
                if (params["response"] as Boolean){
                    val urlpay = params["url"] as String
                    val urlC=urlpay+"?token="+us.access_token
                    //cast paar pasar a User
                    val webVi = Intent(it, WebActivity::class.java)
                    webVi.putExtra("url", urlC)
                    Log.e("url", urlC)
                    startActivity(webVi)
                    //flagPago = 1

                    //startActivity(
                      //  Intent(
                        //    Intent.ACTION_VIEW,
                          //  Uri.parse(urlpay)
                        //)
                    //)
                    val message= params["msg"] as String
                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                    //checkaPay()

                } else {
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    //textView.text = "Error"
                }
            }
        }
    }
}
