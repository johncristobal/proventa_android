package com.pro.venta.fragments

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.GsonBuilder
import org.jetbrains.anko.selector

import com.pro.venta.R
import com.pro.venta.ws.singleton.quality
import com.pro.venta.ws.singleton.settings
import kotlinx.android.synthetic.main.activity_opciones_descarga.*
import kotlinx.android.synthetic.main.fragment_opc_video.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OpcVideoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OpcVideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpcVideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListenerOpcionesVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_opc_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sett = settings()

        cancelarOpcionesVideo.setOnClickListener {
            listener?.onFragmentInteractionVideoOpciones("cancelar")
        }

        CalidadVideo.setOnClickListener{

            val calidad= listOf("Alta","Media","Baja")

            activity!!.selector("Seleccione calidad de video", calidad){dialogInterface, i ->
                when(i){
                    0 -> {
                        //CalidadVideo.text= quality.alta.tipo
                        sett.qualityVideoDownload= quality.alta
                    }

                    1-> {
                        //CalidadVideo.text = quality.media.tipo
                        sett.qualityVideoDownload = quality.media
                    }

                    else -> {
                        //CalidadVideo.text = quality.baja.tipo
                        sett.qualityVideoDownload = quality.baja
                    }
                }
            }
        }

        DescargaCapitulo.setOnClickListener {
            listener?.onFragmentInteractionVideoOpciones("descargar")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            VelocidadReprod.visibility = View.VISIBLE
        }

        VelocidadReprod.setOnClickListener {
            val calidad = listOf("x 0.5", "x 0.75", "Normal", "x 1.25", "x 1.5")
            activity!!.selector("Selecciona velocidad de reproducción", calidad) {dialogInterface, i ->
                when (i){
                    0 -> {
                        listener?.onFragmentInteractionVideoOpciones("0.5")
                    }
                    1 -> {
                        listener?.onFragmentInteractionVideoOpciones("0.75")
                    }
                    2 -> {
                        listener?.onFragmentInteractionVideoOpciones("1.0")
                    }
                    3 -> {
                        listener?.onFragmentInteractionVideoOpciones("1.25")
                    }
                    4 -> {
                        listener?.onFragmentInteractionVideoOpciones("1.5")
                    }
                    else -> {
                        listener?.onFragmentInteractionVideoOpciones("1.0")
                    }
                }
            }
        }

        MarcaCompl.setOnClickListener {
            Toast.makeText(activity!!,"Clase marcada como completa...",Toast.LENGTH_SHORT).show()
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteractionVideoOpciones("")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerOpcionesVideo) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListenerOpcionesVideo")
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
    interface OnFragmentInteractionListenerOpcionesVideo {
        // TODO: Update argument type and name
        fun onFragmentInteractionVideoOpciones(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OpcVideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OpcVideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
