package com.apogee.geomaster.ui.projects.createproject

import android.content.ContentValues.TAG
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
import com.apogee.geomaster.databinding.CreateProjectsFragmentBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.closeKeyboard
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getEmojiByUnicode
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.show
import com.google.android.material.transition.MaterialFadeThrough


class CreateProjectFragment : Fragment(R.layout.create_projects_fragment) {

    private lateinit var binding: CreateProjectsFragmentBinding
    private var datumName: ArrayList<String> = ArrayList()
    private var userDefinedDatumName: ArrayList<String> = ArrayList()
    private var datumNameID: String = ""
    private var angleUnit: ArrayList<String> = ArrayList()
    private var angleUnitID: String = ""
    private var countryName: ArrayList<String> = ArrayList()
    private var countryID: String = ""
    private var continentName: ArrayList<String> = ArrayList()
    private var continentID: String = ""
    private var distanceUnit: ArrayList<String> = ArrayList()
    private var distanceUnitID: String = ""
    private var zoneData: ArrayList<String> = ArrayList()
    private var zoneDataID: String = ""
    private var projectionParamsData: ArrayList<String> = ArrayList()
    private var projectionParamsID: String = ""
    private var zoneHemis: ArrayList<String> = ArrayList()
    //private var zoneHemisID: String = ""
    private var projectionTypes: ArrayList<String> = ArrayList()
    private var projectionTypesID: String = ""
    private var datumTypes: ArrayList<String> = ArrayList()

    //    var idList: ArrayList<String> = ArrayList()
    private var idList: HashMap<String, String> = HashMap()

    private var prjDataList: ArrayList<String> = ArrayList()
    private var datumTypesID: String = ""
    private var elevationType: ArrayList<String> = ArrayList()
    private var elevationTypeID: String = ""

    private lateinit var dbControl: DatabaseRepsoitory
    private val tag = "CreateProjectFragment"

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
        angleUnit = dbControl.angleUnitdata() as ArrayList<String>
        distanceUnit = dbControl.getDistanceUnit() as ArrayList<String>
        zoneData = dbControl.getZoneData() as ArrayList<String>
        zoneHemis = dbControl.getZoneHemisphereData() as ArrayList<String>
        projectionTypes = dbControl.getProjectionType() as ArrayList<String>
        datumTypes = dbControl.getdatumtype() as ArrayList<String>
        elevationType = dbControl.getelevationType() as ArrayList<String>
        Log.d(tag, "onCreate:datumName $datumName")
        Log.d(tag, "onCreate:angleUnit $angleUnit")
        Log.d(tag, "onCreate:distanceUnit $distanceUnit")
        Log.d(tag, "onCreate:zoneData $zoneData")
        Log.d(tag, "onCreate:zoneHemis $zoneHemis")
        Log.d(tag, "onCreate:projectionTypes $projectionTypes")
        Log.d(tag, "onCreate:datumTypes $datumTypes")
        Log.d(tag, "onCreate:elevationType $elevationType")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateProjectsFragmentBinding.bind(view)
        displayActionBar(
            "Create Project ${getEmojiByUnicode(0x1F4DD)}",
            binding.actionLayout,
          -1,
            menuCallback
        )

        (activity as HomeScreen?)?.hideActionBar()
//        activity?.openKeyBoard(binding.projectNme)

        idList.clear()




        binding.zoneProjectionLayout.hide()
        binding.continentLayout.hide()
        binding.countriesLayout.hide()
        binding.geoidLayout.hide()
        setDropdownAdapters()


        binding.angleUnitTxt.setOnItemClickListener { _, _, _, _ ->

            val name = binding.angleUnitTxt.text.toString().trim()
            angleUnitID = dbControl.angleUnitID(name)
            idList["angleUnit"] = angleUnitID.trim()
        }

        binding.distanceTxt.setOnItemClickListener { _, _, _, _ ->

            val name = binding.distanceTxt.text.toString().trim()
            distanceUnitID = dbControl.getDistanceUnitID(name)
            idList["distanceUnit"] = distanceUnitID.trim()

        }

        binding.zoneData.setOnItemClickListener { _, _, _, _ ->

            val name = binding.zoneData.text.toString().trim()
            zoneDataID = dbControl.getZoneDataID(name)
            idList["zoneData"] = zoneDataID.trim()


        }

