package com.apogee.geomaster.ui.projects.createproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateProjectsFragmentBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.closeKeyboard
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getEmojiByUnicode
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.openKeyBoard
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.show
import com.google.android.material.transition.MaterialFadeThrough


class CreateProjectFragment : Fragment(R.layout.create_projects_fragment) {

    private lateinit var binding: CreateProjectsFragmentBinding
    var datumName: ArrayList<String> = ArrayList()
    var userDefinedDatumName: ArrayList<String> = ArrayList()
    var datumNameID: String = ""
    var angleUnit: ArrayList<String> = ArrayList()
    var angleUnitID: String = ""
    var distanceUnit: ArrayList<String> = ArrayList()
    var distanceUnitID: String = ""
    var zoneData: ArrayList<String> = ArrayList()
    var zoneDataID: String = ""
    var projectionParamsData: ArrayList<String> = ArrayList()
    var projectionParamsID: String = ""
    var zoneHemis: ArrayList<String> = ArrayList()
    var zoneHemisID: String = ""
    var projectionTypes: ArrayList<String> = ArrayList()
    var projectionTypesID: String = ""
    var datumTypes: ArrayList<String> = ArrayList()
    var idList : HashMap<String, String> = HashMap<String, String> ()

    var prjDataList: ArrayList<String> = ArrayList()
    var datumTypesID: String = ""
    var elevationType: ArrayList<String> = ArrayList()
    var elevationTypeID: String = ""

    private lateinit var dbControl: DatabaseRepsoitory
    val TAG = "CreateProjectFragment"

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
        dbControl = DatabaseRepsoitory(this.requireContext())

        projectionParamsData=dbControl.getprojectionParamData() as ArrayList<String>
        projectionParamsData.add(0,"Add Custom Projection") // Add the new element at the 0th index
        Log.d(TAG, "onCreate:projectionParamsData $projectionParamsData")


        angleUnit = dbControl.angleUnitdata() as ArrayList<String>
        distanceUnit = dbControl.getDistanceUnit() as ArrayList<String>
        zoneData = dbControl.getZoneData() as ArrayList<String>
        zoneHemis = dbControl.getZoneHemisphereData() as ArrayList<String>
        projectionTypes = dbControl.getProjectionType() as ArrayList<String>
        datumTypes = dbControl.getdatumtype() as ArrayList<String>
        elevationType = dbControl.getelevationType() as ArrayList<String>
        Log.d(TAG, "onCreate:datumName $datumName")
        Log.d(TAG, "onCreate:angleUnit $angleUnit")
        Log.d(TAG, "onCreate:distanceUnit $distanceUnit")
        Log.d(TAG, "onCreate:zoneData $zoneData")
        Log.d(TAG, "onCreate:zoneHemis $zoneHemis")
        Log.d(TAG, "onCreate:projectionTypes $projectionTypes")
        Log.d(TAG, "onCreate:datumTypes $datumTypes")
        Log.d(TAG, "onCreate:elevationType $elevationType")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateProjectsFragmentBinding.bind(view)
        displayActionBar(
            "Create Project ${getEmojiByUnicode(0x1F4DD)}",
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        (activity as HomeScreen?)?.hideActionBar()
        activity?.openKeyBoard(binding.projectNme)
        setDropdownAdapters()
        idList.clear()


        binding.angleUnitTxt.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.angleUnitTxt.text.toString().trim()
            angleUnitID = dbControl.angleUnitID(name)
            idList.put("angleUnit",angleUnitID.trim())

        }

