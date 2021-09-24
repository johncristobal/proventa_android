package com.pro.venta.ws.task

interface CallBack {


    fun callback(responseCode: Boolean, resultMap: Map<String, Any>)

}