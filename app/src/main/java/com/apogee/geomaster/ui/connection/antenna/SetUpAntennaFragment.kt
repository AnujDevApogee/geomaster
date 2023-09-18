package com.apogee.geomaster.ui.connection.antenna

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.SetUpAntennaLayoutBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.base.BaseProfileFragment
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.showMessage
import com.apogee.updatedblelibrary.Utils.checkString

class SetUpAntennaFragment : Fragment(R.layout.set_up_antenna_layout) {

    private lateinit var binding: SetUpAntennaLayoutBinding

    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }

    companion object{
        // delete this information
        var measuredHeight=-1
        var model=""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SetUpAntennaLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.setup_antenna_source),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        (activity as HomeScreen?)?.hideActionBar()
        binding.modelEd.setText(BaseProfileFragment.DeviceName)

        val coordinateAdaptor: ArrayAdapter<String> = ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.select_dialog_item,
            resources.getStringArray(R.array.measure_point)
        )

        binding.measurePointsComplete.setText(resources.getStringArray(R.array.measure_point).first())
        binding.measurePointsComplete.setAdapter(coordinateAdaptor)




        binding.doneBtn.setOnClickListener {
            val measuredHeight = binding.measuredHeight.text.toString()
            val model = binding.modelEd.text.toString()
            val measured = binding.measurePointsComplete.text.toString()

            if (checkString(measuredHeight)) {
                showMessage("Invalid Measured Height")
                return@setOnClickListener
            }

            if (measuredHeight.toInt() <= 0) {
                showMessage("Measured Height Count be Zero")
                return@setOnClickListener
            }

            if (checkString(model)) {
                showMessage("Add the Model Type")
                return@setOnClickListener
            }

            if (checkString(measured)) {
                showMessage("Invalid Measured Point")
                return@setOnClickListener
            }
            SetUpAntennaFragment.measuredHeight=measuredHeight.toInt()
            SetUpAntennaFragment.model=model

            findNavController().popBackStack()

        }

    }


}