        binding.continentView.setOnItemClickListener { _, _, _, _ ->
            val name = binding.continentView.text.toString().trim()
            if (name.equals("Continent",true)) {
                Log.d(tag, "onViewCreated:Continent $name")
            } else {
                continentID = dbControl.getContinentId(name)
                countryName = dbControl.getCountryName(continentID.toInt()) as ArrayList<String>
                val countryNameView: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this.requireContext(),
                        android.R.layout.select_dialog_item,
                        countryName
                    )
                binding.countriesView.threshold = 1
                binding.countriesView.setAdapter(countryNameView)
            }

        }

        binding.countriesView.setOnItemClickListener { _, _, _, _ ->
            val name = binding.countriesView.text.toString().trim()
            if (name.equals("Countries",true)) {
                Log.d(tag, "onViewCreated:Countries $name")
            } else {
                countryID = dbControl.getCountryId(name)
                datumName = dbControl.getPredefinedDatumName(countryID.toInt()) as ArrayList<String>
                val countryNameView: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this.requireContext(),
                        android.R.layout.select_dialog_item,
                        datumName
                    )
                binding.datums.threshold = 1
                binding.datums.setAdapter(countryNameView)
            }

        }

        binding.projectionTypeConn.setOnItemClickListener { _, _, _, _ ->

            val name = binding.projectionTypeConn.text.toString().trim()
            if (name.equals("LCC",true) || name.equals("LTM",true)) {
                projectionTypesID = dbControl.getProjectionTypeID(name)
                binding.zoneProjectionLayout.show()
                binding.zoneDataLayout.hide()
//               binding.zoneHemisphereLayout.hide()

                projectionParamsData =
                    dbControl.getprojectionParamData(projectionTypesID.toInt()) as ArrayList<String>
                projectionParamsData.add(
                    0,
                    "Add Custom Projection"
                ) // Add the new element at the 0th index

                    val projectionParamView: ArrayAdapter<String> =
                        ArrayAdapter<String>(
                            this.requireContext(),
                            android.R.layout.select_dialog_item,
                            projectionParamsData
                        )
                    binding.zoneProjection.threshold = 1
                    binding.zoneProjection.setAdapter(projectionParamView)

                idList["projectionType"] = projectionTypesID.trim()

            } else if (name.equals("UTM",true)) {
                projectionTypesID = dbControl.getProjectionTypeID(name)
                binding.zoneProjectionLayout.hide()
                binding.zoneDataLayout.show()
//                binding.zoneHemisphereLayout.show()
                idList["projectionType"] = projectionTypesID.trim()

            }
        }

        binding.datumTypeConn.setOnItemClickListener { _, _, _, _ ->
            val name = binding.datumTypeConn.text.toString().trim()
            if (name.equals("User Defined",true)) {
                userDefinedDatumName = dbControl.getUserDefinedDatumName() as ArrayList<String>
                userDefinedDatumName.add(0, "+Create Custom Datum")
                Log.d(tag, "onViewCreated:userDefinedDatumName $userDefinedDatumName")
                val datumNameView: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this.requireContext(),
                        android.R.layout.select_dialog_item,
                        userDefinedDatumName
                    )
                binding.datums.threshold = 1
                binding.datums.setAdapter(datumNameView)
                datumTypesID = dbControl.getdatumtypeID(name)
                binding.continentLayout.hide()
                binding.countriesLayout.hide()

            } else {
                binding.continentLayout.show()
                binding.countriesLayout.show()


                continentName = dbControl.getContinentName() as ArrayList<String>
                val continentNameView: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this.requireContext(),
                        android.R.layout.select_dialog_item,
                        continentName
                    )
                binding.continentView.threshold = 1
                binding.continentView.setAdapter(continentNameView)

            }
            idList["datumType"] = datumTypesID.trim()

        }

        binding.datums.setOnItemClickListener { _, _, _, _ ->

            val name = binding.datums.text.toString().trim()

            if (name.equals("+Create Custom Datum",true)) {
                findNavController().safeNavigate(R.id.action_createProjectFragment_to_addCustomDatumCreationFragment)
            } else {
                Log.d(TAG, "onViewCreated: "+name)
                datumNameID = dbControl.getDatumId(name)
                idList["datumName"] = datumNameID.trim()

            }

        }

        binding.elevationKey.setOnItemClickListener { _, _, _, _ ->

            if (binding.elevationKey.text.toString().equals("Ellipsoid Height",true)) {
                binding.geoidLayout.hide()
            } else if (binding.elevationKey.text.toString().equals("MSL Height",true)) {
                binding.geoidLayout.show()
            }
            val name = binding.elevationKey.text.toString().trim()
            elevationTypeID = dbControl.getelevationTypeID(name)
            idList["elevation"] = elevationTypeID.trim()

        }

        binding.zoneProjection.setOnItemClickListener { _, _, _, _ ->
            val name = binding.zoneProjection.text.toString().trim()
            if (name.equals("Add Custom Projection", true)) {
                findNavController().safeNavigate(CreateProjectFragmentDirections.actionCreateProjectFragmentToZoneProjection())
            } else {
                projectionParamsID = dbControl.getprojectionParamDataID(name)
                idList["zoneProjection"] = projectionParamsID.trim()
            }

        }

        binding.btnSubmit.setOnClickListener {
            idList["projectName"] = binding.projectNme.text.toString().trim() + "Config"

            Log.d(tag, "onViewCreated: idList $idList")
            // Check each condition individually using else if

            if (binding.projectNme.text.toString().equals("")) {
                Toast.makeText(
                    this.requireContext(),
                    "Please enter Project Name",
                    Toast.LENGTH_SHORT
                ).show();
            } else if (binding.datumTypeConn.text.toString().equals("Datum Type")) {
                Toast.makeText(
                    this.requireContext(),
                    "Please select Datum Type",
                    Toast.LENGTH_SHORT
                ).show();
            } else if (binding.datums.text.toString()
                    .equals("Datum Name") || binding.datums.text.toString()
                    .equals("+Create Custom Datum")
            ) {
                Toast.makeText(
                    this.requireContext(),
                    "Please select Datum Name",
                    Toast.LENGTH_SHORT
                ).show();
            } else if (binding.projectionTypeConn.text.toString().equals("Type")) {
                Toast.makeText(
                    this.requireContext(),
                    "Please select Projection Type",
                    Toast.LENGTH_SHORT
                ).show();
            } else if (binding.projectionTypeConn.text.toString()
                    .equals("LCC") && (binding.zoneProjection.text.toString()
                    .equals("Type") || binding.zoneProjection.text.toString()
                    .equals("Add Custom Projection"))
            ) {

                Toast.makeText(
                    this.requireContext(),
                    "Please select Projection Parameter",
                    Toast.LENGTH_SHORT
                ).show();

            } else if (binding.projectionTypeConn.text.toString()
                    .equals("UTM") && binding.zoneData.text.toString().equals("Zone Data")
            ) {

                Toast.makeText(
                    this.requireContext(),
                    "Please select a Zone ",
                    Toast.LENGTH_SHORT
                ).show();
            } else if (binding.elevationKey.text.toString().equals("Elevation")) {
                Toast.makeText(
                    this.requireContext(),
                    "Please select Elevation",
                    Toast.LENGTH_SHORT
                ).show();
            } else if (binding.distanceTxt.text.toString().equals("Distance Unit")) {
                Toast.makeText(
                    this.requireContext(),
                    "Please select Distance Unit",
                    Toast.LENGTH_SHORT
                ).show();
            } else if (binding.angleUnitTxt.text.toString().equals("Angle Unit")) {
                Toast.makeText(
                    this.requireContext(),
                    "Please select Angle Unit",
                    Toast.LENGTH_SHORT
                ).show();
            } else {
                Log.d(tag, "onViewCreated: LCC")
                setConfigurationPrams()
            }


        }

    }

