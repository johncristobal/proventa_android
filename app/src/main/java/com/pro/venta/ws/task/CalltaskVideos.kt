package com.pro.venta.ws.task

import android.util.Log
import com.pro.venta.model.User
import com.pro.venta.ws.server.Server
import org.json.JSONObject

class CalltaskVideos {

// ============================= login usuario =======================================
    fun videoUpdateProgress(id: Int, user: User) : HashMap<String, Any> {

        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        Data["token"] = user.access_token //"cristobaljohn00@gmail.com"
        Data["id"] = id.toString() //"123456789"

        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaPost("course/episode/next", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                params["msg"] = obj.getString("message")
                params["response"] = true //dato boolean
                return params

            } else {
                params["msg"] = obj.getString("message")
                Log.e("algo falla", "entra al else del calltask")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            Log.e("algo falla", "entra al catch del calltask")
            return params
        }
    }


}