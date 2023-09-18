package com.apogee.geomaster.response_handling

import android.annotation.SuppressLint
import android.content.Context
import com.apogee.geomaster.response_handling.model.DBResponseModel
import com.apogee.basicble.Utils.MultiMap
import com.apogee.geomaster.response_handling.model.ResponseHandlingModel
import com.apogee.geomaster.bluetooth.DataResponseHandlingInterface
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants.gsaFlag
import com.apogee.geomaster.service.Constants.gsa_res_map
import com.apogee.geomaster.service.Constants.gsvFlag
import com.apogee.geomaster.service.Constants.gsv_res_map


import java.util.ArrayList
import java.util.HashMap

class ResponseHandling(context: Context ) {

    var dbTask: DatabaseRepsoitory = DatabaseRepsoitory(context)

     fun validateResponse(response: String, headerLength: Int, response_id_list: List<DBResponseModel>, listner: DataResponseHandlingInterface) {
        // Log.d(TAG, "validateResponse: "+response)
        var response = response
        val validate_res_map: MultiMap<String, String> = MultiMap()
        val validate_gsv_map: MultiMap<String, String> = MultiMap()
        val validate_gsa_map: MultiMap<String, String> = MultiMap()


        var headerValue: String? = ""
        val result = ""
        val headerData = response.substring(0, headerLength.toInt())

        try {
            var res_id: String? = ""
            for (rl in response_id_list.indices) {
                if (response_id_list[rl].response.contains(headerData.trim { it <= ' ' })) {

                    //Log.d(TAG, "headerData: "+headerData+"==="+response_id_list[rl].response_type_id+"==="+response_id_list[rl].response)
                    if (response_id_list[rl].response_type_id.toInt() != 0) {
                        response_id_list[rl].flag = 0
                    }

                    if (response_id_list[rl].flag == 0) {

                        res_id = response_id_list[rl].response_id

                        var db_response = response_id_list[rl].response
                        val data_extract_type = response_id_list[rl].data_extract_type
                        var response_type_id = response_id_list[rl].response_type_id
                        var command_accepted = response_id_list[rl].command_accepted
                        var param: String? = ""
                        var param_val = ""
                        if (response_type_id == null) {
                            response_type_id = ""
                        }
                        if (response_type_id != "") {
                            if (response_type_id == "1") {
                                if (command_accepted.toLowerCase().equals("y")) {
                                    listner.ackRecieveData(1)
                                } else {
                                    listner.ackRecieveData(0)
                                }
                            } else {
                                val all_param_list = response_id_list[rl].parameterList
                                val delimeter_list = response_id_list[rl].delimeterList
                                var index = 0
                                while (index < delimeter_list.size) {
                                    val validation_value = delimeter_list[index].validation_value
                                    val validation_index = delimeter_list[index].validation_index
                                    var remark = delimeter_list[index].remark
                                    val type = delimeter_list[index].type
                                    val sateliteTypeModelArrayList =
                                        delimeter_list[index].sateliteTypeModelArrayList
                                    if (remark == null) {
                                        remark = ""
                                    }
                                    if (remark == "*") {
                                        remark = ";"
                                        response = response.replace("*", ";")
                                        db_response = db_response.replace("*", ";")
                                    }
                                    if (response_type_id == "2") {
                                        if (remark != "") {
                                            val res_split = response.split(remark.toRegex())
                                                .dropLastWhile { it.isEmpty() }
                                                .toTypedArray()
                                            val header = res_split[0]
                                            val header2 = res_split[1]
                                            val split_index = validation_index.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                            if (split_index.size > 0) {
                                                val start = split_index[0]
                                                val end = split_index[1]
                                                var res_val = ""
                                                var start_index = 0
                                                var end_index = 0
                                                if (start != "") {
                                                    start_index = start.toInt()
                                                }
                                                if (end != "") {
                                                    end_index = end.toInt()
                                                }
                                                res_val =
                                                    header.substring(start_index - 1, end_index)
                                                if (res_val == validation_value) {
                                                    val flag_param_map = checkParameter(
                                                        res_id,
                                                        remark,
                                                        all_param_list,
                                                        db_response,
                                                        data_extract_type
                                                    )
                                                    if (flag_param_map != "") {
                                                        val flag_param_map_arr =
                                                            flag_param_map.split("~".toRegex())
                                                                .dropLastWhile { it.isEmpty() }
                                                                .toTypedArray()
                                                        val flag = flag_param_map_arr[0].toInt()
                                                        var parm_map = flag_param_map_arr[1]
                                                        parm_map = parm_map.substring(
                                                            1,
                                                            parm_map.length - 1
                                                        )
                                                        val keyValuePairs =
                                                            parm_map.split(",".toRegex())
                                                                .dropLastWhile { it.isEmpty() }
                                                                .toTypedArray()
                                                        val keyList: MutableList<Int> = ArrayList()
                                                        val valList: MutableList<String> =
                                                            ArrayList()
                                                        for (pair in keyValuePairs) {
                                                            val entry = pair.split("=".toRegex())
                                                                .dropLastWhile { it.isEmpty() }
                                                                .toTypedArray()
                                                            keyList.add(entry[0].trim { it <= ' ' }
                                                                .toInt())
                                                            valList.add(entry[1].trim { it <= ' ' })
                                                        }
                                                        if (flag == 1) {
                                                            val header_split =
                                                                header.split(data_extract_type.toRegex())
                                                                    .dropLastWhile { it.isEmpty() }
                                                                    .toTypedArray()
                                                            for (i in header_split.indices) {
                                                                for (j in keyList.indices) {
                                                                    val key = keyList[j]
                                                                    if (i == key) {
                                                                        var `val` = header_split[i]
                                                                        val param_type_and_id =
                                                                            checkParamType(
                                                                                valList[j]
                                                                            )
                                                                        val param_type_and_id_arr =
                                                                            param_type_and_id.split(
                                                                                ",".toRegex()
                                                                            )
                                                                                .dropLastWhile { it.isEmpty() }
                                                                                .toTypedArray()
                                                                        val param_type =
                                                                            param_type_and_id_arr[0]
                                                                        val param_id =
                                                                            param_type_and_id_arr[1]
                                                                        if (param_type == "Fixed Response") {
                                                                            val disp_val =
                                                                                getDisplayValue(
                                                                                    `val`,
                                                                                    param_id
                                                                                )
                                                                            if (type!!.contains("GSA")) {
                                                                                headerValue =
                                                                                    validation_value
                                                                                gsaFlag = 1
                                                                                gsvFlag = 0
                                                                                validate_gsa_map.put(
                                                                                    valList[j],
                                                                                    disp_val
                                                                                )
                                                                            } else {
                                                                                gsvFlag = 0
                                                                                gsaFlag = 0
                                                                                validate_res_map.put(
                                                                                    valList[j],
                                                                                    disp_val
                                                                                )
                                                                            }
                                                                        } else {
                                                                            val parameter =
                                                                                valList[j]

                                                                            if (parameter == "SatelliteType") {
                                                                                if (`val` == "") {
                                                                                    `val` = "0"
                                                                                }
                                                                                val value =
                                                                                    `val`.toInt()
                                                                                if (sateliteTypeModelArrayList.size > 0) {
                                                                                    for (index1 in sateliteTypeModelArrayList.indices) {
                                                                                        val startValue =
                                                                                            sateliteTypeModelArrayList[index1].start_prn.trim { it <= ' ' }
                                                                                                .toInt()
                                                                                        val endValue =
                                                                                            sateliteTypeModelArrayList[index1].end_prn.trim { it <= ' ' }
                                                                                                .toInt()
                                                                                        val sateliteName =
                                                                                            sateliteTypeModelArrayList[index1].satelite_type
                                                                                        if (value >= startValue && value <= endValue) {
                                                                                            `val` =
                                                                                                sateliteName
                                                                                            validate_res_map.put(
                                                                                                valList[j],
                                                                                                `val`
                                                                                            )
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (type == "GSA") {
                                                                                headerValue =
                                                                                    validation_value
                                                                                gsaFlag = 1
                                                                                gsvFlag = 0
                                                                            } else {
                                                                                gsvFlag = 0
                                                                                gsaFlag = 0
                                                                                validate_res_map.put(
                                                                                    valList[j],
                                                                                    `val`
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            val header_split =
                                                                header2.split(data_extract_type.toRegex())
                                                                    .dropLastWhile { it.isEmpty() }
                                                                    .toTypedArray()
                                                            for (i in header_split.indices) {
                                                                for (j in keyList.indices) {
                                                                    val key = keyList[j]
                                                                    if (i == key) {
                                                                        var `val` = header_split[i]
                                                                        val param_type_and_id =
                                                                            checkParamType(
                                                                                valList[j]
                                                                            )
                                                                        val param_type_and_id_arr =
                                                                            param_type_and_id.split(
                                                                                ",".toRegex()
                                                                            )
                                                                                .dropLastWhile { it.isEmpty() }
                                                                                .toTypedArray()
                                                                        val param_type =
                                                                            param_type_and_id_arr[0]
                                                                        val param_id =
                                                                            param_type_and_id_arr[1]
                                                                        if (param_type == "Fixed Response") {
                                                                            val disp_val =
                                                                                getDisplayValue(
                                                                                    `val`,
                                                                                    param_id
                                                                                )
                                                                            if (type!!.contains("GSA")) {
                                                                                headerValue =
                                                                                    validation_value
                                                                                gsaFlag = 1
                                                                                gsvFlag = 0
                                                                                validate_gsa_map.put(
                                                                                    valList[j],
                                                                                    disp_val
                                                                                )
                                                                            } else {
                                                                                gsvFlag = 0
                                                                                gsaFlag = 0
                                                                                validate_res_map.put(
                                                                                    valList[j],
                                                                                    disp_val
                                                                                )
                                                                            }
                                                                        } else {
                                                                            val parameter =
                                                                                valList[j]
                                                                            if (parameter == "SatelliteType") {
                                                                                if (`val` == "") {
                                                                                    `val` = "0"
                                                                                }
                                                                                val value =
                                                                                    `val`.toInt()
                                                                                if (sateliteTypeModelArrayList.size > 0) {
                                                                                    for (index1 in sateliteTypeModelArrayList.indices) {
                                                                                        val startValue =
                                                                                            sateliteTypeModelArrayList[index1].start_prn.trim { it <= ' ' }
                                                                                                .toInt()
                                                                                        val endValue =
                                                                                            sateliteTypeModelArrayList[index1].end_prn.trim { it <= ' ' }
                                                                                                .toInt()
                                                                                        val sateliteName =
                                                                                            sateliteTypeModelArrayList[index1].satelite_type
                                                                                        if (value >= startValue && value <= endValue) {
                                                                                            `val` =
                                                                                                sateliteName
                                                                                            validate_gsa_map.put(
                                                                                                valList[j],
                                                                                                `val`
                                                                                            )
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (type == "GSA") {
                                                                                headerValue =
                                                                                    validation_value
                                                                                gsaFlag = 1
                                                                                gsvFlag = 0
                                                                            } else {
                                                                                gsvFlag = 0
                                                                                gsaFlag = 0
                                                                                validate_res_map.put(
                                                                                    valList[j],
                                                                                    `val`
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    response_id_list[rl].flag = 1
                                                }
                                            }
                                        } else {
                                            val split_index = validation_index.split("_".toRegex())
                                                .dropLastWhile { it.isEmpty() }
                                                .toTypedArray()
                                            if (split_index.size > 0) {
                                                val start = split_index[0]
                                                val end = split_index[1]
                                                var res_val = 0.toChar()
                                                var start_index = 0
                                                var end_index = 0
                                                if (start != "") {
                                                    start_index = start.toInt()
                                                }
                                                if (end != "") {
                                                    end_index = end.toInt()
                                                }
                                                if (end_index == 0) {
                                                    res_val = response[start_index - 1]
                                                    if (validation_value == res_val.toString()) {
                                                    }
                                                } else {
                                                    param = validation_value
                                                    if (all_param_list.contains(param)) {
                                                        param_val = response.substring(
                                                            start_index - 1,
                                                            end_index
                                                        )
                                                        validate_res_map.put(param, param_val)
                                                    } else {
                                                        if (validation_value == response.substring(
                                                                start_index - 1,
                                                                end_index
                                                            )
                                                        ) {
                                                            println("Validated")
                                                        } else {
                                                            println("Not Validated")
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        var flag_count = response_id_list[rl].flag
                                        if (flag_count == 0) {
                                            response_id_list[rl].flag = 1
                                            flag_count = 1
                                        }
                                        if (flag_count == 1) {

                                            if (type == "GSV") {
                                                headerValue = validation_value
                                                gsvFlag = 1
                                                gsaFlag = 0
                                                val splitData = response.split(",")

                                                // Step 2: Create an ArrayList to store all the elements
                                                val elements = ArrayList(splitData)

                                                // Step 3: Create an ArrayList to store sub-arrays of size 4
                                                val arrays = ArrayList<List<String>>()
                                                // Step 4: Store elements in sub-arrays of size 4
                                                val size = 4
                                                for (i in 0 until elements.size step size) {
                                                    val subArray = elements.subList(
                                                        i,
                                                        minOf(i + size, elements.size)
                                                    )
                                                    arrays.add(subArray)
                                                }

                                                // Print the arrays
                                                for ((index, array) in arrays.withIndex()) {
                                                    if (index != 0) {

                                                        if (array.size % 4 == 0) {
                                                            var satno: Int? = null
                                                            val noofsat: String = array[0]
                                                            val elevation: String = array[1]
                                                            val azimuth: String = array[2]
                                                            val snr: String = array[3]
                                                            if (noofsat.isNotEmpty()) {
                                                                satno = noofsat.trim().toInt()
                                                            }
                                                            if (noofsat.trim()
                                                                    .isNotEmpty() && elevation.trim()
                                                                    .isNotEmpty() && azimuth.trim()
                                                                    .isNotEmpty()
                                                            ) {
                                                                validate_gsv_map.put(
                                                                    "elevation",
                                                                    elevation
                                                                )
                                                                validate_gsv_map.put("SNR", snr)
                                                                validate_gsv_map.put(
                                                                    "azimuth",
                                                                    azimuth
                                                                )
                                                                validate_gsv_map.put("PRN", noofsat)
                                                            }
                                                        }
                                                    }

                                                }

                                            } else {
                                                if (remark != "") {
                                                    val res_split = response.split(remark.toRegex())
                                                        .dropLastWhile { it.isEmpty() }
                                                        .toTypedArray()
                                                    val header = res_split[0]
                                                    val header2 = res_split[1]
                                                    val split_index =
                                                        validation_index.split("_".toRegex())
                                                            .dropLastWhile { it.isEmpty() }
                                                            .toTypedArray()
                                                    if (split_index.size > 0) {
                                                        val start = split_index[0]
                                                        val end = split_index[1]
                                                        var res_val = ""
                                                        var start_index = 0
                                                        var end_index = 0
                                                        if (start != "") {
                                                            start_index = start.toInt()
                                                        }
                                                        if (end != "") {
                                                            end_index = end.toInt()
                                                        }
                                                        res_val =
                                                            header.substring(
                                                                start_index - 1,
                                                                end_index
                                                            )
                                                        if (res_val == validation_value) {
                                                            val flag_param_map = checkParameter(
                                                                res_id,
                                                                remark,
                                                                all_param_list,
                                                                db_response,
                                                                data_extract_type
                                                            )
                                                            if (flag_param_map != "") {
                                                                val flag_param_map_arr =
                                                                    flag_param_map.split("~".toRegex())
                                                                        .dropLastWhile { it.isEmpty() }
                                                                        .toTypedArray()
                                                                val flag =
                                                                    flag_param_map_arr[0].toInt()
                                                                var parm_map = flag_param_map_arr[1]
                                                                parm_map = parm_map.substring(
                                                                    1,
                                                                    parm_map.length - 1
                                                                )
                                                                val keyValuePairs =
                                                                    parm_map.split(",".toRegex())
                                                                        .dropLastWhile { it.isEmpty() }
                                                                        .toTypedArray()
                                                                val keyList: MutableList<Int> =
                                                                    ArrayList()
                                                                val valList: MutableList<String> =
                                                                    ArrayList()
                                                                for (pair in keyValuePairs) {
                                                                    val entry =
                                                                        pair.split("=".toRegex())
                                                                            .dropLastWhile { it.isEmpty() }
                                                                            .toTypedArray()
                                                                    keyList.add(entry[0].trim { it <= ' ' }
                                                                        .toInt())
                                                                    valList.add(entry[1].trim { it <= ' ' })
                                                                }
                                                                if (flag == 1) {
                                                                    val header_split = header.split(
                                                                        data_extract_type.toRegex()
                                                                    ).dropLastWhile { it.isEmpty() }
                                                                        .toTypedArray()
                                                                    for (i in header_split.indices) {
                                                                        for (j in keyList.indices) {
                                                                            val key = keyList[j]
                                                                            if (i == key) {
                                                                                var `val` =
                                                                                    header_split[i]
                                                                                val param_type_and_id =
                                                                                    checkParamType(
                                                                                        valList[j]
                                                                                    )
                                                                                val param_type_and_id_arr =
                                                                                    param_type_and_id.split(
                                                                                        ",".toRegex()
                                                                                    )
                                                                                        .dropLastWhile { it.isEmpty() }
                                                                                        .toTypedArray()
                                                                                val param_type =
                                                                                    param_type_and_id_arr[0]
                                                                                val param_id =
                                                                                    param_type_and_id_arr[1]
                                                                                if (param_type == "Fixed Response") {
                                                                                    val disp_val =
                                                                                        getDisplayValue(
                                                                                            `val`,
                                                                                            param_id
                                                                                        )
                                                                                    if (type == "GSA") {

                                                                                        headerValue =
                                                                                            validation_value
                                                                                        gsaFlag = 1
                                                                                        gsvFlag = 0
                                                                                        validate_gsa_map.put(
                                                                                            valList[j],
                                                                                            disp_val
                                                                                        )
                                                                                    } else {
                                                                                        gsvFlag = 0
                                                                                        gsaFlag = 0
                                                                                        validate_res_map.put(
                                                                                            valList[j],
                                                                                            disp_val
                                                                                        )
                                                                                    }
                                                                                } else {
                                                                                    val parameter =
                                                                                        valList[j]
                                                                                    if (parameter == "SatelliteType") {
                                                                                        if (`val` == "") {
                                                                                            `val` =
                                                                                                "0"
                                                                                        }
                                                                                        val value =
                                                                                            `val`.trim { it <= ' ' }
                                                                                                .toInt()
                                                                                        if (sateliteTypeModelArrayList.size > 0) {
                                                                                            for (index1 in sateliteTypeModelArrayList.indices) {
                                                                                                val startValue =
                                                                                                    sateliteTypeModelArrayList[index1].start_prn.trim { it <= ' ' }
                                                                                                        .toInt()
                                                                                                val endValue =
                                                                                                    sateliteTypeModelArrayList[index1].end_prn.trim { it <= ' ' }
                                                                                                        .toInt()
                                                                                                val sateliteName =
                                                                                                    sateliteTypeModelArrayList[index1].satelite_type
                                                                                                if (value >= startValue && value <= endValue) {
                                                                                                    `val` =
                                                                                                        sateliteName
                                                                                                    validate_gsa_map.put(
                                                                                                        valList[j],
                                                                                                        `val`
                                                                                                    )
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if (type == "GSA") {
                                                                                        headerValue =
                                                                                            validation_value
                                                                                        gsaFlag = 1
                                                                                        gsvFlag = 0
                                                                                    } else {
                                                                                        gsvFlag = 0
                                                                                        gsaFlag = 0
                                                                                        validate_res_map.put(
                                                                                            valList[j],
                                                                                            `val`
                                                                                        )
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    val header_split =
                                                                        header2.split(
                                                                            data_extract_type.toRegex()
                                                                        )
                                                                            .dropLastWhile { it.isEmpty() }
                                                                            .toTypedArray()
                                                                    for (i in header_split.indices) {
                                                                        for (j in keyList.indices) {
                                                                            val key = keyList[j]
                                                                            if (i == key) {
                                                                                var `val` =
                                                                                    header_split[i]
                                                                                val param_type_and_id =
                                                                                    checkParamType(
                                                                                        valList[j]
                                                                                    )
                                                                                val param_type_and_id_arr =
                                                                                    param_type_and_id.split(
                                                                                        ",".toRegex()
                                                                                    )
                                                                                        .dropLastWhile { it.isEmpty() }
                                                                                        .toTypedArray()
                                                                                val param_type =
                                                                                    param_type_and_id_arr[0]
                                                                                val param_id =
                                                                                    param_type_and_id_arr[1]
                                                                                if (param_type == "Fixed Response") {
                                                                                    val disp_val =
                                                                                        getDisplayValue(
                                                                                            `val`,
                                                                                            param_id
                                                                                        )

                                                                                    if (type == "GSA") {
                                                                                        headerValue =
                                                                                            validation_value
                                                                                        gsaFlag = 1
                                                                                        gsvFlag = 0
                                                                                        validate_gsa_map.put(
                                                                                            valList[j],
                                                                                            disp_val
                                                                                        )
                                                                                    } else {
                                                                                        gsvFlag = 0
                                                                                        gsaFlag = 0
                                                                                        validate_res_map.put(
                                                                                            valList[j],
                                                                                            disp_val
                                                                                        )
                                                                                    }
                                                                                } else {
                                                                                    val parameter =
                                                                                        valList[j]
                                                                                    if (parameter == "SatelliteType") {
                                                                                        if (`val` == "") {
                                                                                            `val` =
                                                                                                "0"
                                                                                        }
                                                                                        val value =
                                                                                            `val`.toInt()
                                                                                        if (sateliteTypeModelArrayList.size > 0) {
                                                                                            for (index1 in sateliteTypeModelArrayList.indices) {
                                                                                                val startValue =
                                                                                                    sateliteTypeModelArrayList[index1].start_prn.trim { it <= ' ' }
                                                                                                        .toInt()
                                                                                                val endValue =
                                                                                                    sateliteTypeModelArrayList[index1].end_prn.trim { it <= ' ' }
                                                                                                        .toInt()
                                                                                                val sateliteName =
                                                                                                    sateliteTypeModelArrayList[index1].satelite_type
                                                                                                if (value >= startValue && value <= endValue) {
                                                                                                    `val` =
                                                                                                        sateliteName
                                                                                                    validate_gsa_map.put(
                                                                                                        valList[j],
                                                                                                        `val`
                                                                                                    )
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if (type == "GSA") {
                                                                                        headerValue =
                                                                                            validation_value
                                                                                        gsaFlag = 1
                                                                                        gsvFlag = 0
                                                                                    } else {
                                                                                        gsvFlag = 0
                                                                                        gsaFlag = 0
                                                                                        validate_res_map.put(
                                                                                            valList[j],
                                                                                            `val`
                                                                                        )
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            response_id_list[rl].flag = 0
                                                        }
                                                    }
                                                } else {
                                                    val split_index =
                                                        validation_index.split("_".toRegex())
                                                            .dropLastWhile { it.isEmpty() }
                                                            .toTypedArray()
                                                    if (split_index.size > 0) {
                                                        val start = split_index[0]
                                                        val end = split_index[1]
                                                        var res_val = 0.toChar()
                                                        var start_index = 0
                                                        var end_index = 0
                                                        if (start != "") {
                                                            start_index = start.toInt()
                                                        }
                                                        if (end != "") {
                                                            end_index = end.toInt()
                                                        }
                                                        if (end_index == 0) {
                                                            res_val = response[start_index - 1]
                                                            if (validation_value == res_val.toString()) {
                                                            } else {
                                                                response_id_list[rl].flag = 0
                                                            }
                                                        } else {
                                                            param = validation_value
                                                            if (all_param_list.contains(param)) {
                                                                param_val = response.substring(
                                                                    start_index - 1,
                                                                    end_index
                                                                )
                                                                validate_res_map.put(
                                                                    param,
                                                                    param_val
                                                                )
                                                            } else {
                                                                if (validation_value == response.substring(
                                                                        start_index - 1,
                                                                        end_index
                                                                    )
                                                                ) {
                                                                    println("Validated")
                                                                    response_id_list[rl].flag = 1
                                                                } else {
                                                                    println("Not Validated")
                                                                    response_id_list[rl].flag = 0
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    index++
                                }
                            }
                        }
                    }
                }
            }


            if(validate_res_map.size()>0)
            {
                listner.fixResponseData(validate_res_map)
            }

            if (gsvFlag == 1) {

                if (!gsv_res_map.contains(
                        ResponseHandlingModel(
                            headerValue.toString(),
                            validate_gsv_map
                        )
                    )) {
                    gsv_res_map.add(
                        ResponseHandlingModel(
                            headerValue.toString(),
                            validate_gsv_map
                        )
                    )
                }
            }
            else
            {
                if (gsv_res_map.size > 0) {
                    listner.gsvRecieveData(gsv_res_map)
                    gsv_res_map.clear()

                }
            }
            if (gsaFlag == 1) {
                if (!gsa_res_map.contains(
                        ResponseHandlingModel(
                            headerValue.toString(),
                            validate_gsa_map
                        )
                    )) {
                    gsa_res_map.add(
                        ResponseHandlingModel(
                            headerValue.toString(),
                            validate_gsa_map
                        )
                    )
                }
            } else {
                if (gsa_res_map.size > 0) {
                    listner.gsaRecieveData(gsa_res_map)
                    gsa_res_map.clear()
                }
            }
        }
        catch (e: java.lang.Exception) {
            println("com.apogee.db.SPU.validateResponse()" + e.message)

        }


    }

    fun checkParameter(
        res_id: String?,
        remark: String,
        param_list: List<String?>,
        response: String,
        data_extract_type: String
    ): String {
        var flag = 0
        val map = HashMap<Int, String>()
        try {
            val response_split = response.split(remark.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val header = response_split[0]
            val header2 = response_split[1]
            if (header.contains("/")) {
                val header_split =
                    header.split(data_extract_type.toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                for (i in header_split.indices) {
                    var param = header_split[i].trim { it <= ' ' }
                    if (param.contains("/")) {
                        param = param.replace("/", "")
                        if (param_list.contains(param)) {
                            map[i] = param
                            flag = 1
                        }
                    }
                }
            } else {
                val header_split =
                    header2.split(data_extract_type.toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                for (i in header_split.indices) {
                    var param = header_split[i]
                    if (param.contains("/")) {
                        param = param.replace("/", "")
                        if (param_list.contains(param)) {
                            map[i] = param
                            flag = 2
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            println("com.apogee.db.SPU.checkParameter()$e")
        }
        return "$flag~$map"
    }
    //End Function Three

    //End Function Three
    //Start Function Four
    @SuppressLint("Range")
    fun getDisplayValue(value: String, param_id: String): String {
        var disp_val = ""
        try {
            val rs = dbTask.getFixedResponse(param_id, value)
            while (rs!!.moveToNext()) {
                disp_val = rs.getString(rs.getColumnIndex("display_value"))
            }
        } catch (e: java.lang.Exception) {
            println("com.apogee.db.SPU.getDisplayValue()$e")
        }
        return disp_val
    }

    @SuppressLint("Range")
    fun checkParamType(param: String): String {
        var type = ""
        var parameter_id = ""
        try {
            val query =
                (" select * from parameter2 p,parameter_type pt where p.active='Y' and pt.active='Y' "
                        + " and p.parameter_type_id=pt.parameter_type_id "
                        + " and p.parameter_name='" + param + "' and p.parameter_type_id in(8,9,10) ")

            val rs = dbTask.getParameterResponse(param)
            while (rs!!.moveToNext()) {
                type = rs.getString(rs.getColumnIndex("parameter_type_name"))
                parameter_id = rs.getString(rs.getColumnIndex("parameter_id"))
                //  Log.d(TAG, "checkParamType: "+type+"===="+parameter_id);
            }
        } catch (e: java.lang.Exception) {
            println("com.apogee.db.SPU.checkParamType()$e")
        }
        return "$type,$parameter_id"
    }
}