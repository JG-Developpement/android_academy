package com.jgdeveloppement.android_academie.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class TabAdapter(fm: FragmentManager, private var totalTabs: Int) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AndroidFragment()
            }
            1 -> {
                KotlinFragment()
            }
            2 -> {
                NewsFragment()
            }
            else -> AndroidFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}