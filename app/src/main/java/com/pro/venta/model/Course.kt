package com.pro.venta.model

data class Course (
    var id: String = "",
    var name: String = "",
    var url: String = "",
    var download: Int = 0,
    var duration: String = "",
    var description: String = "",
    var nameTemp: String = "",
    var size : String = "",
    var videos : ArrayList<Videos> = ArrayList<Videos>()
)
