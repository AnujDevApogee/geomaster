package com.apogee.geomaster.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.apogee.basicble.Utils.DBResponseModel
import com.apogee.basicble.Utils.DelimeterResponse
import com.apogee.basicble.Utils.SateliteTypeModel
import com.apogee.databasemodule.DatabaseSingleton
import com.apogee.databasemodule.TableCreator
import com.apogee.geomaster.model.SatelliteModel
import com.apogee.geomaster.utils.createLog
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime


class DatabaseRepsoitory(context: Context) {
    val TAG = "DBControl"


    val database by lazy {
        DatabaseSingleton.getInstance(context).getDatabase()!!
    }
    val tableCreator = TableCreator(database)


    fun CommonApi_TablesCreation(apiResponse: String) {

        val coordinateSystem = "coordinateSystem"
        val coordinateSystemColumn = arrayOf(
            TableCreator.ColumnDetails("coordinateSystem_id", "INTEGER", true),
            TableCreator.ColumnDetails("coordinateSystem_name", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("created_by", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val coordinateSystemTable =
            tableCreator.createMainTableIfNeeded(coordinateSystem, coordinateSystemColumn)


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
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
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
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val continentsTable = tableCreator.createMainTableIfNeeded(continents, continentsColumn)


        val countries = "countries"
        val countriesColumn = arrayOf(
            TableCreator.ColumnDetails("country_id", "INTEGER", true),
            TableCreator.ColumnDetails("country_name", "STRING", unique = true),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
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
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
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
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("datum_command", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val datum_dataTable = tableCreator.createMainTableIfNeeded(datum_data, datum_dataColumn)


        val angleunit = "angleunit"
        val angleunitColumn = arrayOf(
            TableCreator.ColumnDetails("angleunit_id", "INTEGER", true),
            TableCreator.ColumnDetails("angUnit_name", "STRING", unique = true),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val angleunitTable = tableCreator.createMainTableIfNeeded(angleunit, angleunitColumn)


        val projectiontype = "projectiontype"
        val projectiontypeColumn = arrayOf(
            TableCreator.ColumnDetails("projectiontype_id", "INTEGER", true),
            TableCreator.ColumnDetails("projectionType", "STRING", unique = true),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val projectiontypeTable =
            tableCreator.createMainTableIfNeeded(projectiontype, projectiontypeColumn)


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
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("misc1", "STRING"),
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
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
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
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
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
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val elevationtypeTable =
            tableCreator.createMainTableIfNeeded(elevationtype, elevationtypeColumn)


        val distanceunit = "distanceunit"
        val distanceunitColumn = arrayOf(
            TableCreator.ColumnDetails("distanceunit_id", "INTEGER", true),
            TableCreator.ColumnDetails("disUnit_name", "STRING", unique = true),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("active", "STRING")
        )
        val distanceunitTable =
            tableCreator.createMainTableIfNeeded(distanceunit, distanceunitColumn)

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
            TableCreator.ColumnDetails(
                "parameter_type_id",
                "STRING",
                foreignKey = true,
                foreignKeyReference = "parameter_type (parameter_type_id)"
            ),
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
        val device_typeTable =
            tableCreator.createMainTableIfNeeded(device_type, device_typeColumn)


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
            TableCreator.ColumnDetails("command_accepted", "STRING"),
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
        val parameter_default_valueTable =
            tableCreator.createMainTableIfNeeded(
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
                "model_type_id",
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
                foreignKeyReference = "manufacturer (manufacturer_id)"
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
        val deviceTable =
            tableCreator.createMainTableIfNeeded(device, deviceColumn)


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
        val servicesTable =
            tableCreator.createMainTableIfNeeded(services, servicesColumn)


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


        val device_characteristic_ble_map = "device_characteristic_ble_map"
        val device_characteristic_ble_mapColumn = arrayOf(
            TableCreator.ColumnDetails("device_characteristic_ble_map_id", "INTEGER", true),
            TableCreator.ColumnDetails("write_characteristic_id", "INTEGER"),
            TableCreator.ColumnDetails("order_no", "STRING"),
            TableCreator.ColumnDetails("read_characteristic_id", "INTEGER"),
            TableCreator.ColumnDetails("ble_operation_name_id", "INTEGER"),
            TableCreator.ColumnDetails(
                "device_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device (device_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val device_characteristic_ble_mapTable = tableCreator.createMainTableIfNeeded(
            device_characteristic_ble_map,
            device_characteristic_ble_mapColumn
        )
        Log.d(
            TAG,
            "CommonApi_TablesCreation:device_characteristic_ble_mapTable $device_characteristic_ble_mapTable "
        )


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
            TableCreator.ColumnDetails(
                "response_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "response(response_id)"
            )
        )
        val inputTable =
            tableCreator.createMainTableIfNeeded(input, inputColumn)


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
        val response_sub_byte_divisionTable =
            tableCreator.createMainTableIfNeeded(
                response_sub_byte_division,
                response_sub_byte_divisionColumn
            )


        val sub_byte_division = "sub_byte_division"
        val sub_byte_divisionColumn = arrayOf(
            TableCreator.ColumnDetails("sub_byte_division_id", "INTEGER", true),
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
            ),
            TableCreator.ColumnDetails(
                "module_device_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device(device_id)"
            ),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val device_mapTable =
            tableCreator.createMainTableIfNeeded(device_map, device_mapColumn)


        val byte_data = "byte_data"
        val byte_dataColumn = arrayOf(
            TableCreator.ColumnDetails("byte_data_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("remark", "STRING")
        )
        val byte_dataTable =
            tableCreator.createMainTableIfNeeded(byte_data, byte_dataColumn)


        val constellation_model_map = "constellation_model_map"
        val constellation_model_mapColumn = arrayOf(
            TableCreator.ColumnDetails("constellation_model_map_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails(
                "constellation_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "constellation(constellation_id)"
            ),
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
        val constellation_model_mapTable =
            tableCreator.createMainTableIfNeeded(
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


        val device_configHierarchy = "device_configHierarchy"
        val device_configHierarchyColumn = arrayOf(
            TableCreator.ColumnDetails("device_configHierarchy_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails(
                "device_configHierarchy_name",
                "STRING"
            ),
            TableCreator.ColumnDetails(
                "parent_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device_configHierarchy(device_configHierarchy_id)"
            ),
            TableCreator.ColumnDetails("is_super_child", "STRING"),
            TableCreator.ColumnDetails("generation", "INTEGER"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val device_configHierarchyTable =
            tableCreator.createMainTableIfNeeded(
                device_configHierarchy,
                device_configHierarchyColumn
            )

        val wifiparams = "wifiparams"
        val wifiparamsColumn = arrayOf(
            TableCreator.ColumnDetails("wifiparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails(
                "IP",
                "STRING"
            ),
            TableCreator.ColumnDetails("portNo", "STRING"),
            TableCreator.ColumnDetails("url", "STRING"),
            TableCreator.ColumnDetails("apn", "STRING"),
            TableCreator.ColumnDetails("username", "STRING"),
            TableCreator.ColumnDetails("passwd", "STRING"),
            TableCreator.ColumnDetails("mountPoint", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val wifiparamsTable =
            tableCreator.createMainTableIfNeeded(wifiparams, wifiparamsColumn)

        val via4gparams = "via4gparams"
        val via4gparamsColumn = arrayOf(
            TableCreator.ColumnDetails("via4gparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("IP", "STRING"),
            TableCreator.ColumnDetails("portNo", "STRING"),
            TableCreator.ColumnDetails("url", "STRING"),
            TableCreator.ColumnDetails("ssid", "STRING"),
            TableCreator.ColumnDetails("ssid_password", "STRING"),
            TableCreator.ColumnDetails("username", "STRING"),
            TableCreator.ColumnDetails("passwd", "STRING"),
            TableCreator.ColumnDetails("mountPoint", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val via4gparamsTable =
            tableCreator.createMainTableIfNeeded(via4gparams, via4gparamsColumn)

        val pdaparams = "pdaparams"
        val pdaparamsColumn = arrayOf(
            TableCreator.ColumnDetails("pdaparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("IP", "STRING"),
            TableCreator.ColumnDetails("portNo", "STRING"),
            TableCreator.ColumnDetails("url", "STRING"),
            TableCreator.ColumnDetails("username", "STRING"),
            TableCreator.ColumnDetails("passwd", "STRING"),
            TableCreator.ColumnDetails("mountPoint", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val pdaparamsTable =
            tableCreator.createMainTableIfNeeded(pdaparams, pdaparamsColumn)


        val radiointernalparams = "radiointernalparams"
        val radiointernalparamsColumn = arrayOf(
            TableCreator.ColumnDetails("radiointernalparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("datarate", "STRING"),
            TableCreator.ColumnDetails("baudrate", "STRING"),
            TableCreator.ColumnDetails("power", "STRING"),
            TableCreator.ColumnDetails("frequency", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val radiointernalparamsTable =
            tableCreator.createMainTableIfNeeded(radiointernalparams, radiointernalparamsColumn)


        val radioexternalparams = "radioexternalparams"
        val radioexternalparamsColumn = arrayOf(
            TableCreator.ColumnDetails("radioexternalparams_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("power", "STRING"),
            TableCreator.ColumnDetails("protocol", "STRING"),
            TableCreator.ColumnDetails("frequency", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val radioexternalparamsTable =
            tableCreator.createMainTableIfNeeded(radioexternalparams, radioexternalparamsColumn)


        val type_of_communication = "type_of_communication"
        val type_of_communicationColumn = arrayOf(
            TableCreator.ColumnDetails("type_of_communication_id", "INTEGER", primaryKey = true),
            TableCreator.ColumnDetails("communicationTypes", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val type_of_communicationTable =
            tableCreator.createMainTableIfNeeded(type_of_communication, type_of_communicationColumn)


        val communication_type_mapping = "communication_type_mapping"
        val communication_type_mappingColumn = arrayOf(
            TableCreator.ColumnDetails(
                "communication_type_mapping_id",
                "INTEGER",
                primaryKey = true
            ),
            TableCreator.ColumnDetails(
                "type_of_communication_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "type_of_communication(communication_type_mapping_id)"
            ),
            TableCreator.ColumnDetails(
                "via4gparams_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "via4gParams(via4gparams_id)"
            ),
            TableCreator.ColumnDetails(
                "wifiparams_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "wifiparams(wifiparams_id)"
            ),
            TableCreator.ColumnDetails(
                "pdaparams_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "pdaparams(pdaparams_id)"
            ),
            TableCreator.ColumnDetails(
                "radiointernalparams_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "radiointernalparams(radiointernalparams_id)"
            ),
            TableCreator.ColumnDetails(
                "radioexternalparams_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "radioexternalparams (radioexternalparams_id)"
            ),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("createdAt", "STRING")
        )
        val communication_type_mappingTable =
            tableCreator.createMainTableIfNeeded(
                communication_type_mapping,
                communication_type_mappingColumn
            )


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
            ), TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("config_time", "STRING")

        )
        val survey_configurationTable =
            tableCreator.createMainTableIfNeeded(survey_configuration, survey_configurationColumn)


        if (survey_configurationTable.equals("Table Created Successfully...")) {
            val status =
                tableCreator.executeStaticQuery("INSERT INTO survey_configuration (config_name,zonedata_id,datum_id,elevationtype_id,distanceunit_id,angleunit_id) VALUES ('default',3,43,1,1,2)")
            Log.d(TAG, "CommonApi_TablesCreation: INSERTsurvey_configurationTable--$status-")
        }

        val device_configuration = "device_configuration"
        val device_configurationColumn = arrayOf(
            TableCreator.ColumnDetails("deviceConfig_id", "INTEGER", true, true),
            TableCreator.ColumnDetails(
                "communication_type_mapping_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "communication_type_mapping(communication_type_mapping_id)"
            ),
            TableCreator.ColumnDetails(
                "device_configHierarchy_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device_configHierarchy(device_configHierarchy_id)"
            ),
            TableCreator.ColumnDetails("mask_angle_byteValue", "STRING"),
            TableCreator.ColumnDetails("mask_angle_displayValue", "STRING"),
            TableCreator.ColumnDetails("device_work_mode_name", "STRING"),
            TableCreator.ColumnDetails("device_work_mode_value", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("deviceConfig_time", "STRING")

        )
        val device_configurationTable =
            tableCreator.createMainTableIfNeeded(device_configuration, device_configurationColumn)


        if (device_configurationTable.equals("Table Created Successfully...")) {
            val status =
                tableCreator.executeStaticQuery("INSERT INTO device_configuration (communication_type_mapping_id,device_configHierarchy_id) VALUES (1,2)")
            Log.d(
                TAG,
                "CommonApi_TablesCreation: INSERTdevice_configurationTable--$status--"
            )
        }


        val miscellaneous_configuration = "miscellaneous_configuration"
        val miscellaneous_configurationColumn = arrayOf(
            TableCreator.ColumnDetails("miscConfig_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("miscConfig_name", "STRING", unique = true),
            TableCreator.ColumnDetails(
                "point_name_visibility",
                "STRING",
            ), TableCreator.ColumnDetails(
                "LRF",
                "STRING",
            ), TableCreator.ColumnDetails(
                "code_name",
                "STRING",
            ),
            TableCreator.ColumnDetails(
                "osm_view",
                "STRING",
            ), TableCreator.ColumnDetails(
                "satellite_view",
                "STRING",
            ), TableCreator.ColumnDetails("defaultConfig", "STRING"),

            TableCreator.ColumnDetails("miscConfig_time", "STRING")

        )
        val miscellaneous_configurationTable =
            tableCreator.createMainTableIfNeeded(
                miscellaneous_configuration,
                miscellaneous_configurationColumn
            )

        if (miscellaneous_configurationTable.equals("Table Created Successfully...")) {
            val status =
                tableCreator.executeStaticQuery("INSERT INTO miscellaneous_configuration (miscConfig_name,point_name_visibility,LRF,code_name,osm_view,satellite_view) VALUES ('defaultSatConfig','Y','N','N','N','N')")
            Log.d(TAG, "CommonApi_TablesCreation: INSERTmiscellaneous_configurationTable--$status")

        }


        val manualbasetable = "manualbasetable"
        val manualbasetableColumn = arrayOf(
            TableCreator.ColumnDetails("manualBaseTable_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("latitude", "STRING"),
            TableCreator.ColumnDetails("longitude", "STRING"),
            TableCreator.ColumnDetails("altitude", "STRING"),
            TableCreator.ColumnDetails("manualbasetableCreatedAt", "STRING")

        )
        val manualbaseTable =
            tableCreator.createMainTableIfNeeded(manualbasetable, manualbasetableColumn)


        val staticParams = "staticParams"
        val staticParamsColumn = arrayOf(
            TableCreator.ColumnDetails("staticParams_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("fileName", "STRING"),
            TableCreator.ColumnDetails("totalTime", "STRING"),
            TableCreator.ColumnDetails("samplingRate", "STRING"),
            TableCreator.ColumnDetails("staticParamsCreatedAt", "STRING")

        )
        val staticParamsTable =
            tableCreator.createMainTableIfNeeded(staticParams, staticParamsColumn)


        val ppkParams = "ppkParams"
        val ppkParamsColumn = arrayOf(
            TableCreator.ColumnDetails("ppkParams_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("fileName", "STRING"),
            TableCreator.ColumnDetails("totalTime", "STRING"),
            TableCreator.ColumnDetails("ppkParamsCreatedAt", "STRING")
        )
        val ppkParamsTable =
            tableCreator.createMainTableIfNeeded(ppkParams, ppkParamsColumn)


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
            TableCreator.ColumnDetails("shortNameCreatedAt", "STRING")
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


        val satelliteConfiguration = "satelliteConfiguration"
        val satelliteConfigurationColumn = arrayOf(
            TableCreator.ColumnDetails("satelliteConfig_id", "INTEGER", true),
            TableCreator.ColumnDetails("satelliteConfig_name", "STRING"),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
        )
        val satelliteConfigurationTable =
            tableCreator.createMainTableIfNeeded(
                satelliteConfiguration,
                satelliteConfigurationColumn
            )

        if (satelliteConfigurationTable.equals("Table Created Successfully...")) {
            val status1 =
                tableCreator.executeStaticQuery("INSERT INTO satelliteConfiguration (satelliteConfig_name,active) VALUES ('defaultSatellite','Y')")
            Log.d(TAG, "CommonApi_TablesCreation: INSERTsatelliteConfigurationTable--$status1")
        }


        val satelliteMapping = "satelliteMapping"
        val satelliteMappingColumn = arrayOf(
            TableCreator.ColumnDetails("satelliteMapping_id", "INTEGER", true),
            TableCreator.ColumnDetails(
                "constellation_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "constellation (constellation_id)"
            ),
            TableCreator.ColumnDetails(
                "satelliteConfig_id", "STRING", foreignKey = true, foreignKeyReference =
                "satelliteConfiguration (satelliteConfig_id)"
            ),
            TableCreator.ColumnDetails("active", "STRING", default = true, defaultValue = 'Y'),
            TableCreator.ColumnDetails("created_at", "STRING"),
            TableCreator.ColumnDetails("defaultConfig", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
        )
        val satelliteMappingTable =
            tableCreator.createMainTableIfNeeded(
                satelliteMapping,
                satelliteMappingColumn
            )


        if (satelliteMappingTable.equals("Table Created Successfully...")) {
            val status1 =
                tableCreator.executeStaticQuery("INSERT INTO satelliteMapping (constellation_id,satelliteConfig_id) VALUES (1,1)")
            val status2 =
                tableCreator.executeStaticQuery("INSERT INTO satelliteMapping (constellation_id,satelliteConfig_id) VALUES (2,1)")
            val status3 =
                tableCreator.executeStaticQuery("INSERT INTO satelliteMapping (constellation_id,satelliteConfig_id) VALUES (5,1)")
            val status4 =
                tableCreator.executeStaticQuery("INSERT INTO satelliteMapping (constellation_id,satelliteConfig_id) VALUES (6,1)")
            Log.d(
                TAG,
                "CommonApi_TablesCreation: INSERTsatelliteMapping--$status1--$status2--$status3--$status4"
            )
        }


        val project_configuration_mapping = "project_configuration_mapping"
        val project_configuration_mappingColumn = arrayOf(
            TableCreator.ColumnDetails("project_configuration_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("project_configuration_Name", "STRING"),
            TableCreator.ColumnDetails(
                "config_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "survey_configuration(config_id)"
            ), TableCreator.ColumnDetails(
                "satelliteConfig_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "satelliteConfiguration(satelliteConfig_id)"
            ),
            TableCreator.ColumnDetails(
                "deviceConfig_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "device_configuration(deviceConfig_id)"
            ),
            TableCreator.ColumnDetails(
                "miscConfig_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "miscellaneous_configuration(miscConfig_id)"
            ),
            TableCreator.ColumnDetails("project_configuration_time", "STRING")

        )
        val project_configuration_mappingTable =
            tableCreator.createMainTableIfNeeded(
                project_configuration_mapping,
                project_configuration_mappingColumn
            )


        if (project_configuration_mappingTable.equals("Table Created Successfully...")) {
            val status =
                tableCreator.executeStaticQuery(
                    "INSERT INTO project_configuration_mapping" +
                            " (project_configuration_Name,config_id,satelliteConfig_id,deviceConfig_id) VALUES ('DefaultConfig',1,1,1)"
                )

            Log.d(
                TAG,
                "CommonApi_TablesCreation: INSERTproject_configuration_mappingTable--$status"
            )
        }

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
                "project_configuration_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "project_configuration_mapping(project_configuration_id)"
            ),
            TableCreator.ColumnDetails("projectCreated_at", "STRING")
        )

        val project_tableData =
            tableCreator.createMainTableIfNeeded(project_table, project_tableColumn)

        if (project_tableData.equals("Table Created Successfully...")) {
            val status =
                tableCreator.executeStaticQuery("INSERT INTO project_table (project_name,project_configuration_id) VALUES ('Default',1)")
            /* val status1 =
                 tableCreator.executeStaticQuery("INSERT INTO project_table (project_name,project_configuration_id) VALUES ('Second',2)")
             val status2 =
                 tableCreator.executeStaticQuery("INSERT INTO project_table (project_name,project_configuration_id) VALUES ('Third',3)")
       */
            Log.d(
                TAG,
                "CommonApi_TablesCreation: INSERTproject_configuration_mappingTable--$status"
            )
        }

        val dynamic_project_config = "dynamic_project_mapping"
        val dynamic_project_configColumn = arrayOf(
            TableCreator.ColumnDetails("dynamic_project_config_id", "INTEGER", true, true),
            TableCreator.ColumnDetails(
                "project_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "project_table(project_id)"
            ), TableCreator.ColumnDetails(
                "manualBaseTable_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "manualbasetable(manualBaseTable_id)"
            ),
            TableCreator.ColumnDetails(
                "staticParams_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "staticParams(staticParams_id)"
            ),
            TableCreator.ColumnDetails(
                "ppkParams_id",
                "INTEGER",
                foreignKey = true,
                foreignKeyReference = "ppkParams(ppkParams_id)"
            ),
            TableCreator.ColumnDetails("dynamic_project_config_time", "STRING")

        )
        val dynamic_project_configTable =
            tableCreator.createMainTableIfNeeded(
                dynamic_project_config,
                dynamic_project_configColumn
            )


        val dataSource = "dataSource"
        val dataSourceColumn = arrayOf(
            TableCreator.ColumnDetails("dataSource_id", "INTEGER", true, true),
            TableCreator.ColumnDetails("type", "INTEGER"),
            TableCreator.ColumnDetails("parameter_name", "STRING"),
            TableCreator.ColumnDetails("parameter_value", "STRING"),
            TableCreator.ColumnDetails("parameter_id", "INTEGER"),
            TableCreator.ColumnDetails("operation", "STRING"),
            TableCreator.ColumnDetails("configMode", "STRING"),
            TableCreator.ColumnDetails("revision_no", "INTEGER"),
            TableCreator.ColumnDetails("active", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("dataSource_time", "STRING")

        )
        val dataSourceTable =
            tableCreator.createMainTableIfNeeded(
                dataSource,
                dataSourceColumn
            )





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
                    "\n parameterTable:--$parameterTable" +
                    "\n constellationTable:--$constellationTable" +
                    "\n satelliteConfigurationTable:--$satelliteConfigurationTable" +
                    "\n satelliteMappingTable:--$satelliteMappingTable" +
                    "\n command_param_mapTable:--$command_param_mapTable" +
                    "\n command_typeTable:--$command_typeTable" +
                    "\n byte_data_responseTable:--$byte_data_responseTable" +
                    "\n operationTable:--$operationTable" +
                    "\n parameterTable:--$parameterTable" +
                    "\n modelTable:--$modelTable" +
                    "\n parameter_typeTable:--$parameter_typeTable" +
                    "\n fixed_response_valueTable:--$fixed_response_valueTable" +
                    "\n parameter_default_valueTable:--$parameter_default_valueTable" +
                    "\n charachtristicsTable:--$charachtristicsTable" +
                    "\n commandTable:--$commandTable" +
                    "\n selection_valueTable:--$selection_valueTable" +
                    "\n inputTable:--$inputTable" +
                    "\n selectionTable:--$selectionTable" +
                    "\n deviceTable:--$deviceTable" +
                    "\n response_sub_byte_divisionTable:--$response_sub_byte_divisionTable" +
                    "\n response_sub_byte_divisionTable:--$device_characteristic_ble_mapTable" +
                    "\n command_device_mapTable:--$command_device_mapTable" +
                    "\n sub_byte_divisionTable:--$sub_byte_divisionTable" +
                    "\n sub_division_selectionTable:--$sub_division_selectionTable" +
                    "\n servicesTable:--$servicesTable" +
                    "\n byte_dataTable:--$byte_dataTable" +
                    "\n device_mapTable:--$device_mapTable" +
                    "\n responseTable:--$responseTable" +
                    "\n constellation_model_mapTable:--$constellation_model_mapTable" +
                    "\n device_registrationTable:--$device_registrationTable" +
                    "\n device_configHierarchyTable:--$device_configHierarchyTable" +
                    "\n wifiparamsTable:--$wifiparamsTable" +
                    "\n via4gparamsTable:--$via4gparamsTable" +
                    "\n pdaparamsTable:--$pdaparamsTable" +
                    "\n radiointernalparamsTable:--$radiointernalparamsTable" +
                    "\n radioexternalparamsTable:--$radioexternalparamsTable" +
                    "\n type_of_communicationTable:--$type_of_communicationTable" +
                    "\n communication_type_mappingTable:--$communication_type_mappingTable" +
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
                    "\n distanceunitTable:--$distanceunitTable" +
                    "\n survey_configurationTable:--$survey_configurationTable" +
                    "\n device_configurationTable:--$device_configurationTable" +
                    "\n miscellaneous_configurationTable:--$miscellaneous_configurationTable" +
                    "\n manualbaseTable:--$manualbaseTable" +
                    "\n staticParamsTable:--$staticParamsTable" +
                    "\n ppkParamsTable:--$ppkParamsTable" +
                    "\n dynamic_project_configTable:--$dynamic_project_configTable" +
                    "\n project_configuration_mapping:--$project_configuration_mapping" +
                    "\n project_statusTable:--$project_statusTable" +
                    "\n shortNameTableData:--$shortNameTableData" +
                    "\n siteCalibrationTable:--$siteCalibrationTable" +
                    "\n project_folderTable:--$project_folderTable" +
                    "\n project_tableData:--$project_tableData" +
                    "\n project_configuration_mappingTable:--$project_configuration_mappingTable" +
                    "\n dataSourceTable:--$dataSourceTable"

        )




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
            && satelliteConfigurationTable.equals("Table Created Successfully...")
            && satelliteMappingTable.equals("Table Created Successfully...")
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
            && device_characteristic_ble_mapTable.equals("Table Created Successfully...")
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
            && hemisphereTable.equals("Table Created Successfully...")
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
            && survey_configurationTable.equals("Table Created Successfully...")
            && device_configurationTable.equals("Table Created Successfully...")
            && miscellaneous_configurationTable.equals("Table Created Successfully...")
            && manualbaseTable.equals("Table Created Successfully...")
            && staticParamsTable.equals("Table Created Successfully...")
            && ppkParamsTable.equals("Table Created Successfully...")
            && dynamic_project_configTable.equals("Table Created Successfully...")
            && project_statusTable.equals("Table Created Successfully...")
            && shortNameTableData.equals("Table Created Successfully...")
            && siteCalibrationTable.equals("Table Created Successfully...")
            && project_folderTable.equals("Table Created Successfully...")
            && project_tableData.equals("Table Created Successfully...")
            && project_configuration_mappingTable.equals("Table Created Successfully...")
            && dataSourceTable.equals("Table Created Successfully...")


        ) {
            Log.d(TAG, "CommonApiTablesCreation1: All table created")
            insertDBData(apiResponse)
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
                    dataList.clear()
                    val jsonObject1: JSONObject = jsonArray.getJSONObject(i)
                    if (key.equals("model")) {
                        Log.d(
                            TAG,
                            "insertCommonData: model $key--$  "
                        )

                    }
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

    fun getCoordinateSystem(): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT coordinateSystem_name FROM coordinateSystem where active = 'Y' ")
        return data
    }

    fun getCoordinateSystemID(coordinateSystem_name: String): String {
        var data =
            tableCreator.executeStaticQuery("SELECT coordinateSystem_id FROM coordinateSystem where coordinateSystem_name='" + coordinateSystem_name + "'")
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
        Log.d(TAG, "getproject_configurationID: $config_name")
        var data =
            tableCreator.executeStaticQuery("SELECT config_id FROM survey_configuration where config_name='" + config_name + "'")
        Log.d(TAG, "getproject_configurationID:$config_name--- $data ")
        return data?.get(0) ?: ""
    }

    fun getSatelliteDataList(): List<String>? {
        var data =
            tableCreator.executeStaticQuery(
                " select  cons.constellation_id,cons.constellation_name,cons.active \n" +
                        "from  constellation as cons JOIN constellation_model_map as conMap ON conMap.constellation_id = cons.constellation_id "
            )
        return data
    }


    fun insertSatelliteConfiguration(data: String): Int {
        var result = 0
        val query =
            "INSERT INTO satelliteConfiguration ( satelliteConfig_name,active) VALUES ('" + data.split(
                ","
            )[0] + "','Y')"
        Log.d(TAG, "insertSatelliteDataList: $query")
        val resultResult = tableCreator.executeStaticQuery(query)
        Log.d(TAG, "insertSatelliteConfiguration: $resultResult")
        if (resultResult!!.size != 0) {
            result = 0
            Log.d(TAG, "insertSatelliteDataList: $resultResult")
        } else {
            Log.d(TAG, "insertSatelliteDataList: $resultResult")

            result = 1
        }
        return result
    }


    fun insertMiscellaneousConfigData(value: String): Int {
        var result = 0
        val status = tableCreator.executeStaticQuery(
            "INSERT INTO miscellaneous_configuration (miscConfig_name,point_name_visibility,LRF,code_name,osm_view,satellite_view) " +
                    "VALUES ('${value.split(",")[0]}','${value.split(",")[1]}','${value.split(",")[2]}','${
                        value.split(
                            ","
                        )[3]
                    }','${value.split(",")[4]}','${value.split(",")[5]}')"
        )
        Log.d(TAG, "insertMiscellaneousConfigData--$status")
        if (status!!.size != 0) {
            result = 0
            Log.d(TAG, "insertSatelliteDataList: $status")
        } else {
            Log.d(TAG, "insertSatelliteDataList: $status")

            result = 1
        }
        return result
    }

    fun getSatelliteConfigurationID(satelliteConfig_name: String): String {
        Log.d(TAG, "getproject_configurationID: $satelliteConfig_name")
        var data =
            tableCreator.executeStaticQuery("SELECT satelliteConfig_id FROM satelliteConfiguration where satelliteConfig_name='" + satelliteConfig_name + "'")
        Log.d(TAG, "getproject_configurationID:$satelliteConfig_name--- $data ")
        return data?.get(0) ?: ""
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun addConfigurationData(map: HashMap<String, String>): String {
        var insertResult = ""
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
            " SELECT prj.project_name ,conf.project_configuration_Name\n" +
                    " FROM project_table AS prj JOIN project_configuration_mapping as\n" +
                    " conf ON prj.project_configuration_id =conf.project_configuration_id "
        )
        Log.d(TAG, "getProjectList: data--$data")

        return data
    }


    fun getConfigurationList(): List<String>? {
        var data = tableCreator.executeStaticQuery(
            " select  conf.project_configuration_Name , dd.datum_name \n" +
                    "from  project_configuration_mapping as conf JOIN survey_configuration as sc ON conf.config_id = \n" +
                    "sc.config_id JOIN datum_data as DD ON DD.datum_id = sc.datum_id"
        )
        Log.d(TAG, "getConfigurationList: data--$data")
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

    fun insertConfigMappingData(values: String): List<String> {
        Log.d(TAG, "insertConfigMappingData: $values")
        val data = tableCreator.executeStaticQuery(
            "INSERT INTO project_configuration_mapping" +
                    " (project_configuration_Name,config_id,satelliteConfig_id) VALUES ('${
                        values.split(
                            ","
                        )[0]
                    }',${values.split(",")[1]},${values.split(",")[2]})"
        )

        return data!!
    }

    fun getproject_configurationMappingID(project_configuration_Name: String): String {
        Log.d(TAG, "getproject_configurationID: $project_configuration_Name")
        var data =
            tableCreator.executeStaticQuery("SELECT project_configuration_id FROM project_configuration_mapping where project_configuration_Name='" + project_configuration_Name + "'")
        Log.d(TAG, "getproject_configurationID:$project_configuration_Name--- $data ")
        return data?.get(0) ?: ""
    }

    fun getindexofsatellite(constellationName: String): String {
        var data = tableCreator.executeStaticQuery(
            "SELECT constellation_id FROM constellation where " +
                    "constellation_name='" + constellationName + "'"
        )
        return data?.get(0) ?: ""
    }


    fun insertSatelliteMappingDataasas(
        configMapId: String,
        statusList: ArrayList<SatelliteModel>
    ): Int {
        var result = 0
        var count = 0
        for (i in statusList.indices) {
            val constID = getindexofsatellite(statusList.get(i).satelliteName)
            Log.d(
                TAG,
                "insertSatelliteMappingDataasas:--statusList ${statusList.get(i).satelliteStatus}"
            )
            val status1 = tableCreator.executeStaticQuery(
                "INSERT INTO satelliteMapping (constellation_id,satelliteConfig_id,active) VALUES ($constID,'$configMapId','${
                    statusList.get(i).satelliteStatus
                }')"
            )
            Log.d(TAG, "insertSatelliteMappingDataasas: status1 $status1")
            if (status1!!.size == 0) {
                count++
            }
        }
        if (count == statusList.size) {
            result = 1
        } else {
            result = 0
        }
        return result
    }


    fun insertProjectValues(values: String): Int {
        Log.d(TAG, "insertProjectValues: values --$values")
        var result = 0
        val status1 = tableCreator.executeStaticQuery(
            "INSERT INTO project_table (project_name,project_configuration_id) VALUES ('${
                values.split(",")[0]
            }',${values.split(",")[1]})"
        )
        Log.d(TAG, "project_table: status1 $status1--${status1?.size}")
        if (status1?.size == 0) {
            result = 1
        } else {
            result = 0
        }
        return result
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun insertSatelliteMappingDatajjj(list: List<String>): String {

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


    fun BluetoothConfigurationData(apiResponse: String) {
        insertDBData(apiResponse)
    }

    fun getFixedResponse(param_id: String, value: String): Cursor? {
        val query =
            (" select * from fixed_response fr,fixed_response_value frv where fr.active='Y' and frv.active='Y' "
                    + " and fr.fixed_response_id=frv.fixed_response_id and fr.parameter_id='" + param_id + "'  "
                    + " and frv.select_value='" + value + "' ")
        return tableCreator.executeStaticQueryForCursor(query)
    }

    fun getParameterResponse(param: String): Cursor? {
        val query =
            (" select * from parameter2 p,parameter_type pt where p.active='Y' and pt.active='Y' "
                    + " and p.parameter_type_id=pt.parameter_type_id "
                    + " and p.parameter_name='" + param + "' and p.parameter_type_id in(8,9,10) ")
        return tableCreator.executeStaticQueryForCursor(query)
    }


    //Get All Response Data on bases of command_id from Database
    @SuppressLint("Range")
    fun getResponseList(id: Int): ArrayList<DBResponseModel>? {
        val list: ArrayList<DBResponseModel> = ArrayList<DBResponseModel>()
        try {
            var cursor =
                tableCreator.executeStaticQueryForCursor("SELECT * FROM response where command_id=$id order by response_type_id asc")
            if (cursor != null) {
                for (i in 0 until cursor.count) {
                    cursor.moveToPosition(i)
                    val response_id = cursor.getString(cursor.getColumnIndex("response_id"))
                    val response = cursor.getString(cursor.getColumnIndex("response"))
                    val response_type_id =
                        cursor.getString(cursor.getColumnIndex("response_type_id"))
                    val data_extract_type =
                        cursor.getString(cursor.getColumnIndex("data_extract_type"))
                    val command_accepted =
                        cursor.getString(cursor.getColumnIndex("command_accepted"))
                    val all_param_list = ArrayList<String>()
                    val p_query = "select * from parameter where active='Y'"
                    val p_cursor = tableCreator.executeStaticQueryForCursor(p_query)
                    while (p_cursor!!.moveToNext()) {
                        all_param_list.add(p_cursor.getString(1))
                    }
                    val all_delimeter_list = ArrayList<DelimeterResponse>()
                    val query = (" select * from delimeter_validation  where active='Y' "
                            + " and response_id='" + response_id + "'")
                    val d_cursor = tableCreator.executeStaticQueryForCursor(query)
                    while (d_cursor!!.moveToNext()) {
                        val delimeter_validation_id =
                            d_cursor.getString(d_cursor.getColumnIndex("delimeter_validation_id"))
                        val validation_value =
                            d_cursor.getString(d_cursor.getColumnIndex("validation_value"))
                        val validation_index =
                            d_cursor.getString(d_cursor.getColumnIndex("validation_index"))
                        val remark = d_cursor.getString(d_cursor.getColumnIndex("remark"))
                        val type = d_cursor.getString(d_cursor.getColumnIndex("type"))
                        val sateliteTypeList = ArrayList<SateliteTypeModel>()
                        val query1 =
                            (" select * from satellite_type_delimeter_mapping  where active='Y' "
                                    + " and delimeter_validation_id='" + delimeter_validation_id + "'")
                        val cursor1 = tableCreator.executeStaticQueryForCursor(query1)
                        while (cursor1!!.moveToNext()) {
                            val satelite_type1 =
                                cursor1.getString(cursor1.getColumnIndex("satellite_type"))
                            val start_prn1 = cursor1.getString(cursor1.getColumnIndex("start_prn"))
                            val end_prn1 = cursor1.getString(cursor1.getColumnIndex("end_prn"))
                            sateliteTypeList.add(
                                SateliteTypeModel(
                                    satelite_type1,
                                    start_prn1,
                                    end_prn1
                                )
                            )
                        }
                        all_delimeter_list.add(
                            DelimeterResponse(
                                validation_value,
                                validation_index,
                                remark,
                                type,
                                sateliteTypeList
                            )
                        )
                    }
                    if (response_type_id == "1") {
                        list.add(
                            DBResponseModel(
                                response,
                                response_id,
                                response_type_id,
                                data_extract_type,
                                command_accepted,
                                1,
                                all_param_list,
                                all_delimeter_list
                            )
                        )
                    } else {
                        list.add(
                            DBResponseModel(
                                response,
                                response_id,
                                response_type_id,
                                data_extract_type,
                                command_accepted,
                                0,
                                all_param_list,
                                all_delimeter_list
                            )
                        )
                    }
                    Log.d(TAG, "getResponseId: $response_id")
                }
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "getResponseIdError: $e")
        }
        return list
    }


    fun getCommandID(opId: Int, dgpsId: Int) {
        try {
            val cmdID =
                tableCreator.executeStaticQuery("SELECT command_id FROM command_device_map where operation_id=" + opId + " AND device_id=" + dgpsId + " ORDER BY order_no ASC")
        } catch (e: Exception) {
            Log.d(TAG, "getCommandID:Exception ${e.message} ")
        }
    }


    fun getSelectionId(command_id: Int) {
        try {
            val cmdID =
                tableCreator.executeStaticQuery("  SELECT selection_id FROM selection where command_id IN (" + command_id + ") ")
        } catch (e: Exception) {
            Log.d(TAG, "getSelectionId:Exception ${e.message} ")
        }
    }


    fun getOperationId(operation_name: String): Int {
        var a = 0
        try {
            val cursor =
                tableCreator.executeStaticQueryForCursor("SELECT operation_id FROM operation where operation_name='$operation_name' ")
            cursor!!.moveToPosition(0)
            a = cursor.getInt(0)
        } catch (e: Exception) {
            Log.e(TAG, "getOperationId:Exception ${e.message}")
        }
        return a
    }

    fun delaylist(opId: Int, device_id: Int): ArrayList<String> {
        val list = java.util.ArrayList<String>()
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT delay FROM command_device_map where operation_id = $opId and device_id = $device_id order by order_no; "
            )
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getString(0))
            }
        } catch (e: Exception) {
            Log.e(TAG, "delaylistError: ${e.message}")
        }
        return list
    }

    fun commandforparsinglist(id: Int, Device_id: Int): java.util.ArrayList<String> {
        val list = java.util.ArrayList<String>()
        try {
            val query =
                "SELECT c.command_name  FROM command_device_map as map , command as c WHERE map.device_id= $Device_id AND map.operation_id=$id  and map.command_id = c.command_id ORDER By order_no; "
            val cursor = tableCreator.executeStaticQueryForCursor(query)
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getString(0))
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "commandforparsinglist Error: $e"
            )
        }
        return list
    }

    fun commandformatparsinglist(id: Int, Device_id: Int): ArrayList<String> {
        val list = java.util.ArrayList<String>()
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT c.format   FROM command_device_map as map , command as c WHERE map.device_id= $Device_id AND map.operation_id=$id  and map.command_id = c.command_id ORDER By order_no"
            )
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getString(0))
                // list.add(surveyBean);
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "commandformatparsinglistError: $e"
            )
        }
        return list
    }

    fun getidDataSource(): String {
        var id = "0"
        try {
            val cursor =
                tableCreator.executeStaticQueryForCursor("SELECT Parameter_id FROM DataSource ")
            cursor!!.moveToPosition(0)
            val a = cursor.count
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                id = cursor.getString(0)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getidDataSource Exception: ${e.message}")
        }
        return id
    }

    /**
     * The Function will return the Command form command_device_map
     *
     * @param opId:String
     * @param dgpsId:String
     * @return It will return list of command id
     */
    fun commandidls1(opId: Int, dgpsId: Int): ArrayList<Int> {
        val list = ArrayList<Int>()
        try {
            val cursor =
                tableCreator.executeStaticQueryForCursor("SELECT command_id FROM command_device_map where operation_id=$opId AND device_id=$dgpsId ORDER BY order_no ASC ")
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getInt(0))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCommandIdFromOpIdAndDgpsID:Exception $e")
        }
        return list
    }


    fun getCommand(id: Int): String? {
        var command = ""
        try {
            val cursor = database.rawQuery("SELECT command FROM command where command_id=$id", null)
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                command = cursor.getString(0)
            }
        } catch (e: java.lang.Exception) {
            Log.e(
                TAG,
                "getUserDtailerror: $e"
            )
        }
        return command
    }


    /**
     * This Function return the List of ID of selected command_id is selected
     *
     * @param command_id:String
     * @return with the list of ids as integer
     */

    fun selectionidlist1(command_id: String): List<Int> {
        val list: MutableList<Int> = ArrayList()
        try {
            val query =
                "SELECT selection_value_id FROM command_param_map where command_id IN ($command_id)"
            createLog("TAG_RADIO", "Query is $query")
            val cursor = tableCreator.executeStaticQueryForCursor(
                query
            )
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getInt(0))
            }
        } catch (e: Exception) {
            Log.e(TAG, "selectionValueId Exception: $e")
        }
        return list
    }

    /**
     * The Function is Use to configure the InputList
     *
     * @param command_id:String
     * @return ArrayList<Int> for all input type
     */
    fun inputlist(command_id: String): ArrayList<Int> {
        val list = ArrayList<Int>()
        try {
            /* val query = "SELECT parameter_id FROM input Where command_id IN ($joined) "
             Log.d(TAG, "inputlist: $query")*/
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT parameter_id FROM input Where command_id IN ($command_id) "
            )
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getInt(0))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getParameterIdList: Exception- $e")
        }
        return list
    }

    /**
     * This function will display the Parameter of given on connection setup UI
     *
     * @param joined:String
     * @return the Map of Parameter_name and Display_value and byte_value
     */
    fun displayvaluelist1(joined: String): Map<String, Map<String, String>> {
        val selectionMap: MutableMap<String, Map<String, String>> = LinkedHashMap()
        var selectionValueMap: MutableMap<String, String> = LinkedHashMap()
        var parameter = ""
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT distinct parameter_name,display_value,byte_value FROM command_param_map, parameter, selection_value " +
                        "where command_param_map.parameter_id = parameter.parameter_id and command_param_map.selection_value_id = selection_value.selection_value_id " +
                        "and command_param_map.selection_value_id IN (" + joined + ")"
            )

            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                val para1 = cursor.getString(0)
                if ((parameter == "")) {
                    selectionValueMap[cursor.getString(1)] = cursor.getString(2)
                    parameter = para1
                } else if ((parameter == para1)) {
                    selectionValueMap[cursor.getString(1)] = cursor.getString(2)
                } else if (parameter != para1) {
                    selectionMap[parameter] = selectionValueMap
                    parameter = para1
                    selectionValueMap = LinkedHashMap()
                    selectionValueMap[cursor.getString(1)] = cursor.getString(2)
                }
                if (i == cursor.count - 1) {
                    selectionMap[parameter] = selectionValueMap
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getDisplayValByteValandParamName:Exception $e")
        }
        return selectionMap
    }

    fun inputparameterlists(joined: String): ArrayList<String> {
        val list = ArrayList<String>()
        var name: String
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT parameter_name FROM parameter Where parameter_id IN ($joined) "
            )
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                name = cursor.getString(0)
                list.add(name)
            }
        } catch (e: Exception) {
            Log.e(TAG, "inputparameterLists:Exception ${e.message}")
        }
        return list
    }


    fun inputparameterlist(joined: String): List<String> {
        val list: MutableList<String> = ArrayList()
        var name: String
        var type: String
        var remark: String
        try {

            /*val query =
                "SELECT parameter_name,parameter_type,remark FROM parameter Where parameter_id IN ($joined) ; "
            Log.d(TAG, "inputparameterlist: $query")*/

            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT p.parameter_name,pt.parameter_type_name,p.remark FROM parameter as p JOIN " +
                        "parameter_type as pt ON p.parameter_type_id=pt.parameter_type_id Where " +
                        "parameter_id IN ($joined)"
            )
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                name = cursor.getString(0)
                type = cursor.getString(1)
                remark = cursor.getString(2)
                list.add("$name,$type,$remark")
            }
        } catch (e: Exception) {
            Log.e(TAG, "parameterDataList: Exception ${e.message}")
        }
        return list
    }


    fun inputparameterlistMAP(joined: String): Map<String, Pair<String, String>> {
        val map = mutableMapOf<String, Pair<String, String>>()
        var name: String
        var type: String
        var remark: String
        try {
            val query =
                "SELECT p.parameter_name,pt.parameter_type_name,p.remark FROM parameter as p " +
                        "JOIN parameter_type as pt ON p.parameter_type_id=pt.parameter_type_id Where parameter_id IN ($joined)"
            createLog("TAG_OPERATION", query)
            val cursor = tableCreator.executeStaticQueryForCursor(
                query
            )
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                name = cursor.getString(0)
                type = cursor.getString(1)
                remark = cursor.getString(2)
                //list.add("$name,$type,$remark")
                map.put(name, Pair(type, remark))
            }
        } catch (e: Exception) {
            Log.e(TAG, "parameterDataList: Exception ${e.message}")
        }
        return map.toMap()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertSatelliteMappingDatajjjsds(list: List<String>): String {
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


    fun insertdataSorcestable(
        type: String, Parameter_Name: String, Parameter_value: String,
        parameter_id: String, operation: String, configMode: String
    ): Boolean {
        var result = false
        val dataList: MutableList<ContentValues> = ArrayList()
        val contentValues = ContentValues()
        contentValues.put("type", type.trim())
        contentValues.put("parameter_name", Parameter_Name.trim())
        contentValues.put("parameter_value", Parameter_value.trim())
        contentValues.put("Parameter_id", parameter_id.trim())
        contentValues.put("operation", operation.trim())
        contentValues.put("configMode", configMode.trim())
        contentValues.put("dataSource_time", "${LocalDateTime.now()}")
        dataList.add(contentValues)
        val status = tableCreator.insertDataIntoTable("dataSource", dataList)
        result = status.equals("Data inserted successfully")
        return result
    }


    fun getDataSource(
        types: String,
        parameter_id: String,
        configMode: String
    ): java.util.HashMap<String, String>? {
        val commandmap: java.util.HashMap<String, String> = java.util.LinkedHashMap()
        val query =
            "SELECT Parameter_Name,Parameter_value,Operation FROM DataSource Where type = '$types' AND Parameter_id = '$parameter_id' AND configMode = '$configMode'"
        Log.d(TAG, "getDataSourceQuery: $query")
        try {
            val cursor =
                tableCreator.executeStaticQueryForCursor("SELECT Parameter_Name,Parameter_value,Operation FROM DataSource Where type = '$types' AND Parameter_id = '$parameter_id' AND configMode = '$configMode' ")
            cursor!!.moveToPosition(0)
            val a = cursor.count
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                val name = cursor.getString(0).trim { it <= ' ' }
                val value = cursor.getString(1).trim { it <= ' ' }
                val operation = cursor.getString(2).trim { it <= ' ' }
                commandmap[name] = value
                commandmap["operation"] = operation
            }
            if (!commandmap.isEmpty()) {
                commandmap["param_id"] = parameter_id
            }
        } catch (e: java.lang.Exception) {
            Log.e(
                TAG,
                "getDataSource error: $e"
            )
        }
        return commandmap
    }


    fun deletedataSource(parameter_id: String): Boolean {
        try {
            tableCreator.executeStaticQueryForCursor("DELETE FROM DataSource WHERE parameter_id= '$parameter_id'")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return true
    }

    fun parameteridlist(joined: String): java.util.ArrayList<Int> {
        val list = java.util.ArrayList<Int>()
        try {
            //  Cursor cursor = database.rawQuery("SELECT parameter_id FROM selection  where command_id IN ("+joined+") ; ", null);
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT parameter_id FROM command_param_map  where command_id IN ($joined) "
            )
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getInt(0))
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "parameteridlist: $e")
        }
        return list
    }

    fun retrnfromtfromrmrk(paramatername: String): String? {
        var rtrnfrmrmk: String? = null
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT remark FROM parameter Where parameter_name='$paramatername'"
            )
            for (i in 0 until cursor!!.count) {
                cursor.moveToPosition(i)
                rtrnfrmrmk = cursor.getString(0)
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "getmorecommandlistError: $e")
        }
        return rtrnfrmrmk
    }


    fun inputhint(name: String): String? {
        var a: String? = null
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT remark FROM parameter pm  WHERE pm.parameter_name='$name' "
            )
            cursor!!.moveToPosition(0)
            a = cursor.getString(0)
        } catch (e: java.lang.Exception) {
            Log.e(
                TAG,
                "getUserDtailerror: $e"
            )
        }
        return a
    }

    /*   fun getDeviceModule(deviceId: String): java.util.ArrayList<String>? {
           val list = java.util.ArrayList<String>()
           var id: String
           var deviceTypeId: String
           var modelId: String
           try {
               val cursor = tableCreator.executeStaticQueryForCursor(
                   "SELECT device_type_id FROM device WHERE device_id IN ($deviceId) "
               )
               cursor!!.moveToPosition(0)
               val a = cursor.count
               for (i in 0 until cursor.count) {
                   cursor.moveToPosition(i)
                   modelId = cursor.getString(0)
                   list.add(modelId)
               }
           } catch (e: java.lang.Exception) {
               Log.e(
                  TAG,
                   "getDeviceModule error: ${e.message}"
               )
           }
           return list
       }
   */
    /* fun getUserRegNo(device_name: String): String? {
         var a = ""
         try {
             val query = "SELECT model_id FROM model WHERE device_no LIKE  '%$device_name%'"
             Log.d(TAG,"getUserRegNo: $query")
             val cursor = tableCreator.executeStaticQueryForCursor(query)
             //  Cursor cursor =  database.rawQuery( "SELECT id FROM model WHERE device_name = '"+device_name+"'" , null );
             cursor!!.moveToPosition(0)
             a = cursor.getString(0)
         } catch (e: java.lang.Exception) {
             Log.e(
                 TAG,
                 "getUserRegNo error: ${e.message}"
             )
         }
         return a
     }
 */

    fun getdeviceId(modelId: String): String? {
        var a = ""
        var manufacture_id = ""
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT device_id, manufacture_id FROM device WHERE model_id = '$modelId' and device_type_id = '2'"
            )
            cursor!!.moveToPosition(0)
            a = cursor.getString(0)
            manufacture_id = cursor.getString(1)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "getdeviceId error: ${e.message}")
        }
        return "$a,$manufacture_id"
    }

