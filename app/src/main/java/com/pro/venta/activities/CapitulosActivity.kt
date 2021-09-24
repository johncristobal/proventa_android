package com.pro.venta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.pro.venta.R
import com.pro.venta.adapter.DownloadAdapter
import com.pro.venta.model.Course
import kotlinx.android.synthetic.main.activity_capitulos.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class CapitulosActivity : AppCompatActivity() {

    var adapterCapitulosDescargados: DownloadAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capitulos)

        val prefs = getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val jsonData = prefs.getString("ids","")
        if (jsonData!!.equals("")){
            //no hay datos guardados, lista vacia

        }else{
            //recuperamos lista y agrego el nuevo id
            val gson = Gson()
            val descargadosTemp = gson.fromJson(jsonData,Array<Course>::class.java).toList()
            val descargados = ArrayList(descargadosTemp)

            adapterCapitulosDescargados = DownloadAdapter(this,descargados,{ course, pos ->

                //reproducir capitulo
                Log.w("launch","cargando video...")
                val int = Intent(this,PlayVideoDownActivity::class.java)
                int.putExtra("name",course.name)
                startActivity(int)

            },{ course, pos ->
                //eliminar capitulo -- logic
                alert("¿Desea eliminar el capitulo?") {
                    title = ""
                    positiveButton("Si"){
                        descargados.remove(course)
                        val json = gson.toJson(descargados)
                        prefs.edit().putString("ids",json).apply()

                        adapterCapitulosDescargados!!.notifyDataSetChanged()
                    }
                    negativeButton("No") { }
                }.show()
            })

            capitulosCursoDown.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
            capitulosCursoDown.adapter = adapterCapitulosDescargados

        }

        ReturnCVButton.setOnClickListener{
            finish()
        }
    }
}
