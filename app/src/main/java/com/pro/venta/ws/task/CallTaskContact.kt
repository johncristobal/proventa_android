package com.pro.venta.ws.task

import com.pro.venta.ws.server.Server
import org.json.JSONObject

class CallTaskContact{
    // ==================funcion De contacto======================
    fun UserContactTask(name: String, mail: String, company: String, commentary: String ) : HashMap<String, Any>{

        val Data = HashMap<String, String>()
        val Header = HashMap<String, String>()


        //Tommamos los valores del form y los almacenamos en la hashmap
        //las llaves de la hashmap deben ser igual a las del servicio...
        Data["name"] = name
        Data["email"] = mail
        Data["company"] = company
        Data["comment"] = commentary

        //llama a servidor
        val ws= Server()
        //
        val response= ws.enviaPost("contact", Data, Header)

        //Hashmap que obtiene las respuestas del servidor
        val params = HashMap<String, Any>()

        try {
            val obj= JSONObject(response)

            if(obj.getString("resp") == "ok"){
                //seria revisar lo que nos mande el servidor, recuperamos resp y el mensaje del server a mostrar al usuario...
                params["response"] = true
                params["msg"]= "Se han enviado tus datos correctamente"//sustituir este msj por el mensaje del servicio...
                return params
            } else {
                params["msg"] = obj.getString("error_description")
                params["response"] = false
                return params
            }

        }catch (t: Throwable){
            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }
}