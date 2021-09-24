package com.pro.venta.model

data class User(
    var id: String = "",
    var name: String = "",
    var last_name: String = "",
    var email: String = "",
    var pseudonym: String = "",
    var gender: String = "",
    var birth_day: String = "",
    var progress: Int = 0,
    var age: String = "",
    var location: String = "",
    var phone: String = "",
    var full: Int = -1,
    var tok: String = "",
    var peso: Double = 0.0,
    var estatura: Int = -1,
    var frecuenciabasal: Int = 60,
    var token_type: String = "",
    var access_token: String = "",
    var expires_in: Int = 0,
    var refresh_token: String = "",
    var image_path: String="",
    var password: String="",
    var account: Boolean = false,
    var course_id: Int = 0,
    var episode_id: Int = 0,
    var time: String = "",
    var capitulos : ArrayList<Course> = ArrayList<Course>(),

    var accountDescription: String = "",
    var consume_date: String = "",
    var expire_date: String = "",
    var nameAccount: String = "",
    var statusAccount: String = ""

)
