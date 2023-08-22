package com.apogee.geomaster.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.apogee.databasemodule.DatabaseSingleton
import com.apogee.databasemodule.TableCreator
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime

class DatabaseRepsoitory(context: Context) {
    val TAG = "DBControl"


    val database by lazy {
        DatabaseSingleton.getInstance(context).getDatabase()!!
    }
    val tableCreator = TableCreator(database)


    fun CommonApiTablesCreation(apiResponse: String) {
        val coordinateSystem = "coordinateSystem"
        val coordinateSystemColumn = arrayOf(
            TableCreator.ColumnDetails("coordinateSystem_id", "INTEGER", true),
            TableCreator.ColumnDetails("coordinateSystem_name", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val coordinateSystemTable = tableCreator.createMainTableIfNeeded(coordinateSystem, coordinateSystemColumn)


        val hemisphere = "hemisphere"
        val hemisphereColumn = arrayOf(
            TableCreator.ColumnDetails("hemisphere_id", "INTEGER", true),
            TableCreator.ColumnDetails("zoneHemisphere", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val hemisphereTable = tableCreator.createMainTableIfNeeded(hemisphere, hemisphereColumn)

        val zonedata = "zonedata"
        val zonedataColumn = arrayOf(
            TableCreator.ColumnDetails("zonedata_id", "INTEGER", true),
            TableCreator.ColumnDetails("zone", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING"),
            TableCreator.ColumnDetails(
                "hemisphere_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "hemisphere(hemisphere_id)"
            )
        )
        val zonedataTable = tableCreator.createMainTableIfNeeded(zonedata, zonedataColumn)


        val continents = "continents"
        val continentsColumn = arrayOf(
            TableCreator.ColumnDetails("continent_id", "INTEGER", true),
            TableCreator.ColumnDetails("continent_name", "STRING", unique = true),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val continentsTable = tableCreator.createMainTableIfNeeded(continents, continentsColumn)


        val countries = "countries"
        val countriesColumn = arrayOf(
            TableCreator.ColumnDetails("country_id", "INTEGER", true),
            TableCreator.ColumnDetails("country_name", "STRING", unique = true),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails(
                "continent_id", "INTEGER",
                foreignKey = true, foreignKeyReference = "continents(continent_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val countriesTable = tableCreator.createMainTableIfNeeded(countries, countriesColumn)


        val datumtype = "datumtype"

        val datumtypeColumn = arrayOf(
            TableCreator.ColumnDetails("datumType_id", "INTEGER", true),
            TableCreator.ColumnDetails("datumType_name", "STRING", unique = true),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val datumtypeTable = tableCreator.createMainTableIfNeeded(datumtype, datumtypeColumn)


        val datum_data = "datum_data"
        val datum_dataColumn = arrayOf(
            TableCreator.ColumnDetails("datum_id", "INTEGER", true),
            TableCreator.ColumnDetails("datum_name", "STRING", unique = true),
            TableCreator.ColumnDetails("major_axis", "STRING"),
            TableCreator.ColumnDetails("flattening", "STRING"),
            TableCreator.ColumnDetails("scale", "STRING"),
            TableCreator.ColumnDetails("x_axis_shift", "STRING"),
            TableCreator.ColumnDetails("y_axis_shift", "STRING"),
            TableCreator.ColumnDetails("z_axis_shift", "STRING"),
            TableCreator.ColumnDetails("rot_x_axis", "STRING"),
            TableCreator.ColumnDetails("rot_y_axis", "STRING"),
            TableCreator.ColumnDetails("rot_z_axis", "STRING"),
            TableCreator.ColumnDetails(
                "datumType_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "datumtype(datumType_id)"
            ),
            TableCreator.ColumnDetails(
                "country_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "countries(country_id)"
            ),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("datum_command", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val datum_dataTable = tableCreator.createMainTableIfNeeded(datum_data, datum_dataColumn)


        val angleunit = "angleunit"

        val angleunitColumn = arrayOf(
            TableCreator.ColumnDetails("angleunit_id", "INTEGER", true),
            TableCreator.ColumnDetails("angUnit_name", "STRING", unique = true),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val angleunitTable = tableCreator.createMainTableIfNeeded(angleunit, angleunitColumn)


        val projectiontype = "projectiontype"
        val projectiontypeColumn = arrayOf(
            TableCreator.ColumnDetails("projectiontype_id", "INTEGER", true),
            TableCreator.ColumnDetails("projectionType", "STRING", unique = true),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val projectiontypeTable =
            tableCreator.createMainTableIfNeeded(projectiontype, projectiontypeColumn)
        Log.d(TAG, "CommonApiTablesCreation: projectiontypeTable $projectiontypeTable")


        val projectionParameters = "projectionParameters"
        val projectionParametersColumn = arrayOf(
            TableCreator.ColumnDetails("projectionParam_id", "INTEGER", true),
            TableCreator.ColumnDetails("zone_name", "STRING", unique = true),
            TableCreator.ColumnDetails("origin_lat", "STRING"),
            TableCreator.ColumnDetails("origin_lng", "STRING"),
            TableCreator.ColumnDetails("false_easting", "STRING"),
            TableCreator.ColumnDetails("false_northing", "STRING"),
            TableCreator.ColumnDetails("scale", "STRING"),
            TableCreator.ColumnDetails("paralell_1", "STRING"),
            TableCreator.ColumnDetails("paralell_2", "STRING"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("active", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("created_by", "STRING"),
            TableCreator.ColumnDetails("misc1", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("misc2", "STRING"),
            TableCreator.ColumnDetails("misc3", "STRING"),
            TableCreator.ColumnDetails("misc4", "STRING"),
            TableCreator.ColumnDetails(
                "projectiontype_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "projectiontype(projectiontype_id)"
            )
        )
        val projectionParametersTable =
            tableCreator.createMainTableIfNeeded(projectionParameters, projectionParametersColumn)
        Log.d(TAG, "CommonApiTablesCreation: projectionParametersTable $projectionParametersTable")


        val autocad_file_type = "autocad_file_type"
        val autocad_file_typeColumn = arrayOf(
            TableCreator.ColumnDetails("autocad_file_type_id", "INTEGER", true),
            TableCreator.ColumnDetails("file_name", "STRING"),
            TableCreator.ColumnDetails("file_type", "STRING"),
            TableCreator.ColumnDetails("misc_1", "STRING"),
            TableCreator.ColumnDetails("misc_2", "STRING"),
            TableCreator.ColumnDetails("misc_3", "STRING"),
            TableCreator.ColumnDetails("misc_4", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val autocad_file_typeTable =
            tableCreator.createMainTableIfNeeded(autocad_file_type, autocad_file_typeColumn)


        val autocad_file_map = "autocad_file_map"
        val autocad_file_mapColumn = arrayOf(
            TableCreator.ColumnDetails("autocad_file_map_id", "INTEGER", true),
            TableCreator.ColumnDetails(
                "autocad_file_type_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "autocad_file_type(autocad_file_type_id)"
            ),
            TableCreator.ColumnDetails(
                "import_export_file_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "autocad_file_type(autocad_file_type_id)"
            ),
            TableCreator.ColumnDetails("active", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("misc_1", "STRING"),
            TableCreator.ColumnDetails("misc_2", "STRING"),
            TableCreator.ColumnDetails("misc_3", "STRING"),
            TableCreator.ColumnDetails("misc_4", "STRING")
        )
        val autocad_file_mapTable =
            tableCreator.createMainTableIfNeeded(autocad_file_map, autocad_file_mapColumn)


        val elevationtype = "elevationtype"

        val elevationtypeColumn = arrayOf(
            TableCreator.ColumnDetails("elevationtype_id", "INTEGER", true),
            TableCreator.ColumnDetails("elevationType", "STRING", unique = true),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val elevationtypeTable =
            tableCreator.createMainTableIfNeeded(elevationtype, elevationtypeColumn)


        val distanceunit = "distanceunit"


        val distanceunitColumn = arrayOf(
            TableCreator.ColumnDetails("distanceunit_id", "INTEGER", true),
            TableCreator.ColumnDetails("disUnit_name", "STRING", unique = true),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val distanceunitTable =
            tableCreator.createMainTableIfNeeded(distanceunit, distanceunitColumn)

        Log.d(
            TAG, "CommonApiTablesCreation: " +
                    "\n hemisphereTable:--$hemisphereTable" +
                    "\n coordinateSystemTable:--$coordinateSystemTable" +
                    "\n continentsTable:--$continentsTable" +
                    "\n projectionParametersTable:--$projectionParametersTable" +
                    "\n zonedataTable:--$zonedataTable" +
                    "\n countriesTable:--$countriesTable" +
                    "\n datum_dataTable:--$datum_dataTable" +
                    "\n datumtypeTable:--$datumtypeTable" +
                    "\n angleunitTable:--$angleunitTable" +
                    "\n projectiontypeTable:--$projectiontypeTable" +
                    "\n autocad_file_typeTable:--$autocad_file_typeTable" +
                    "\n autocad_file_mapTable:--$autocad_file_mapTable" +
                    "\n elevationtypeTable:--$elevationtypeTable" +
                    "\n distanceunitTable:--$distanceunitTable"
        )
        if (hemisphereTable.equals("Table Created Successfully...")
            && coordinateSystemTable.equals("Table Created Successfully...")
            && continentsTable.equals("Table Created Successfully...")
            && projectionParametersTable.equals("Table Created Successfully...")
            && zonedataTable.equals("Table Created Successfully...")
            && countriesTable.equals("Table Created Successfully...")
            && datum_dataTable.equals("Table Created Successfully...")
            && datumtypeTable.equals("Table Created Successfully...")
            && angleunitTable.equals("Table Created Successfully...")
            && projectiontypeTable.equals("Table Created Successfully...")
            && autocad_file_typeTable.equals("Table Created Successfully...")
            && autocad_file_mapTable.equals("Table Created Successfully...")
            && elevationtypeTable.equals("Table Created Successfully...")
            && distanceunitTable.equals("Table Created Successfully...")
        ) {
            Log.d(TAG, "CommonApiTablesCreation1: All table created")
            insertDBData(apiResponse)
            projectManagementData()
        } else {
            Log.d(TAG, "CommonApiTablesCreation1: Error while table creation ")
        }


    }


    fun insertDBData(resp: String): String {
        Log.d(TAG, "insertCommonData: $resp")
        var result = ""
        val jsonObject = JSONObject(resp)
        for (key in jsonObject.keys()) {

            val dataList: MutableList<ContentValues> = ArrayList()
            val jsonArray = jsonObject.getJSONArray(key)
            try {
                for (i in 0 until jsonArray.length()) {
                    if (key.equals("datum_data")) {
                        Log.d(
                            TAG,
                            "insertCommonData: datum_data $key--${jsonArray.getJSONObject(i)}  "
                        )
                    }
                    dataList.clear()
                    val jsonObject1: JSONObject = jsonArray.getJSONObject(i)

                    val iter: Iterator<String> = jsonObject1.keys()
                    val values1 = ContentValues()
                    while (iter.hasNext()) {
                        val keyss = iter.next()
                        try {
                            val valueddd: Any = jsonObject1.get(keyss)
                            values1.put(keyss, valueddd.toString())
                        } catch (e: JSONException) {
                            Log.d(TAG, "onCreate:JSONException ${e.message}")
                        }
                    }
                    dataList.add(values1)
                    result = tableCreator.insertDataIntoTable(key.toString(), dataList)


                    Log.d(TAG, "onCreate: resultinsertData:--$result---$key")
                }

            } catch (e: Exception) {
                Log.d(TAG, "onCreate: Exception " + e.message)
            }
            Log.d(TAG, "insertDBData: $key")
        }
        return result
    }


    fun projectManagementData() {

        val survey_configuration = "survey_configuration"
        val survey_configurationColumn = arrayOf(
            TableCreator.ColumnDetails("config_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("config_name", "STRING", unique = true),
            TableCreator.ColumnDetails(
                "zonedata_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "zonedata(zonedata_id)"
            ),
            TableCreator.ColumnDetails(
                "datum_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "datum_data(datum_id)"
            ),
            TableCreator.ColumnDetails(
                "elevationtype_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "elevationtype(elevationtype_id)"
            ),
            TableCreator.ColumnDetails(
                "distanceunit_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "distanceunit(distanceunit_id)"
            ),
            TableCreator.ColumnDetails(
                "angleunit_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "angleunit(angleunit_id)"
            ),
            TableCreator.ColumnDetails(
                "projectionParam_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "projectionParameters(projectionParam_id)"
            ),
            TableCreator.ColumnDetails("config_time", "STRING")

        )
        val survey_configurationTable =
            tableCreator.createMainTableIfNeeded(survey_configuration, survey_configurationColumn)


        val project_status = "project_status"
        val project_statusColumn = arrayOf(
            TableCreator.ColumnDetails("status_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("status_types", "STRING"),
        )
        val project_statusTable =
            tableCreator.createMainTableIfNeeded(project_status, project_statusColumn)

        if (project_statusTable.equals("Table Created Successfully...")) {
            val dataList: MutableList<ContentValues> = ArrayList()
            val values1 = ContentValues()
            values1.put("status_types", "Active")
            dataList.add(values1)
            var result = tableCreator.insertDataIntoTable(project_status, dataList)
            dataList.clear()
            values1.put("status_types", "Inactive")
            Log.d(TAG, "projectManagementData:result $result")

            dataList.add(values1)

            result = tableCreator.insertDataIntoTable(project_status, dataList)
            Log.d(TAG, "projectManagementData:result $result")
        }


        val shortNameTable = "shortNameTable"
        val shortNameTableColumn = arrayOf(
            TableCreator.ColumnDetails("shortName_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("shortName", "STRING"),
            TableCreator.ColumnDetails("project_id", "INTEGER"),
        )
        val shortNameTableData =
            tableCreator.createMainTableIfNeeded(shortNameTable, shortNameTableColumn)


        val siteCalibration = "siteCalibration"
        val siteCalibrationColumn = arrayOf(
            TableCreator.ColumnDetails("siteCal_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("scale", "STRING"),
            TableCreator.ColumnDetails("angle", "STRING"),
            TableCreator.ColumnDetails("Tx", "STRING"),
            TableCreator.ColumnDetails("Ty", "STRING"),
            TableCreator.ColumnDetails("Fixed_Easting", "STRING"),
            TableCreator.ColumnDetails("Fixed_Northing", "STRING"),
            TableCreator.ColumnDetails("sigmaZ", "STRING"),
            TableCreator.ColumnDetails("siteCal_createdAt", "STRING"),
            TableCreator.ColumnDetails("project_id", "INTEGER"),
        )
        val siteCalibrationTable =
            tableCreator.createMainTableIfNeeded(siteCalibration, siteCalibrationColumn)


        val project_folder = "project_folder"
        val project_folderColumn = arrayOf(
            TableCreator.ColumnDetails("folder_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("folderName", "STRING"),
            TableCreator.ColumnDetails("folderPath", "STRING"),
            TableCreator.ColumnDetails("fileTypes", "STRING"),
            TableCreator.ColumnDetails("folderCreatedAt", "STRING")
        )
        val project_folderTable =
            tableCreator.createMainTableIfNeeded(project_folder, project_folderColumn)


        val project_table = "project_table"
        val project_tableColumn = arrayOf(
            TableCreator.ColumnDetails("project_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("project_name", "STRING", unique = true),
            TableCreator.ColumnDetails("operator", "STRING"),
            TableCreator.ColumnDetails("comment", "STRING"),
            TableCreator.ColumnDetails(
                "folder_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "project_folder(folder_id)"
            ),
            TableCreator.ColumnDetails(
                "siteCal_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "siteCalibration(siteCal_id)"
            ),
            TableCreator.ColumnDetails(
                "status_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "project_status(status_id)"
            ),
            TableCreator.ColumnDetails(
                "config_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "survey_configuration(config_id)"
            ),
            TableCreator.ColumnDetails("projectCreated_at", "STRING")
        )
        val project_tableData =
            tableCreator.createMainTableIfNeeded(project_table, project_tableColumn)
    }

    fun insertProjectionPrameters(paramValues: String): String {
        var result = ""
        Log.d(TAG, "insertProjectionPrameters: paramValues $paramValues")
        val dataList: MutableList<ContentValues> = ArrayList()
        val values1 = ContentValues()
        values1.put("zone_name", paramValues.split(",")[0].trim())
        values1.put("origin_lat", paramValues.split(",")[1].trim())
        values1.put("origin_lng", paramValues.split(",")[2].trim())
        values1.put("false_easting", paramValues.split(",")[3].trim())
        values1.put("false_northing", paramValues.split(",")[4].trim())
        values1.put("paralell_1", paramValues.split(",")[5].trim())
        values1.put("paralell_2", paramValues.split(",")[6].trim())
        values1.put("projectiontype_id", paramValues.split(",")[7].trim())
        dataList.add(values1)
        result = tableCreator.insertDataIntoTable("projectionParameters", dataList)
        Log.d(TAG, "insertProjectionPrameters: $result")

        return result
    }

    fun getUserDefinedDatumName(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT datum_name FROM datum_data where datumType_id = '2' and active = 'Y' ")

        return data
    }

    fun getContinentName(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT continent_name FROM continents where active = 'Y' ")

        return data
    }

    fun getContinentId(continent_name: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT continent_id FROM continents where continent_name='" + continent_name + "'")
        return data?.get(0) ?: ""
    }

    fun getCountryName(continentId: Int): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT country_name FROM countries where continent_id = " + continentId + "")
        Log.d(TAG, "getCountryName: $data")
        return data
    }

    fun getCountryId(country_name: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT country_id FROM countries where country_name='" + country_name + "'")
        return data?.get(0) ?: ""
    }

    fun getPredefinedDatumName(country_id: Int): List<String>? {
        var data: List<String> = ArrayList()

        try {

            data =
                tableCreator.executeStaticQuery("SELECT datum_name FROM datum_data where country_id = " + country_id + "")!!
            Log.d(TAG, "getPredefinedDatumName:  $data")
        } catch (e: Exception) {
            Log.d(TAG, "getPredefinedDatumName:Exception ${e.message} ")
        }
        return data
    }

    fun getDatumId(datum_name: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT datum_id FROM datum_data where datum_name='" + datum_name + "'")
        return data?.get(0) ?: ""
    }

    fun getprojectionParamData(projectiontype_id: Int): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT zone_name FROM projectionParameters where projectiontype_id=" + projectiontype_id + "")
        return data
    }

    fun getprojectionParamDataID(zone_name: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT projectionParam_id FROM projectionParameters where zone_name='" + zone_name + "'")
        return data?.get(0) ?: ""
    }

    fun deleteProjectionParamData(zone_name: String): String {
        var data =
            tableCreator.executeStaticQuery("DELETE FROM projectionParameters WHERE  zone_name ='" + zone_name + "'")
        return data?.get(0) ?: ""
    }

    fun angleUnitdata(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT angUnit_name FROM angleunit where active = 'Y' ")
        return data
    }

    fun angleUnitID(angUnit_name: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT angleunit_id FROM angleunit where angUnit_name='" + angUnit_name + "'")
        return data?.get(0) ?: ""
    }

    fun getDistanceUnit(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT disUnit_name FROM distanceunit where active = 'Y' ")
        return data
    }

    fun getDistanceUnitID(disUnit_name: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT distanceunit_id FROM distanceunit where disUnit_name='" + disUnit_name + "'")
        return data?.get(0) ?: ""
    }

    fun getZoneHemisphereData(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT zoneHemisphere FROM hemisphere where active = 'Y' ")

        return data
    }

    fun getZoneHemisphereID(zoneHemisphere: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT hemisphere_id FROM hemisphere where zoneHemisphere='" + zoneHemisphere + "'")
        return data?.get(0) ?: ""
    }

    fun getZoneData(): List<String>? {
        val data = tableCreator.executeStaticQuery("SELECT zone FROM zonedata where active = 'Y' ")
        return data
    }

    fun getZoneDataID(zone: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT zonedata_id FROM zonedata where zone='" + zone + "'")
        return data?.get(0) ?: ""
    }

    fun getProjectionType(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT projectionType FROM projectiontype where active = 'Y'")
        return data
    }

    fun getProjectionTypeID(projectionType: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT projectiontype_id FROM projectiontype where projectionType='" + projectionType + "'")
        return data?.get(0) ?: ""
    }

    fun getdatumtype(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT datumType_name FROM datumtype where active = 'Y' ")
        return data
    }

    fun getdatumtypeID(datumType_name: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT datumType_id FROM datumtype where datumType_name='" + datumType_name + "'")
        return data?.get(0) ?: ""
    }

    fun getelevationType(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT elevationType FROM elevationtype  where active = 'Y' ")
        return data
    }

    fun getelevationTypeID(elevationtype: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT elevationtype_id FROM elevationtype where elevationType='" + elevationtype + "'")
        return data?.get(0) ?: ""
    }
    fun getdeviceTabledata(): List<String>? {
        var data =
            tableCreator.executeStaticQuery("SELECT * from device ")
        return data
    }


    fun getproject_configurationID(config_name: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT config_id FROM survey_configuration where config_name='" + config_name + "'")
        return data?.get(0) ?: ""
    }

    fun getSatelliteDataList(): List<String>?{
        var data =
            tableCreator.executeStaticQuery("SELECT constellation_name,active FROM constellation ")
        return data
    }
    fun insertSatelliteDataList(data:String):String{

        var query=" UPDATE constellation SET active = '"+ data.split(",")[1]+ "' WHERE constellation_name = '"+data.split(",")[0]+"'"
        Log.d(TAG, "insertSatelliteDataList: $query")
        var result = tableCreator.executeStaticQuery(query)
        Log.d(TAG, "insertSatelliteDataList: $result")
        return result.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addConfigurationData(map: HashMap<String, String>): String {

        val result = tableCreator.getTableSchema("survey_configuration")
        var insertResult = ""

        if (result.equals("")) {

            val survey_configuration = "survey_configuration"
            val survey_configurationColumn = arrayOf(
                TableCreator.ColumnDetails("config_id", "INTEGER", true, true),
                TableCreator.ColumnDetails("config_name", "STRING", unique = true),
                TableCreator.ColumnDetails(
                    "zonedata_id",
                    "INTEGER",
                    foreignKey = true,
                    foreignKeyReference = "zonedata(zonedata_id)"
                ),
                TableCreator.ColumnDetails(
                    "datum_id",
                    "INTEGER",
                    foreignKey = true,
                    foreignKeyReference = "datum_data(datum_id)"
                ),
                TableCreator.ColumnDetails(
                    "elevationtype_id",
                    "INTEGER",
                    foreignKey = true,
                    foreignKeyReference = "elevationtype(elevationtype_id)"
                ),
                TableCreator.ColumnDetails(
                    "distanceunit_id",
                    "INTEGER",
                    foreignKey = true,
                    foreignKeyReference = "distanceunit(distanceunit_id)"
                ),
                TableCreator.ColumnDetails(
                    "angleunit_id",
                    "INTEGER",
                    foreignKey = true,
                    foreignKeyReference = "angleunit(angleunit_id)"
                ),
                TableCreator.ColumnDetails(
                    "projectionParam_id",
                    "INTEGER",
                    foreignKey = true,
                    foreignKeyReference = "projectionParameters(projectionParam_id)"
                ),
                TableCreator.ColumnDetails("config_time", "STRING")
            )
            val survey_configurationTable =
                tableCreator.createMainTableIfNeeded(
                    survey_configuration,
                    survey_configurationColumn
                )


        }
        else {
            Log.d(TAG, "addConfigurationData: Else")

            val dataList: MutableList<ContentValues> = ArrayList()
            val values1 = ContentValues()
            Log.d(TAG, "addConfigurationData:projectName ${map.get("projectName")}")
            values1.put("config_name", map.get("projectName"))
            values1.put("datum_id", map.get("datumName"))
            values1.put("zonedata_id", map.get("zoneData"))
            values1.put("elevationtype_id", map.get("elevation"))
            values1.put("distanceunit_id", map.get("distanceUnit"))
            values1.put("angleunit_id", map.get("angleUnit"))
            values1.put("projectionParam_id", map.get("zoneProjection"))
            values1.put("config_time", "${LocalDateTime.now()}")
            dataList.add(values1)

            insertResult = tableCreator.insertDataIntoTable("survey_configuration", dataList)
        }
        return insertResult
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun defaultProjectConfig(map: HashMap<String, String>): String {
        var result = ""
        val dataList: MutableList<ContentValues> = ArrayList()
        val values1 = ContentValues()
        values1.put("config_name", map.get("config_name"))
        values1.put("datum_id", map.get("datumName"))
        values1.put("zonedata_id", map.get("zoneData"))
        values1.put("elevationtype_id", map.get("elevation"))
        values1.put("distanceunit_id", map.get("distanceUnit"))
        values1.put("angleunit_id", map.get("angleUnit"))
        values1.put("projectionParam_id", "")
        values1.put("config_time", "${LocalDateTime.now()}")
        dataList.add(values1)

        result = tableCreator.insertDataIntoTable("survey_configuration", dataList)
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addProjectData(list: List<String>): String {

        val dataList: MutableList<ContentValues> = ArrayList()
        val values1 = ContentValues()

        values1.put("project_name", list.get(0))
        values1.put("operator", list.get(2))
        values1.put("comment", list.get(3))
        values1.put("config_id", list.get(1))
        values1.put("projectCreated_at", "${LocalDateTime.now()}")
        values1.put("status_id", "1")
        dataList.add(values1)

        val result = tableCreator.insertDataIntoTable("project_table", dataList)
        return result
    }

    fun getProjectList(): List<String>? {
        var data = tableCreator.executeStaticQuery(
            "SELECT   prj.project_name ,dd.datum_name, z.zone\n" +
                    "FROM project_table AS prj  ,survey_configuration AS conf,datum_data as dd, zonedata as z\n" +
                    " where prj. config_id=conf.config_id and conf.datum_id=dd.datum_id and \n" +
                    " conf.zonedata_id = z.zonedata_id"
        )
        Log.d(TAG, "getProjectList: data--$data")

        return data
    }

    fun getProjectListCustomProjection(): List<String>? {
        var data = tableCreator.executeStaticQuery(
            "select  prj.project_name , DD.datum_name ,PP.zone_name,PT.projectionType\n" +
                    "from  project_table as PRJ JOIN survey_configuration as PC ON PRJ.config_id = PC.config_id\n" +
                    "JOIN datum_data as DD ON DD.datum_id = PC.datum_id\n" +
                    "JOIN projectionParameters as PP ON PP.projectionParam_id =  PC.projectionParam_id\n" +
                    "JOIN projectiontype as PT ON PT.projectiontype_id = PP.projectiontype_id"
        )

        return data
    }

    fun BluetoothConfigurationData(apiResponse: String) {

        val modal_type = "modal_type"
        val modal_typeColumn = arrayOf(
            TableCreator.ColumnDetails("modal_type_id", "INTEGER", true),
            TableCreator.ColumnDetails("type", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )

        val modal_typeTable = tableCreator.createMainTableIfNeeded(modal_type, modal_typeColumn)


        val sub_division_selection = "sub_division_selection"
        val sub_division_selectionColumn = arrayOf(
            TableCreator.ColumnDetails("sub_division_selection_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val sub_division_selectionTable = tableCreator.createMainTableIfNeeded(
            sub_division_selection, sub_division_selectionColumn
        )


        val command_type = "command_type"
        val command_typeColumn = arrayOf(
            TableCreator.ColumnDetails("command_type_id", "INTEGER", true),
            TableCreator.ColumnDetails("name", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
        )
        val command_typeTable =
            tableCreator.createMainTableIfNeeded(command_type, command_typeColumn)

        val command = "command"
        val commandColumn = arrayOf(
            TableCreator.ColumnDetails("command_id", "INTEGER", true),
            TableCreator.ColumnDetails("input", "INTEGER"),
            TableCreator.ColumnDetails("selection", "INTEGER"),
            TableCreator.ColumnDetails("command_name", "STRING"),
            TableCreator.ColumnDetails("starting_del", "STRING"),
            TableCreator.ColumnDetails("end_del", "STRING"),
            TableCreator.ColumnDetails("format", "STRING"),
            TableCreator.ColumnDetails(
                "command_type_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "command_type (command_type_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val commandTable = tableCreator.createMainTableIfNeeded(command, commandColumn)


        val parameter_type = "parameter_type"
        val parameter_typeColumn = arrayOf(
            TableCreator.ColumnDetails("parameter_type_id", "INTEGER", true),
            TableCreator.ColumnDetails("parameter_type_name", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val parameter_typeTable =
            tableCreator.createMainTableIfNeeded(parameter_type, parameter_typeColumn)


        val parameter = "parameter"
        val parameterColumn = arrayOf(
            TableCreator.ColumnDetails("parameter_id", "INTEGER", true),
            TableCreator.ColumnDetails("parameter_name", "STRING"),
            TableCreator.ColumnDetails("parameter_type_id", "STRING"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("created_by", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val parameterTable = tableCreator.createMainTableIfNeeded(parameter, parameterColumn)


        val selection = "selection"
        val selectionColumn = arrayOf(
            TableCreator.ColumnDetails("selection_id", "INTEGER", true),
            TableCreator.ColumnDetails("selection_value_no", "INTEGER"),
            TableCreator.ColumnDetails(
                "command_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "command(command_id)"
            ),
            TableCreator.ColumnDetails(
                "parameter_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "parameter(parameter_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val selectionTable = tableCreator.createMainTableIfNeeded(selection, selectionColumn)


        val selection_value = "selection_value"
        val selection_valueColumn = arrayOf(
            TableCreator.ColumnDetails("selection_value_id", "INTEGER", true),
            TableCreator.ColumnDetails("display_value", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("byte_value", "STRING"),
            TableCreator.ColumnDetails(
                "selection_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "selection(selection_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val selection_valueTable =
            tableCreator.createMainTableIfNeeded(selection_value, selection_valueColumn)


        val fixed_response = "fixed_response"
        val fixed_responseColumn = arrayOf(
            TableCreator.ColumnDetails("fixed_response_id", "INTEGER", true),
            TableCreator.ColumnDetails("fixed_response_value_no", "INTEGER"),
            TableCreator.ColumnDetails(
                "parameter_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "parameter (parameter_id)"
            ),
            TableCreator.ColumnDetails("no_of_byte", "STRING"),
            TableCreator.ColumnDetails("created_by", "STRING"),
            TableCreator.ColumnDetails("start_pos", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val fixed_responseTable =
            tableCreator.createMainTableIfNeeded(fixed_response, fixed_responseColumn)

        val response_sub_division_selection = "response_sub_division_selection"
        val response_sub_division_selectionColumn = arrayOf(
            TableCreator.ColumnDetails("response_sub_division_selection_id", "INTEGER", true),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val response_sub_division_selectionTable = tableCreator.createMainTableIfNeeded(
            response_sub_division_selection,
            response_sub_division_selectionColumn
        )

        val response_type = "response_type"
        val response_typeColumn = arrayOf(
            TableCreator.ColumnDetails("response_type_id", "INTEGER", true),
            TableCreator.ColumnDetails("response_type", "STRING"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER")
        )
        val response_typeTable =
            tableCreator.createMainTableIfNeeded(response_type, response_typeColumn)

        val device_type = "device_type"
        val device_typeColumn = arrayOf(
            TableCreator.ColumnDetails("device_type_id", "INTEGER", true),
            TableCreator.ColumnDetails("type", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val device_typeTable = tableCreator.createMainTableIfNeeded(device_type, device_typeColumn)


        val response = "response"
        val responseColumn = arrayOf(
            TableCreator.ColumnDetails("response_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("response_name", "STRING"),
            TableCreator.ColumnDetails("format", "STRING"),
            TableCreator.ColumnDetails("flag", "STRING"),
            TableCreator.ColumnDetails("fixed_response", "STRING"),
            TableCreator.ColumnDetails("bitwise_response", "STRING"),
            TableCreator.ColumnDetails("data_extract_type", "STRING"),
            TableCreator.ColumnDetails("variable_response", "STRING"),
            TableCreator.ColumnDetails("starting_del", "STRING"),
            TableCreator.ColumnDetails("end_del", "STRING"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails(
                "response_type_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "response_type (response_type_id)"
            ),
            TableCreator.ColumnDetails(
                "command_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "command (command_id)"
            ),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val responseTable = tableCreator.createMainTableIfNeeded(response, responseColumn)


        val delimeter_validation = "delimeter_validation"
        val delimeter_validationColumn = arrayOf(
            TableCreator.ColumnDetails("delimeter_validation_id", "INTEGER", true),
            TableCreator.ColumnDetails("type", "STRING"),
            TableCreator.ColumnDetails("validation_value", "STRING"),
            TableCreator.ColumnDetails("validation_index", "INTEGER"),
            TableCreator.ColumnDetails(
                "response_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "response (response_id)"
            ),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val delimeter_validationTable =
            tableCreator.createMainTableIfNeeded(delimeter_validation, delimeter_validationColumn)


        val manufacturer = "manufacturer"
        val manufacturerColumn = arrayOf(
            TableCreator.ColumnDetails("manufacturer_id", "INTEGER", true),
            TableCreator.ColumnDetails("name", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val manufacturerTable =
            tableCreator.createMainTableIfNeeded(manufacturer, manufacturerColumn)


        val response_param_map = "response_param_map"
        val response_param_mapColumn = arrayOf(
            TableCreator.ColumnDetails("response_param_map_id", "INTEGER", true),
            TableCreator.ColumnDetails(
                "selection_value_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "selection_value (selection_value_id)"
            ),
            TableCreator.ColumnDetails(
                "response_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "response (response_id)"
            ),
            TableCreator.ColumnDetails(
                "parameter_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "parameter (parameter_id)"
            ),
            TableCreator.ColumnDetails(
                "sub_division_selection_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "sub_division_selection(sub_division_selection_id)"
            ),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val response_param_mapTable =
            tableCreator.createMainTableIfNeeded(response_param_map, response_param_mapColumn)


        val variable_response = "variable_response"
        val variable_responseColumn = arrayOf(
            TableCreator.ColumnDetails("variable_response_id", "INTEGER", true),
            TableCreator.ColumnDetails("no_of_byte", "STRING"),
            TableCreator.ColumnDetails("start_pos", "STRING"),
            TableCreator.ColumnDetails(
                "response_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "response (response_id)"
            ),
            TableCreator.ColumnDetails(
                "parameter_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "parameter (parameter_id)"
            ),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val variable_responseTable =
            tableCreator.createMainTableIfNeeded(variable_response, variable_responseColumn)


        val parameter_default_value = "parameter_default_value"
        val parameter_default_valueColumn = arrayOf(
            TableCreator.ColumnDetails("parameter_default_value_id", "INTEGER", true),
            TableCreator.ColumnDetails("selection_default_value", "STRING"),
            TableCreator.ColumnDetails(
                "selection_value_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "selection_value (selection_value_id)"
            ),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("sub_division_default_value", "STRING"),
            TableCreator.ColumnDetails(
                "sub_division_selection_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "sub_division_selection (sub_division_selection_id)"
            )
        )
        val parameter_default_valueTable = tableCreator.createMainTableIfNeeded(
            parameter_default_value,
            parameter_default_valueColumn
        )


        val constellation = "constellation"
        val constellationColumn = arrayOf(
            TableCreator.ColumnDetails("constellation_id", "INTEGER", true),
            TableCreator.ColumnDetails("constellation_name", "STRING"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_by", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
        )
        val constellationTable =
            tableCreator.createMainTableIfNeeded(constellation, constellationColumn)


        val byte_data_response = "byte_data_response"
        val byte_data_responseColumn = arrayOf(
            TableCreator.ColumnDetails("byte_data_response_id", "INTEGER", true),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_by", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
        )
        val byte_data_responseTable =
            tableCreator.createMainTableIfNeeded(byte_data_response, byte_data_responseColumn)


        val model = "model"
        val modelColumn = arrayOf(
            TableCreator.ColumnDetails("model_id", "INTEGER", true),
            TableCreator.ColumnDetails("device_name", "STRING"),
            TableCreator.ColumnDetails("device_no", "STRING"),
            TableCreator.ColumnDetails("device_address", "STRING"),
            TableCreator.ColumnDetails(
                "modal_type_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "modal_type(modal_type_id)"
            ),
            TableCreator.ColumnDetails("no_of_module", "STRING"),
            TableCreator.ColumnDetails("warranty_period", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val modelTable = tableCreator.createMainTableIfNeeded(model, modelColumn)

        val command_param_map = "command_param_map"
        val command_param_mapColumn = arrayOf(
            TableCreator.ColumnDetails("command_param_map_id", "INTEGER", true),
            TableCreator.ColumnDetails(
                "command_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "command (command_id)"
            ),
            TableCreator.ColumnDetails(
                "selection_value_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "selection_value (selection_value_id)"
            ),
            TableCreator.ColumnDetails(
                "parameter_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "parameter (parameter_id)"
            ),
            TableCreator.ColumnDetails(
                "sub_division_selection_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "sub_division_selection (sub_division_selection_id)"
            ),
        )
        val command_param_mapTable =
            tableCreator.createMainTableIfNeeded(command_param_map, command_param_mapColumn)


        val operation = "operation"
        val operationColumn = arrayOf(
            TableCreator.ColumnDetails("operation_id", "INTEGER", true),
            TableCreator.ColumnDetails("operation_name", "STRING"),
            TableCreator.ColumnDetails(
                "parent_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "operation (operation_id)"
            ),
            TableCreator.ColumnDetails("is_super_child", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val operationTable = tableCreator.createMainTableIfNeeded(operation, operationColumn)


        val fixed_response_value = "fixed_response_value"
        val fixed_response_valueColumn = arrayOf(
            TableCreator.ColumnDetails("fixed_response_value_id", "INTEGER", true),
            TableCreator.ColumnDetails("display_value", "STRING"),
            TableCreator.ColumnDetails("select_value", "STRING"),
            TableCreator.ColumnDetails(
                "fixed_response_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "fixed_response (fixed_response_id)"
            ),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val fixed_response_valueTable =
            tableCreator.createMainTableIfNeeded(fixed_response_value, fixed_response_valueColumn)


        val device = "device"
        val deviceColumn = arrayOf(
            TableCreator.ColumnDetails("device_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails(
                "manufacture_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "manufacturer (manufacture_id)"
            ),
            TableCreator.ColumnDetails(
                "device_type_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device_type (device_type_id)"
            ),
            TableCreator.ColumnDetails(
                "model_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "model (model_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("created_by", "STRING"),
        )
        val deviceTable = tableCreator.createMainTableIfNeeded(device, deviceColumn)


        val services = "services"
        val servicesColumn = arrayOf(
            TableCreator.ColumnDetails("services_id", "INTEGER", true),
            TableCreator.ColumnDetails("service_name", "STRING"),
            TableCreator.ColumnDetails("service_uuid", "STRING"),
            TableCreator.ColumnDetails(
                "device_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device (device_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val servicesTable = tableCreator.createMainTableIfNeeded(services, servicesColumn)


        val charachtristics = "charachtristics"
        val charachtristicsColumn = arrayOf(
            TableCreator.ColumnDetails("char_id", "INTEGER", true),
            TableCreator.ColumnDetails("char_name", "STRING"),
            TableCreator.ColumnDetails("uuid", "STRING"),
            TableCreator.ColumnDetails(
                "service_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "services(services_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING"),
        )
        val charachtristicsTable =
            tableCreator.createMainTableIfNeeded(charachtristics, charachtristicsColumn)


        val input = "input"
        val inputColumn = arrayOf(
            TableCreator.ColumnDetails("input_id", "INTEGER", true),
            TableCreator.ColumnDetails(
                "command_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "command (command_id)"
            ),
            TableCreator.ColumnDetails(
                "parameter_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "parameter (parameter_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("response_id", "INTEGER")
        )
        val inputTable = tableCreator.createMainTableIfNeeded(input, inputColumn)


        val command_device_map = "command_device_map"
        val command_device_mapColumn = arrayOf(
            TableCreator.ColumnDetails("command_device_map_id", "INTEGER", true),
            TableCreator.ColumnDetails(
                "device_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device(device_id)"
            ),
            TableCreator.ColumnDetails(
                "command_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "command(command_id)"
            ),
            TableCreator.ColumnDetails(
                "operation_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "operation(operation_id)"
            ),
            TableCreator.ColumnDetails("delay", "STRING"),
            TableCreator.ColumnDetails("order_no", "INTEGER"),
            TableCreator.ColumnDetails("remark", "STRING"),
        )
        val command_device_mapTable =
            tableCreator.createMainTableIfNeeded(command_device_map, command_device_mapColumn)


        val response_sub_byte_division = "response_sub_byte_division"
        val response_sub_byte_divisionColumn = arrayOf(
            TableCreator.ColumnDetails("response_sub_byte_division_id", "INTEGER", true),
            TableCreator.ColumnDetails("remark", "STRING"),
        )
        val response_sub_byte_divisionTable = tableCreator.createMainTableIfNeeded(
            response_sub_byte_division,
            response_sub_byte_divisionColumn
        )

        val sub_byte_division = "sub_byte_division"
        val sub_byte_divisionColumn = arrayOf(
            TableCreator.ColumnDetails("response_sub_byte_division_id", "INTEGER", true),
            TableCreator.ColumnDetails("remark", "STRING"),
        )
        val sub_byte_divisionTable =
            tableCreator.createMainTableIfNeeded(sub_byte_division, sub_byte_divisionColumn)


        val device_map = "device_map"
        val device_mapColumn = arrayOf(
            TableCreator.ColumnDetails("device_map_id", "INTEGER", true),
            TableCreator.ColumnDetails(
                "finished_device_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device(device_id)"
            )
            /* ,
             TableCreator.ColumnDetails(
                 "ble_device_id",
                 "INTEGER",
                 foreignKey = true,
                 foreignKeyReference = "device(id)")*/,
            TableCreator.ColumnDetails(
                "module_device_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device(device_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val device_mapTable = tableCreator.createMainTableIfNeeded(device_map, device_mapColumn)


        val byte_data = "byte_data"
        val byte_dataColumn = arrayOf(
            TableCreator.ColumnDetails("byte_data_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val byte_dataTable = tableCreator.createMainTableIfNeeded(byte_data, byte_dataColumn)


        val constellation_model_map = "constellation_model_map"
        val constellation_model_mapColumn = arrayOf(
            TableCreator.ColumnDetails("constellation_model_map_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("constellation_id", "INTEGER"),
            TableCreator.ColumnDetails(
                "model_id", "INTEGER",
                foreignKey = true,
                foreignKeyReference = "model (model_id)"
            ),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("created_by", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val constellation_model_mapTable = tableCreator.createMainTableIfNeeded(
            constellation_model_map,
            constellation_model_mapColumn
        )


        val device_registration = "device_registration"
        val device_registrationColumn = arrayOf(
            TableCreator.ColumnDetails("device_registration_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails(
                "device_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device(device_id)"
            ),
            TableCreator.ColumnDetails("reg_no", "STRING"),
            TableCreator.ColumnDetails("manufacture_date", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("date2", "STRING")
        )
        val device_registrationTable =
            tableCreator.createMainTableIfNeeded(device_registration, device_registrationColumn)


        Log.d(
            TAG, "BluetoothConfigurationData1: " +
                    "\n fixed_responseTable:--$fixed_responseTable" +
                    "\n response_sub_division_selectionTable:--$response_sub_division_selectionTable" +
                    "\n response_typeTable:--$response_typeTable" +
                    "\n device_typeTable:--$device_typeTable" +
                    "\n delimeter_validationTable:--$delimeter_validationTable" +
                    "\n manufacturerTable:--$manufacturerTable" +
                    "\n response_param_mapTable:--$response_param_mapTable" +
                    "\n variable_responseTable:--$variable_responseTable" +
                    "\n modal_typeTable:--$modal_typeTable" +
                    "\n parameter_default_valueTable:--$parameter_default_valueTable" +
                    "\n constellationTable:--$constellationTable" +
                    "\n byte_data_responseTable:--$byte_data_responseTable" +
                    "\n parameterTable:--$parameterTable" +
                    "\n command_param_mapTable:--$command_param_mapTable" +
                    "\n command_typeTable:--$command_typeTable" +
                    "\n operationTable:--$operationTable" +
                    "\n modelTable:--$modelTable" +
                    "\n parameter_typeTable:--$parameter_typeTable" +
                    "\n fixed_response_valueTable:--$fixed_response_valueTable" +
                    "\n charachtristicsTable:--$charachtristicsTable" +
                    "\n commandTable:--$commandTable" +
                    "\n selection_valueTable:--$selection_valueTable" +
                    "\n inputTable:--$inputTable" +
                    "\n selectionTable:--$selectionTable" +
                    "\n deviceTable:--$deviceTable" +
                    "\n command_device_mapTable:--$command_device_mapTable" +
                    "\n response_sub_byte_divisionTable:--$response_sub_byte_divisionTable" +
                    "\n sub_byte_divisionTable:--$sub_byte_divisionTable" +
                    "\n servicesTable:--$servicesTable" +
                    "\n device_mapTable:--$device_mapTable" +
                    "\n sub_division_selectionTable:--$sub_division_selectionTable" +
                    "\n byte_dataTable:--$byte_dataTable" +
                    "\n responseTable:--$responseTable" +
                    "\n constellation_model_mapTable:--$constellation_model_mapTable" +
                    "\n device_registrationTable:--$device_registrationTable"


        )




        val device_configHierarchy = "device_configHierarchy"
        val device_configHierarchyColumn = arrayOf(
            TableCreator.ColumnDetails("device_configHierarchy_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails(
                "device_configHierarchy_name",
                "STRING"),
            TableCreator.ColumnDetails("parent_id", "INTEGER", foreignKey = true, foreignKeyReference = "device_configHierarchy(device_configHierarchy_id)"),
            TableCreator.ColumnDetails("is_super_child", "STRING"),
            TableCreator.ColumnDetails("generation", "INTEGER"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val device_configHierarchyTable =
            tableCreator.createMainTableIfNeeded(device_configHierarchy, device_configHierarchyColumn)

   val wifiparams = "wifiparams"
        val wifiparamsColumn = arrayOf(
            TableCreator.ColumnDetails("wifiparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails(
                "IP",
                "STRING"),
            TableCreator.ColumnDetails("portNo", "STRING"),
            TableCreator.ColumnDetails("url", "STRING"),
            TableCreator.ColumnDetails("apn", "STRING"),
            TableCreator.ColumnDetails("username", "STRING"),
            TableCreator.ColumnDetails("passwd", "STRING"),
            TableCreator.ColumnDetails("mountPoint", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val wifiparamsTable =
            tableCreator.createMainTableIfNeeded(wifiparams, wifiparamsColumn)

  val via4gparams = "via4gparams"
        val via4gparamsColumn = arrayOf(
            TableCreator.ColumnDetails("via4gparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("IP","STRING"),
            TableCreator.ColumnDetails("portNo", "STRING"),
            TableCreator.ColumnDetails("url", "STRING"),
            TableCreator.ColumnDetails("ssid", "STRING"),
            TableCreator.ColumnDetails("ssid_password", "STRING"),
            TableCreator.ColumnDetails("username", "STRING"),
            TableCreator.ColumnDetails("passwd", "STRING"),
            TableCreator.ColumnDetails("mountPoint", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val via4gparamsTable =
            tableCreator.createMainTableIfNeeded(via4gparams, via4gparamsColumn)

  val pdaparams = "pdaparams"
        val pdaparamsColumn = arrayOf(
            TableCreator.ColumnDetails("pdaparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("IP","STRING"),
            TableCreator.ColumnDetails("portNo", "STRING"),
            TableCreator.ColumnDetails("url", "STRING"),
            TableCreator.ColumnDetails("username", "STRING"),
            TableCreator.ColumnDetails("passwd", "STRING"),
            TableCreator.ColumnDetails("mountPoint", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val pdaparamsTable =
            tableCreator.createMainTableIfNeeded(pdaparams, pdaparamsColumn)



  val radiointernalparams = "radiointernalparams"
        val radiointernalparamsColumn = arrayOf(
            TableCreator.ColumnDetails("radiointernalparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("datarate","STRING"),
            TableCreator.ColumnDetails("baudrate", "STRING"),
            TableCreator.ColumnDetails("power", "STRING"),
            TableCreator.ColumnDetails("frequency", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val radiointernalparamsTable =
            tableCreator.createMainTableIfNeeded(radiointernalparams, radiointernalparamsColumn)


 val radioexternalparams = "radioexternalparams"
        val radioexternalparamsColumn = arrayOf(
            TableCreator.ColumnDetails("radioexternalparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("power","STRING"),
            TableCreator.ColumnDetails("protocol", "STRING"),
            TableCreator.ColumnDetails("frequency", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val radioexternalparamsTable =
            tableCreator.createMainTableIfNeeded(radioexternalparams, radioexternalparamsColumn)


 val type_of_communication = "type_of_communication"
        val type_of_communicationColumn = arrayOf(
            TableCreator.ColumnDetails("type_of_communication_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("communicationTypes","STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val type_of_communicationTable =
            tableCreator.createMainTableIfNeeded(type_of_communication, type_of_communicationColumn)

 val communication_type_mapping = "communication_type_mapping"
        val communication_type_mappingColumn = arrayOf(
            TableCreator.ColumnDetails("communication_type_mapping_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("type_of_communication_id","INTEGER", foreignKey = true, foreignKeyReference = "type_of_communication(communication_type_mapping_id)"),
            TableCreator.ColumnDetails("via4gparams_id","INTEGER", foreignKey = true, foreignKeyReference = "via4gParams(via4gparams_id)"),
            TableCreator.ColumnDetails("wifiparams_id","INTEGER", foreignKey = true, foreignKeyReference = "wifiparams(wifiparams_id)"),
            TableCreator.ColumnDetails("pdaparams_id","INTEGER", foreignKey = true, foreignKeyReference = "pdaparams(pdaparams_id)"),
            TableCreator.ColumnDetails("radiointernalparams_id","INTEGER", foreignKey = true, foreignKeyReference = "radiointernalparams(radiointernalparams_id)"),
            TableCreator.ColumnDetails("radioexternalparams_id","INTEGER", foreignKey = true, foreignKeyReference = "radioexternalparams (radioexternalparams_id)"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val communication_type_mappingTable =
            tableCreator.createMainTableIfNeeded(communication_type_mapping, communication_type_mappingColumn)




        if (fixed_responseTable.equals("Table Created Successfully...")
            && response_sub_division_selectionTable.equals("Table Created Successfully...")
            && response_typeTable.equals("Table Created Successfully...")
            && device_typeTable.equals("Table Created Successfully...")
            && delimeter_validationTable.equals("Table Created Successfully...")
            && manufacturerTable.equals("Table Created Successfully...")
            && response_param_mapTable.equals("Table Created Successfully...")
            && variable_responseTable.equals("Table Created Successfully...")
            && modal_typeTable.equals("Table Created Successfully...")
            && parameterTable.equals("Table Created Successfully...")
            && constellationTable.equals("Table Created Successfully...")
            && command_param_mapTable.equals("Table Created Successfully...")
            && command_typeTable.equals("Table Created Successfully...")
            && byte_data_responseTable.equals("Table Created Successfully...")
            && operationTable.equals("Table Created Successfully...")
            && parameterTable.equals("Table Created Successfully...")
            && modelTable.equals("Table Created Successfully...")
            && parameter_typeTable.equals("Table Created Successfully...")
            && fixed_response_valueTable.equals("Table Created Successfully...")
            && parameter_default_valueTable.equals("Table Created Successfully...")
            && charachtristicsTable.equals("Table Created Successfully...")
            && commandTable.equals("Table Created Successfully...")
            && selection_valueTable.equals("Table Created Successfully...")
            && inputTable.equals("Table Created Successfully...")
            && selectionTable.equals("Table Created Successfully...")
            && deviceTable.equals("Table Created Successfully...")
            && response_sub_byte_divisionTable.equals("Table Created Successfully...")
            && command_device_mapTable.equals("Table Created Successfully...")
            && sub_byte_divisionTable.equals("Table Created Successfully...")
            && sub_division_selectionTable.equals("Table Created Successfully...")
            && servicesTable.equals("Table Created Successfully...")
            && byte_dataTable.equals("Table Created Successfully...")
            && device_mapTable.equals("Table Created Successfully...")
            && responseTable.equals("Table Created Successfully...")
            && constellation_model_mapTable.equals("Table Created Successfully...")
            && device_registrationTable.equals("Table Created Successfully...")
            && device_configHierarchyTable.equals("Table Created Successfully...")
            && wifiparamsTable.equals("Table Created Successfully...")
            && via4gparamsTable.equals("Table Created Successfully...")
            && pdaparamsTable.equals("Table Created Successfully...")
            && radiointernalparamsTable.equals("Table Created Successfully...")
            && radioexternalparamsTable.equals("Table Created Successfully...")
            && type_of_communicationTable.equals("Table Created Successfully...")
            && communication_type_mappingTable.equals("Table Created Successfully...")


        ) {
            Log.d(TAG, "BluetoothConfigurationData1: All table created")
            val result = insertDBData(apiResponse)
            Log.d(TAG, "BluetoothConfigurationData: result $result")
        } else {
            Log.d(TAG, "BluetoothConfigurationData1: Error while table creation ")
        }
    }


}









