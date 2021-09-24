package com.pro.venta.ws.server

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONArray
import org.json.JSONException
import java.io.*
import java.net.URL
import java.net.URLEncoder
import java.util.HashMap
import javax.net.ssl.HttpsURLConnection
import org.json.JSONObject
import java.net.HttpURLConnection


public class Server {

    /**
     *
     */
    private val endpoint = "https://api.proventas.pro/v1/"
    /**
     *
     */
    private val version = "v1.0"
    /**
     *
     */
    private val channel = "app"

    private var cadenaHtml = ""


    fun enviaPost(uri: String, params: HashMap<String, String>, headers: HashMap<String, String>): String? {
        try {
            //val urlObj = URL(this.endpoint + this.version + "/" + this.channel + "/" + uri)
            val urlObj = URL(this.endpoint  + uri)
            val conn = urlObj.openConnection() as HttpsURLConnection
            conn.requestMethod = "POST"
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Type", "application/json")
            val securitys = SecurityData()
            securitys.SecurityData()
            conn.setRequestProperty("aptok", securitys.getTokenApp())
            conn.setRequestProperty("apv", securitys.getTokenVersion())
            for (key in headers.keys) {
                conn.setRequestProperty(key, headers[key])
            }
            conn.doOutput = true

            val sbParams = this.preParamasJSON(params)
            val paramsString = sbParams!!.toString()

            val wr = BufferedWriter(OutputStreamWriter(conn.outputStream,"UTF-8"))
            wr.write(paramsString)
            //val wr = DataOutputStream(conn.outputStream)
            //wr.writeBytes(URLEncoder.encode(paramsString,"UTF-8"))
            wr.flush()
            wr.close()

            val code = conn.responseCode
            if (code == HttpURLConnection.HTTP_OK){
                val inputData = BufferedInputStream(conn.inputStream)
                val reader = BufferedReader(InputStreamReader(inputData))
                val result = StringBuilder()
                var line: String?
                line = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }

                conn?.disconnect()

                try {
                    val obj = JSONObject(result.toString())
                    obj.put("resp", "ok")
                    return obj.toString()
                }catch(e: java.lang.Exception){
                    val obj = JSONObject()
                    obj.put("resp","ok")
                    return obj.toString()
                }
            }else{
                //val inputData = BufferedInputStream(conn.inputStream)
                //val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val reader = BufferedReader(InputStreamReader(conn.errorStream))
                val result = StringBuilder()
                var line: String?
                line = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }

                conn?.disconnect()

                val obj = JSONObject(result.toString())
                obj.put("resp","error")
                return obj.toString()
            }

        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return e.toString()
        }
    }

    fun enviaPostArray(uri: String, params: HashMap<String, Any>, headers: HashMap<String, String>): String? {
        try {
            //val urlObj = URL(this.endpoint + this.version + "/" + this.channel + "/" + uri)
            val urlObj = URL(this.endpoint  + uri)
            val conn = urlObj.openConnection() as HttpsURLConnection
            conn.requestMethod = "POST"
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Type", "application/json")
            val securitys = SecurityData()
            securitys.SecurityData()
            conn.setRequestProperty("aptok", securitys.getTokenApp())
            conn.setRequestProperty("apv", securitys.getTokenVersion())
            for (key in headers.keys) {
                conn.setRequestProperty(key, headers[key])
            }
            conn.doOutput = true

            val sbParams = this.preParamasJSONArray(params)
            val paramsString = sbParams!!.toString()

            val wr = BufferedWriter(OutputStreamWriter(conn.outputStream,"UTF-8"))
            wr.write(paramsString)
            //val wr = DataOutputStream(conn.outputStream)
            //wr.writeBytes(URLEncoder.encode(paramsString,"UTF-8"))
            wr.flush()
            wr.close()

            val code = conn.responseCode
            if (code == HttpURLConnection.HTTP_OK){
                val inputData = BufferedInputStream(conn.inputStream)
                val reader = BufferedReader(InputStreamReader(inputData))
                val result = StringBuilder()
                var line: String?
                line = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }

                conn?.disconnect()

                try {
                    val obj = JSONObject(result.toString())
                    obj.put("resp", "ok")
                    return obj.toString()
                }catch(e: java.lang.Exception){
                    val obj = JSONObject()
                    obj.put("resp","ok")
                    return obj.toString()
                }
            }else{
                //val inputData = BufferedInputStream(conn.inputStream)
                //val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val reader = BufferedReader(InputStreamReader(conn.errorStream))
                val result = StringBuilder()
                var line: String?
                line = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }

                conn?.disconnect()

                val obj = JSONObject(result.toString())
                obj.put("resp","error")
                return obj.toString()
            }

        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return e.toString()
        }
    }

    private fun preParamas(params: HashMap<String, String>): StringBuilder? {
        var sbParams: StringBuilder? = StringBuilder()
        var i = 0
        for (key in params.keys) {
            try {
                if (i != 0) {
                    sbParams!!.append("&")
                }
                sbParams!!.append(key).append("=")
                    .append(URLEncoder.encode(params[key], "UTF-8"))

            } catch (e: UnsupportedEncodingException) {
                sbParams = null
            }

            i++
        }
        return sbParams
    }

    private fun preParamasJSON(params: HashMap<String, String>): String? {
        var sbParams: StringBuilder? = StringBuilder()
        val data = JSONObject()
        //data.put("email", "25");

        var i = 0
        for (key in params.keys) {
            try {
                if (i != 0) {
                    sbParams!!.append("&")
                }
                data.put(key,params[key])

                sbParams!!.append(key).append("=")
                    .append(URLEncoder.encode(params[key], "UTF-8"))

            } catch (e: UnsupportedEncodingException) {
                sbParams = null
            }

            i++
        }
        return data.toString()
    }

    private fun preParamasJSONArray(params: HashMap<String, Any>): String? {
        var sbParams: StringBuilder? = StringBuilder()
        val data = JSONObject()
        //data.put("email", "25");

        var i = 0
        for (key in params.keys) {
            try {
                if (i != 0) {
                    sbParams!!.append("&")
                }
                data.put(key,params[key])

                //sbParams!!.append(key).append("=").append(URLEncoder.encode(params[key], "UTF-8"))

            } catch (e: UnsupportedEncodingException) {
                sbParams = null
            }

            i++
        }
        return data.toString()
    }

    fun enviaGet(uri: String, params: HashMap<String, String>, headers: HashMap<String, String>): String? {
        try {
            //val stringBuilder = StringBuilder(this.endpoint + this.version + "/" + this.channel + "/" + uri)
            val stringBuilder = StringBuilder(this.endpoint + uri)
            stringBuilder.append("?")
            for (key in params.keys) {
                stringBuilder.append("$key=")
                stringBuilder.append(URLEncoder.encode(params[key], "UTF-8"))
                stringBuilder.append("&")
            }
            val urlObj = URL(stringBuilder.toString())
            val conn = urlObj.openConnection() as HttpsURLConnection
            conn.requestMethod = "GET"
            val securitys = SecurityData()
            conn.setRequestProperty("aptok", securitys.getTokenApp())
            conn.setRequestProperty("apv", securitys.getTokenVersion())
            for (key in headers.keys) {
                conn.setRequestProperty(key, headers[key])
            }
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK){
                val inputData = BufferedInputStream(conn.inputStream)
                val reader = BufferedReader(InputStreamReader(inputData))
                val result = StringBuilder()
                var line: String?
                line = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }

                conn?.disconnect()
                val obj = JSONObject(result.toString())
                obj.put("resp","ok")
                return obj.toString()
            }else{
                val inputData = BufferedInputStream(conn.errorStream)
                val reader = BufferedReader(InputStreamReader(inputData))
                val result = StringBuilder()
                var line: String?
                line = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }

                conn?.disconnect()
                val obj = JSONObject(result.toString())
                obj.put("resp","ok")
                return obj.toString()
            }

            /*cadenaHtml = result.toString()

            if (cadenaHtml.contains("<p>")){
                val obj = JSONObject()
                obj.put("resp","ok")
                obj.put("html",result.toString())
                return obj.toString()
            }else{
                val obj = JSONObject(result.toString())
                obj.put("resp","ok")
                return obj.toString()
            }*/
            //return result.toString()

        } catch (e: IOException) {
            return null
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun enviaPostForm(uri: String, params: HashMap<String, String>, headers: HashMap<String, String>): String? {
        var inData: InputStream? = null
        val twoHyphens = "--"
        val boundary = "*****" + java.lang.Long.toString(System.currentTimeMillis()) + "*****"
        val lineEnd = "\r\n"
        var outputStream: DataOutputStream? = null

        val bytesRead: Int
        val bytesAvailable: Int
        val bufferSize: Int
        val buffer: ByteArray
        val maxBufferSize = 1 * 1024 * 1024

        val o = JSONObject()
        try {
            val imageName = params["avatar"]
            val options = BitmapFactory.Options()
            options.inSampleSize = 8
            val img = BitmapFactory.decodeFile(imageName,options)
            val stream = ByteArrayOutputStream()
            img.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()

            val url = URL(this.endpoint + uri)
            val urlConnection = url.openConnection() as HttpURLConnection
            //urlConnection.setReadTimeout(0);
            //urlConnection.setConnectTimeout(0);
            urlConnection.requestMethod = "POST"
            urlConnection.setRequestProperty("Connection", "Keep-Alive")
            urlConnection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0")
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            urlConnection.setRequestProperty("Authorization", headers["Authorization"])
            urlConnection.doInput = true
            urlConnection.doOutput = true
            urlConnection.useCaches = false
            //urlConnection.setRequestProperty("Content-Type", "application/json");
            //urlConnection.setRequestProperty("Accept","application/json");

            outputStream = DataOutputStream(urlConnection.outputStream)
            outputStream.writeBytes(twoHyphens + boundary + lineEnd)
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"avatar.jpg\"$lineEnd")
            outputStream.writeBytes("Content-Type: image/jpg$lineEnd")
            outputStream.writeBytes("Content-Transfer-Encoding: binary$lineEnd")
            outputStream.writeBytes(lineEnd)
            outputStream.write(byteArray)
            outputStream.writeBytes(lineEnd)

            /*
            val parmas = HashMap<String, String>()
            parmas["Type"] = filename
            parmas["PolicyId"] = filename2
            parmas["PolicyFolio"] = filename3
            parmas["AppOdometer"] = appOdo
            parmas["Lat"] = lat
            parmas["Lng"] = lon
            parmas["AppZipCode"] = cp
            parmas["InsuranceId"] =
                String.valueOf(IinfoClient.getInfoClientObject().getPolicies().getInsuranceCarrier().getId())
            */
            // Upload POST Data

            val keys = params.keys.iterator()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = params[key]

                outputStream.writeBytes(twoHyphens + boundary + lineEnd)
                outputStream.writeBytes("Content-Disposition: form-data; name=\"$key\"$lineEnd")
                outputStream.writeBytes("Content-Type: text/plain$lineEnd")
                outputStream.writeBytes(lineEnd)
                outputStream.writeBytes(value)
                outputStream.writeBytes(lineEnd)
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)

            urlConnection.connect()
            val statusCode = urlConnection.responseCode
            if (statusCode != 200) {
                var jsonstring = ""
                //Log.d(TAG, "The response is: " + statusCode);
                inData = urlConnection.errorStream
                //jsonstring = getStringFromInputStream(inData)
                val reader = BufferedReader(InputStreamReader(urlConnection.errorStream))
                val result = StringBuilder()
                var line: String?
                line = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }

                urlConnection?.disconnect()

                val obj = JSONObject(result.toString())
                obj.put("resp","error")
                return obj.toString()

                /*try {
                    val error = JSONObject(jsonstring)
                    throw IOException(error.getString("Message"))
                } catch (ex: JSONException) {
                    throw IOException("Error al Convertir Respuesta de Error")
                }*/

            } else {

                val inputData = BufferedInputStream(urlConnection.inputStream)
                val reader = BufferedReader(InputStreamReader(inputData))
                val result = StringBuilder()
                var line: String?
                line = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }

                urlConnection?.disconnect()

                val obj = JSONObject(result.toString())
                obj.put("resp","ok")
                return obj.toString()

            }
        } catch (e: Exception) {
            e.printStackTrace()

            throw IOException("Error al subir la fotografía. Intente más tarde.")
        } finally {
            inData?.close()
        }
    }

}