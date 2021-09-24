package com.pro.venta.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_completar_registro.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList

class CompletarRegistroActivity : AppCompatActivity() {

    var message: String = ""
    var idZipcode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completar_registro)

        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        val thisDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        for (i in thisYear downTo 1920) {
            years.add(Integer.toString(i))
        }
        val adapter = ArrayAdapter<String>(this, R.layout.spinneritem, years)

        //val spinYear = findViewById(R.id.yearspin) as Spinner
        spinnerAnioComplete.adapter = adapter

        val dayadapter = ArrayAdapter.createFromResource(
            this, R.array.item_day, R.layout.spinneritem);
        //dayadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerDiaComplete.adapter = dayadapter

        val monthadapter = ArrayAdapter.createFromResource(
            this, R.array.item_month, R.layout.spinneritem);
        //dayadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerMesComplete.adapter = monthadapter

        ContinuarregistroComplete.setOnClickListener {
            val name = NombreCompleteR.text.toString()
            val last_name: String= ApellidosCompleteR.text.toString()
            val mail: String=CorreoCompleteR.text.toString()
            val pass1: String= ContrasCompleteR.text.toString()
            val pass2: String= RepiteContrasCompleteR.text.toString()
            val check: Boolean= terminosOkComplete.isChecked
            val yearuser = spinnerAnioComplete.selectedItem.toString().toInt()
            val monthuser = spinnerMesComplete.selectedItem.toString().toInt()
            val dayuser = spinnerDiaComplete.selectedItem.toString().toInt()

            val yearus = spinnerAnioComplete.selectedItem.toString()
            val monthus = spinnerMesComplete.selectedItem.toString()
            val dayus = spinnerDiaComplete.selectedItem.toString()

            val birth_day = yearus+"-"+monthus+"-"+dayus


            var diffyear = thisYear - yearuser  //2019 - 1992 = 27
            val diffmonth = thisMonth - monthuser  //05 - 05 = 0
            if (diffmonth > 0){
                diffyear += 0
            }else if(diffmonth < 0){
                diffyear -= 1
            }else{
                val diffday = thisDay - dayuser  //30 - 30 = -14
                if (diffday > 0){
                    diffyear += 0
                }else if(diffday < 0){
                    diffyear -= 1
                }else{
                    diffyear += 0
                }
            }

            Log.e("edad","$diffyear")

            val codigopostal = postalTexComplete.text.toString()

            if(validaciones(name, last_name, mail, pass1, pass2, check,codigopostal)) {
                //paso a siguiente fragment
                //val postal = codigopostal.toInt()
                sendData(name, last_name, mail, pass1, pass2, check, birth_day ,codigopostal)
            }else{
                //mando mensaje error
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            }
        }

        closeButton.setOnClickListener{
            finish()
        }

        buttonValidarCPFull.setOnClickListener{
            val zipcode = postalTexComplete.text.toString()
            if (zipcode.equals("")){
                Toast.makeText(this,"Favor de ingresar código postal",Toast.LENGTH_SHORT).show()
            }else{

                buttonValidarCPFull.alpha = 0.0f
                progressBarValidandoFull.alpha = 1.0f

                valideZipCode(zipcode)
            }
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
                    codigoListo(1, id)

                } else {
                    codigoListo(0,"")
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    //textView.text = "Error"
                }
            }
        }
    }

    fun codigoListo(status: Int, idtemp: String){

        when (status){
            1 -> {
                buttonValidarCPFull.alpha = 0.0f
                progressBarValidandoFull.alpha = 0.0f
                imageReadyIconFull.alpha = 1.0f
                idZipcode = idtemp
            }
            else -> {
                buttonValidarCPFull.alpha = 1.0f
                progressBarValidandoFull.alpha = 0.0f
                imageReadyIconFull.alpha = 0.0f
            }
        }
    }

    private fun validaciones(name: String, last_name: String, email: String, pass1: String, pass2: String, check: Boolean,  location: String): Boolean {
        if(name == ""){
            message="El campo nombre está vacío"
            return false
        }

        else if(last_name == ""){
            message="El campo apellido está vacío"
            return false
        }

        else if(email == ""){
            message="El campo correo electrónico está vacío"
            return false
        }


        else if(pass1 == ""){
            message="El campo contraseña está vacío"
            return false
        }

        else if(pass1.length  < 8){
            message="La contraseña debe tener como mínimo 8 carácteres"
            return false
        }

        else if(pass2 == ""){
            message="El campo confirmar contraseña está vacío"
            return false
        }

        else if(pass1 != pass2){
            message="Las contraseñas deben ser idénticas"
            return false
        }

        else if(location == ""){
            message="El campo código postal está vacío"
            return false
        }

        else if(!check){
            message="Debe aceptar los términos y condiciones"
            return false

        }else if(idZipcode.equals("")) {
            message="Favor de validar código postal"
            return false
        }
        return true
    }

    private fun sendData(name: String, last_name: String, email: String, pass1: String, pass2: String, check: Boolean, birth_day: String, location: String){
        //validar datos app
        //dialog para decirle al usuario que su peticion esta en proceso
        val dialog = indeterminateProgressDialog("Enviando información...", "Atención")
        dialog.show()

        //tarea asincrona para llamar servidor
        doAsync {

            val params = CalltaskUser().userRegisterTask(name,last_name,email,pass1,pass2,birth_day ,location)
            //UserLoginTask(MainActivity.this, this).execute()

            uiThread {
                dialog.dismiss()

                //cast para convertir Any a Boolean
                if (params["response"] as Boolean) {

                    //cast paar pasara a User
                    val user = params["user"] as User

                    //gson para transformar User en json (string)
                    val gson = GsonBuilder().create()
                    val stringData = gson.toJson(user)

                    val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                    prefs.edit().putString("sesion", "1").apply()
                    prefs.edit().putString("user", stringData).apply()

                    //viene de codigo, entonces va directo a dashboarx
                    val intent = Intent(applicationContext, DashboardActivity::class.java)
                    startActivity(intent)

                } else {
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    //textView.text = "Error"
                }
            }
        }
    }
}
