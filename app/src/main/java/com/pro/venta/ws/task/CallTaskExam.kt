package com.pro.venta.ws.task

import android.util.Log
import com.pro.venta.model.*
import com.pro.venta.ws.server.Server
import org.json.JSONObject
import java.util.HashMap

class CallTaskExam {

    //===========================Funcion para recuperar datos del examen================================

    fun userGetExamTask(token: String): HashMap <String, Any >{

        val Data = HashMap<String, String>() //se almacena el token del usuario para realizar el examen
        val Headers = HashMap<String, String>() //alguna autorixacion para acceder a la url

        Data["token"] = token
        Headers["Authorization"] = "Bearer "+ token

        //llamamos al server
        val ws = Server()

        //se envian datos al server y se obtiene una respuesta
        val response = ws.enviaGet("course/exam/1", Data, Headers)

        //val params: objeto para almacenar respuestas del servidor
        val params = HashMap<String, Any>()

        try {
            //del string response obtengo JSON
            val obj = JSONObject(response)

            if(obj.getString("resp")=="ok"){

                val preg= ArrayList<Questions>()
                val idExam = obj.getString("exam_id")
                //val ExamData= obj.getJSONObject("questions")
                val pregExam= obj.getJSONArray("questions")

                if(pregExam.length() > 0){
                    for(i in 0 until pregExam.length()){
                        val item = pregExam.getJSONObject(i)
                        val id = item.getInt("id")
                        val pregunta = item.getString("question")
                        val opcionPreg= item.getJSONArray("options")

                        Log.e("id de pregunta", id.toString())
                        Log.e("preguntas del examen", pregunta)

                        if(opcionPreg.length() > 0){
                            val op= ArrayList<Opciones>()
                            Log.w("taamaño de opciones", opcionPreg.length().toString())

                            for (j in 0 until opcionPreg.length()){
                                val itemPreg = opcionPreg.getJSONObject(j)
                                val id1= itemPreg.getInt("id")
                                val valor= itemPreg.getString("value")
                                val datos= Opciones(id1, valor)
                                op.add(datos)
                                Log.d("id", id1.toString())
                                Log.d("opcion:", valor)
                                Log.w("opcion del array", op.get(j).opcion)
                            }
                            val quest = Questions(id, pregunta, 0, op)
                            preg.add(quest)
                        }
                        else{
                            params["preg"]=preg
                        }

                    }

                    params["preg"]= preg
                }
                else{
                    params["preg"]=preg
                }

                //imprimir el arraylist
                //params["msg"] = obj.getString("message")
                params["response"] = true
                params["idExam"] = idExam
                return params
            }
            else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        }catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

    fun userSendExamTask(accessToken: String, idExam: Int, respustas: HashMap<String,String>): HashMap <String, Any > {
        val Data = HashMap<String, Any>()
        val Header = HashMap<String, String>()

        //Tommamos los valores del form y los almacenamos en la hashmap
        //las llaves de la hashmap deben ser igual a las del servicio...
        Data["token"] = accessToken
        val objectJs = JSONObject()
        for ((key, value) in respustas) {

            objectJs.put(key,value)

        }
        Data["questions"] = objectJs

        //llama a servidor
        val ws= Server()
        //
        val response= ws.enviaPostArray("course/exam/${idExam}/add", Data, Header)

        //Hashmap que obtiene las respuestas del servidor
        val params = HashMap<String, Any>()

        try {
            val obj= JSONObject(response)

            if(obj.getString("resp") == "ok"){
                //seria revisar lo que nos mande el servidor, recuperamos resp y el mensaje del server a mostrar al usuario...
                params["response"] = true
                params["msg"]= obj.getString("message")//"Se han enviado tus datos correctamente"//sustituir este msj por el mensaje del servicio...
                return params
            } else {
                params["msg"] = obj.getString("message")
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