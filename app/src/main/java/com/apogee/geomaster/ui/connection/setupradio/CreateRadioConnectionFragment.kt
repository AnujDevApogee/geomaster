package com.apogee.geomaster.ui.connection.setupradio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateRadioConnLayoutFragmentBinding
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.google.android.material.transition.MaterialFadeThrough

class CreateRadioConnectionFragment : Fragment(R.layout.create_radio_conn_layout_fragment) {

    private lateinit var binding: CreateRadioConnLayoutFragmentBinding

    private val args: CreateRadioConnectionFragmentArgs by navArgs()

    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
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
        binding = CreateRadioConnLayoutFragmentBinding.bind(view)
        displayActionBar(
            getString(R.string.radio_comm),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )




    }


}