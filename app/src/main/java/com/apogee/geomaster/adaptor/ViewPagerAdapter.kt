package com.apogee.geomaster.adaptor

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter : FragmentStateAdapter {

    constructor(fragment: Fragment) : super(fragment)

    private val TAG = "ViewPagerAdapter_Adaptor"

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

    private val getTotalFragment: MutableList<Fragment> = mutableListOf()


    companion object {

        private var INSTANCE: ViewPagerAdapter? = null
        fun getInstance(fragment: Fragment): ViewPagerAdapter? {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = ViewPagerAdapter(fragment)
                    Log.i("ViewPagerAdapter_Adaptor", "getInstance: instance is Null $INSTANCE")
                    return INSTANCE
                }
                return INSTANCE
            }
        }
    }

    override fun getItemCount(): Int {
        return getTotalFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return getTotalFragment[position]
    }

    fun setFragment(fragment: Fragment) {
        getTotalFragment.add(fragment)
        Log.i("ViewPagerAdapter_Adaptor", "setFragment: $getTotalFragment")
    }

    fun removedFragment(position: Int, isRemove: Boolean) {
        getTotalFragment.removeAt(position)
        Log.i("ViewPagerAdapter_Adaptor", "removedFragment: $getTotalFragment")
    }

    fun getSize(): Int {
        Log.i("ViewPagerAdapter_Adaptor", "getSize: ${getTotalFragment.size}")
        return getTotalFragment.size
    }
}