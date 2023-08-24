package com.apogee.geomaster.ui.configuration.addconfig

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateConfigurationFragmentBinding
import com.apogee.geomaster.databinding.CreateProjectsFragmentBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setHtmlBoldTxt
import com.apogee.geomaster.utils.setHtmlTxt
import com.google.android.material.transition.MaterialFadeThrough

class CreateConfigurationFragment : Fragment(R.layout.create_configuration_fragment) {

    private lateinit var binding: CreateConfigurationFragmentBinding
    var zoneData: ArrayList<String> = ArrayList()
    var zoneDataID: String = ""
    var datumId: String = ""
    var distanceUnitId: String = ""
    var angleUnitId: String = ""
    var elevationTypeId: String = ""
    var zoneHemis: ArrayList<String> = ArrayList()
    private lateinit var dbControl: DatabaseRepsoitory
    var idList: HashMap<String, String> = HashMap<String, String>()
    var prjDataList: ArrayList<String> = ArrayList()



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 1000
        }

        enterTransition = fadeThrough
        reenterTransition = fadeThrough


    }
    fun addAdapters() {
        val zoneDataView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                zoneData
            )
        binding.zoneData.threshold = 1
        binding.zoneData.setAdapter(zoneDataView)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateConfigurationFragmentBinding.bind(view)
        dbControl = DatabaseRepsoitory(this.requireContext())

        displayActionBar("Survey Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()
        zoneData = dbControl.getZoneData() as ArrayList<String>
        zoneHemis = dbControl.getZoneHemisphereData() as ArrayList<String>
        datumId = dbControl.getDatumId("WGS84")
        idList.put("datumName", datumId)
        distanceUnitId = dbControl.getDistanceUnitID("meters")
        idList.put("distanceUnit", distanceUnitId)
        angleUnitId = dbControl.angleUnitID("DD")
        idList.put("angleUnit", angleUnitId)
        elevationTypeId = dbControl.getelevationTypeID("Ellipsoid Height")
        idList.put("elevation", elevationTypeId)
        addAdapters()




        binding.projectDetailInfo.apply {
            text = setHtmlBoldTxt("Dataum\t\t")
            append(setHtmlTxt("WGS84", "'#0E4A88'"))
            append("\n")
            append(setHtmlBoldTxt("Projection\t\t"))
            append(setHtmlTxt("UTM", "'#0E4A88'"))
            append("\n")
            append(setHtmlBoldTxt("Distance\t\t"))
            append(setHtmlTxt("meter", "'#0E4A88'"))
            append("\n")
            append(setHtmlBoldTxt("Angle\t\t"))
            append(setHtmlTxt("DD", "'#0E4A88'"))
            append("\n")
        }

        binding.zoneData.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.zoneData.text.toString().trim()
            zoneDataID = dbControl.getZoneDataID(name)
            idList.put("zoneData", zoneDataID.trim())

        }
        binding.btnSubmit.setOnClickListener {

            /*       idList.put("datumName", "1")
                   idList.put("distanceUnit", "1")
                   idList.put("angleUnit", "1")
                   idList.put("elevation", "1")
                   idList.put("zoneData", "1")
                   idList.put("config_name", "test")


                   val result=dbControl.addConfigurationData(idList)
                   if(result.equals("Data inserted successfully")){
                       findNavController().safeNavigate(R.id.action_createConfigurationFragment_to_satelliteConfigurationFragment)
                   }else{
                       Toast.makeText(
                           this.requireContext(),
                           "Error While Insertion try Again",
                           Toast.LENGTH_SHORT
                       ).show()
                   }*/

            idList.put("config_name", binding.projectNme.text.toString() + "Config")
            if (binding.projectNme.text.toString().equals("")) {
                Toast.makeText(
                    this.requireContext(), "Enter Project Name",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (binding.zoneData.text.toString().equals("Zone Data")) {
                    Toast.makeText(
                        this.requireContext(), "Select Zone",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val result = dbControl.defaultProjectConfig(idList)
                    Log.d("TAG", "onViewCreated:result $result")
                    if (result.equals("Data inserted successfully")) {
                        prjDataList.clear()
                        val configId = dbControl.getproject_configurationID(binding.projectNme.text.toString() + "Config")
                        prjDataList.add(binding.projectNme.text.toString())
                        prjDataList.add(configId)
                        prjDataList.add(binding.operatorNm.text.toString())
                        prjDataList.add(binding.commentEd.text.toString())
                        findNavController().safeNavigate(R.id.action_createConfigurationFragment_to_satelliteConfigurationFragment)


                       /* val result = dbControl.addProjectData(prjDataList)

                        if (result.equals("Data inserted successfully")) {
                            Toast.makeText(
                                this.requireContext(),
                                "Data inserted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().safeNavigate(R.id.action_createConfigurationFragment_to_satelliteConfigurationFragment)
                        } else {
                            Toast.makeText(this.requireContext(), result, Toast.LENGTH_SHORT).show()
                        }*/

                    } else {
                        Toast.makeText(this.requireContext(), result, Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }
        binding.createNewProject.setOnClickListener {
            Log.d("TAG", "onViewCreated: newPrj")
            findNavController().navigate(R.id.action_createConfigurationFragment_to_createProjectFragment)
        }

    }

}