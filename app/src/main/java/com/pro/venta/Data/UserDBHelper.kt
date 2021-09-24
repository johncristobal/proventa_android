package com.pro.venta.Data

import android.content.ContentValues
import android.content.Context
import  android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.pro.venta.model.User

import java. util.ArrayList

class UserDBHelper(context: Context) : SQLiteOpenHelper( context, DATABASE_NAME, null, DATABASE_VERSION){

    override fun onCreate( db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

   override fun onUpgrade( db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
       db.execSQL(SQL_DETELE_ENTRIES)
            onCreate(db)
   }

   override fun onDowngrade( db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade( db, oldVersion, newVersion)
   }

   //@Throws(SQLiteConstrainException::class)
        fun insertUser(user: User): Boolean {
            //obtenemos el repositorio de datos en modo de escritura...
            val db = writableDatabase

       //Creamos un nuevo mapa de valores donde los nombres de las columnas son las llaves...
            val values = ContentValues()
            //values.put(DBUser.UserEntry.COLUMN_ID, user.id)
            values.put(DBUser.UserEntry.COLUMN_NAME, user.name)
            values.put(DBUser.UserEntry.COLUMM_LAST_NAME, user.last_name)
            values.put(DBUser.UserEntry.COLUMN_EMAIL, user.email)
            values.put(DBUser.UserEntry.COLUMN_AGE, user.age)
            values.put(DBUser.UserEntry.COLUMN_LOCATION, user.location)
            values.put(DBUser.UserEntry.COLUMN_PASSWORD, user.password)

       //insertamos el nuevo renglon retornando el valor de la llave primaria de la columna nueva...
            val newRowId = db.insert(DBUser.UserEntry.TABLE_NAME, null, values)
            return true
        }

    /*
        fun readUser( userid: String): ArrayList <Usuario>{
            val users = ArrayList<Usuario>()

            val db= writableDatabase
            var cursor: Cursor? = null

            try{
                cursor = db.rawQuery("select * from" + DBUser.UserEntry.TABLE_NAME + "WHERE" + DBUser.UserEntry.COLUMN_ID
                    +   "='" + userid + "'", null)
            }
            catch(e : SQLiteException){
                db.execSQL(SQL_CREATE_ENTRIES)
                return ArrayList()

            }

            var name: String
            var last_name: String
            var email:String
            var password: String
            var age: Int
            var location: Int

            if(cursor !!.moveToFirst()){
                while (cursor.isAfterLast == false){
                    name = cursor.getString(cursor.getColumnIndex(DBUser.UserEntry.COLUMN_NAME))
                    last_name = cursor.getString(cursor.getColumnIndex(DBUser.UserEntry.COLUMM_LAST_NAME))
                    email = cursor.getString(cursor.getColumnIndex(DBUser.UserEntry.COLUMN_EMAIL))
                    password = cursor.getString(cursor.getColumnIndex(DBUser.UserEntry.COLUMN_PASSWORD))
                    age = cursor.getInt(cursor.getColumnIndex(DBUser.UserEntry.COLUMN_AGE))
                    location = cursor.getInt(cursor.getColumnIndex(DBUser.UserEntry.COLUMN_LOCATION))

                    users.add(Usuario(userid, name, last_name, email, age, location, password))
                    cursor.moveToNext()
                }
            }

            return users
        }
    */

    companion object{
        //si se cambia el esque,a de la bd, puedes incrementar la version de la bd...
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "feedReader.sqlite"

        private val SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + DBUser.UserEntry.TABLE_NAME + " (" +
                                            DBUser.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                            DBUser.UserEntry.COLUMN_NAME + " TEXT NOT NULL," +
                                            DBUser.UserEntry.COLUMM_LAST_NAME + " TEXT NOT NULL," +
                                            DBUser.UserEntry.COLUMN_EMAIL + " TEXT NOT NULL," +
                                            DBUser.UserEntry.COLUMN_AGE + " INTEGER," +
                                            DBUser.UserEntry.COLUMN_LOCATION + " INTEGER," +
                                            DBUser.UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL);"

        private  val SQL_DETELE_ENTRIES = "DROP TABLE IF EXISTS "+ DBUser.UserEntry.TABLE_NAME
    }
}