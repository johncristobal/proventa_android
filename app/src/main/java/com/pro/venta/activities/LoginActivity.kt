package com.pro.venta.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Environment
import android.text.format.Time
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.pro.venta.model.Course
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    var message: String = ""
    var mCurrentPhotoPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Tomamos los valores de los componentes y los convertimos a cadena para poder utilizarlos
        //Usamos val y el id del componente para manipularlos
        button.setOnClickListener{

           val user = nombre_usuario.text.toString()
           val password = contraseña.text.toString()

            if(validaciones(user, password)){
                sendData(user,password)
            }
            else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        //Manda a activity de recuperar contraseña
        contraseniaButton.setOnClickListener{
            val intent= Intent(this, RecoveryPassword::class.java)
            startActivity(intent)
        }

        //usamos el id del boton y el metodo setonclicklistener para accionarlo
        registro.setOnClickListener{
           val intent= Intent(this, RegisterActivity::class.java)
           startActivity(intent)
        }

        buttonCodigo.setOnClickListener{
            val intent= Intent(this, CodigoAccesoActivity::class.java)
            startActivity(intent)
        }

        closeButton.setOnClickListener{
            val intent = Intent(applicationContext, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    fun validaciones(user: String, pass: String): Boolean{
        if(user.equals("")){
            message="El campo correo electrónico está vacio"
            return false
        }
        else if(pass.equals("")){
            message="El campo contraseña está vacio"
            return false
        }

        else{
            return true
        }
    }

    fun sendData(mail: String, pass: String) {

        //dialog para decirle al usuario que su peticion esta en proceso
        val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
        dialog.show()
        val context = this

        var userF = User()

        //tarea asincrona para llamar servidor se hace por atras de la aplicacion..
        doAsync {

            val params = CalltaskUser().userLoginTask(mail, pass)
            //UserLoginTask(MainActivity.this, this).execute()
            if (params["response"] as Boolean) {

                //cast para pasara a objeto User
                val user = params["user"] as User

                val paramsData = CalltaskUser().userGetDataTask(user)
                if (paramsData["response"] as Boolean) {

                    val userT = paramsData["user"] as User
                    user.name = userT.name
                    user.last_name = userT.last_name
                    user.birth_day = userT.birth_day
                    user.image_path = userT.image_path
                    user.progress = userT.progress

                    user.account = userT.account
                    user.location = userT.location
                    user.course_id = userT.course_id
                    user.episode_id = userT.episode_id
                    user.time = userT.time

                    user.accountDescription = userT.accountDescription
                    user.consume_date = userT.consume_date
                    user.expire_date = userT.expire_date
                    user.nameAccount = userT.nameAccount
                    user.statusAccount = userT.statusAccount

                    //get videos from ws
                    val paramsVideos = CalltaskUser().userGetVideos(user)
                    user.capitulos = paramsVideos["capitulos"] as ArrayList<Course>

                    userF = user

                } else {
                    params["response"] = false
                }
            }

            //a partir de aqui se le informa al usuario la respuesta del server...
            uiThread {
                if (params["response"] as Boolean) {

                    val pathimage = userF.image_path

                    if (!pathimage.equals("")) {
                        if (pathimage.contains("http")) {
                            //guardamos imagen url en local y actializaos path image de user
                            Glide.with(context)
                                .asBitmap()
                                .load(pathimage)
                                .into(object : SimpleTarget<Bitmap>(){
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        //guardamos imagen en local para mejor uso
                                        val time = Time()
                                        time.setToNow()
                                        val nametine = java.lang.Long.toString(time.toMillis(false))

                                        //salvar de galeria a appfolder
                                        val photoFile = createImageFile(nametine)
                                        try {

                                            val out = FileOutputStream(photoFile)
                                            resource.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance

                                            val prefs= getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
                                            val filePath = prefs.getString("nombrefotofrontal","null") //mCurrentPhotoPath
                                            userF.image_path = filePath!!

                                            dialog.dismiss()
                                            saveUserGson(userF)

                                        } catch (e: IOException) {
                                            e.printStackTrace()
                                            dialog.dismiss()
                                            userF.image_path = imageTemp()
                                            saveUserGson(userF)
                                        }
                                    }
                                    override fun onLoadCleared(placeholder: Drawable?) {
                                        // this is called when imageView is cleared on lifecycle call or for
                                        // some other reason.
                                        // if you are referencing the bitmap somewhere else too other than this imageView
                                        // clear it here as you can no longer have the bitmap
                                    }

                                    override fun onLoadFailed(errorDrawable: Drawable?) {
                                        super.onLoadFailed(errorDrawable)

                                        Log.w("error","no cargo imagen")
                                        userF.image_path = imageTemp()
                                        dialog.dismiss()
                                        saveUserGson(userF)
                                    }
                                })
                        } else {
                            Log.w("error","no cargo imagen")
                            userF.image_path = imageTemp()
                            dialog.dismiss()
                            saveUserGson(userF)
                        }
                    }else {
                        Log.w("error","no cargo imagen")
                        userF.image_path = imageTemp()
                        dialog.dismiss()
                        saveUserGson(userF)
                    }
                } else {
                    val msg = params["msg"].toString()
                    Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    //textView.text = "Error"
                }
            }
        }
    }

    fun imageTemp() : String{
        val time = Time()
        time.setToNow()
        val nametine = java.lang.Long.toString(time.toMillis(false))

        //salvar de galeria a appfolder
        val photoFile = createImageFile(nametine)
        try {

            val resource = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_user_png);

            val out = FileOutputStream(photoFile)
            resource.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance

            val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            val filePath = prefs.getString("nombrefotofrontal", "null") //mCurrentPhotoPath
            return filePath!!

        }catch(e:java.lang.Exception){
            return "null"
        }
    }

    fun saveUserGson(user: User){
        //gson para transformar User en json(string)
        val gson = GsonBuilder().create()
        val stringData = gson.toJson(user)

        val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        prefs.edit().putString("sesion", "1").apply()

        //Aqui se guarda el usuario con sus datos.
        prefs.edit().putString("user", stringData).apply()

        val intent = Intent(applicationContext, DashboardActivity::class.java)
        startActivity(intent)
    }

    @Throws(IOException::class)
    private fun createImageFile(username: String): File? {
        // Create an image file name
        try {
            val state = Environment.getExternalStorageState()
            val image: File
            if (Environment.MEDIA_MOUNTED == state){
                image = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)  , "/$username.png")
            } else {
                image = File(filesDir, "pictures" + File.separator + username + ".png")
            }

            if (image.exists()) {
                image.delete()
            }

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.absolutePath
            val prefs= getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            prefs.edit().putString("nombrefotofrontal", mCurrentPhotoPath).apply()

            return image
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}

/*  -Mover un poco el primer textview de detalles del video...
*   -Buscar la manera de hacer que la foto se vea bien tanto en perfil como en curso, ya que en curso se hace un poco mas grande...
*   -Falta en editar perfil el boton de adquirir mas cuentas...
*   cuando poner el numero de cuentas a adquirir en completar registro, se truena la app...
*   Corregir los datos de perfil, separar nombre de apellidos porque no se muestra, al igual que la empresa, ponerla en el registro
*   o quitarla del modelo ya que cuando te registras no te la piden...*/