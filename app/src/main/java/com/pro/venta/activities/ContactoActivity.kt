package com.pro.venta.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.pro.venta.R
import com.pro.venta.ws.task.CallTaskContact
import kotlinx.android.synthetic.main.activity_contacto.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class ContactoActivity : AppCompatActivity() {

    var message: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacto)

        ReturnRVButton.setOnClickListener{
            finish()
        }

        EnviaComentario.setOnClickListener(){
            val name= NombreContacto.text.toString()
            val mail= CorreoContacto.text.toString()
            val company= EmpresaContacto.text.toString()
            val coment= ComentarioContacto.text.toString()

            sendData(name, mail, company, coment)
        }

    }

    fun sendData(name: String, mail: String, company: String, commentary:String){

       if(validaciones(name, mail,company,commentary)){
           val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
           dialog.show()

           //tarea asincrona para llamar al server...
           doAsync {
               val params= CallTaskContact().UserContactTask(name, mail, company, commentary)

               uiThread {
                   dialog.dismiss()

                   if(params ["response"] as Boolean){
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
        else{
           Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
       }
    }

    private fun validaciones(name: String, mail: String, company: String, commentary: String): Boolean {
        if(name.equals("")){
            message="El campo nombre está vacío"
            return false
        }
        else if(mail.equals("")){
            message="El campo correo electrónico está vacío"
            return false
        }
        else if(company.equals("")){
            message="El campo empresa está vacío"
            return false
        }
        else if(commentary.equals("")){
            message="Ingresa un comentario para ayudarte..."
            return false
        }
        else{
            return true
        }


    }
}



