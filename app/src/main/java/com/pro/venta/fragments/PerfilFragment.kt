package com.pro.venta.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.pro.venta.R
import com.pro.venta.model.User
import kotlinx.android.synthetic.main.fragment_perfil.*
import android.content.Context as Context1
import android.content.SharedPreferences as SharedPreferences1
import android.widget.Toast.makeText as makeText1
import org.json.JSONArray as JSONArray1
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.pro.venta.activities.*
import com.pro.venta.model.Questions
import com.pro.venta.ws.task.CallTaskExam
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.selector
import org.jetbrains.anko.uiThread
import com.pro.venta.activities.ExamActivity as ExamActivity
import com.pro.venta.activities.MainActivity
import android.view.MenuItem
import android.R.attr.button
import androidx.appcompat.widget.PopupMenu


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PerfilFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

var preguntas: ArrayList<Questions> = ArrayList()

class PerfilFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentPerfilListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    //aqui en este metodo se programa lo que se va a visualizaer en el fragment...
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        muestraDatos()

        editarButton.setOnClickListener {
            listener?.onPerfilListener("editar")
        }

        EdButton.setOnClickListener{
            //listener?.onPerfilListener("editar")

            //val q= Intent(activity!!, ExamActivity::class.java)
            //startActivity(q)
            //muestraPreguntas()

            /*val calidad = listOf("Mi cuenta", "Membresías adicionales", "Éxamenes", "Órdenes", "Pagos","Cancelar")
            activity!!.selector("Más opciones...", calidad) {dialogInterface, i ->
                when (i){
                    0 -> {
                        startActivity(Intent(activity!!, MembresiaActivity::class.java))
                    }
                    1 -> {
                        startActivity(Intent(activity!!, MembresiasAdicionalesActivity::class.java))
                    }
                    2 -> {
                        //startActivity(Intent(activity!!, ExamenesActivity::class.java))
                        startActivity(Intent(activity!!, ExamActivity::class.java))
                    }
                    3 -> {
                        startActivity(Intent(activity!!, OrdenesActivity::class.java))
                    }
                    4 -> {
                        startActivity(Intent(activity!!, PagosActivity::class.java))
                    }
                    else -> {

                    }
                }
            }*/

            val popup = PopupMenu(activity!!, EdButton)
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.moreperil, popup.getMenu())

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {

                override fun onMenuItemClick(item: MenuItem?): Boolean {

                    when(item!!.itemId){
                        R.id.cuenta -> {
                            startActivity(Intent(activity!!, MembresiaActivity::class.java))
                        }
                        R.id.membresias -> {
                            startActivity(Intent(activity!!, MembresiasAdicionalesActivity::class.java))
                        }
                        R.id.examenes -> {
                            startActivity(Intent(activity!!, ExamenesActivity::class.java))
                        }
                        R.id.ordenes -> {
                            startActivity(Intent(activity!!, OrdenesActivity::class.java))
                        }
                        R.id.pagos -> {
                            startActivity(Intent(activity!!, PagosActivity::class.java))
                        }
                        else -> {

                        }
                    }

                    return true
                }
            })
            popup.show()
        }


        showFinal.setOnClickListener {
            /*val webVi = Intent(activity!!,WebActivity::class.java)
            webVi.putExtra("url","http://xatsaautopartes.xyz/moodle/images/ready.pdf")
            activity!!.startActivity(webVi)*/
            //val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://xatsaautopartes.xyz/moodle/images/ready.pdf"))
            //startActivity(browserIntent)

            listener?.onPerfilListener("enviarMAIL")
        }

        downloadFinal.setOnClickListener {
            /*val webVi = Intent(activity!!,WebActivity::class.java)
            webVi.putExtra("url","http://xatsaautopartes.xyz/moodle/images/ready.pdf")
            activity!!.startActivity(webVi)
            */
            //val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://xatsaautopartes.xyz/moodle/images/ready.pdf"))
            //startActivity(browserIntent)
            startActivity(Intent(activity!!, ExamActivity::class.java))
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
        val last_name:String = us.last_name
        val correo:String =us.email
        val imgPath = us.image_path

        if (imgPath != ""){

            /*usar la libreria glide para cargar foto*/

            //val bitmap = BitmapFactory.decodeFile(imgPath)
            //FotoPerfil.setImageBitmap(bitmap)

            Glide.with(activity!!)
                .load(imgPath)
                .into(FotoPerfil)
        }

        //Log.d("hola", nombre)
        //Log.d("empresa", empresa)
        //Se asignan los valores a los textview para mostrarlos.
        tvND.text= nombre
        tvCD.text= correo
        tvNombreEmp.text= last_name
        progressBar2.progress = us.progress
        tvPorcentaje_progreso.text = "${us.progress}%"
        tvfechanacimiento.text = us.birth_day
        tvcodigopostal.text = us.location

        //FotoPerfil.setImageURI(img)

        val fincurso = prefs.getString("findecursos", "")
        Log.e("valor fin de curso", fincurso)

        //if(fincurso.equals("1") ){
        if(us.progress == 100){
            val llp =  tvDatosG.layoutParams as ConstraintLayout.LayoutParams
            val scale = context!!.resources.displayMetrics.density
            val pixels = ((160 * scale + 0.5f)).toInt() //as Int

            //val llp = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            llp.setMargins(0, pixels, 0, 0) // llp.setMargins(left, top, right, bottom);
            tvDatosG.setLayoutParams(llp)

            constraintLayout.visibility = View.INVISIBLE
            constraintLayoutFinCurso.visibility = View.VISIBLE

        }else{
            Log.w("aun no termina","false")
            constraintLayout.visibility = View.VISIBLE
            constraintLayoutFinCurso.visibility = View.INVISIBLE
        }
    }

    fun muestraPreguntas(){
        //dialog para decirle al usuario que su peticion esta en proceso
        val dialog = activity!!.indeterminateProgressDialog("Enviando información…", "Atención")
        dialog.show()
        val context = this

        val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val datos = prefs.getString("user","")
        //Creamos una variable gson para volverla a formato objeto.
        val gson1 = Gson()
        val us = gson1.fromJson(datos, User::class.java)

        doAsync {
            val params = CallTaskExam().userGetExamTask(us.access_token) //recuperamos el token del usuario

            uiThread {
                dialog.dismiss()//Eliminamos el dialogo

                if(params ["response"] as Boolean){
                    preguntas = params["preg"] as ArrayList<Questions>
                    preguntas.size


                    val msg = "mnda preguntas desde ws"//params["msg"].toString()
                    Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()

                    Log.w("tamaño del arraylist", preguntas.size.toString())
                    Log.w("pregunata 1:", preguntas.get(0).question)

                }
                else{
                    val msg = params["msg"].toString()
                    Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onPerfilListener("")
    }

    override fun onAttach(context: Context1) {
        super.onAttach(context)
        if (context is OnFragmentPerfilListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentPerfilListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onResume() {
        super.onResume()

        muestraDatos()
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
    interface OnFragmentPerfilListener {
        // TODO: Update argument type and name
        fun onPerfilListener(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PerfilFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PerfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
