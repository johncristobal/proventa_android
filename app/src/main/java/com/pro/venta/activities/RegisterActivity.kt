package com.pro.venta.activities


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pro.venta.Data.UserDBHelper
import com.pro.venta.R
import com.pro.venta.fragments.RegisterPartAFragment
import com.pro.venta.fragments.RegisterPartBFragment
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class RegisterActivity : AppCompatActivity(), RegisterPartAFragment.RegisterAListener, RegisterPartBFragment.RegisterBListener {

    lateinit var  UserDBHelper : UserDBHelper

    var name: String = ""
    var message: String = ""
    var last_name: String = ""
    var email: String = ""
    var password: String = ""
    var password_confirmation: String = ""
    var check: Boolean = false
    var birth_day: String = ""
    var location: String = ""
    var counts: Int = 0

    var flagPago = 0

    var registerA: RegisterPartAFragment = RegisterPartAFragment.newInstance("","")
    var registerB: RegisterPartBFragment = RegisterPartBFragment.newInstance("","")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        UserDBHelper = UserDBHelper(this)

        supportFragmentManager.beginTransaction().add(R.id.containerRegister,registerA).commit()
    }

    override fun sendDataApart(data: Any) {

        val datos = data as HashMap<*, *>
        if(datos["response"] == "0"){
            finish()
        }else{
            name = datos["name"] as String
            last_name = datos["last_name"] as String
            email = datos["email"] as String
            password = datos["password"] as String
            password_confirmation = datos["password_confirmation"] as String
            check = datos["check"] as Boolean

            //us.id="01PR"
            /*
            us.name = datos["name"] as String
            us.last_name= datos["last_name"] as String
            us.mail = datos["email"] as String
            us.password = datos["password"] as String
            */

            if(validacionesA(name,last_name,email,password,password_confirmation, check)) {
                //paso a siguiente fragment
                supportFragmentManager.beginTransaction().replace(R.id.containerRegister, registerB).commit()
            }else{
                //mando mensaje error
                Toast.makeText(this,message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun sendDataBpart(uri: Any) {
        val datos = uri as HashMap<*, *>
        if(datos["response"] == "0"){
            finish()
        }else if (datos["response"] == "1"){
            Log.w("finish","parte complete")
            birth_day = datos["date_birth"] as String
            location = datos["cp"] as String
            counts = datos["counts"] as Int


            /*
            us.age= datos["age"] as Int
            us.location = datos["location"] as Int
            */

            if(validacionesB(location)) {
                //paso a siguiente fragment
                val user = User()
                user.name = name
                user.last_name = last_name
                user.email = email
                user.password = password
                user.birth_day = birth_day
                user.location = location.toString()

                addUser(user)
                Log.e("si llega hasta aqui", location.toString())
                sendData()

            }else{
                //mando mensaje error
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                Log.e("Codigo postal",location.toString())

            }
        }
        /*
        else if (datos["response"] == "2") {
            val zipcode = datos["zipcode"] as String
            valideZipCode(zipcode)
        }*/
    }

    private fun addUser(user: User){
        val result= UserDBHelper.insertUser(user)

        if (result){
            //Toast.makeText(this,"agregado a la bd", Toast.LENGTH_SHORT).show()
        }
        else{
            //Toast.makeText(this,"nel gallo, no jaló", Toast.LENGTH_SHORT).show()
        }
    }

    private fun valideZipCode(zipcode: String){

        //tarea asincrona para llamar servidor
        doAsync {

            val params = CalltaskUser().userZipcodeTask(zipcode)
            //UserLoginTask(MainActivity.this, this).execute()

            uiThread {

                //cast para convertir Any a Boolean
                if (params["response"] as Boolean) {

                    //cast paar pasara a User
                    val id = params["id"] as String
                    //registerB.codigoListo(1, id)

                //} else {
                   // registerB.codigoListo(0,"")

                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    //textView.text = "Error"
                }
            }
        }
    }

    private fun sendData(){
        //validar datos app
        //dialog para decirle al usuario que su peticion esta en proceso
        val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
        dialog.show()

        //tarea asincrona para llamar servidor
        doAsync {

            val params = CalltaskUser().userRegisterTask(name,last_name,email,password,password_confirmation,birth_day ,location)
            //UserLoginTask(MainActivity.this, this).execute()
            uiThread {

                dialog.dismiss()

                //cast para convertir Any a Boolean
                if (params["response"] as Boolean) {

                    //cast paar pasara a User
                    val user = params["user"] as User
                    addUser(user)

                    //gson para transformar User en json (string)
                    val gson = GsonBuilder().create()
                    //val stringData = gson.toJson(user)

                    //val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                    //prefs.edit().putString("sesion", "1").apply()
                    //prefs.edit().putString("user", stringData).apply()
                    //prefs.edit().putString("findecursos","0").apply()

                    //lanzarPago()
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    Log.e("entra aqui", "ya jala")

                    val intent= Intent(applicationContext, LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent)

                }else {
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    Log.e("entra aqui", location)
                    //textView.text = "Error"
                }
            }
        }

    }

    private fun lanzarPago(){
        val dialog = indeterminateProgressDialog("Recuperando liga de pago…", "Atención")
        dialog.show()

        //tarea asincrona para llamar servidor
        doAsync {

            val prefs= getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            val datos= prefs.getString("user","")

            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            val us = gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

            val params = CalltaskUser().userPagoTask(counts, "${us.token_type} ${us.access_token}","")
            //UserLoginTask(MainActivity.this, this).execute()

            uiThread {
                dialog.dismiss()

                //cast para convertir Any a Boolean
                if (params["response"] as Boolean) {

                    //cast paar pasar a User
                    val urlpay = params["urlpay"] as String
                    val webVi = Intent(it,WebActivity::class.java)
                    webVi.putExtra("url",urlpay)
                    startActivity(webVi)

                    flagPago = 1
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

    private fun validacionesA(name: String, last_name: String, mail: String, pass1: String, pass2: String, check: Boolean): Boolean {

        if(name == ""){
            message="El nombre es obligatorio"
            return false
        }

        else if(last_name == ""){
            message="Los apellidos son obligatorios"
            return false
        }

        else if(mail == ""){
            message="El correo electrónico es obligatorio"
            return false
        }


        else if(pass1 == ""){
            message="La contraseña es obligatoria"
            return false
        }

        else if(pass1.length  < 8){
            message="La contraseña debe contener como mínimo 8 caracteres"
            return false
        }

        else if(pass2 == ""){
            message="La contraseña es obligatoria"
            return false
        }

        else if(pass1 != pass2){
            message="Las contraseñas no coinciden"
            return false
        }

        else if(!check){
            message="Debes aceptar los términos y el aviso de privacidad"
            return false
        }

        return true
    }

    private fun validacionesB(location: String): Boolean {

        if(location == "") {
            message = "Debes ingresar tu código postal"
            return false
        }
        else
            return true
    }

    override fun onResume() {
        super.onResume()

        //val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        //val pago = prefs.getString("pago","-1")

        if (flagPago == 1){
            //prefs.edit().putString("pago","1").apply()
            //regresamos del pago, y ahora si gracias
            //val intent = Intent(applicationContext, DashboardActivity::class.java)
            val intent = Intent(applicationContext, GraciasCompraActivity::class.java)
            startActivity(intent)
        }
    }
}
