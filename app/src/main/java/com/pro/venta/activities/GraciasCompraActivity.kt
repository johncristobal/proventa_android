package com.pro.venta.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_gracias_compra.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class GraciasCompraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gracias_compra)

        closeButton.setOnClickListener(){
            finish()
        }

        iniciarcursoButton.setOnClickListener(){

            //me imagino que aqui mandaremos los correos (esperando servicios)
            val correo1 = email1compra.text.toString()
            val correo2 = email2compra.text.toString()

            sendData(correo1, correo2)

        }
    }

    fun validaciones() : Boolean {
        //return false
        return true
    }

    private fun sendData(mail1: String, mail2:String){
        //validar datos app
        if (validaciones()){
            //dialog para decirle al usuario que su peticion esta en proceso
            val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
            dialog.show()

            //tarea asincrona para llamar servidor
            doAsync {

                val prefs= getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
                val datos= prefs.getString("user","")

                //Creamos una variable gson para volverla a formato objeto.
                val gson = Gson()
                val us = gson.fromJson(datos, User::class.java)

                val token= "${us.token_type}${us.access_token}" //Recuperamos el access token  desde el shared preference
                //val token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjNiM2VmNDViZTM2Y2I1NmRhMDgxMzAzZjFiMjhkMzMwZGRjZDk3NWVkYmI0MWRkNDBjYjRmNDE5YWYzYmNmMzY5ZWYxYWQwYmQ3OTljZmQ1In0.eyJhdWQiOiIxIiwianRpIjoiM2IzZWY0NWJlMzZjYjU2ZGEwODEzMDNmMWIyOGQzMzBkZGNkOTc1ZWRiYjQxZGQ0MGNiNGY0MTlhZjNiY2YzNjllZjFhZDBiZDc5OWNmZDUiLCJpYXQiOjE1Nzg1MjE0OTAsIm5iZiI6MTU3ODUyMTQ5MCwiZXhwIjoxNjEwMTQzODkwLCJzdWIiOiIzNyIsInNjb3BlcyI6WyJ1c2VyIl19.WabvlNsg5Ngiw9NHvuOUKzDLysY0iGm2aMeNTmNicWfmrePRDtQy9v88PTPDi4KhLpgWfSJ129f-_6lvVX867v0ij8Lb2OosoLS2KQ5ghdGxKEEv8LdK6BdTh3hXxH8qX_J9S4rDNO9CMkHli2tqtkazQs8l_sWCXbl3_mxM-xNG2pwzqhKz56kjy4LH-XLMG2OXrtPmQNn4SZlA-XvciajT76q92CvfO-kpevscviyVcYsmWPO9WXOXhF4rChoHKTL1txD0JmQ9b9uv90huF7xeAkaS4eZNgvvVoPotvnOnKE6qst8cLQ529a245k0A_P5l7j8YBxmqqubpgJ0q6AS1FZfYaWAKkz2qVdulMKlYWf5qhUqEXriz_aDrPamT2FRw-qFOoGgdkoVGYvdMDoB0k4ygTw9VG8S9IFczly2hzFdU_ZowKfIIBy0Gn4ZA4PoEAMAHOeAZMeRBu4t5GUpBZ89B-w-u-EzES7pCF47z857PHK3f6eIVqDE8s7BDJlYBjS-_bPn9WKS9H2SGuSVYBVCUMiYvCEwDFoGxAxo4Nss7TOKr-siJ3lxEm2BEffHrzwShU3xZAN01yTtlfV2QQbG5s-Lex_1wYfNmFUS1iK5Gbatv4L-hyc48T-eBPCIPTsTOGEhh9zNUUk8MrTn0FIYDDIUIsgMFUZ3b99k"
                //Recuperamos el access token  desde el shared preference

                val params = CalltaskUser().GraciasCompraTask(mail1, mail2, token)
                //UserLoginTask(MainActivity.this, this).execute()


                uiThread {
                    dialog.dismiss()

                    //val intent = Intent(applicationContext, DashboardActivity::class.java)
                    //startActivity(intent)
                    if(params ["response"] as Boolean){
                        //val msg = params["msg"].toString()
                        //Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, DashboardActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        //val msg = params["msg"].toString()
                        //Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()

                       val intent = Intent(applicationContext, DashboardActivity::class.java)
                       startActivity(intent)
                    }
                }
            }
        }else{
            /*doAsync {
                val token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjNiM2VmNDViZTM2Y2I1NmRhMDgxMzAzZjFiMjhkMzMwZGRjZDk3NWVkYmI0MWRkNDBjYjRmNDE5YWYzYmNmMzY5ZWYxYWQwYmQ3OTljZmQ1In0.eyJhdWQiOiIxIiwianRpIjoiM2IzZWY0NWJlMzZjYjU2ZGEwODEzMDNmMWIyOGQzMzBkZGNkOTc1ZWRiYjQxZGQ0MGNiNGY0MTlhZjNiY2YzNjllZjFhZDBiZDc5OWNmZDUiLCJpYXQiOjE1Nzg1MjE0OTAsIm5iZiI6MTU3ODUyMTQ5MCwiZXhwIjoxNjEwMTQzODkwLCJzdWIiOiIzNyIsInNjb3BlcyI6WyJ1c2VyIl19.WabvlNsg5Ngiw9NHvuOUKzDLysY0iGm2aMeNTmNicWfmrePRDtQy9v88PTPDi4KhLpgWfSJ129f-_6lvVX867v0ij8Lb2OosoLS2KQ5ghdGxKEEv8LdK6BdTh3hXxH8qX_J9S4rDNO9CMkHli2tqtkazQs8l_sWCXbl3_mxM-xNG2pwzqhKz56kjy4LH-XLMG2OXrtPmQNn4SZlA-XvciajT76q92CvfO-kpevscviyVcYsmWPO9WXOXhF4rChoHKTL1txD0JmQ9b9uv90huF7xeAkaS4eZNgvvVoPotvnOnKE6qst8cLQ529a245k0A_P5l7j8YBxmqqubpgJ0q6AS1FZfYaWAKkz2qVdulMKlYWf5qhUqEXriz_aDrPamT2FRw-qFOoGgdkoVGYvdMDoB0k4ygTw9VG8S9IFczly2hzFdU_ZowKfIIBy0Gn4ZA4PoEAMAHOeAZMeRBu4t5GUpBZ89B-w-u-EzES7pCF47z857PHK3f6eIVqDE8s7BDJlYBjS-_bPn9WKS9H2SGuSVYBVCUMiYvCEwDFoGxAxo4Nss7TOKr-siJ3lxEm2BEffHrzwShU3xZAN01yTtlfV2QQbG5s-Lex_1wYfNmFUS1iK5Gbatv4L-hyc48T-eBPCIPTsTOGEhh9zNUUk8MrTn0FIYDDIUIsgMFUZ3b99k"
                //Recuperamos el access token  desde el shared preference

                val params = CalltaskUser().GraciasCompraTask(mail1, mail2, token)

            }*/
        }
    }
}



