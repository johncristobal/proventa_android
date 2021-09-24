package com.pro.venta.ws.server

import java.text.SimpleDateFormat
import java.util.*

class SecurityData {

    /**
     *
     */
    private var app = "#Rook19M61#"
    /**
     *
     */
    private var version = "1.0.0"
    /**
     *
     */
    private var fecha: String? = null
    /**
     *
     */
    private var tokenApp: String? = null
    /**
     *
     */
    private var tokenVersion: String? = null

    /**
     *
     */

    fun SecurityData() {
        val fechaT = Date()
        this.fecha = SimpleDateFormat("dd-yyyy-MM").format(fechaT)
        val securitys = Security()
        this.tokenApp = securitys.encrypt(this.app + "-" + this.fecha)
        this.tokenVersion = securitys.encrypt(this.version + "-" + this.fecha)
    }

    fun getApp(): String {
        return app
    }

    fun setApp(app: String) {
        this.app = app
    }

    fun getVersion(): String {
        return version
    }

    fun setVersion(version: String) {
        this.version = version
    }

    fun getFecha(): String? {
        return fecha
    }

    fun setFecha(fecha: String) {
        this.fecha = fecha
    }

    fun getTokenApp(): String? {
        return tokenApp
    }

    fun setTokenApp(tokenApp: String) {
        this.tokenApp = tokenApp
    }

    fun getTokenVersion(): String? {
        return tokenVersion
    }

    fun setTokenVersion(tokenVersion: String) {
        this.tokenVersion = tokenVersion
    }
}