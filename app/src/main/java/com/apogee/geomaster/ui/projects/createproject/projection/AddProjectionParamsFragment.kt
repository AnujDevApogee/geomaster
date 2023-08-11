package com.apogee.geomaster.ui.projects.createproject.projection

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.AddProjectionParamsBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.toastMsg

class AddProjectionParamsFragment: Fragment(R.layout.add_projection_params) {
    private lateinit var binding:AddProjectionParamsBinding
    var list:ArrayList<String> = ArrayList()
    var zoneName = ""
    private lateinit var dbControl: DatabaseRepsoitory



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= AddProjectionParamsBinding.bind(view)
        dbControl = DatabaseRepsoitory(this.requireContext())


        binding.lccButton.setOnClickListener {

                try {
                    zoneName = binding.etLccZone.text.toString()
                    Log.d("TAG", "onViewCreated: paramValues $zoneName")
                    if (validateValues()!!) {
                        val centralLatitude =
                            binding.degOgLat.text.toString()
                                .toDouble() + binding.minsOgLat.text.toString()
                                .toDouble() / 60 + binding.secOgLat.text.toString()
                                .toDouble() / 3600
                        val centralLongitude =
                            binding.degOgLng.text.toString()
                                .toDouble() + binding.minOgLng.text.toString()
                                .toDouble() / 60 + binding.secOgLng.text.toString()
                                .toDouble() / 3600
                        val falseEasting = binding.lccFalseEasting.text.toString()
                        val falseNorthing = binding.lccFalseNorthing.text.toString()
                        val standardParallel1 =
                            binding.degPar1.text.toString()
                                .toDouble() + binding.minPar1.text.toString()
                                .toDouble() / 60 + binding.secPar1.text.toString().toDouble() / 3600
                        val standardParallel2 =
                            binding.degPar2.text.toString()
                                .toDouble() + binding.minPar2.text.toString()
                                .toDouble() / 60 + binding.secPar2.text.toString().toDouble() / 3600
                        if (centralLatitude < 90 && centralLatitude > -90 && centralLongitude < 180 &&
                            centralLongitude > -180 && standardParallel1 < 90
                            && standardParallel1 > -90
                            && standardParallel2 < 90 && standardParallel2 > -90) {
                            var central_latitudeLcc =
                                centralLatitude
                            var central_longitudeLcc =
                                centralLongitude
                            var false_eastingLcc =
                                falseEasting.toDouble()
                            var false_northingLcc =
                                falseNorthing.toDouble()
                            var standard_parallel_1Lcc =
                                standardParallel1
                            var standard_parallel_2Lcc =
                                standardParallel2
                            Log.d(
                                "TAG",
                                "lambertConformalConic2Parallel: " + central_latitudeLcc +"--"
                                       +central_longitudeLcc + "--"+false_eastingLcc + false_northingLcc + "--"+ standard_parallel_1Lcc + "--"+
                                       standard_parallel_2Lcc
                            )
                            val paramValues: String = zoneName + "," + central_latitudeLcc + "," + central_longitudeLcc + "," + false_eastingLcc + "," +
                                    false_northingLcc + "," + standard_parallel_1Lcc + "," + standard_parallel_2Lcc+","+2
                            Log.d("TAG", "onViewCreated: paramValues $paramValues")
                            val result=dbControl.insertProjectionPrameters(paramValues)
                            Log.d("TAG", "onViewCreated: $result")
                            if(result.equals("Data inserted successfully")){
                                findNavController().safeNavigate(R.id.action_addProjectionParamsFragment_to_zoneProjection)
                            }else{
                                activity?.toastMsg("failed To insert Data")
                            }

                        } else {
                            activity?.toastMsg("Invalid Input")
                        }
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "lambertConformalConic2Parallel: " + e.message)
                }

        }
    }

    fun  DDtoDMS(value:String):String{
        var degrees=0.0
        degrees=value.split(".")[0].toDouble()

        val temp_min = ("." + value.split(".")[1]).toDouble() * 60
        val min = temp_min.toString().split(".")[0].toDouble()
        val sec = (("." + temp_min.toString().split(".")[1]).toDouble() * 60)

        Log.d("Utils", "DDtoDMS:-- $degrees'$min'$sec")
        return "$degrees'$min'$sec"
    }

    fun validateValues(): Boolean? {
        var validate = true
        if (TextUtils.isEmpty(binding.etLccZone.text.toString().trim())) {
            binding.etLccZone.requestFocus()
            binding.etLccZone.error = "Enter a Zone Name"
            validate = false
        } else if (TextUtils.isEmpty(binding.degOgLat.text.toString().trim()) ||
            TextUtils.isEmpty(binding.minsOgLat.text.toString().trim()) ||
            TextUtils.isEmpty(binding.secOgLat.text.toString().trim())
        ) {
            binding.etLccZone.error = null
            binding.secOgLat.requestFocus()
            binding.secOgLat.error = "Enter a valid origin lat"
            validate = false
        } else if (TextUtils.isEmpty(binding.degOgLng.text.toString().trim()) ||
            TextUtils.isEmpty(binding.minOgLng.text.toString().trim()) ||
            TextUtils.isEmpty(binding.secOgLng.text.toString().trim())
        ) {
            binding.secOgLat.error = null
            binding.secOgLng.requestFocus()
            binding.secOgLng.error = "Enter a valid origin long"
            validate = false
        } else if (TextUtils.isEmpty(binding.degPar1.text.toString().trim()) ||
            TextUtils.isEmpty(binding.minPar1.text.toString().trim()) ||
            TextUtils.isEmpty(binding.secPar1.text.toString().trim())
        ) {
            binding.secOgLng.error = null
            binding.secPar1.requestFocus()
            binding.secPar1.error = "Enter a valid parallel-1"
            validate = false
        } else if (TextUtils.isEmpty(binding.degPar2.text.toString().trim()) ||
            TextUtils.isEmpty(binding.minPar2.text.toString().trim()) ||
            TextUtils.isEmpty(binding.secPar2.text.toString().trim())
        ) {
            binding.secPar1.error = null
            binding.secPar2.requestFocus()
            binding.secPar2.error = "Enter a valid parallel-2"
            validate = false
        } else if (TextUtils.isEmpty(binding.lccFalseEasting.text.toString().trim())) {
            binding.secPar2.error = null
            binding.lccFalseEasting.requestFocus()
            binding.lccFalseEasting.error = "Enter a valid false easting"
        } else if (TextUtils.isEmpty(binding.lccFalseNorthing.text.toString().trim())) {
            binding.lccFalseEasting.error = null
            binding.lccFalseNorthing.requestFocus()
            binding.lccFalseNorthing.error = "Enter a valid false northing"
        } else {
            binding.etLccZone.error = null
            binding.secOgLat.error = null
            binding.secOgLng.error = null
            binding.secPar1.error = null
            binding.secPar2.error = null
            binding.lccFalseEasting.error = null
            binding.lccFalseNorthing.error = null
            validate = true
        }
        return validate
    }


}