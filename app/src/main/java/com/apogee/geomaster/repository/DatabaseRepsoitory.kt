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
            TableCreator.ColumnDetails("continent_id", "INTEGER",
                foreignKey = true, foreignKeyReference = "continents(continent_id)" ),
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
            TableCreator.ColumnDetails("datumType_id", "INTEGER", foreignKey = true, foreignKeyReference = "datumtype(datumType_id)"),
            TableCreator.ColumnDetails("country_id", "INTEGER", foreignKey = true, foreignKeyReference = "countries(country_id)"),
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
            ))
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
            TableCreator.ColumnDetails("misc_4", "STRING") )
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
            insertCommonData(apiResponse)
            projectManagementData()
        } else {
            Log.d(TAG, "CommonApiTablesCreation1: Error while table creation ")
        }


    }


    fun insertCommonData(resp: String): String {
        Log.d(TAG, "insertCommonData: $resp")
        var result = ""
        val jsonObject = JSONObject(resp)
        for (key in jsonObject.keys()) {

            val dataList: MutableList<ContentValues> = ArrayList()
            val jsonArray = jsonObject.getJSONArray(key)
            try {
                for (i in 0 until jsonArray.length()) {
                    if(key.equals("datum_data")){
                        Log.d(TAG, "insertCommonData: datum_data $key--${jsonArray.getJSONObject(i)}  ")
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
        }
        return result
    }


    fun projectManagementData() {

        val project_configuration = "project_configuration"
        val project_configurationColumn = arrayOf(
            TableCreator.ColumnDetails("config_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("config_name", "STRING", unique = true),
            TableCreator.ColumnDetails("zonedata_id","INTEGER",foreignKey = true,foreignKeyReference = "zonedata(zonedata_id)"),
            TableCreator.ColumnDetails("datum_id","INTEGER",foreignKey = true,foreignKeyReference = "datum_data(datum_id)"),
            TableCreator.ColumnDetails("elevationtype_id","INTEGER",foreignKey = true,foreignKeyReference = "elevationtype(elevationtype_id)"),
            TableCreator.ColumnDetails("distanceunit_id","INTEGER",foreignKey = true,foreignKeyReference = "distanceunit(distanceunit_id)"),
            TableCreator.ColumnDetails("angleunit_id","INTEGER",foreignKey = true,foreignKeyReference = "angleunit(angleunit_id)"),
            TableCreator.ColumnDetails("projectionParam_id","INTEGER",foreignKey = true,foreignKeyReference = "projectionParameters(projectionParam_id)"),
            TableCreator.ColumnDetails("config_time", "STRING")

        )
        val project_configurationTable =
            tableCreator.createMainTableIfNeeded(project_configuration, project_configurationColumn)


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
            TableCreator.ColumnDetails("folder_id","INTEGER",foreignKey = true,foreignKeyReference = "project_folder(folder_id)"),
            TableCreator.ColumnDetails("siteCal_id","INTEGER",foreignKey = true,foreignKeyReference = "siteCalibration(siteCal_id)"),
            TableCreator.ColumnDetails("status_id","INTEGER",foreignKey = true,foreignKeyReference = "project_status(status_id)"),
            TableCreator.ColumnDetails("config_id","INTEGER",foreignKey = true,foreignKeyReference = "project_configuration(config_id)"),
            TableCreator.ColumnDetails("projectCreated_at", "STRING")
        )
        val project_tableData =
            tableCreator.createMainTableIfNeeded(project_table, project_tableColumn)
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
    fun getContinentId(continent_name : String): String {
        var data = tableCreator.executeStaticQuery("SELECT continent_id FROM continents where continent_name='"+continent_name +"'")
        return data?.get(0) ?: ""
    }
    fun getCountryName(continentId:Int): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT country_name FROM countries where continent_id = "+continentId+"")
        Log.d(TAG, "getCountryName: $data")
        return data
    }
    fun getCountryId(country_name : String): String {
        var data = tableCreator.executeStaticQuery("SELECT country_id FROM countries where country_name='"+country_name+"'")
        return data?.get(0) ?: ""
    }
    fun getPredefinedDatumName(country_id:Int): List<String>? {
        var data :List<String> =ArrayList()

        try {

        data =
            tableCreator.executeStaticQuery("SELECT datum_name FROM datum_data where country_id = "+country_id+"")!!
        Log.d(TAG, "getPredefinedDatumName:  $data")
        }catch (e:Exception){
            Log.d(TAG, "getPredefinedDatumName:Exception ${e.message} ")
        }
        return data
    }
    fun getDatumId(datum_name : String): String {
        var data = tableCreator.executeStaticQuery("SELECT datum_id FROM datum_data where datum_name='"+datum_name+"'")
        return data?.get(0) ?: ""
    }
    fun getprojectionParamData(projectiontype_id:Int): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT zone_name FROM projectionParameters where projectiontype_id="+projectiontype_id+"")
        return data
    }
    fun getprojectionParamDataID(zone_name : String): String {
        var data = tableCreator.executeStaticQuery("SELECT projectionParam_id FROM projectionParameters where zone_name='"+zone_name+"'")
        return data?.get(0) ?: ""
    }
    fun angleUnitdata(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT angUnit_name FROM angleunit where active = 'Y' ")
        return data
    }
    fun angleUnitID(angUnit_name : String): String {
        var data = tableCreator.executeStaticQuery("SELECT angleunit_id FROM angleunit where angUnit_name='"+angUnit_name+"'")
        return data?.get(0) ?: ""
    }
    fun getDistanceUnit(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT disUnit_name FROM distanceunit where active = 'Y' ")
        return data
    }
    fun getDistanceUnitID(disUnit_name : String): String {
        var data = tableCreator.executeStaticQuery("SELECT distanceunit_id FROM distanceunit where disUnit_name='"+disUnit_name+"'")
        return data?.get(0) ?: ""
    }
    fun getZoneHemisphereData(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT zoneHemisphere FROM hemisphere where active = 'Y' ")

        return data
    }
    fun getZoneHemisphereID(zoneHemisphere : String): String {
        var data = tableCreator.executeStaticQuery("SELECT hemisphere_id FROM hemisphere where zoneHemisphere='"+zoneHemisphere+"'")
        return data?.get(0) ?: ""
    }
    fun getZoneData(): List<String>? {
        val data = tableCreator.executeStaticQuery("SELECT zone FROM zonedata where active = 'Y' ")
        return data
    }
    fun getZoneDataID(zone : String): String {
        var data = tableCreator.executeStaticQuery("SELECT zonedata_id FROM zonedata where zone='"+zone+"'")
        return data?.get(0) ?: ""
    }
    fun getProjectionType(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT projectionType FROM projectiontype where active = 'Y'")
        return data
    }
    fun getProjectionTypeID(projectionType : String): String {
        var data = tableCreator.executeStaticQuery("SELECT projectiontype_id FROM projectiontype where projectionType='"+projectionType+"'")
        return data?.get(0) ?: ""
    }
    fun getdatumtype(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT datumType_name FROM datumtype where active = 'Y' ")
        return data
    }
    fun getdatumtypeID(datumType_name : String): String {
        var data = tableCreator.executeStaticQuery("SELECT datumType_id FROM datumtype where datumType_name='"+datumType_name+"'")
        return data?.get(0) ?: ""
    }
    fun getelevationType(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT elevationType FROM elevationtype  where active = 'Y' ")
        return data
    }
    fun getelevationTypeID(elevationtype : String): String {
        var data = tableCreator.executeStaticQuery("SELECT elevationtype_id FROM elevationtype where elevationType='"+elevationtype+"'")
        return data?.get(0) ?: ""
    }

    fun getproject_configurationID(config_name : String): String {
        var data = tableCreator.executeStaticQuery("SELECT config_id FROM project_configuration where config_name='"+config_name+"'")
        return data?.get(0) ?: ""
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addConfigurationData(map: HashMap<String, String>): String {

        val result = tableCreator.getTableSchema("project_configuration")
        var insertResult= ""

        if (result.equals("")) {

            val project_configuration = "project_configuration"
            val project_configurationColumn = arrayOf(
                TableCreator.ColumnDetails("config_id", "INTEGER", true, true),
                TableCreator.ColumnDetails("config_name", "STRING", unique = true),
                TableCreator.ColumnDetails("zonedata_id","INTEGER",foreignKey = true,foreignKeyReference = "zonedata(zonedata_id)"),
                TableCreator.ColumnDetails("datum_id","INTEGER",foreignKey = true,foreignKeyReference = "datum_data(datum_id)"),
                TableCreator.ColumnDetails("elevationtype_id","INTEGER",foreignKey = true,foreignKeyReference = "elevationtype(elevationtype_id)"),
                TableCreator.ColumnDetails("distanceunit_id","INTEGER",foreignKey = true,foreignKeyReference = "distanceunit(distanceunit_id)"),
                TableCreator.ColumnDetails("angleunit_id","INTEGER",foreignKey = true,foreignKeyReference = "angleunit(angleunit_id)"),
                TableCreator.ColumnDetails("projectionParam_id","INTEGER",foreignKey = true,foreignKeyReference = "projectionParameters(projectionParam_id)"),
                TableCreator.ColumnDetails("config_time", "STRING")
            )
            val project_configurationTable =
                tableCreator.createMainTableIfNeeded(project_configuration, project_configurationColumn)


        } else {
            Log.d(TAG, "addConfigurationData: Else")

            val dataList: MutableList<ContentValues> = ArrayList()
            val values1 = ContentValues()
            Log.d(TAG, "addConfigurationData:projectName ${map.get("projectName")}")
            values1.put("config_name", map.get("projectName") )
            values1.put("datum_id",  map.get("datumName"))
            values1.put("zonedata_id",  map.get("zoneData"))
            values1.put("elevationtype_id",  map.get("elevation"))
            values1.put("distanceunit_id",  map.get("distanceUnit"))
            values1.put("angleunit_id",  map.get("angleUnit"))
            values1.put("projectionParam_id",  map.get("zoneProjection"))
            values1.put("config_time", "${LocalDateTime.now()}")
            dataList.add(values1)

            insertResult= tableCreator.insertDataIntoTable("project_configuration",dataList)
        }
        return insertResult
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun defaultProjectConfig(map: HashMap<String, String>):String{
        var result=""
        val dataList: MutableList<ContentValues> = ArrayList()
        val values1 = ContentValues()
        values1.put("config_name",map.get("config_name"))
        values1.put("datum_id",  map.get("datumName"))
        values1.put("zonedata_id", map.get("zoneData"))
        values1.put("elevationtype_id",  map.get("elevation"))
        values1.put("distanceunit_id",  map.get("distanceUnit"))
        values1.put("angleunit_id",  map.get("angleUnit"))
        values1.put("projectionParam_id",  "")
        values1.put("config_time", "${ LocalDateTime.now()}")
        dataList.add(values1)

        result= tableCreator.insertDataIntoTable("project_configuration",dataList)
        return result
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addProjectData(list: List<String>): String{

        val dataList: MutableList<ContentValues> = ArrayList()
        val values1 = ContentValues()

        values1.put("project_name", list.get(0))
        values1.put("operator",  list.get(2))
        values1.put("comment",  list.get(3))
        values1.put("config_id",  list.get(1))
        values1.put("projectCreated_at",  "${ LocalDateTime.now()}")
        values1.put("status_id",  "1")
        dataList.add(values1)

        val result=tableCreator.insertDataIntoTable("project_table",dataList)
        return result
    }

    fun getProjectList(): List<String>? {
        var data = tableCreator.executeStaticQuery("SELECT   prj.project_name ,dd.datum_name, z.zone\n" +
                "FROM project_table AS prj  ,project_configuration AS conf,datum_data as dd, zonedata as z\n" +
                " where prj. config_id=conf.config_id and conf.datum_id=dd.datum_id and \n" +
                " conf.zonedata_id = z.zonedata_id")

        return data
    }
}









