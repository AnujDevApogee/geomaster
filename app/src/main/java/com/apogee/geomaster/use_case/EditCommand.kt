package com.apogee.geomaster.use_case

import com.apogee.geomaster.utils.createLog


class EditCommand {

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun getEditCommand(
            originalCmd: List<String>,
            mainCmd: Pair<String, Map<String, Any?>>,
            baseType: Pair<String, Map<String, Any?>>
        ): MutableList<String> {
            val perfectCmd = mutableListOf<String>()

            originalCmd.forEach { str ->
                var mutableString = str

                mainCmd.second.forEach {
                    val value = if (it.value is String)
                        it.value as String
                    else
                        ((it.value as Pair<*, *>?)?.second).toString()

                    mutableString = mutableString.replace("/${it.key}/", value)
                    createLog("TAG_ITEM", "Edited $mutableString ")
                }

                baseType.second.forEach {
                    val value = if (it.value is String)
                        it.value as String
                    else
                        ((it.value as Pair<*, *>?)?.second).toString()

                    mutableString =
                        mutableString.replace("/${it.key}/", value)
                    createLog("TAG_ITEM", "BASE Edited $mutableString ")
                }

                perfectCmd.add(mutableString)
            }


            /*
             originalCmd.forEach {
                 if (it.contains("/".toRegex()) && it != originalCmd.last()) {
                     val value = it.split("/".toRegex())
                     val string = StringBuilder()
                     value.forEach { spList ->
                         if (baseType.second.containsKey(spList)) {
                             if (baseType.second[spList] is String) {
                                 string.append("${baseType.second[spList]}")
                             } else {
                                 createLog("TAG_INFO_BLE_UPDATE","")
                                 string.append("${(baseType.second[spList] as Pair<*, *>).second}")
                             }
                         } else {
                             string.append(spList)
                         }
                     }
                     perfectCmd.add(string.toString())
                 } else if (!it.contains("/".toRegex()) && it != originalCmd.last()) {
                     perfectCmd.add(it)
                 } else {
                     val value = it.split("/".toRegex())
                     val string = StringBuilder()
                     value.forEach { splist ->
                         if (mainCmd.second.containsKey(splist)) {
                             if (mainCmd.second[splist] is String) {
                                 string.append("${mainCmd.second[splist]}")
                             } else {
                                 string.append("${(mainCmd.second[splist] as Pair<String, String>?)?.second}")
                             }
                         } else {
                             string.append(splist)
                         }
                     }
                     perfectCmd.add(string.toString())
                 }
             }*/
            createLog("MAIN_CMD_ITEM", "$perfectCmd")
            return perfectCmd
        }
    }

}