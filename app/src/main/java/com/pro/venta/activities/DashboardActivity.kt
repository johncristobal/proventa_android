package com.pro.venta.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.pro.venta.R
import kotlinx.android.synthetic.main.fragment_curso.*
import androidx.core.view.OneShotPreDrawListener.add
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pro.venta.fragments.*
import com.pro.venta.model.Course
import com.pro.venta.model.User
import com.pro.venta.ws.task.CalltaskUser
import com.pro.venta.ws.task.CalltaskVideos
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class DashboardActivity : AppCompatActivity(),
    CursoFragment.OnFragmentCursoListener,
    PerfilFragment.OnFragmentPerfilListener,
    AjustesFragment.OnFragmentAjustesListener,
    EditPerfilFragment.OnFragmentEditPerfilListener,
    OpcVideoFragment.OnFragmentInteractionListenerOpcionesVideo,
    ListCapitulosFragment.CapitulosInteractionListener,
    DetallesFragment.OnFragmentDetallesListener
{
    private lateinit var textMessage: TextView

    private lateinit var perfil_fragment: PerfilFragment // = PerfilFragment.newInstance("","")
    private lateinit var curso_fragment: CursoFragment // = CursoFragment.newInstance("","")
    private lateinit var ajustes_fragment: AjustesFragment // = AjustesFragment.newInstance("","")
    private lateinit var editar_perfil_fragment: EditPerfilFragment // = AjustesFragment.newInstance("","")
    var videoOpcioesFrag: OpcVideoFragment = OpcVideoFragment.newInstance("","")
    var listCapitulosFrag: ListCapitulosFragment = ListCapitulosFragment.newInstance("","")
    var detallesCapitulosFrag: DetallesFragment = DetallesFragment.newInstance("","")

    val fm = supportFragmentManager
    lateinit var navView: BottomNavigationView

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.curso -> {
                //textMessage.setText(R.string.title_home)
                //curso_fragment = CursoFragment.newInstance("","")
                //fm.beginTransaction()
                    //.addToBackStack(null)
                //    .replace(R.id.containerFrame,curso_fragment).commit()

                changeFragment(curso_fragment,curso_fragment.javaClass.simpleName)
                return@OnNavigationItemSelectedListener true
            }
            R.id.perfil -> {
                //textMessage.setText(R.string.title_dashboard)
                //perfil_fragment = PerfilFragment.newInstance("","")
                //fm.beginTransaction()
                    //.addToBackStack(null)
                    //.replace(R.id.containerFrame,perfil_fragment).commit()
                changeFragment(perfil_fragment,perfil_fragment.javaClass.simpleName)

                return@OnNavigationItemSelectedListener true
            }
            R.id.ajustes -> {
                //textMessage.setText(R.string.title_notifications)
                //ajustes_fragment = AjustesFragment.newInstance("","")
                //fm.beginTransaction()
                    //.addToBackStack(null)
                    //.replace(R.id.containerFrame,ajustes_fragment).commit()
                changeFragment(ajustes_fragment,ajustes_fragment.javaClass.simpleName)

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        navView = findViewById(R.id.nav_view)

        Log.e("tag","oncreated again raote")
        /*
        */
        if(savedInstanceState == null) {
            Log.e("tag","savedinstances == null")

            perfil_fragment = PerfilFragment.newInstance("","")
            curso_fragment = CursoFragment.newInstance("","")
            curso_fragment.fragment1 = listCapitulosFrag
            curso_fragment.fragment2 = detallesCapitulosFrag
            ajustes_fragment = AjustesFragment.newInstance("","")
            editar_perfil_fragment = EditPerfilFragment.newInstance("","")

            //curso_fragment = CursoFragment.newInstance("","")
            //fm.beginTransaction().add(R.id.containerFrame, curso_fragment).commit()
            changeFragment(curso_fragment,curso_fragment.javaClass.simpleName)
        }

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }

        val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)

        val sesion = prefs.getString("sesion","null")
        if (sesion!!.equals("null")){
            val menuAettings = nav_view.getMenu().getItem(2)
            menuAettings.title = "Menu"
            menuAettings.setIcon(R.drawable.ic_menuhb)
            nav_view.getMenu().removeItem(R.id.perfil);
        }else{
            //nav_view.getMenu().removeItem(R.id.perfil);
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

        super.onConfigurationChanged(newConfig)
        val currentOrientation = resources.configuration.orientation

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v("TAG", "Landscape !!!")

            navView.visibility = View.GONE
            cursoTitle.visibility = View.GONE
            constraintLayout2.visibility = View.GONE
            //capitulosCurso.visibility = View.GONE

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            Log.v("TAG", "Portrait !!!")

            navView.visibility = View.VISIBLE
            cursoTitle.visibility = View.VISIBLE
            constraintLayout2.visibility = View.VISIBLE
            //capitulosCurso.visibility = View.VISIBLE

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    fun changeFragment(fragment: Fragment, tagFragmentName: String) {

        val mFragmentManager = supportFragmentManager
        val fragmentTransaction = mFragmentManager.beginTransaction()

        //esconde el actual
        val currentFragment = mFragmentManager.primaryNavigationFragment
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment)
        }

        //si no esta agregado, hace add
        var fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName)
        if (fragmentTemp == null) {
            fragmentTemp = fragment
            fragmentTransaction.add(R.id.containerFrame, fragmentTemp, tagFragmentName)
        } else {
            //caso contrario, solo show
            fragmentTransaction.show(fragmentTemp)
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNowAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()

        Log.e("data","resume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("data","reiniciadno")
    }

    //Listener de cursofragment a dashboard
    override fun onCursoListener(message: String) {
        if (message.equals("opciones")){
            val fragmanager = supportFragmentManager.beginTransaction()
            fragmanager.setCustomAnimations(R.anim.enter_from_down,android.R.anim.slide_out_right)
            fragmanager.addToBackStack(null)
            fragmanager.add(R.id.containerFrameOpcVideo,videoOpcioesFrag).commit()

            navView.visibility = View.GONE
        } else if(message.equals("next")){

            doAsync {
                val gson1 = Gson()
                val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
                val datos = prefs.getString("user","")
                val us = gson1.fromJson(datos, User::class.java)
                val params = CalltaskVideos().videoUpdateProgress(curso_fragment.posActual + 2, us)
                val paramsData = CalltaskUser().userGetDataTask(us)
                if (paramsData["response"] as Boolean) {

                    val userT = paramsData["user"] as User
                    us.progress = userT.progress
                    us.course_id = userT.course_id
                    us.episode_id = userT.episode_id
                    us.time = userT.time

                } else {
                    params["response"] = false
                }

                uiThread {

                    val gson = GsonBuilder().create()
                    val stringData = gson.toJson(us)

                    val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                    prefs.edit().putString("sesion", "1").apply()

                    //Aqui se guarda el usuario con sus datos.
                    prefs.edit().putString("user", stringData).apply()

                    curso_fragment.muestraData()
                    listCapitulosFrag.nextVideo(curso_fragment.finishVideo)

                }
            }

        }else if(message.equals("listodescarga")){
            listCapitulosFrag.deleteIconChange()
        }
    }

    //Listener de ajustefragment a dashboard
    override fun onAjustesListener(message: String) {
        if(message.equals("0")){

            //mandamos a cerrar sesion
            val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
            dialog.show()
            val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            val datos = prefs.getString("user","")
            //Creamos una variable gson para volverla a formato objeto.
            val gson1 = Gson()
            val us = gson1.fromJson(datos, User::class.java)
            doAsync {
                val params = CalltaskUser().userLogoutTask(us.access_token)

                uiThread {
                    dialog.dismiss()

                    prefs.edit().putString("user","").apply()
                    prefs.edit().putString("sesion","null").apply()
                    prefs.edit().putString("ids","[]").apply()
                    startActivity(Intent(it, LoginActivity::class.java))

                    /*if(params ["response"] as Boolean){

                        prefs.edit().putString("user","").apply()
                        prefs.edit().putString("sesion","0").apply()
                        prefs.edit().putString("ids","[]").apply()
                        startActivity(Intent(it, LoginActivity::class.java))
                    }
                    else{

                        val msg = params["msg"].toString()
                        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    }*/
                }
            }
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    //Listener de perfilfragment a dashboard
    override fun onPerfilListener(message: String) {
        if(message.equals("editar")){
            //abrir editperfilfragment
            //editar_perfil_fragment = EditPerfilFragment.newInstance("","")
            //fm.beginTransaction().add(R.id.containerFrame,editar_perfil_fragment).commit()

            changeFragment(editar_perfil_fragment,editar_perfil_fragment.javaClass.simpleName)
        }

        if(message.equals("enviarMAIL")){
            val dialog = indeterminateProgressDialog("Enviando información…", "Atención")
            dialog.show()
            val context = this

            //tarea asincrona para llamar servidor se hace por atras de la aplicacion..
            doAsync {

                val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
                val datos = prefs.getString("user","")
                //Creamos una variable gson para volverla a formato objeto.
                val gson1 = Gson()
                val us = gson1.fromJson(datos, User::class.java)
                val params = CalltaskUser().userGetCertificado(us.access_token)
                //UserLoginTask(MainActivity.this, this).execute()


                //a partir de aqui se le informa al usuario la respuesta del server...
                uiThread {
                    if (params["response"] as Boolean) {

                        dialog.dismiss()
                        val msg = params["msg"].toString()
                        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                    } else {
                        dialog.dismiss()
                        val msg = params["msg"].toString()
                        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                        //textView.text = "Error"
                    }
                }
            }
        }
    }

    override fun onFragmentInteractionEditPerfil(message: String) {
        if(message.equals("0")){
            //aqui lo que se hacia era eliminar el fragment y retornar al mismo sin datos actualizados.
            fm.beginTransaction().remove(editar_perfil_fragment).commit()
        }
        else{
            //se utiliza para mostrar el fragment deseado, con los datos actualizados...
            //perfil_fragment = PerfilFragment.newInstance("","")
            //fm.beginTransaction().add(R.id.containerFrame,perfil_fragment).commit()
            perfil_fragment.muestraDatos()
            curso_fragment.muestraData()
            changeFragment(perfil_fragment,perfil_fragment.javaClass.simpleName)
        }
    }

    override fun onFragmentInteractionVideoOpciones(message: String) {
        if(message.equals("cancelar")){
            closeOptions()
        }else if(message.equals("descargar")){
            closeOptions()
            curso_fragment.downloadFromOpt()
            //listCapitulosFrag.descargarVideo()
        } else {
            //else es velocidad de reproduccion
            closeOptions()
            val vel = message.toFloat()
            curso_fragment.changeSpeed(vel)
        }
    }

    fun closeOptions(){
        if(curso_fragment.landPort == 1) {
            navView.visibility = View.GONE
        }else{
            navView.visibility = View.VISIBLE
        }
        val fragmanager = supportFragmentManager.beginTransaction()
        fragmanager.remove(videoOpcioesFrag).commit()
    }

    override fun capitulosSendVideo(course: Course, pos: Int, action: String) {
        if(action.equals("launch")){
            curso_fragment.preparedVideo(course, pos)
        }else if (action.equals("downdload")){
            curso_fragment.downloadVideo(course, pos)
        }else if (action.equals("registro")){
            val intent= Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onFragmentInteractionDetalles(uri: Uri) {
        Log.w("data","detalles")
    }

    override fun onBackPressed(){
        //super.onBackPressed()
        navView.visibility = View.VISIBLE
    }
}
