package com.pro.venta.fragments

import android.app.AlertDialog
import android.app.AlertDialog.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pro.venta.R
import com.pro.venta.activities.*
import kotlinx.android.synthetic.main.fragment_ajustes.*
import android.app.AlertDialog.Builder as Builder1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AjustesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AjustesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AjustesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentAjustesListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ajustes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)

        val sesion = prefs.getString("sesion","null")
        if (sesion!!.equals("null")){
            cerrarSesion.text = "Iniciar sesion"
            layoutAjustes.visibility = View.GONE
            cerrarSesion.setOnClickListener{
                //falta alert antes
                listener?.onAjustesListener("1")
                //listener?.onAjustesListener("0")
            }
        }else{
            cerrarSesion.text = "Cerrar sesion"
            layoutAjustes.visibility = View.VISIBLE
            cerrarSesion.setOnClickListener{
                //falta alert antes
                val alert = AlertDialog.Builder(activity!!, AlertDialog.THEME_DEVICE_DEFAULT_DARK)


                alert.setMessage("¿Deseas cerrar sesión?")
                alert.setPositiveButton("SI", DialogInterface.OnClickListener{dialog ,
                                                                              id -> listener?.onAjustesListener("0")})//Toast.makeText(activity!!, "funciona", Toast.LENGTH_SHORT).show()})

                alert.setNegativeButton("NO", DialogInterface.OnClickListener{dialog ,
                                                                              id -> dialog.dismiss()})//Toast.makeText(activity!!, "no funciona", Toast.LENGTH_SHORT).show()})

                val al= alert.create()
                al.setTitle("Cerrar sesión")
                al.show()
                //listener?.onAjustesListener("0")
            }
        }


        OpcionDescarga.setOnClickListener{
            activity!!.startActivity(Intent(activity, OpcionesDescargaActivity::class.java))
        }

        OpcionVideo.setOnClickListener{
            activity!!.startActivity(Intent(activity,ReproduccionActivity::class.java))
        }

        CapitulosDescargados.setOnClickListener{
            activity!!.startActivity(Intent(activity,CapitulosActivity::class.java))
        }

        OpcionContacto.setOnClickListener {
            activity!!.startActivity(Intent(activity,ContactoActivity::class.java))
        }

        OpcionLegales.setOnClickListener {
            activity!!.startActivity(Intent(activity,LegalesActivity::class.java))
        }

        OpcionAcercaDe.setOnClickListener {
            activity!!.startActivity(Intent(activity,AcercaDeActivity::class.java))
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onAjustesListener("")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentAjustesListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentAjustesListener")
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
    interface OnFragmentAjustesListener {
        // TODO: Update argument type and name
        fun onAjustesListener(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AjustesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AjustesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
