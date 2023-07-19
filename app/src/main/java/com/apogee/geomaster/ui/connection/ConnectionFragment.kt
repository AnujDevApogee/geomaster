package com.apogee.geomaster.ui.connection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ViewPagerAdapter
import com.apogee.geomaster.databinding.ConnectionLayoutFragmentBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.connection.internet.InternetFragment
import com.apogee.geomaster.ui.connection.radio.RadioFragment
import com.apogee.geomaster.ui.connection.wifi.WifiFragment
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialFadeThrough

class ConnectionFragment : Fragment(R.layout.connection_layout_fragment) {

    private lateinit var binding: ConnectionLayoutFragmentBinding
    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val tabArr by lazy {
        listOf(
            resources.getString(R.string.rtk_by_radio),
            resources.getString(R.string.rtk_by_internet),
            resources.getString(R.string.rtk_by_wifIn)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 1000
        }

        enterTransition = fadeThrough
        reenterTransition = fadeThrough
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ConnectionLayoutFragmentBinding.bind(view)
        displayActionBar(
            getString(R.string.corr_txt),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        (activity as HomeScreen?)?.hideActionBar()
        setupViewPagerAdaptor()
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, pos ->
            tab.text = tabArr[pos]
        }.attach()


    }

    fun goToCreateConnection() {
        findNavController().safeNavigate(
            ConnectionFragmentDirections
                .actionConnectionFragmentToCreateConnectionFragment()
        )
    }

    private fun setupViewPagerAdaptor() {
        viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.setFragment(RadioFragment())
        viewPagerAdapter.setFragment(InternetFragment())
        viewPagerAdapter.setFragment(WifiFragment())
    }
}