//    override fun onStart() {
//        super.onStart()
//
//        binding.zoneProjectionLayout.hide()
//        binding.continentLayout.hide()
//        binding.countriesLayout.hide()
//        binding.geoidLayout.hide()
//        setDropdownAdapters()
//
//    }

    @RequiresApi(Build.VERSION_CODES.O)

    fun setConfigurationPrams() {

        val configtable = dbControl.addConfigurationData(idList)
        Log.d(tag, "onViewCreated:configtable $configtable --")
        if (configtable.equals("Data inserted successfully",true)) {
            prjDataList.clear()
            val configId = dbControl.getproject_configurationID(binding.projectNme.text.toString()+"Config")
            prjDataList.add(binding.projectNme.text.toString())
            prjDataList.add(configId)
            prjDataList.add(binding.operatorNm.text.toString())
            prjDataList.add(binding.commentEd.text.toString())
            prjDataList.add("sdfsdf")

            val result = dbControl.addProjectData(prjDataList)
            if(result.equals("Data inserted successfully")){
                Toast.makeText(this.requireContext(), "Data inserted successfully",Toast.LENGTH_SHORT ).show()
                findNavController().safeNavigate(R.id.action_createProjectFragment_to_satelliteConfigurationFragment)
            }else{
                Toast.makeText(this.requireContext(), result,Toast.LENGTH_SHORT ).show()
            }


        } else {
            Toast.makeText(this.requireContext(), "Error Occured", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onPause() {
        super.onPause()
        activity?.closeKeyboard(binding.projectNme)
    }

    private fun setDropdownAdapters() {
        val datumNameView: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this.requireContext(),
                android.R.layout.select_dialog_item,
                datumName
            )
        binding.datums.threshold = 1
        binding.datums.setAdapter(datumNameView)

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


//        val zoneHemisView: ArrayAdapter<String> =
//            ArrayAdapter<String>(
//                this.requireContext(),
//                android.R.layout.select_dialog_item,
//                zoneHemis
//            )
//        binding.zoneHemisphereData.threshold = 1
//        binding.zoneHemisphereData.setAdapter(zoneHemisView)


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

    }
}