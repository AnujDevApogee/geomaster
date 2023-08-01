package com.apogee.geomaster.ui.projects.createproject.projection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.AddProjectionParamsBinding

class AddProjectionParamsFragment: Fragment(R.layout.add_projection_params) {
    private lateinit var binding:AddProjectionParamsBinding
    var list:ArrayList<String> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= AddProjectionParamsBinding.bind(view)
    }
}