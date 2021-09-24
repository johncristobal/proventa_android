package com.pro.venta.ws.server

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Security {

    /**
     *
     */
    private val llave = "Au34y#gjhFS35W#&"
    /**
     *
     */
    private val iv = "fnF354#fft#d4jRw"
    /**
     *
     */
    private val CIPHER_NAME = "AES/CBC/PKCS5PADDING"
    /**
     *
     */
    private val CIPHER_KEY_LEN = 16 //128 bits

    fun encrypt(data: String): String {

        try {
            val ivSpec = IvParameterSpec(this.iv.toByteArray(charset("UTF-8")))
            val secretKey = SecretKeySpec(fixKey(this.llave).toByteArray(charset("UTF-8")), "AES")

            val cipher = Cipher.getInstance(this.CIPHER_NAME)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

            val encryptedData = cipher.doFinal(data.toByteArray())

            val encryptedDataInBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT)
            val ivInBase64 = Base64.encodeToString(this.iv.toByteArray(charset("UTF-8")), Base64.DEFAULT)

            return encryptedDataInBase64

        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }

    }

    private fun fixKey(key: String): String {
        var key = key

        if (key.length < this.CIPHER_KEY_LEN) {
            val numPad = this.CIPHER_KEY_LEN - key.length

            for (i in 0 until numPad) {
                key += "0" //0 pad to len 16 bytes
            }

            return key

        }

        return if (key.length > this.CIPHER_KEY_LEN) {
            key.substring(0, this.CIPHER_KEY_LEN) //truncate to 16 bytes
        } else key

    }

}