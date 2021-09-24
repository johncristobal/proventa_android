package com.pro.venta.ws.task

import android.preference.PreferenceActivity
import android.util.Log
import com.pro.venta.model.Course
import com.pro.venta.model.User
import com.pro.venta.model.Videos
import com.pro.venta.ws.server.Server
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class CalltaskUser {

// ============================= login usuario =======================================
    fun userLoginTask(mail: String, pass: String) : HashMap<String, Any> {

        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        Data["email"] = mail //"cristobaljohn00@gmail.com"
        Data["password"] = pass //"123456789"

        //llama a servidor
    // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaPost("login", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos del usuario creando una instancia del modelo User()
                val user = User()

                user.access_token = obj.getString("access_token")
                user.refresh_token = obj.getString("refresh_token")
                user.token_type = "" //obj.getString("token_type")
                user.expires_in = obj.getInt("expires_in")
                user.email = mail
                user.password = pass

                params["user"] = user //string json

                //params["msg"] = obj.getString("message")

                params["msg"] = "true"//obj.getString("message")

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


// ============================= logput usuario =======================================
    fun userLogoutTask(token: String) : HashMap<String, Any> {

        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        Data["token"] = token  //"123456789"
        Headers["Authorization"] = "Bearer "+token

        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaPost("logout", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //params["msg"] = obj.getString("msg")
                params["response"] = true //dato boolean
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }


// ============================= registrar usuario =======================================
    fun userRegisterTask(name: String, last_name: String, email: String, pass1: String, pass2: String, birth_day: String, location: String) : HashMap<String, Any> {

        val Data = HashMap<String, String>()
        val Headers = HashMap<String, String>()

        Data["email"] = email //"cristobaljohn00@gmail.com"
        Data["password"] = pass1 //"123456789"
        Data["password_confirmation"] = pass2 //"123456789"
        Data["name"] = name //"123456789"
        Data["last_name"] = last_name //"123456789"
        Data["date_birth"] = birth_day //"123456789"
        Data["cp"] = location //"123456789"
        Data["legal"] = "true"//"123456789"
        Data["cupon"] = "" //"123456789"

        //llama a servidor
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)
        val response = ws.enviaPost("register", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try {
            //del string responde => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok") {

                val user = User()

                //user.access_token = obj.getString("access_token")
                //user.refresh_token = obj.getString("refresh_token")
                //user.token_type = "" //obj.getString("token_type")
                //user.expires_in = obj.getInt("expires_in")
                user.email = email
                user.name = name
                user.last_name = last_name
                user.birth_day = birth_day
                user.password = pass1
                user.location = location

                params["user"] = user
                params["msg"] = obj.getString("message")
                params["response"] = true
                return params

            } else {

                params["msg"] = obj.getString("message")
                params["response"] = false
                Log.e("entra", "al else")
                return params
            }
        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            Log.e("entra", "al catch")
            return params
        }
    }


// ===========Funcion para recuperar datos del usuario===================
    fun userGetDataTask(user: User):  HashMap<String, Any> {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        Data["token"] = user.access_token //"123456789"
        Headers["Authorization"] = user.token_type +" "+user.access_token
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaGet("profile", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos
                val userT = User()

                val userData = obj.getJSONObject("user")
                userT.name = userData.getString("name")
                userT.last_name = userData.getString("last_name")
                userT.image_path = "https://api.proventas.pro/v1/multimedia/avatar?token=${user.access_token}" //userData.getString("avatar")
                userT.birth_day = userData.getString("birth_date")
                userT.location = userData.getString("cp")
                userT.account = userData.getBoolean("isAccount")
                if(userT.account){
                    val course = userData.getJSONObject("course")
                    userT.progress = course.getInt("progress")
                    userT.course_id = course.getInt("course_id")
                    userT.episode_id = course.getInt("episode_id")
                    userT.time = course.getString("time")

                    val accountData = userData.getJSONObject("account")
                    userT.accountDescription = accountData.getString("description")
                    userT.consume_date = accountData.getString("consume_date")
                    userT.expire_date = accountData.getString("expire_date")
                    userT.nameAccount = accountData.getString("name")
                    userT.statusAccount = accountData.getString("status")

                }else{
                    userT.progress = 0
                    userT.course_id = 0
                    userT.episode_id = 0
                    userT.time = "0.00"
                }
                params["user"] = userT
                params["response"] = true
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

// ===========Funcion para recuperar videos ===================
    fun userGetVideos(user: User):  HashMap<String, Any> {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        Data["token"] = user.access_token //"123456789"
        Headers["Authorization"] = user.token_type +" "+user.access_token
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaGet("courses/view/1", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos
                val capitulos = ArrayList<Course>()

                val courseData = obj.getJSONObject("data")
                val arrayEpidsodes = courseData.getJSONArray("episodes")
                if (arrayEpidsodes.length() > 0){
                    for (i in 0 until arrayEpidsodes.length()) {
                        val item = arrayEpidsodes.getJSONObject(i)
                        val id = item.getInt("id")
                        val episode = item.getString("episode")
                        val description = item.getString("description")
                        val arrayVideos = item.getJSONArray("videos")
                        if (arrayVideos.length() > 0){
                            val arrayTempVideos = ArrayList<Videos>()
                            for (j in 0 until arrayVideos.length()) {
                                val itemVideo = arrayVideos.getJSONObject(j)
                                val type = itemVideo.getString("type")
                                val url = itemVideo.getString("url")
                                val videoTemp = Videos(type,url)
                                arrayTempVideos.add(videoTemp)
                            }

                            val course = Course(id.toString(),"name${i}.mp4","",0,"",description,episode,"",arrayTempVideos)
                            capitulos.add(course)

                        }else{
                            params["capitulos"] = capitulos
                        }
                    }

                    params["capitulos"] = capitulos

                }else{
                    params["capitulos"] = capitulos
                }

                params["response"] = true
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

// ===========Funcion para actualizar datos del usuario===================
    fun updateUserTask(name: String, mail: String, last_name: String, pass1: String, pass2:String, imagePath: String, token: String, passActual: String, location: String, birth_day: String): HashMap<String, Any>{

        val Data= HashMap<String, String>()
        val Header = HashMap<String, String>()

        Header["Authorization"] = token

        Data["name"] = name
        Data["last_name"] = last_name
        Data["email"] = mail
        Data["date_birth"] = birth_day
        Data["cp"] = location
        Data["passworda"] = passActual
        if(pass1.equals("")){
            Data["password"] = passActual
            Data["password_confirmation"] = passActual
        }else{
            Data["password"] = pass1
            Data["password_confirmation"] = pass1
        }
        //Data["email_confirmation"] = mail
        Data["token"] = token

        val ws = Server()

        val response = ws.enviaPost("profile/update", Data, Header)

        val params=HashMap<String, Any>()

        try {

            val obj= JSONObject(response)

            if (obj.getString("resp") == "ok"){

                params["msg"]= "Perfil actualizado correctamente"
                params["response"] = true
                return params
            }
            else{
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable){
            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

// ====================Funcion para recuperar contraseña==================
    fun recoveryPassword(mail: String):HashMap<String, Any>{
        val Data= HashMap<String, String>()
        val Header = HashMap<String, String>()
        Data["email"] = mail
        val ws= Server()

        val response= ws.enviaPost("password/email", Data, Header)

        val params= HashMap<String, Any>()

        try {

            val obj= JSONObject(response)

            if (obj.getString("resp") == "ok"){

                params["msg"]= "Correo enviado correctamente"
                params["response"] = true
                return params
            }
            else{
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable){
            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

    fun userCodeAccessTask(code: String): HashMap<String, Any> {

        //terminar servicio para avlidar codigo
        val params= HashMap<String, Any>()
        params["msg"]= "Correo Enviado Correctamente"
        params["response"] = true
        return params
    }

// ====================Funcion para recuperar avisos de privacidad==================
    fun aviso_terminosTask(tipo:String, accessToken: String): HashMap<String, Any> {

        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        //Data["password"] = pass //"123456789"
        Headers["Authorization"] = accessToken
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        var response: String? = ""
        if (tipo.equals("aviso")) {
            response = ws.enviaGet("content/page/2", Data, Headers)
        }else if (tipo.equals("terminos")){
            response = ws.enviaGet("content/page/1", Data, Headers)
        }else if (tipo.equals("uso")){
            response = ws.enviaGet("content/page/3", Data, Headers)
        }
        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                params["html"] = obj.getJSONObject("data").getString("content") //string json
                //params["msg"] = obj.getString("msg")
                params["response"] = true //dato boolean
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

// ====================Funcion para validar codigo postal==================
    fun userZipcodeTask(zipcode: String): HashMap<String, Any>  {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        //Data["password"] = pass //"123456789"

        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaGet("zip-code/${zipcode}?page=1", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos
                val data = obj.getJSONArray("data")
                if (data.length() == 0){
                    params["msg"] = "Código postal inválido"
                    params["response"] = false
                    return params
                }else{
                    //params["msg"] = obj.getString("msg")
                    val dataFrom0 = data.getJSONObject(0)
                    val idtemp = dataFrom0.getString("id")
                    params["id"] = idtemp //dato boolean
                    params["response"] = true //dato boolean
                    return params
                }

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

//===========================funcion para poner correos para invitar a amigos a usar la app===============================
    fun GraciasCompraTask(email1: String, email2: String, accessToken: String): HashMap<String, Any>{
        val Data = HashMap<String, Any>()
        val Headers = HashMap<String, String>()

        val mails = JSONArray()

        mails.put(email1)
        mails.put(email2)

        Data["email"] = mails

        Headers["Authorization"] = accessToken

        val ws = Server()

        val response = ws.enviaPostArray("referrals", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try {
            //del string responde => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok") {

                //params["msg"] = obj.getString("msg")
                params["response"] = true
                return params

            } else {

                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }
        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }
//==============================Función para pagar con paypal====================================================================
    fun userPagoTask(counts: Int, token: String, idpa: String): HashMap<String, Any> {
        val Data = HashMap<String, String>()
        val Headers = HashMap<String, String>()

        Data["qty"] = ""+counts

        if(counts == 1){
            Data["option"] = "false"
        }else{
            Data["option"] = "true"
        }
        Data["token"] = token
        Data["id"] = idpa

        val ws = Server()

        val response = ws.enviaPost("membership/add", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try {
            //del string responde => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok") {

                //params["msg"] = obj.getString("msg")
                /*
                val payment_method = obj.getString("payment_method")
                val urlpay = JSONObject(payment_method).get("url")
                params["response"] = true
                params["urlpay"] = urlpay
                return params
                */

                params["response"] = true
                params["idpay"] = obj.getString("id")
                return params

            } else {

                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }
        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }

    }

    fun userPagoCheckTask(token: String): HashMap<String, Any> {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        //Data["password"] = pass //"123456789"
        Headers["Authorization"] = token
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaGet("user", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos

                val memebership = obj.getString("membership")
                val memebershipData = JSONObject(memebership).getString("name")

                params["userType"] = memebershipData
                params["response"] = true
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

    fun userGetCertificado(token: String): HashMap<String, Any>  {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        Data["token"] = token //"123456789"
        Headers["Authorization"] = token
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaGet("course/certificate", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos
                val memebership = obj.getString("message")
                params["msg"] = memebership
                params["response"] = true
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

    fun updateUserImageTask(filePath: String?, s: String): HashMap<String, Any> {
        val Data= HashMap<String, String>()
        val Header = HashMap<String, String>()

        Data["token"] = s
        Data["avatar"] = filePath!!

        val ws = Server()

        val response = ws.enviaPostForm("multimedia/avatar/update", Data, Header)

        val params=HashMap<String, Any>()

        try {

            val obj= JSONObject(response)

            if (obj.getString("resp") == "ok"){

                params["msg"]= "Perfil actualizado correctamente"
                params["response"] = true
                return params
            }
            else{
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable){
            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

    fun getCuentas(token: String): HashMap<String, Any> {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        Data["token"] = token //"123456789"
        Data["page"] = "1" //"123456789"
        Headers["Authorization"] = token
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaGet("account/list", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos
                val data = obj.getString("data")
                params["data"] = data
                params["response"] = true
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

    fun getExamenes(token: String): HashMap<String, Any> {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        Data["token"] = token //"123456789"
        Data["page"] = "1" //"123456789"
        Headers["Authorization"] = token
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaGet("course/exam/1/list", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos
                val data = obj.getString("data")
                params["data"] = data
                params["response"] = true
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

    fun getOrdenes(token: String): HashMap<String, Any> {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        Data["token"] = token //"123456789"
        Data["page"] = "1" //"123456789"
        Headers["Authorization"] = token
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaGet("order/list", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos
                val data = obj.getString("data")
                params["data"] = data
                params["response"] = true
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

    fun userPagoGetId(token: String):  HashMap<String, Any> {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        Data["token"] = token //"123456789"
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaGet("membership/list", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos
                val data = obj.getJSONArray("data")

                val oject = data[0] as JSONObject

                params["idPago"] = "${oject.getInt("id")}"
                params["price"] = oject.getString("pricing").toDouble()
                params["response"] = true
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }

    //Metodo para acceder a los pagos dependiendo que tipo de pago.
    fun realizaPagoTask(token: String, method: String, idPay: String):  HashMap<String, Any> {
        val Data = HashMap<String, String>() //se almacenan los datos que se introducen en el form
        val Headers = HashMap<String, String>() //para que es esta? //llaves de autorizacion para cuando haces login, consumir servicios del usuario.

        //hashmap es un arreglo llave, valor...
        //Data["username"] = mail //"cristobaljohn00@gmail.com"
        Data["token"] = token
        Data["method"] = method
        Data["id"] = idPay//"123456789"
        //llama a servidor
        // ws es una instancia de la clase server()
        val ws = Server()
        //val response = ws.enviaPost("users/login", Data, Headers)//hace una peticion al server
        //val response es la respuesta que el servidor nos da... es una cadena json...
        val response = ws.enviaPost("membership/payment/movil", Data, Headers)

        //params: objeto para almacenar las respuestas del servidor
        val params = HashMap<String, Any>()

        try{
            //del string response => obtengo json
            val obj = JSONObject(response)

            if (obj.getString("resp") == "ok"){

                //Se obtienen los datos

                params["msg"] = obj.getString("message")
                params["url"] = obj.getString("url")
                params["response"] = true
                return params

            } else {
                params["msg"] = obj.getString("message")
                params["response"] = false
                return params
            }

        } catch (t: Throwable) {

            params["msg"] = "Error el enviar datos, intente más tarde."
            params["response"] = false
            return params
        }
    }
}