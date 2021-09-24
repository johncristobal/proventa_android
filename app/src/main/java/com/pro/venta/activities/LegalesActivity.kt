package com.pro.venta.activities

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.pro.venta.R
import com.pro.venta.adapter.SectionsPagerAdapter

import kotlinx.android.synthetic.main.activity_legales.*

class LegalesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legales)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        //var toolbar: Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(toolbar)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)

        //setupViewPager(viewPager)
        //val tabs: TabLayout = findViewById(R.id.tabs)

        //hacer finish y automaticamente se regresa a la pantalla anterior...
        ReturnButton.setOnClickListener{
            finish()
        }
    }

    /*private fun setupViewPager(viewPager: ViewPager){
        var adapter: ViewPagerAdapter =ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(TerminosFragment(),"Términos y condiciones")
        adapter.addFragment(AvisoFragment(), "Aviso de privacidad")
        viewPager.adapter = adapter
    }*/

}