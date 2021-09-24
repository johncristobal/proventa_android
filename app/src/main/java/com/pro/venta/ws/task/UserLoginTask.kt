package com.pro.venta.ws.task

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.util.HashMap

class UserLoginTask(var context: Context, var callback: CallBack) : AsyncTask<Void, Void, Void>() {

    /**
     *
     */
    internal val TAG = "UserLoginTask"
    /**
     *
     */
    //internal lateinit var pd: progressDialog //("This a progress dialog")
    //internal lateinit var dialog: progressDialog //(message = "Please wait a bit…", title = "Fetching data")
    /**
     *
     */
    /**
     *
     */
    private lateinit var resultMap: Map<String, Any>

    init {
        resultMap = HashMap()
    }

    /*fun UserLoginTask(context: Context, callback: CallBack) {
        this.context = context
        this.callback = callback
        resultMap = HashMap()
    }*/

    override fun onPreExecute() {
        super.onPreExecute()

        Log.e(TAG,"preexecuted")
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        Log.e(TAG,"back")

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)

        Log.e(TAG,"onpost")

        val params = HashMap<String, Any>()
        this.callback.callback(true, params)
    }
}