        binding.distanceTxt.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.distanceTxt.text.toString().trim()
            distanceUnitID = dbControl.getDistanceUnitID(name)
            idList.put("distanceUnit",distanceUnitID.trim())

        }

        binding.zoneHemisphereData.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.zoneHemisphereData.text.toString().trim()
            zoneHemisID = dbControl.getZoneHemisphereID(name)
            idList.put("zoneHemi",zoneHemisID.trim())

        }

        binding.zoneData.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.zoneData.text.toString().trim()
            zoneDataID = dbControl.getZoneDataID(name)
            idList.put("zoneData",zoneDataID.trim())

        }

        binding.projectionTypeConn.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.projectionTypeConn.text.toString().trim()
            projectionTypesID = dbControl.getProjectionTypeID(name)
            if(name.equals("LCC")){
               binding.zoneDataLayout.hide()
               binding.zoneHemisphereLayout.hide()
            }else{
                binding.zoneDataLayout.show()
                binding.zoneHemisphereLayout.show()
            }
            idList.put("projectionType",projectionTypesID.trim())

        }

        binding.datumTypeConn.setOnItemClickListener { adapterView, view, position, l ->
            var name = binding.datumTypeConn.text.toString().trim()
            if(name.equals("User Defined")){
                userDefinedDatumName  = dbControl.getUserDefinedDatumName() as ArrayList<String>
                userDefinedDatumName.add(0,"+Create Custom Datum")
                Log.d(TAG, "onViewCreated:userDefinedDatumName $userDefinedDatumName")
                val datumNameView: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this.requireContext(),
                        android.R.layout.select_dialog_item,
                        userDefinedDatumName
                    )
                binding.datums.threshold = 1
                binding.datums.setAdapter(datumNameView)
                datumTypesID = dbControl.getdatumtypeID(name)

            }
            else{
                datumName  = dbControl.getPredefinedDatumName() as ArrayList<String>
                val datumNameView: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this.requireContext(),
                        android.R.layout.select_dialog_item,
                        datumName
                    )
                binding.datums.threshold = 1
                binding.datums.setAdapter(datumNameView)
                datumTypesID = dbControl.getdatumtypeID(name)
            }
            idList.put("datumType",datumTypesID.trim())

        }


        binding.datums.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.datums.text.toString().trim();
            if(name.equals("+Create Custom Datum")){
                findNavController().safeNavigate(CreateProjectFragmentDirections.actionCreateProjectFragmentToCustomDatumCreationFragment())
            }else{
                datumNameID = dbControl.getDatumId(name)
                idList.put("datumName",datumNameID.trim())

            }

        }
        binding.elevationKey.setOnItemClickListener { adapterView, view, position, l ->
            var name = binding.elevationKey.text.toString().trim()
            elevationTypeID = dbControl.getelevationTypeID(name)
            idList.put("elevation",elevationTypeID.trim())

        }

        binding.zoneProjection.setOnItemClickListener { adapterView, view, position, l ->
            val name = binding.zoneProjection.text.toString().trim()
            if(name.equals("Add Custom Projection")){
                findNavController().safeNavigate(CreateProjectFragmentDirections.actionCreateProjectFragmentToAddProjectionParamsFragment())

            }else{
                projectionParamsID = dbControl.getprojectionParamDataID(name)
                idList.put("zoneProjection",projectionParamsID.trim())
            }

        }

        binding.btnSubmit.setOnClickListener {
            if (binding.projectNme.text!!.equals("")) {
                Toast.makeText(this.requireContext(), "Add project name", Toast.LENGTH_SHORT).show()

            } else {
                idList.put("projectName",binding.projectNme.text.toString().trim())
                if (idList.size < 10) {
                    Toast.makeText(
                        this.requireContext(),
                        "Select All necessary values",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val configtable = dbControl.addConfigurationData(idList)
                    Log.d(TAG, "onViewCreated:configtable $configtable --")
                  if(configtable.equals("Data inserted successfully")){
                        prjDataList.clear()
                        val configId=dbControl.getproject_configurationID(binding.projectNme.text.toString())
                        prjDataList.add(binding.projectNme.text.toString())
                        prjDataList.add(configId)
                        prjDataList.add(binding.operatorNm.text.toString())
                        prjDataList.add(binding.commentEd.text.toString())
                        prjDataList.add("sdfsdf")

                        val result=dbControl.addProjectData(prjDataList)
                        Log.d(TAG, "onViewCreated:aaa idList --$idList")
                        Log.d(TAG, "onViewCreated:aaa configtable --$configtable")
                        Log.d(TAG, "onViewCreated:aaa projectTable --$result")
                          Toast.makeText(
                              this.requireContext(),"Data inserted successfully",
                              Toast.LENGTH_SHORT
                          ).show()

                    }else{
                      Toast.makeText(
                          this.requireContext(),
                          "Error Occured",
                          Toast.LENGTH_SHORT
                      ).show()
                    }

                }


            }
        }

    }

    override fun onResume() {
        super.onResume()
        setDropdownAdapters()

    }

    override fun onPause() {
        super.onPause()
        activity?.closeKeyboard(binding.projectNme)
    }

    fun setDropdownAdapters() {
    /*    val datumNameView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                datumName
            )
        binding.datums.threshold = 1
        binding.datums.setAdapter(datumNameView)

*/
        val angleUnitView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                angleUnit
            )
        binding.angleUnitTxt.threshold = 1
        binding.angleUnitTxt.setAdapter(angleUnitView)


        val distanceUnitView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                distanceUnit
            )
        binding.distanceTxt.threshold = 1
        binding.distanceTxt.setAdapter(distanceUnitView)


        val zoneDataView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                zoneData
            )
        binding.zoneData.threshold = 1
        binding.zoneData.setAdapter(zoneDataView)


        val zoneHemisView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                zoneHemis
            )
        binding.zoneHemisphereData.threshold = 1
        binding.zoneHemisphereData.setAdapter(zoneHemisView)


        val projectionTypesView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                projectionTypes
            )
        binding.projectionTypeConn.threshold = 1
        binding.projectionTypeConn.setAdapter(projectionTypesView)


        val datumTypesView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                datumTypes
            )
        binding.datumTypeConn.threshold = 1
        binding.datumTypeConn.setAdapter(datumTypesView)


        val elevationTypeView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                elevationType
            )
        binding.elevationKey.threshold = 1
        binding.elevationKey.setAdapter(elevationTypeView)

   val projectionParamView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                projectionParamsData
            )
        binding.zoneProjection.threshold = 1
        binding.zoneProjection.setAdapter(projectionParamView)


    }
}