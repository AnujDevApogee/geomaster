package com.apogee.geomaster.repository

import android.content.ContentValues
import android.content.Context
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
            TableCreator.ColumnDetails("zoneHemisphere", "STRING"))
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
            && distanceunitTable.equals("Table Created Successfully...")){
            Log.d(TAG, "CommonApiTablesCreation1: All table created")
            insertCommonData(apiResponse)
        }else{
            Log.d(TAG, "CommonApiTablesCreation1: Error table ")
        }


    }

    fun insertCommonData(resp:String) {
        Log.d(TAG, "insertCommonData: $resp")

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
                    val result=tableCreator.insertDataIntoTable(key.toString(), dataList)
                    Log.d(TAG, "onCreate: result:--$result")
                }
            } catch (e: Exception) {
                Log.d(TAG, "onCreate: Exception " + e.message)
            }
        }




    }

}


