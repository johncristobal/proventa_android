package com.pro.venta.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pro.venta.R
import com.pro.venta.fragments.AvisoFragment
import com.pro.venta.fragments.DetallesFragment
import com.pro.venta.fragments.ListCapitulosFragment
import com.pro.venta.fragments.TerminosFragment

class DetallesFragmentAdapter(private val context: Context, fm: FragmentManager, val fragment1: ListCapitulosFragment, val fragment2: DetallesFragment) :
    FragmentPagerAdapter(fm) {

    private val TAB_TITLES = arrayOf(
        "Capítulos", "Detalles"
    )



    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        return when (position){
            0 -> {
                fragment1 //ListCapitulosFragment.newInstance("","")
            }
            1 -> {
                fragment2 //DetallesFragment.newInstance("","")
            }else -> {
                fragment2 //DetallesFragment.newInstance("","")
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return (TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}