package com.pro.venta.model

data class Questions(
    var id: Int = 0,
    var question: String = "",
    var selected: Int = 0,
    var options:  ArrayList<Opciones> = ArrayList()

)
