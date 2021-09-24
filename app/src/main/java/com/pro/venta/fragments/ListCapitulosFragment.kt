package com.pro.venta.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

import com.pro.venta.R
import com.pro.venta.adapter.CourseAdapter
import com.pro.venta.model.Course
import com.pro.venta.ws.singleton.settings
import kotlinx.android.synthetic.main.fragment_list_capitulos.*
import java.util.*
import kotlin.collections.ArrayList
import android.net.ConnectivityManager
import android.os.Environment
import android.widget.Toast
import com.pro.venta.model.User
import com.pro.venta.model.Videos
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListCapitulosFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListCapitulosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListCapitulosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: CapitulosInteractionListener? = null
    var capitulos = ArrayList<Course>()
    var adapterCapitulos: CourseAdapter? = null

    var settingsShared : settings? = null

    var posActual = 0
    var courseActual: Course? = null
    var posDescargando = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_capitulos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val sesion = prefs.getString("sesion","null")
        if (sesion!!.equals("null")){
            var videos = ArrayList<Videos>()
            videos.add(Videos("","https://api.proventas.pro/media1/teaser/0.mp4"))
            videos.add(Videos("","https://api.proventas.pro/media1/teaser/0.mp4"))

            capitulos.add(Course("0","name0.mp4","https://websiteqas2019.miituo.com/img_cupones/muertos150/capitulo1.mp4",0,"","¡Por fin! Llegó un curso que habla en tu idioma, sin rodeos. Aprende sólo lo que deseas saber para lograr que tus ventas sean exitosas.","La mejor inversión para ser exitoso en ventas","",videos))
            capitulos.add(Course("-1","name1.mp4","https://websiteqas2019.miituo.com/img_cupones/muertos150/capitulo1.mp4",0,"","Prepárate para conocer sobre los efectos \n de las compras en las personas.","Capítulo 1. ¿Qué tipo de vendedor quieres ser?","",videos))
            capitulos.add(Course("-2","name2.mp4","https://proventas.pro/media/capitulos/2-54uUxZGBQ2QBLMEkXmT6fpDgC9XqxZTC.mp4",0,"","En compañía de \"La Kikis\" aprenderemos la magía que existe detrás del Método Proventas y la importancia del IPC.","Capítulo 2. Método IPC","",videos))
            capitulos.add(Course("-3","name3.mp4","https://proventas.pro/media/capitulos/3-HXUM76M3Fm53v4xxaDTXTSVYntryCaSE.mp4",0,"","Cazar prospectos no es algo que únicamente se dé en el ligue. ¡Aprende a cazar a tu prospecto ideal en la vid y en las ventas!","Capítulo 3. Cazando en la vida y en las ventas","",videos))
            capitulos.add(Course("-4","name4.mp4","https://proventas.pro/media/capitulos/4-HXUM76M3Fm53v4xxaDTXTSVYntryCaSE.mp4",0,"","La máquina de ventas Jiménez vs El muñeco de las ventas Ramirez. Solo el que domina el IPC ganará esta epica batalla.","Capítulo 4. Vendedores al ring","",videos))

            adapterCapitulos = CourseAdapter(activity!!, capitulos, { course, pos ->

                if (pos == 0){
                    listener?.capitulosSendVideo(course, pos, "launch")
                    adapterCapitulos!!.row_index = pos
                    posActual = pos
                    courseActual = course
                    adapterCapitulos!!.notifyDataSetChanged()
                }else{
                    //launch register
                    listener?.capitulosSendVideo(course, pos, "registro")
                }
            }, { course, pos ->
                //descargar capitulo -- logic

            })

            capitulosCurso.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            capitulosCurso.adapter = adapterCapitulos

            //lanzamos primer video por default
            //listener?.capitulosSendVideo(capitulos[0], 0, "launch")

        }else {
            val datos = prefs.getString("user", "")
            //Creamos una variable gson para volverla a formato objeto.
            val gson1 = Gson()
            val us = gson1.fromJson(datos, User::class.java)

            capitulos = us.capitulos

            //la lista de videos viene del json
            //ya tenemos una lista de videos descargados...
            //id video
            //loop a json y videos descargados para cambiar en caso que ya este desacargado
            //sera necesario dos loops anidados???

            /*
            var videos = ArrayList<Videos>()
            videos.add(Videos("","https://websiteqas2019.miituo.com/img_cupones/muertos150/2480.mp4"))
            videos.add(Videos("","https://websiteqas2019.miituo.com/img_cupones/muertos150/2480.mp4"))
            capitulos.add(Course("-1","name0.mp4","https://websiteqas2019.miituo.com/img_cupones/muertos150/2480.mp4",0,"9:59","Prepárate para conocer sobre los efectos \n de las compras en las personas.","Capítulo 1. ¿Qué tipo de vendedor quieres ser?","",videos))

            videos = ArrayList<Videos>()
            videos.add(Videos("","http://xatsaautopartes.xyz/ClauWeb/2.mp4"))
            videos.add(Videos("","http://xatsaautopartes.xyz/ClauWeb/2.mp4"))
            capitulos.add(Course("-1","name0.mp4","https://websiteqas2019.miituo.com/img_cupones/muertos150/2480.mp4",0,"9:59","Prepárate para conocer sobre los efectos \n de las compras en las personas.","Capítulo 1. ¿Qué tipo de vendedor quieres ser?","",videos))

            videos = ArrayList<Videos>()
            videos.add(Videos("","https://api.proventas.pro/v1/multimedia/episode/video/2.mp4"))
            videos.add(Videos("","https://api.proventas.pro/v1/multimedia/episode/video/2.mp4"))
            capitulos.add(Course("-1","name0.mp4","https://websiteqas2019.miituo.com/img_cupones/muertos150/2480.mp4",0,"9:59","Prepárate para conocer sobre los efectos \n de las compras en las personas.","Capítulo 1. ¿Qué tipo de vendedor quieres ser?","",videos))
            */
            //capitulos.add(Course("-2","name1.mp4","http://xatsaautopartes.xyz/moodle/images/TEASER.mp4",0,"0:25","Prepárate para conocer sobre los efectos \n de las compras en las personas.","Capítulo 1. ¿Qué tipo de vendedor quieres ser?"))
            //capitulos.add(Course("-1","name1.mp4","https://proventas.pro/media/capitulos/1-LhVZHq8wFVkq3U2495H2CBBuRPA2EwnK.mp4",0,"04:52","Prepárate para conocer sobre los efectos \n de las compras en las personas.","Capítulo 1. ¿Qué tipo de vendedor quieres ser?"))
            //capitulos.add(Course("-1","name1.mp4","https://websiteqas2019.miituo.com/img_cupones/muertos150/capitulo1.mp4",0,"04:52","Prepárate para conocer sobre los efectos \n de las compras en las personas.","Capítulo 1. ¿Qué tipo de vendedor quieres ser?",""))
            //capitulos.add(Course("-2","name2.mp4","https://proventas.pro/media/capitulos/2-54uUxZGBQ2QBLMEkXmT6fpDgC9XqxZTC.mp4",0,"0:25","En compañía de \"La Kikis\" aprenderemos la magía que existe detrás del Método Proventas y la importancia del IPC.","Capítulo 2. Método IPC",""))
            //capitulos.add(Course("-3","name3.mp4","https://proventas.pro/media/capitulos/3-HXUM76M3Fm53v4xxaDTXTSVYntryCaSE.mp4",0,"0:25","Cazar prospectos no es algo que únicamente se dé en el ligue. ¡Aprende a cazar a tu prospecto ideal en la vid y en las ventas!","Capítulo 3. Cazando en la vida y en las ventas",""))
            //capitulos.add(Course("-4","name4.mp4","https://proventas.pro/media/capitulos/4-HXUM76M3Fm53v4xxaDTXTSVYntryCaSE.mp4",0,"0:25","La máquina de ventas Jiménez vs El muñeco de las ventas Ramirez. Solo el que domina el IPC ganará esta epica batalla.","Capítulo 4. Vendedores al ring",""))
            //capitulos.add(Course("-3","name2.mp4","https://www.w3schools.com/html/mov_bbb.mp4",0))
            //capitulos.add(Course("-4","name3.mp4","https://ve.media.tumblr.com/tumblr_q2l4uvc9bQ1y4pkz0.mp4",0))
            //capitulos.add(Course("-5","name4.mp4","https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4",0))

            val jsonData = prefs.getString("ids", "")
            if (jsonData!!.equals("[]") || jsonData.equals("")) {
                //no hay datos guardados
                Log.w("sin datos", "next")
            } else {
                Log.w("con datos de ids", "loop")
                //recuperamos lista
                val gson = Gson()
                val descargadosTemp = gson.fromJson(jsonData, Array<Course>::class.java).toList()
                val descargados = ArrayList(descargadosTemp)

                capitulos.forEach { chapter ->
                    descargados.forEach { chapTemp ->
                        if (chapter.id.equals(chapTemp.id)) {
                            chapter.download = 1
                        } else {
                            chapter.download = 0
                        }
                    }
                }
            }


            adapterCapitulos = CourseAdapter(activity!!, capitulos, { course, pos ->

                if (us.account) {
                    if ((pos != 0) && (us.progress != 100)) {

                        val alert =
                            AlertDialog.Builder(activity!!, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                        alert.setTitle("No puedes saltarte un capítulo")
                        alert.setMessage("Es necesario que termines el capítulo actual para poder ver el siguiente.")
                        alert.setPositiveButton(
                            "Ok",
                            DialogInterface.OnClickListener { dialog, id ->

                            })

                        val al = alert.create()
                        al.show()

                    } else {
                        listener?.capitulosSendVideo(course, pos, "launch")
                        adapterCapitulos!!.row_index = pos
                        posActual = pos
                        courseActual = course
                        adapterCapitulos!!.notifyDataSetChanged()
                    }
                } else {
                    listener?.capitulosSendVideo(course, pos, "launch")
                    adapterCapitulos!!.row_index = pos
                    posActual = pos
                    courseActual = course
                    adapterCapitulos!!.notifyDataSetChanged()
                }


            }, { course, pos ->
                //descargar capitulo -- logic
                if (capitulos[pos].download == 0) {

                    var flagWifi = false
                    var flagDatos = false

                    val cm =
                        context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork = cm.activeNetworkInfo
                    if (activeNetwork != null) {
                        // connected to the internet
                        if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                            // connected to wifi
                            flagWifi = true
                        } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                            // connected to mobile data
                            flagDatos = true
                        }
                    } else {
                        // not connected to the internet
                        flagWifi = false
                        flagDatos = false
                    }

                    if (settingsShared!!.wifiDownload) {
                        //si es true entonces solo podemos descargar por wifi
                        if (flagWifi) {
                            Log.e("descargando", "des")
                            listener?.capitulosSendVideo(course, pos, "downdload")
                            capitulos[pos].download = 2
                            posDescargando = pos
                            adapterCapitulos!!.notifyDataSetChanged()

                        } else {
                            //no podemos descargar, sin conexion
                            Toast.makeText(
                                activity!!,
                                "Sin conexión a Internet, intente más tarde...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        //si es false, entonces descargamos por wifi y datos sin tema
                        if (flagWifi || flagDatos) {
                            Log.e("descargando", "des")
                            listener?.capitulosSendVideo(course, pos, "downdload")
                            capitulos[pos].download = 2
                            posDescargando = pos
                            adapterCapitulos!!.notifyDataSetChanged()

                        } else {
                            //sin conexion, no podemos descargar
                            Toast.makeText(
                                activity!!,
                                "Sin conexión a Internet, intente más tarde...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else if (capitulos[pos].download == 1) {
                    //eliminar capitulo y regresar icoono de donwload
                    Log.e("eliminar de descargas", "not now")
                    capitulos[pos].download = 0
                    posDescargando = pos
                    adapterCapitulos!!.notifyDataSetChanged()

                    val gson = Gson()
                    val jsonDataDown = prefs.getString("ids", "")
                    val descargadosTemp =
                        gson.fromJson(jsonDataDown, Array<Course>::class.java).toList()
                    val descargados = ArrayList(descargadosTemp)
                    descargados.remove(capitulos[pos])

                    val json = gson.toJson(descargados)
                    prefs.edit().putString("ids", json).apply()
                }
            })

            capitulosCurso.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            capitulosCurso.adapter = adapterCapitulos
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val currentOrientation = resources.configuration.orientation

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v("TAG", "Landscape !!!")
            capitulosCurso.visibility = View.GONE
        } else {
            Log.v("TAG", "Portrait !!!")
            capitulosCurso.visibility = View.VISIBLE
        }
    }

    fun deleteIconChange(){
        //val mFile3: File? = File(Environment.getExternalStorageDirectory(), "pictures/${capitulos[posDescargando].name}")
        val mFile3: File? = File(activity!!.filesDir, "videos/${capitulos[posDescargando].name}")
        val sizeT = mFile3!!.length()
        var stringSize = ""
        val size = sizeT / 1024 // Get size and convert bytes into Kb.
        if (size >= 1024) {
            stringSize = "${(size / 1024)} Mb"
        } else {
            stringSize = "${size} Kb"
        }
        capitulos[posDescargando].size = stringSize

        val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val jsonData = prefs.getString("ids","")
        if (jsonData!!.equals("[]") || jsonData.equals("")){
            //no hay datos guardados, recupero id y guardo en lista
            val iddescargado = capitulos[posDescargando]
            val descargados = ArrayList<Course>()
            descargados.add(iddescargado)

            val gson = Gson()
            val json = gson.toJson(descargados)
            prefs.edit().putString("ids",json).apply()
        }else{
            //recuperamos lista y agrego el nuevo id
            val gson = Gson()
            val descargadosTemp = gson.fromJson(jsonData,Array<Course>::class.java).toList()
            val descargados = ArrayList(descargadosTemp)
            val iddescargado = capitulos[posDescargando]
            descargados.add(iddescargado)
            val json = gson.toJson(descargados)
            prefs.edit().putString("ids",json).apply()
        }

        //ahora si guardamos el idvideo en algun lugar
        capitulos[posDescargando].download = 1
        adapterCapitulos!!.notifyDataSetChanged()
    }

    fun nextVideo(finishVideo: Boolean){

        Log.w("next","$finishVideo")
        Log.w("posActual","$posActual")

        val totales = capitulos.size - 1
        if (posActual+1 <= totales){
            Log.w("siguiente","${posActual+1}")

            //capitulosCurso.scrollToPosition(posActual+1)
            val task = object : TimerTask() {
                override fun run() {
                    Log.d("fin de timer","cambando curso")
                    activity!!.runOnUiThread(Runnable {
                        if(finishVideo) {
                            listener?.capitulosSendVideo(
                                capitulos.get(posActual + 1),
                                posActual + 1,
                                "launch"
                            )
                            adapterCapitulos!!.row_index = posActual + 1
                            posActual = posActual + 1
                            adapterCapitulos!!.notifyDataSetChanged()
                        }
                    })
                }
            }

            val timerTemp = Timer()
            timerTemp.schedule(task, 3000)
        }else{
            //no hay mas cursos, fin del todo
            Log.w("fin del curso","no hay mas videos")
            val prefs= activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
            prefs.edit().putString("findecursos", "1").apply()


            //val fincurso= prefs.getString("findecursos", "")
            //Log.e("valorfin",fincurso)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CapitulosInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement CapitulosInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()

        Log.e("resume","lista resumen")

        val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val datos = prefs.getString("settings","")

        if(datos != "") {
            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            settingsShared = gson.fromJson(datos, settings::class.java) //aqui la cadena se convierte en un objeto.
        }else{
            settingsShared = settings()
        }

        var jsonData = prefs.getString("ids","")
        if (jsonData!!.equals("[]") || jsonData.equals("")){
            //no hay datos guardados
            Log.w("sin datos","next")
            capitulos.forEach { chapter ->
                chapter.download = 0
            }
            adapterCapitulos!!.notifyDataSetChanged()
        }else{
            Log.w("con datos de ids","loop")
            //recuperamos lista
            val gson = Gson()
            val descargadosTemp = gson.fromJson(jsonData,Array<Course>::class.java).toList()
            val descargados = ArrayList(descargadosTemp)

            capitulos.forEach { chapter ->
                descargados.forEach { chapTemp ->
                    if(chapter.id.equals(chapTemp.id)){
                        chapter.download = 1
                    }else{
                        chapter.download = 0
                    }
                }
            }
            adapterCapitulos!!.notifyDataSetChanged()
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
    interface CapitulosInteractionListener {
        // TODO: Update argument type and name
        fun capitulosSendVideo(course: Course, pos: Int, action: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListCapitulosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListCapitulosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
