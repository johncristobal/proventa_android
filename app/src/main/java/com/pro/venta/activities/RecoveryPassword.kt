package com.pro.venta.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.pro.venta.R
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_recovery_password.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class RecoveryPassword : AppCompatActivity() {

    var message: String= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery_password)

        closeButton.setOnClickListener{
            finish()
        }

        CancelaRecup.setOnClickListener {
            finish()
        }

        EnviarRec.setOnClickListener(){
            val mail=CorreoRec.text.toString()
            if(validaciones(mail)){
                sendData(mail)
            }
            else{
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendData(mail: String){
        if (validaciones(mail)){
            val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
            dialog.show()

            doAsync {
                val params= CalltaskUser().recoveryPassword(mail)

                uiThread {
                    dialog.dismiss()

                    if (params["response"] as Boolean){
                        val msg = params["msg"].toString()
                        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        val msg = params["msg"].toString()
                        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun validaciones( mail: String): Boolean {
        if(mail.equals("")){
            message="Ingresa un correo electrónico"
            return false
        }
        else{
            return true
        }


    }
}
