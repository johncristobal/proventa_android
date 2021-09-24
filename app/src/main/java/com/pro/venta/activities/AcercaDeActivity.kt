package com.pro.venta.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pro.venta.R
import kotlinx.android.synthetic.main.activity_acerca_de.*

class AcercaDeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acerca_de)

        backAcerca.setOnClickListener { finish() }
    }
}
