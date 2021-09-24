package com.pro.venta.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.format.Time
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide

import com.pro.venta.R
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import kotlinx.android.synthetic.main.fragment_edit_perfil.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var path_image: String = ""


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EditPerfilFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EditPerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EditPerfilFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentEditPerfilListener? = null

    var photoFile: File? = null
    var mCurrentPhotoPath: String? = null
    var message: String = ""

    var adapter: ArrayAdapter<String>? = null
    var dayadapter: ArrayAdapter<CharSequence>? = null
    var monthadapter: ArrayAdapter<CharSequence>? = null

    var flagFoto = false
    var passActual = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )
        }

        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        val thisDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        for (i in thisYear downTo 1920) {
            years.add(Integer.toString(i))
        }
        adapter = ArrayAdapter<String>(activity!!, R.layout.spinneritem, years)

        //val spinYear = findViewById(R.id.yearspin) as Spinner
        spinnerAnioCompleteEdita.adapter = adapter

        dayadapter = ArrayAdapter.createFromResource(
            activity!!, R.array.item_day, R.layout.spinneritem);
        //dayadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerDiaCompleteEdita.adapter = dayadapter

        monthadapter = ArrayAdapter.createFromResource(
            activity!!, R.array.item_month, R.layout.spinneritem);
        //dayadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerMesCompleteEdita.adapter = monthadapter

        muestraDatos()

        FotoPerfilEdit.setOnClickListener{
            Log.e("data user", "click")
            showAlertFoto()
        }

        ReturnButton.setOnClickListener{
            listener?.onFragmentInteractionEditPerfil("1")
        }

        GuardaButton.setOnClickListener{
            val name:String = NombreEdita.text.toString()
            val email:String = CorreoEdita.text.toString()
            val last_name: String = EmpresaEdita.text.toString()
            val pass1: String = Contrasenaedita.text.toString()
            val pass2: String = RepiteContraEdita.text.toString()

            val location: String = postalTexEdita.text.toString()
            val yearus = spinnerAnioCompleteEdita.selectedItem.toString()
            val monthus = spinnerMesCompleteEdita.selectedItem.toString()
            val dayus = spinnerDiaCompleteEdita.selectedItem.toString()

            val birth_day = yearus+"-"+monthus+"-"+dayus
            val actualPass: String = actualPassEdit.text.toString()

            actualizarDatos(name, email, last_name, pass1, pass2, location, actualPass,birth_day)

            //listener?.onFragmentInteractionEditPerfil("1")
        }

        editarButton.setOnClickListener {
            val name:String = NombreEdita.text.toString()
            val email:String = CorreoEdita.text.toString()
            val last_name: String = EmpresaEdita.text.toString()
            val pass1: String = Contrasenaedita.text.toString()
            val pass2: String = RepiteContraEdita.text.toString()
            val location: String = postalTexEdita.text.toString()
            val yearus = spinnerAnioCompleteEdita.selectedItem.toString()
            val monthus = spinnerMesCompleteEdita.selectedItem.toString()
            val dayus = spinnerDiaCompleteEdita.selectedItem.toString()

            val birth_day = yearus+"-"+monthus+"-"+dayus
            val actualPass: String = actualPassEdit.text.toString()

            actualizarDatos(name, email, last_name, pass1, pass2, location, actualPass,birth_day)
            //listener?.onFragmentInteractionEditPerfil("1")
        }
    }

    fun muestraDatos(){
        //Modo de declarar un sharedPreferences en un fragment
        val prefs= activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val datos= prefs.getString("user","")

        //Creamos una variable gson para volverla a formato objeto.
        val gson = Gson()
        val us=gson.fromJson(datos, User::class.java) //aqui la cadena se convierte en un objeto.

        //Se le asignan a las variables los valores cada uno de los atributos del objeto que estamos deserializando
        val nombre:String = us.name
        val last_name:String= us.last_name
        val correo:String =us.email
        val pass:String =us.password
        passActual = pass
        val location:String =us.location
        val birth_day:String =us.birth_day
        var data = birth_day.split("-")

        try {
            val spinnerPosition = adapter!!.getPosition(data[0])
            spinnerAnioCompleteEdita.setSelection(spinnerPosition)

            val spinnerPositionDay = dayadapter!!.getPosition(data[2])
            spinnerDiaCompleteEdita.setSelection(spinnerPositionDay)

            val spinnerPositionMonth = monthadapter!!.getPosition(data[1])
            spinnerMesCompleteEdita.setSelection(spinnerPositionMonth)

        }catch(e: Exception){

        }

        val imgPath = us.image_path

        if (imgPath != ""){

            /*usar la libreria glide para cargar foto*/
            //val bitmap = BitmapFactory.decodeFile(imgPath)
            //FotoPerfil.setImageBitmap(bitmap)

            Glide.with(activity!!)
                .load(imgPath)
                .into(FotoPerfilEdit)
        }

        //Se asignan los valores a los textview para mostrarlos.
        NombreEdita.setText(nombre)
        CorreoEdita.setText(correo)
        EmpresaEdita.setText(last_name)
        //Contrasenaedita.setText(pass)
        //RepiteContraEdita.setText(pass)
        postalTexEdita.setText(location)

        progressBarEdit.progress = us.progress
        tvPorcentaje_progreso.text = "${us.progress}%"
    }

    private fun showAlertFoto(){
        val options = arrayOf<CharSequence>("Tomar Foto", "Elegir de la galería", "Cancelar")

        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Elige tu foto")

        builder.setItems(options) { dialog, item ->
            if (options[item] == "Tomar Foto") {
                openCamera()

            } else if (options[item] == "Elegir de la galería") {
                loadGallery()

            } else if (options[item] == "Cancelar") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun loadGallery(){
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, 3)
    }

    private fun openCamera(){
        val time = Time()
        time.setToNow()
        val nametine = java.lang.Long.toString(time.toMillis(false))

        if (Build.VERSION.SDK_INT < 23) {

            takePicture23(2, nametine)
        } else {

            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
            }else {
                takePicture(2, nametine)
            }

            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    fun takePicture23(p: Int, name: String) {
        val takepic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //startActivityForResult(i, FRONT_VEHICLE);
        if (takepic.resolveActivity(activity!!.packageManager) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile(name)
            } catch (ex: IOException) {
                // Error occurred while creating the File...
                ex.printStackTrace()
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Uri photoURI = FileProvider.getUriForFile(VehiclePictures.this, "miituo.com.miituo.provider", photoFile);
                val photoURI = Uri.fromFile(photoFile)
                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takepic, p)
            } else {
                Toast.makeText(
                    activity!!,
                    "Tuvimos un problema al tomar la imagen. Intente mas tarde.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun takePicture(p: Int, name: String) {
        val takepic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //startActivityForResult(i, FRONT_VEHICLE);
        if (takepic.resolveActivity(activity!!.packageManager) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile(name)
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File...
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity!!, "com.pro.venta.provider", photoFile!!)

                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takepic, p)
            } else {
                Toast.makeText(activity!!, "Tuvimos un problema al tomar la imagen. Intente mas tarde.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(username: String): File? {
        // Create an image file name
        try {
            val state = Environment.getExternalStorageState()
            val image: File
            if (Environment.MEDIA_MOUNTED == state){
                image = File(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)  , "/$username.png")
            } else {
                image = File(activity!!.getFilesDir(), "pictures" + File.separator + username + ".png")
            }

            if (image.exists()) {
                image.delete()
            }

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.absolutePath
            val prefs= activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            prefs.edit().putString("nombrefotofrontal", mCurrentPhotoPath).apply()

            return image
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){

            flagFoto = true
            when (requestCode) {
                2 -> { //camera
                    val prefs= activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
                    val filePath = prefs.getString("nombrefotofrontal","null") //mCurrentPhotoPath

                    val options = BitmapFactory.Options()
                    options.inSampleSize = 8
                    val img = BitmapFactory.decodeFile(filePath,options)
                    Glide.with(activity!!)
                        .load(img)
                        .into(FotoPerfilEdit)


                    /*try {
                        val ei = ExifInterface(filePath)
                        val orientation = ei.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED
                        )
                        //Bitmap selectedImage = decodeFile(filePath);// BitmapFactory.decodeFile(filePath,bmOptions);
                        val bitmap = BitmapFactory.decodeFile(filePath)
                        var rotatedBitmap: Bitmap? = null
                        when (orientation) {

                            ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap =
                                rotateImage(bitmap, 90f)

                            ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap =
                                rotateImage(bitmap, 180f)

                            ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap =
                                rotateImage(bitmap, 270f)

                            ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
                            else -> rotatedBitmap = bitmap
                        }
                        val selectedImage = Bitmap.createScaledBitmap(
                            rotatedBitmap!!,
                            rotatedBitmap.width / 16,
                            rotatedBitmap.height / 16,
                            false
                        )

                        FotoPerfilEdit.setImageBitmap(selectedImage)
                        //mandar acutalizacion de foto a ws...

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }*/
                }

                3 -> {  //gallery
                    val path = data!!.data
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), path)
                    //FotoPerfilEdit.setImageBitmap(bitmap)
                    Glide.with(activity!!)
                        .load(bitmap)
                        .into(FotoPerfilEdit)

                    val time = Time()
                    time.setToNow()
                    val nametine = java.lang.Long.toString(time.toMillis(false))

                    //salvar de galeria a appfolder
                    photoFile = createImageFile(nametine)
                    try {

                        val out = FileOutputStream(photoFile)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    } catch (e: IOException) {
                            e.printStackTrace();
                    }
                }
                else -> {
                    Log.d("data","no entra")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 0){
            if(permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val time = Time()
                time.setToNow()
                val nametine = java.lang.Long.toString(time.toMillis(false))

                takePicture(2, nametine)
            }
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

   private fun actualizarDatos(name:String, mail:String, last_name:String, pass1:String, pass2:String, location: String, actualPassKey: String, birth_day: String){

        if(validaciones(name,mail,last_name,pass1,pass2, location, actualPassKey)) {

            //cast paar pasara a User
            val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            val datos = prefs.getString("user","")
            //Creamos una variable gson para volverla a formato objeto.
            val gson1 = Gson()
            val us = gson1.fromJson(datos, User::class.java)
            //Asignamos los nuevos valores a el objeto para despues serializarlo...

            us.name = name
            us.email = mail
            us.last_name = last_name
            us.location = location
            us.birth_day = birth_day

            if(pass1.equals("")){
                us.password = passActual
            }else{
                us.password = pass1
            }

            val filePath = prefs.getString("nombrefotofrontal","null") //mCurrentPhotoPath
            us.image_path = filePath!!

            //gson para transformar el objeto User en json (string)
            val gson = GsonBuilder().create()
            val stringData = gson.toJson(us)
            prefs.edit().putString("user", stringData).apply()
            listener?.onFragmentInteractionEditPerfil("1")

            val dialog = activity!!.indeterminateProgressDialog("Enviando información…", "Atención")
            dialog.show()

            doAsync {
                val params = CalltaskUser().updateUserTask(name, mail, last_name, pass1, pass2, filePath, " ${us.token_type} ${us.access_token}", passActual, location, birth_day)

                if (flagFoto){
                    //call ws to upload image
                    CalltaskUser().updateUserImageTask(filePath, " ${us.token_type} ${us.access_token}")
                }

                uiThread {
                    dialog.dismiss()

                    if(params ["response"] as Boolean){

                        val msg = params["msg"].toString()
                        Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val msg = params["msg"].toString()
                        Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }//aqui iria el hilo principal para actualizar informacion en el server...

        } else {
            //val msg = "no se pudo..."
            Toast.makeText(activity!!, message, Toast.LENGTH_SHORT).show()
            //textView.text = "Error"
        }
    }


    private fun validaciones(name: String, mail: String, last_name: String, pass1: String, pass2: String, location: String, actualPassKey: String): Boolean {
        if (name.equals("")){
            message="El nombre es obligatorio"
            return false
        }
        else if(mail.equals("")){
            message="El correo electrónico es obligatorio"
            return false
        }
        else if(last_name.equals("")){
            message="Los apellidos son obligatorios"
            return false
        }
        else if(location.equals("")){
            message="Debes ingresar tu código postal"
            return false
        }
        else if(actualPassKey.equals("")){
            message="Tu contraseña actual es obligatoria"
            return false
        }else if(!actualPassKey.equals(passActual)){
            message="Tu contraseña actual no coincide"
            return false
        }
        else if(pass1 != pass2){
            message="Las contraseñas no coinciden"
            return false
        }else if(!pass1.equals("")){
            if(pass1.length <= 7){
                message="La contraseña debe contener como mínimo 8 caracteres"
                return false
            }else{
                return true
            }
        }
        else{
            return true
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteractionEditPerfil("")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentEditPerfilListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentEditPerfilListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentEditPerfilListener {
        // TODO: Update argument type and name
        fun onFragmentInteractionEditPerfil(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditPerfilFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditPerfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


