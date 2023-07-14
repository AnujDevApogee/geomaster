package com.apogee.geomaster.repository

import android.content.ContentValues
import android.content.Context
import android.os.FileObserver.CREATE
import android.util.Log
import com.apogee.databasemodule.DatabaseSingleton
import com.apogee.databasemodule.TableCreator
import org.json.JSONException
import org.json.JSONObject

class DatabaseRepsoitory(context: Context) {
    val TAG = "DBControl"


    val database by lazy {
        DatabaseSingleton.getInstance(context).getDatabase()!!
    }
    val tableCreator = TableCreator(database)


    fun CommonApiTablesCreation(apiResponse: String) {

        val zonedata = "zonedata"
        val zonedataColumn = arrayOf(
            TableCreator.ColumnDetails("zonedata_id", "INTEGER", true),
            TableCreator.ColumnDetails("zone", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING"),
            TableCreator.ColumnDetails("zoneHemisphere", "STRING")
        )
        val zonedataTable = tableCreator.createMainTableIfNeeded(zonedata, zonedataColumn)


        val datum_data = "datum_data"
        val datum_dataColumn = arrayOf(
            TableCreator.ColumnDetails("datum_id", "INTEGER", true),
            TableCreator.ColumnDetails("datum_name", "STRING", unique = true),
            TableCreator.ColumnDetails("major_axis", "STRING"),
            TableCreator.ColumnDetails("flattening", "STRING"),
            TableCreator.ColumnDetails("scale", "STRING"),
            TableCreator.ColumnDetails("x_axis_shift", "STRING", defaultValue = "0"),
            TableCreator.ColumnDetails("y_axis_shift", "STRING", defaultValue = "0"),
            TableCreator.ColumnDetails("z_axis_shift", "STRING", defaultValue = "0"),
            TableCreator.ColumnDetails("rot_x_axis", "STRING"),
            TableCreator.ColumnDetails("rot_y_axis", "STRING"),
            TableCreator.ColumnDetails("rot_z_axis", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val datum_dataTable = tableCreator.createMainTableIfNeeded(datum_data, datum_dataColumn)


        val datumtype = "datumtype"
        val datumtypeColumn = arrayOf(
            TableCreator.ColumnDetails("datumtype_id", "INTEGER", true),
            TableCreator.ColumnDetails("datumType_name", "STRING", unique = true),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val datumtypeTable = tableCreator.createMainTableIfNeeded(datumtype, datumtypeColumn)


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
            TableCreator.ColumnDetails("misc_4", "STRING"),
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
                    "\n zonedataTable:--$zonedataTable" +
                    "\n datum_dataTable:--$datum_dataTable" +
                    "\n datumtypeTable:--$datumtypeTable" +
                    "\n angleunitTable:--$angleunitTable" +
                    "\n projectiontypeTable:--$projectiontypeTable" +
                    "\n autocad_file_typeTable:--$autocad_file_typeTable" +
                    "\n autocad_file_mapTable:--$autocad_file_mapTable" +
                    "\n elevationtypeTable:--$elevationtypeTable" +
                    "\n distanceunitTable:--$distanceunitTable"
        )
        if (zonedataTable.equals("Table Created Successfully...")
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
            Log.d(TAG, "CommonApiTablesCreation1: Error table ")
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
                            Log.d(TAG, "onCreate: ${e.message}")
                        }
                    }
                    dataList.add(values1)
                    result = tableCreator.insertDataIntoTable(key.toString(), dataList)


                    Log.d(TAG, "onCreate: result:--$result")
                }

            } catch (e: Exception) {
                Log.d(TAG, "onCreate: Exception " + e.message)
            }
        }
        return result
    }


    fun projectManagementData() {


        val projectionParameters = "projectionParameters"
        val projectionParametersColumn = arrayOf(
            TableCreator.ColumnDetails("projectionParam_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("zone_name", "STRING", unique = true),
            TableCreator.ColumnDetails("origin_lat", "STRING"),
            TableCreator.ColumnDetails("origin_lng", "STRING"),
            TableCreator.ColumnDetails("false_easting", "STRING"),
            TableCreator.ColumnDetails("false_northing", "STRING"),
            TableCreator.ColumnDetails("paralell_1", "STRING"),
            TableCreator.ColumnDetails("paralell_2", "STRING"),
            TableCreator.ColumnDetails("misc1", "STRING"),
            TableCreator.ColumnDetails("misc2", "STRING"),
            TableCreator.ColumnDetails("misc3", "STRING"),
            TableCreator.ColumnDetails("misc4", "STRING"),
            TableCreator.ColumnDetails(
                "projectiontype_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "projectiontype(projectiontype_id)"
            ),
            TableCreator.ColumnDetails("created_at", "STRING")
        )
        val projectionParametersTable =
            tableCreator.createMainTableIfNeeded(projectionParameters, projectionParametersColumn)


        if(projectionParametersTable.equals("Table Created Successfully...")){
            Log.d(TAG, "projectManagementData: sfsdf")
            val paramValues = "Zone-1,45.000,0.0,0.0,0.0,40.0000,50.0000"

            val dataList: MutableList<ContentValues> = ArrayList()
            val values1=ContentValues()

            values1.put("zone_name",paramValues.split(",")[0].trim())
            values1.put("origin_lat",paramValues.split(",")[1].trim())
            values1.put("origin_lng",paramValues.split(",")[2].trim())
            values1.put("false_easting",paramValues.split(",")[3].trim())
            values1.put("false_northing",paramValues.split(",")[4].trim())
            values1.put("paralell_1",paramValues.split(",")[5].trim())
            values1.put("paralell_2",paramValues.split(",")[6].trim())
            dataList.add(values1)

            var result = tableCreator.insertDataIntoTable(projectionParameters, dataList)
            Log.d(TAG, "projectionParametersTable: $result")
        }



        val project_configuration = "project_configuration"
        val project_configurationColumn = arrayOf(
            TableCreator.ColumnDetails("config_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("config_name", "STRING"),
            TableCreator.ColumnDetails("datum_id","INTEGER",foreignKey = true,foreignKeyReference = "datum_data(datum_id)"),
            TableCreator.ColumnDetails("zone_id","INTEGER",foreignKey = true,foreignKeyReference = "zonedata(zonedata_id)"),
            TableCreator.ColumnDetails("datumType_id","INTEGER",foreignKey = true,foreignKeyReference = "datumtype(datumtype_id)"),
            TableCreator.ColumnDetails(
                "elev_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "elevationtype(elevationtype_id)"
            ),
            TableCreator.ColumnDetails(
                "disUnit_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "distanceunit(distanceunit_id)"
            ),
            TableCreator.ColumnDetails(
                "angUnit_id",
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
        val project_configurationTable =
            tableCreator.createMainTableIfNeeded(project_configuration, project_configurationColumn)


        val project_status = "project_status"
        val project_statusColumn = arrayOf(
            TableCreator.ColumnDetails("status_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("status_types", "STRING"),)
        val project_statusTable =
            tableCreator.createMainTableIfNeeded(project_status, project_statusColumn)

        if(project_statusTable.equals("Table Created Successfully...")){
            val dataList: MutableList<ContentValues> = ArrayList()
            val values1=ContentValues()
            values1.put("status_types","Active")
            dataList.add(values1)
            var result = tableCreator.insertDataIntoTable(project_status, dataList)
            dataList.clear()
            values1.put("status_types","Inactive")
            Log.d(TAG, "projectManagementData:result $result")

            dataList.add(values1)

             result = tableCreator.insertDataIntoTable(project_status, dataList)
            Log.d(TAG, "projectManagementData:result $result")


        }


        val shortNameTable = "shortNameTable"
        val shortNameTableColumn = arrayOf(
            TableCreator.ColumnDetails("shortName_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("shortName", "STRING"),
            TableCreator.ColumnDetails("project_id", "INTEGER"),)
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
            TableCreator.ColumnDetails("project_id", "INTEGER"),)
        val siteCalibrationTable =
            tableCreator.createMainTableIfNeeded(siteCalibration, siteCalibrationColumn)


       val project_folder = "project_folder"
        val project_folderColumn = arrayOf(
            TableCreator.ColumnDetails("folder_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("folderName", "STRING"),
            TableCreator.ColumnDetails("folderPath", "STRING"),
            TableCreator.ColumnDetails("fileTypes", "STRING"),
            TableCreator.ColumnDetails("folderCreatedAt", "STRING"))
        val project_folderTable =
            tableCreator.createMainTableIfNeeded(project_folder, project_folderColumn)



        val project_table = "project_table"
        val project_tableColumn = arrayOf(
            TableCreator.ColumnDetails("project_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("project_name", "STRING"),
            TableCreator.ColumnDetails("operator", "STRING"),
            TableCreator.ColumnDetails("comment", "STRING"),
            TableCreator.ColumnDetails("folder_id", "INTEGER",foreignKey = true,foreignKeyReference = "project_folder(folder_id)"),
            TableCreator.ColumnDetails("siteCal_id", "INTEGER",foreignKey = true,foreignKeyReference = "siteCalibration(siteCal_id)"),
            TableCreator.ColumnDetails("status_id", "INTEGER",foreignKey = true,foreignKeyReference = "project_status(status_id)"),
            TableCreator.ColumnDetails("config_id", "INTEGER",foreignKey = true,foreignKeyReference = "project_configuration(config_id)"),
            TableCreator.ColumnDetails("projectCreated_at", "STRING")
        )
        val project_tableData =
            tableCreator.createMainTableIfNeeded(project_table, project_tableColumn)


    }


}









