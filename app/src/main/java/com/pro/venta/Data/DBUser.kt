package com.pro.venta.Data

import android.provider.BaseColumns

object DBUser {
    class UserEntry : BaseColumns{
        companion object{
            val TABLE_NAME = "Usuario"

            val COLUMN_ID = "id"
            val COLUMN_NAME = "name"
            val COLUMM_LAST_NAME = "last_name"
            val COLUMN_EMAIL = "email"
            val COLUMN_AGE = "age"
            val COLUMN_LOCATION = "location"
            val COLUMN_PASSWORD = "password"

        }
    }
}