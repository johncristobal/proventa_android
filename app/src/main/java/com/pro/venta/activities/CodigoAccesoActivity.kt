package com.pro.venta.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_codigo_acceso.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class CodigoAccesoActivity : AppCompatActivity() {

    var message: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codigo_acceso)

        validarcodigoButton.setOnClickListener(){
            //mandaremos el code a un servicio y al regresar pum, next view
            val code = editTextCodigo.text.toString()

            if(validacion(code)){
                sendData(code)
            }
            else{
                Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
            }
        }

        closeButton.setOnClickListener(){
            finish()
        }
    }

         fun sendData(code: String){
            val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
            dialog.show()

            //tarea asincrona para llamar servidor
            doAsync {

                val params = CalltaskUser().userCodeAccessTask(code)
                //UserLoginTask(MainActivity.this, this).execute()

                uiThread {
                    dialog.dismiss()

                    //cast para convertir Any a Boolean
                    if (params["response"] as Boolean) {

                        //si valida codigo, terminamos registro
                        val intent = Intent(applicationContext, CompletarRegistroActivity::class.java)
                        startActivity(intent)

                    } else {
                        val msg = params["msg"].toString()
                        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                        //textView.text = "Error"
                    }
                }
            }
        }


    fun validacion(code:String):Boolean{
        if(code==""){
            message="Introduce un codigo por favor"
            return false
        }
        else
            return true
        //aqui va cuando la validacion cuando el codigo exista, y si no que muestre un msj
    }




}
