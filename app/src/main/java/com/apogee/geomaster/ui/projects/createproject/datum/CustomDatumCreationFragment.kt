package com.apogee.geomaster.ui.projects.createproject.datum

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragementCustumDatumDataBinding

class CustomDatumCreationFragment : Fragment(R.layout.fragement_custum_datum_data) {

    private lateinit var binding:FragementCustumDatumDataBinding
    var switchParams = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragementCustumDatumDataBinding.bind(view)



binding.moreParams.setOnClickListener {
    if (!switchParams) {
        binding.addParams.setVisibility(View.GONE)
        binding.moreParams.setText("Show More")
        switchParams = true
    } else {
        binding.addParams.setVisibility(View.VISIBLE)
        binding.moreParams.setText("Show Less")
        switchParams = false
    }
}

    }
}