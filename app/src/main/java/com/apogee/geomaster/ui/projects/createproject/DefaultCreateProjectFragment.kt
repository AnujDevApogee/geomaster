package com.apogee.geomaster.ui.projects.createproject

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
import com.apogee.geomaster.databinding.DefaultProjectLayoutBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getEmojiByUnicode
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.openKeyBoard
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setHtmlBoldTxt
import com.apogee.geomaster.utils.setHtmlTxt
import com.apogee.geomaster.utils.show
import java.time.LocalDateTime

class DefaultCreateProjectFragment : Fragment(R.layout.default_project_layout) {

    private lateinit var binding: DefaultProjectLayoutBinding
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


    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DefaultProjectLayoutBinding.bind(view)
        dbControl = DatabaseRepsoitory(this.requireContext())
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
        displayActionBar(
            "Create Project ${getEmojiByUnicode(0x1F4DD)}",
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )

        (activity as HomeScreen?)?.hideActionBar()
//        activity?.openKeyBoard(binding.projectNme)
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
                        val configId =
                            dbControl.getproject_configurationID(binding.projectNme.text.toString() + "Config")
                        prjDataList.add(binding.projectNme.text.toString())
                        prjDataList.add(configId)
                        prjDataList.add(binding.operatorNm.text.toString())
                        prjDataList.add(binding.commentEd.text.toString())
                        val result = dbControl.addProjectData(prjDataList)

                        if (result.equals("Data inserted successfully")) {
                            Toast.makeText(
                                this.requireContext(),
                                "Data inserted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().safeNavigate(R.id.action_defaultCreateProjectFragment_to_homeScreenMainFragment)
                        } else {
                            Toast.makeText(this.requireContext(), result, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this.requireContext(), result, Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }
        binding.createNewProject.setOnClickListener {
            Log.d("TAG", "onViewCreated: newPrj")
            findNavController().navigate(R.id.action_defaultCreateProjectFragment_to_createProjectFragment)
        }

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

}
