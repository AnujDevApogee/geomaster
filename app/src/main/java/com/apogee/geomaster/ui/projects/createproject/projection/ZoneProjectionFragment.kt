package com.apogee.geomaster.ui.projects.createproject.projection

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apogee.geomaster.R
import com.apogee.geomaster.adapter.ZoneNameAdapter
import com.apogee.geomaster.databinding.CreateProjectsFragmentBinding
import com.apogee.geomaster.databinding.FragmentZoneProjectionBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.projects.createproject.CreateProjectFragmentDirections
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getEmojiByUnicode
import com.apogee.geomaster.utils.safeNavigate


class ZoneProjectionFragment : Fragment(R.layout.fragment_zone_projection),
    ZoneNameAdapter.OnItemClickListner {
    private lateinit var binding: FragmentZoneProjectionBinding
    var list: ArrayList<String> = ArrayList()
    private lateinit var dbControl: DatabaseRepsoitory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbControl = DatabaseRepsoitory(this.requireContext())
        binding = FragmentZoneProjectionBinding.bind(view)
        binding.rcProjectionParams.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)

        displayActionBar(
            "Create Project ${getEmojiByUnicode(0x1F4DD)}",
            binding.actionLayout,
            -1,
            menuCallback
        )

        val prjectionType = 2
        list = dbControl.getprojectionParamData(prjectionType) as ArrayList<String>
        Log.d("ZoneProjectionActivity", "onCreate: $list")
        var adapter = ZoneNameAdapter(this.requireContext(), list, this)
        binding.rcProjectionParams.adapter = adapter


        binding.newParams.setOnClickListener {
            findNavController().safeNavigate(R.id.action_zoneProjection_to_addProjectionParamsFragment)
        }

    }

    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }

    override fun onResume() {
        super.onResume()
        val prjectionType = 2
        list = dbControl.getprojectionParamData(prjectionType) as ArrayList<String>
        Log.d("ZoneProjectionActivity", "onCreate: $list")
        var adapter = ZoneNameAdapter(this.requireContext(), list, this)
        binding.rcProjectionParams.adapter = adapter
    }

    override fun onItemClick(name: String) {
        findNavController().safeNavigate(R.id.action_zoneProjection_to_createProjectFragment)
    }


}