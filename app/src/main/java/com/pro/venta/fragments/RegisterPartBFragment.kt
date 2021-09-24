package com.pro.venta.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.pro.venta.R
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_register_part_b.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegisterPartBFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RegisterPartBFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterPartBFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: RegisterBListener? = null
    private var idZipcode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_part_b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        gruporadios.setOnCheckedChangeListener { radioGroup, i ->
            val selectedId = gruporadios.checkedRadioButtonId
            when (selectedId){
                R.id.radioButtonIndividual -> {
                    Log.e("radio","individual")
                    //hide label y campo
                    textViewCuantas.visibility = View.INVISIBLE
                    cuentasNumber.visibility = View.INVISIBLE
                    textView21.visibility = View.INVISIBLE
                    textViewCantidad.visibility = View.INVISIBLE
                }
                R.id.radioButtonMulti -> {
                    Log.e("radio","multi")
                    //show label y campo
                    textViewCuantas.visibility = View.VISIBLE
                    cuentasNumber.visibility = View.VISIBLE
                    textView21.visibility = View.VISIBLE
                    textViewCantidad.visibility = View.VISIBLE

                }else -> {
                    Log.e("radio","else")
                }
            }
        }



        cuentasNumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!cuentasNumber.text.toString().equals("")){
                    val formatter = DecimalFormat("#,###.00")
                    val subtotal = 1500.00 * cuentasNumber.text.toString().toInt()
                    //val cadena = String.format("%.2f", subtotal)
                    val precio= formatter.format(subtotal)
                    val total=precio.toString()

                    Log.e("subtotal $", total)
                    //la falla esta en esta linea... ya quedo xD
                    textViewCantidad.text ="$"+total
                }
            }
        })
            */
        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        val thisDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        for (i in thisYear downTo 1920) {
            years.add(Integer.toString(i))
        }
        val adapter = ArrayAdapter<String>(activity!!, R.layout.spinneritem, years)

        //val spinYear = findViewById(R.id.yearspin) as Spinner
        spinnerAnio.adapter = adapter

        val dayadapter = ArrayAdapter.createFromResource(
            activity!!, R.array.item_day, R.layout.spinneritem);
        //dayadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerDia.adapter = dayadapter

        val monthadapter = ArrayAdapter.createFromResource(
            activity!!, R.array.item_month, R.layout.spinneritem);
        //dayadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerMes.adapter = monthadapter

        /* Boton de la validacion del codigo postal

        buttonValidarCP.setOnClickListener {
            //validar el codigo...
            val zipcode = postalText.text.toString()
            if (zipcode.equals("")){
                Toast.makeText(activity!!,"Favor de ingresar código postal",Toast.LENGTH_SHORT).show()
            }else{

                buttonValidarCP.alpha = 0.0f
                progressBarValidando.alpha = 1.0f

                val params = HashMap<String,Any>()
                params["response"] = "2"
                params["zipcode"] = zipcode
                listener?.sendDataBpart(params)
            }
        }
            */
        pagoButton.setOnClickListener {

            val params = HashMap<String,Any>()
            params["response"] = "1"

            Log.e("dia",spinnerDia.selectedItem.toString())
            Log.e("mes",spinnerMes.selectedItem.toString())
            Log.e("anio",spinnerAnio.selectedItem.toString())

            val thisYear = Calendar.getInstance().get(Calendar.YEAR)
            val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
            val thisDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

            val yearuser = spinnerAnio.selectedItem.toString().toInt()
            val monthuser = spinnerMes.selectedItem.toString().toInt()
            val dayuser = spinnerDia.selectedItem.toString().toInt()

            val yearus = spinnerAnio.selectedItem.toString()
            val monthus = spinnerMes.selectedItem.toString()
            val dayus = spinnerDia.selectedItem.toString()

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

            val fecha = yearus+"-"+monthus+"-"+dayus

            Log.e("fecha", fecha)

            Log.e("edad","$diffyear")
            //params["age"] = diffyear
            params["date_birth"] = fecha
            params["counts"] = 1

            /* PAra la parte de cuenta y multicuentas
            val selectedId = gruporadios.checkedRadioButtonId
            when (selectedId){
                R.id.radioButtonIndividual -> {
                    Log.e("radio","individual")
                    params["counts"] = 1
                }
                R.id.radioButtonMulti -> {
                    Log.e("radio","multi")
                    params["counts"] = cuentasNumber.text.toString().toInt()
                }else -> {
                    Log.e("radio","else")
                }
            }
            */


            val codigopostal = postalText.text.toString()
            if (codigopostal == ""){
                params["cp"] =""
            }
            else{
                params["cp"] = codigopostal
            }
            /*
            else{
                if(!idZipcode.equals("")) {
                    params["location"] = idZipcode.toInt() //codigopostal.toInt()
                }else{
                    params["location"] = -1
                }
            }
             */

            listener?.sendDataBpart(params)
        }

        closeButton.setOnClickListener {
            val params = HashMap<String,String>()
            params["response"] = "0"

            listener?.sendDataBpart(params)
        }

    }

    /*
    fun codigoListo(status: Int, idtemp: String){

        when (status){
            1 -> {
                buttonValidarCP.alpha = 0.0f
                progressBarValidando.alpha = 0.0f
                imageReadyIcon.alpha = 1.0f
                idZipcode = idtemp
            }
            else -> {
                buttonValidarCP.alpha = 1.0f
                progressBarValidando.alpha = 0.0f
                imageReadyIcon.alpha = 0.0f
            }
        }
    }
    */


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.sendDataBpart(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RegisterBListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement RegisterBListener")
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
    interface RegisterBListener {
        // TODO: Update argument type and name
        fun sendDataBpart(uri: Any)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterPartBFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterPartBFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
