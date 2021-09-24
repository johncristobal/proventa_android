package com.pro.venta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_buy_counts.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread


import java.text.DecimalFormat

class BuyCountsActivity : AppCompatActivity() {
    var counts: Int = 1
    var flagPago = 0
    var idPago = ""
    var price = 0.0
    var total = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_counts)

        //Para darle formato al texto y se muestre bien en el activity...
        cuentasNumber.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!cuentasNumber.text.toString().equals("")){
                    val formatter = DecimalFormat("#,###.00")
                    val subtotal = price * cuentasNumber.text.toString().toInt()
                    //val cadena = String.format("%.2f", subtotal)
                    counts = cuentasNumber.text.toString().toInt()
                    val precio= formatter.format(subtotal)
                    total=precio.toString()

                    Log.e("subtotal $", total)
                    Log.e("cuentas numero", counts.toString())
                    //la falla esta en esta linea... ya quedo xD
                    textViewCantidad.text ="$"+total
                }
            }
        })

        gruporadios.setOnCheckedChangeListener { radioGroup, i ->
            val selectedId = gruporadios.checkedRadioButtonId
            when (selectedId){
                R.id.radioButtonIndividual -> {
                    Log.e("radio","individual")
                    //hide label y campo
                    textViewCuantas.visibility = View.INVISIBLE
                    cuentasNumber.visibility = View.INVISIBLE
                    //textView21.visibility = View.INVISIBLE
                    //textViewCantidad.visibility = View.INVISIBLE
                }
                R.id.radioButtonMulti -> {
                    Log.e("radio","multi")
                    //show label y campo
                    textViewCuantas.visibility = View.VISIBLE
                    cuentasNumber.visibility = View.VISIBLE
                    textView21.visibility = View.VISIBLE
                    textViewCantidad.visibility = View.VISIBLE

                }else -> {
                Log.e("radio","else")
            }
            }
        }

        closeButton.setOnClickListener(){
            finish()
        }

        pagoCoinsButton.setOnClickListener(){

            lanzarPago()
        }

        recuperaId()

    }

    private fun recuperaId(){
        doAsync {

            val prefs = getSharedPreferences(
                getString(R.string.preferences),
                android.content.Context.MODE_PRIVATE
            )
            val datos = prefs.getString("user", "")

            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            val us =gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

            val params = CalltaskUser().userPagoGetId("${us.access_token}")
            //UserLoginTask(MainActivity.this, this).execute()

            uiThread {

                //cast para convertir Any a Boolean
                if (params["response"] as Boolean) {

                    //cast paar pasar a User
                    idPago = params["idPago"] as String
                    price = params["price"] as Double
                    val formatter = DecimalFormat("#,###.00")
                    val subtotal = price * 1.0
                    //val cadena = String.format("%.2f", subtotal)
                    val precio= formatter.format(subtotal)
                     total=precio.toString()

                    textViewCantidad.text ="$"+total

                } else {
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    //funcion que me devuelve el apgo por paypal, mada a llamar al servicio y ayuda a que se muestre la forma de pago...
    private fun lanzarPago() {
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

            val params = CalltaskUser().userPagoTask(counts, "${us.access_token}",idPago)
            //UserLoginTask(MainActivity.this, this).execute()

            uiThread {
                dialog.dismiss()

                //cast para convertir Any a Boolean
                if (params["response"] as Boolean) {

                    //cast paar pasar a User
                    val idpay = params["idpay"] as String
                    val intent: Intent = Intent(it, PaymentActivity::class.java)
                    intent.putExtra("total", total)
                    intent.putExtra("idpay", idpay)
                    startActivity(intent)
                    //val webVi = Intent(it, WebActivity::class.java)
                    //webVi.putExtra("url", urlpay)
                    //startActivity(webVi)

                    //flagPago = 1
                    /*val urlpay = params["urlpay"] as String
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(urlpay)
                        )
                    )*/
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

