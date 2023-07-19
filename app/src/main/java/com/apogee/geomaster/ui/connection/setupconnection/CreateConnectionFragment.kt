package com.apogee.geomaster.ui.connection.setupconnection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateConnectionLayoutBinding
import com.apogee.geomaster.databinding.CreateProjectsFragmentBinding
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.google.android.material.transition.MaterialFadeThrough

class CreateConnectionFragment : Fragment(R.layout.create_connection_layout) {

    private lateinit var binding: CreateConnectionLayoutBinding

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
        binding = CreateConnectionLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.new_corr_source),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )


    }


}