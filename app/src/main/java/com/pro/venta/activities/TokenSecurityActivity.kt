package com.pro.venta.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pro.venta.R
import kotlinx.android.synthetic.main.activity_token_security.*

class TokenSecurityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token_security)

        closeButton.setOnClickListener(){
            finish()
        }

        ValidaTokenButton.setOnClickListener(){
            //metodfo que valide el Token
        }

    }

    //Aqui va el metodo que mande a llamar al web service y valide si el token existe...
}
