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
    var countryName: ArrayList<String> = ArrayList()
    var countryID: String = ""
    var continentName: ArrayList<String> = ArrayList()
    var continentID: String = ""
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

    //    var idList: ArrayList<String> = ArrayList()
    var idList: HashMap<String, String> = HashMap<String, String>()

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
        setDropdownAdapters()
        idList.clear()

        binding.zoneProjectionLayout.hide()
        binding.continentLayout.hide()
        binding.countriesLayout.hide()
        binding.geoidLayout.hide()
        binding.angleUnitTxt.setOnItemClickListener { adapterView, view, position, l ->

            val name = binding.angleUnitTxt.text.toString().trim()
            angleUnitID = dbControl.angleUnitID(name)
            idList.put("angleUnit", angleUnitID.trim())
        }

        binding.distanceTxt.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.distanceTxt.text.toString().trim()
            distanceUnitID = dbControl.getDistanceUnitID(name)
            idList.put("distanceUnit", distanceUnitID.trim())

        }


        binding.zoneData.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.zoneData.text.toString().trim()
            zoneDataID = dbControl.getZoneDataID(name)
            idList.put("zoneData", zoneDataID.trim())


        }


        binding.continentView.setOnItemClickListener { adapterView, view, position, l ->
            var name = binding.continentView.text.toString().trim()
            if (name.equals("Continent")) {
                Log.d(TAG, "onViewCreated:Continent $name")
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


        binding.countriesView.setOnItemClickListener { adapterView, view, position, l ->
            var name = binding.countriesView.text.toString().trim()
            if (name.equals("Countries")) {
                Log.d(TAG, "onViewCreated:Countries $name")
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

        binding.projectionTypeConn.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.projectionTypeConn.text.toString().trim()
            if (name.equals("LCC") || name.equals("LTM")) {
                projectionTypesID = dbControl.getProjectionTypeID(name)
                binding.zoneProjectionLayout.show()
                binding.zoneDataLayout.hide()
//               binding.zoneHemisphereLayout.hide()

                    projectionParamsData = dbControl.getprojectionParamData(projectionTypesID.toInt()) as ArrayList<String>
                    projectionParamsData.add(0, "Add Custom Projection") // Add the new element at the 0th index

                    val projectionParamView: ArrayAdapter<String> =
                        ArrayAdapter<String>(
                            this.requireContext(),
                            android.R.layout.select_dialog_item,
                            projectionParamsData
                        )
                    binding.zoneProjection.threshold = 1
                    binding.zoneProjection.setAdapter(projectionParamView)

                idList.put("projectionType", projectionTypesID.trim())

            } else if (name.equals("UTM")) {
                projectionTypesID = dbControl.getProjectionTypeID(name)
                binding.zoneProjectionLayout.hide()
                binding.zoneDataLayout.show()
//                binding.zoneHemisphereLayout.show()
                idList.put("projectionType", projectionTypesID.trim())

            }
        }

        binding.datumTypeConn.setOnItemClickListener { adapterView, view, position, l ->
            var name = binding.datumTypeConn.text.toString().trim()
            if (name.equals("User Defined")) {
                userDefinedDatumName = dbControl.getUserDefinedDatumName() as ArrayList<String>
                userDefinedDatumName.add(0, "+Create Custom Datum")
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
            idList.put("datumType", datumTypesID.trim())

        }


        binding.datums.setOnItemClickListener { adapterView, view, position, l ->

            var name = binding.datums.text.toString().trim();
            if (name.equals("+Create Custom Datum")) {
                findNavController().safeNavigate(CreateProjectFragmentDirections.actionCreateProjectFragmentToAddCustomDatumCreationFragment())
            } else {
                datumNameID = dbControl.getDatumId(name)
                idList.put("datumName", datumNameID.trim())

            }

        }
        binding.elevationKey.setOnItemClickListener { adapterView, view, position, l ->

            if(binding.elevationKey.text.toString().equals("Ellipsoid Height")){
                binding.geoidLayout.hide()
                }else if(binding.elevationKey.text.toString().equals("MSL Height")){
                binding.geoidLayout.show()
            }
            var name = binding.elevationKey.text.toString().trim()
            elevationTypeID = dbControl.getelevationTypeID(name)
            idList.put("elevation", elevationTypeID.trim())

        }

        binding.zoneProjection.setOnItemClickListener { adapterView, view, position, l ->
            val name = binding.zoneProjection.text.toString().trim()
            if (name.equals("Add Custom Projection")) {
                findNavController().safeNavigate(CreateProjectFragmentDirections.actionCreateProjectFragmentToAddProjectionParamsFragment())
            } else {
                projectionParamsID = dbControl.getprojectionParamDataID(name)
                idList.put("zoneProjection", projectionParamsID.trim())
            }

        }

        binding.btnSubmit.setOnClickListener {
            idList.put("projectName", binding.projectNme.text.toString().trim()+"Config")

            Log.d(TAG, "onViewCreated: idList $idList")
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
                Log.d(TAG, "onViewCreated: LCC")
                setConfigurationPrams()
            }


        }

    }

    override fun onResume() {
        super.onResume()

        binding.zoneProjectionLayout.hide()
        binding.continentLayout.hide()
        binding.countriesLayout.hide()
        binding.geoidLayout.hide()
        setDropdownAdapters()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setConfigurationPrams() {

        val configtable = dbControl.addConfigurationData(idList)
        Log.d(TAG, "onViewCreated:configtable $configtable --")
        if (configtable.equals("Data inserted successfully")) {
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
                findNavController().safeNavigate(R.id.action_createProjectFragment_to_homeScreenMainFragment2)
            }else{
                Toast.makeText(this.requireContext(), result,Toast.LENGTH_SHORT ).show()
            }


        } else {
            Toast.makeText(
                this.requireContext(),
                "Error Occured",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    override fun onPause() {
        super.onPause()
        activity?.closeKeyboard(binding.projectNme)
    }

    fun setDropdownAdapters() {
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