package com.pro.venta.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pro.venta.R
import kotlinx.android.synthetic.main.fragment_register_part_a.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegisterPartAFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RegisterPartAFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterPartAFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: RegisterAListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_part_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Continuarregistro.setOnClickListener{
            val name = NombreR.text.toString()
            val last_name: String= ApellidosR.text.toString()
            val mail: String=CorreoR.text.toString()
            val pass1: String= ContrasR.text.toString()
            val pass2: String= RepiteContrasR.text.toString()
            val check:Boolean = terminosOk.isChecked

            //sendData(name,mail,company,pass1,pass2,check)
            val params = HashMap<String,Any>()
            params["response"] = "1"
            params["name"] = name
            params["last_name"] = last_name
            params["email"] = mail
            params["password"] = pass1
            params["password_confirmation"] = pass2
            params["check"] = check

            listener?.sendDataApart(params)

        }

        //Usamos el metodo finish para cerrar la actividad sin problemas
        closeButton.setOnClickListener{
            val params = HashMap<String,String>()
            params["response"] = "0"

            listener?.sendDataApart(params)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RegisterAListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement RegisterAListener")
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
    interface RegisterAListener {
        // TODO: Update argument type and name
        fun sendDataApart(data: Any)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterPartAFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterPartAFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