    fun getMakeName(manufacturer_id: String): String? {
        Log.d(TAG, "getMakeName: manufacturer_id--$manufacturer_id")
        var a = ""
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT name FROM manufacturer WHERE manufacturer_id = '$manufacturer_id'"
            )
            cursor!!.moveToPosition(0)
            a = cursor.getString(0)
        } catch (e: java.lang.Exception) {
            Log.e(
                TAG,
                "getMakeName error: ${e.message}"
            )
        }
        return a
    }
    /*

        fun getModuleFinishedId(deviceId: String): java.util.ArrayList<String>? {
            val list = java.util.ArrayList<String>()
            var id: String
            try {
                val cursor = tableCreator.executeStaticQueryForCursor(
                    "SELECT module_device_id FROM device_map WHERE finished_device_id = '$deviceId'  ")
                cursor!!.moveToPosition(0)
                val a = cursor.count
                for (i in 0 until cursor.count) {
                    cursor.moveToPosition(i)
                    id = cursor.getString(0)
                    list.add(id)
                }
            } catch (e:Exception) {
                Log.e( TAG,"getModuleFinishedId error: ${e.message}")
            }
            return list
        }

    */

    fun getDeviceDetail(deviceId: String): java.util.ArrayList<String>? {
        val list = java.util.ArrayList<String>()
        var id: String
        var deviceTypeId: String
        var modelId: String
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT model_id, device_type_id, device_id FROM device WHERE device_id IN ($deviceId)"
            )
            cursor!!.moveToPosition(0)
            val a = cursor.count
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                modelId = cursor.getString(0)
                deviceTypeId = cursor.getString(1)
                id = cursor.getString(2)
                list.add("$modelId,$deviceTypeId,$id")
            }
        } catch (e: Exception) {
            Log.e(TAG, "getDeviceDetail error: ${e.message}")
        }
        return list
    }

    fun getDeviceTypeeId(name: String): String? {
        var a = ""
        try {
            val cursor =
                tableCreator.executeStaticQueryForCursor("SELECT device_type_id FROM device_type WHERE type = '$name'")
            cursor!!.moveToPosition(0)
            a = cursor.getString(0)
        } catch (e: Exception) {
            Log.e(
                TAG,
                "getDeviceTypeeId error: ${e.message}"
            )
        }
        return a
    }

    fun getModelDetail(deviceId: String): java.util.ArrayList<String>? {
        val list = java.util.ArrayList<String>()
        var id: String
        var modelTypeId: String
        try {
            val cursor = tableCreator.executeStaticQueryForCursor(
                "SELECT device_name,model_type_id FROM model WHERE model_id IN ($deviceId)"
            )
            cursor!!.moveToPosition(0)
            val a = cursor.count
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                id = cursor.getString(0)
                modelTypeId = cursor.getString(1)
                list.add("$id,$modelTypeId")

            }
            Log.d(TAG, "getModelDetail: list--$list")
        } catch (e: Exception) {
            Log.e(TAG, "getModelDetail error: ${e.message}")
        }
        return list
    }


    fun getSsidPassword(ssid: String?): String {
        var password = ""
        try {
            val c = tableCreator.executeStaticQueryForCursor(
                "SELECT password FROM WifiData WHERE ssid=" + DatabaseUtils.sqlEscapeString(ssid) + " "
            )
            c!!.moveToFirst()
            if (c != null) {
                for (i in 0 until c.count) {
                    c.moveToPosition(i)
                    password = c.getString(0)
                }
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "getSsidPassword: " + e.message)
        }
        return password
    }

    fun insertSSIDPassword(ssid: String, password: String): Boolean {
        var result = false
        val query =
            "SELECT * FROM WifiData WHERE ssid='$ssid' AND password='$password' "
        Log.d(TAG, "insertSSIDPassword: $query")
        val cursor = tableCreator.executeStaticQueryForCursor("SELECT * FROM WifiData ")
        try {
            if (cursor != null) {
                if (cursor.count < 5) {
                    Log.d(TAG, "getCount: " + cursor.count)
                    val c = tableCreator.executeStaticQueryForCursor(
                        "SELECT * FROM WifiData WHERE ssid=" + DatabaseUtils.sqlEscapeString(ssid) + " AND password='" + password + "' "
                    )
                    result = if (!c!!.moveToFirst()) {
                        val contentValues = ContentValues()
                        contentValues.put("ssid", ssid)
                        contentValues.put("password", password)
                        database.insert("WifiData", null, contentValues)
                        true
                    } else {
                        false
                    }
                } else {
                    val c = tableCreator.executeStaticQueryForCursor(
                        "SELECT * FROM WifiData WHERE ssid=" + DatabaseUtils.sqlEscapeString(ssid) + " AND password='" + password + "' "
                    )
                    result = if (!c!!.moveToFirst()) {
                        val contentValues = ContentValues()
                        contentValues.put("ssid", ssid)
                        contentValues.put("password", password)
                        database.insert("WifiData", null, contentValues)
                        true
                    } else {
                        false
                    }
                    cursor.moveToFirst()
                    val attr_id = cursor.getInt(0)
                    Log.d(TAG, "attr_id: $attr_id")
                    deleteWifiData(attr_id)
                }
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "insertSSIDPassword: " + e.message)
        }
        return result
    }

    fun deleteWifiData(id: Int): Boolean {
        var result = false
        try {
            val status = tableCreator.executeStaticQuery("DELETE FROM WifiData WHERE  id = $id ")
            result = status!!.size == 0

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d(TAG, "deleteWifiData: " + e.message)
        }
        return true
    }


    /**
     * This Function Return the the of given Device Number i.e for device number NAVIK200-1 it return model_id list
     **/
    fun getUserRegNo(values: String): String? {
        val query = "SELECT model_id FROM model WHERE device_no LIKE  '%$values%'"
        val cursor = tableCreator.executeStaticQueryForCursor(query)
        return if (cursor != null && cursor.moveToPosition(0)) {
            cursor.getString(0)
        } else {
            null
        }
    }


    /**
     * This function return the DeviceID,ManufacturerID,CreateBy info from device table with model_id & device_type_id
     **/
    fun getDeviceId(modelId: String): HashMap<String, String> {
        val hashMap = hashMapOf<String, String>()
        val query =
            "SELECT device_id, manufacture_id,created_by FROM device WHERE model_id = '$modelId' and device_type_id = '2'"
        val cursor = tableCreator.executeStaticQueryForCursor(query)
        return if (cursor != null && cursor.moveToPosition(0)) {
            hashMap["DeviceID"] = cursor.getString(0)
            hashMap["ManufactureID"] = cursor.getString(1)
            hashMap["CREATED_BY"] = cursor.getString(2)
            hashMap
        } else {
            hashMap
        }
    }


    /**
     * This function will return the manufacturer name via its id
     **/
    fun getMakeName(id: Int): String? {
        val query = "SELECT name FROM manufacturer WHERE manufacturer_id = '$id'"
        val cursor = tableCreator.executeStaticQueryForCursor(query)
        return if (cursor != null && cursor.moveToPosition(0)) {
            cursor.getString(0)
        } else {
            null
        }
    }

    /**
     * This function will takes the device id and return the list of module device id
     *
     * @param deviceId :String
     * @return list of module Ids
     */
    fun getModuleFinishedId(deviceId: String): List<String> {
        val list = mutableListOf<String>()
        var id: String
        val query =
            "SELECT module_device_id FROM device_map WHERE finished_device_id = '$deviceId'"
        val cursor = tableCreator.executeStaticQueryForCursor(query)
        if (cursor != null && cursor.moveToPosition(0)) {
            cursor.moveToPosition(0)
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                id = cursor.getString(0)
                list.add(id)
            }

        }
        return list.toList()
    }

    /**
     * The function will return the Device_type_Id for give device id
     * i.e what type of conenction it can make like Radio,Internet,Wifi setup
     *
     * @param deviceId :STRING
     * @return total list of Connection set up Ids
     */
    fun getDeviceModule(deviceId: String): List<String> {
        val list = mutableListOf<String>()
        val query = "SELECT device_type_id FROM device WHERE device_id IN ($deviceId)"
        var modelId: String
        val cursor = tableCreator.executeStaticQueryForCursor(query)
        if (cursor != null && cursor.moveToPosition(0)) {
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                modelId = cursor.getString(0)
                list.add(modelId)
            }
        }
        return list
    }

    /**
     * This Function will return the Operator Name With ID
     *
     * @param name:String
     * @return it will return the operator id
     */
    fun detopnameid(name: String): Int? {
        val query = "SELECT operation_id FROM operation where operation_name='$name'"
        createLog("TAG_OPERATION", query)
        val cursor =
            tableCreator.executeStaticQueryForCursor(query)
        return if (cursor != null && cursor.moveToPosition(0)) {
            cursor.getInt(0)
        } else {
            null
        }

    }

    /**
     * Get Operation Id for BLE Device status:Successfully
     */
    fun getOperationIDBLE(baseSelection: String, rtkSetting: String): List<String> {
        val query =
            "SELECT operation_id FROM operation where operation_name in ('$baseSelection','$rtkSetting')"
        val list = mutableListOf<String>()
        val cursor = tableCreator.executeStaticQueryForCursor(query)
        if (cursor != null && cursor.moveToPosition(0)) {
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getString(0))
            }
        }
        return list.toList()
    }

    /**
     * Get Command List BLE device with Operation IDs
     */
    fun getCommandListBLE(operationIds: String, dgps: Int): List<String> {
        val query =
            "SELECT command_id FROM command_device_map where operation_id in ($operationIds) AND device_id='$dgps' ORDER BY order_no ASC"
        createLog("TAG_BLE_CMD",query)
        val list = mutableListOf<String>()
        val cursor = tableCreator.executeStaticQueryForCursor(query)
        if (cursor != null && cursor.moveToPosition(0)) {
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getString(0))
            }
        }
        return list.toList()
    }


    /**
     * Command List Operation with Command List and Selected Operation IDs
     */
    fun getCommandList(commandList: String): List<String> {
        val query = "SELECT command_name FROM command  where command_id IN ($commandList)"
        createLog("TAG_BLE_CMD",query)
        val list = mutableListOf<String>()
        val cursor = tableCreator.executeStaticQueryForCursor(query)
        if (cursor != null && cursor.moveToPosition(0)) {
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                list.add(cursor.getString(0))
            }
        }
        return list.toList()
    }

}









