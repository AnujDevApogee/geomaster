package com.apogee.geomaster.model.MultiView

import java.io.Serializable


class ItemType(type: Int, title: String, stringStringMapdrop1: Map<String, String>) : Serializable {

    companion object {
        const val DROPDOWNTYPE = 0
        const val INPUTTYPE = 1
        const val INPUTTYPEPROJECT = 2
        const val INPUTONLYTEXT = 3
    }

    var type1 = type
    var title1 = title
    var stringStringMapdrop = stringStringMapdrop1
    var oprtr: String? = null
    var time: String? = null
    var code: String? = null
    var elevation1: String? = null
    var zone: String? = null
    var hemisphereZone: String? = null
    var stringList: ArrayList<String>? = null
    var timeStamp: String? = null
    var isFromNmea = false
    var isSelected = false

    constructor(type: Int, title: String) : this(type, title, emptyMap())

    constructor(type: Int, title: String, stringList: ArrayList<String>?, oprtr: String?, time: String?,
                timeStamp: String?, code: String?, elevation: String?, isFromNmea: Boolean, isSelected: Boolean) :
            this(type, title) {
        this.oprtr = oprtr
        this.time = time
        this.stringList = stringList
        this.timeStamp = timeStamp
        this.code = code
        this.isFromNmea = isFromNmea
        this.isSelected = isSelected
        this.elevation1 = elevation
    }

    constructor(type: Int, title: String, stringList: ArrayList<String>?, oprtr: String?, time: String?,
                timeStamp: String?, code: String?, elevation: String?, zone: String?, hemisphereZone: String?,
                isFromNmea: Boolean, isSelected: Boolean) :
            this(type, title, stringList, oprtr, time, timeStamp, code, elevation, isFromNmea, isSelected) {
        this.zone = zone
        this.hemisphereZone = hemisphereZone
    }

    fun getElevation(): String? {
        return elevation1
    }

    override fun toString(): String {
        return "ItemType{" +
                "type=$type1, title='$title1', oprtr='$oprtr', time='$time', code='$code', elevation='$elevation1', " +
                "stringList=$stringList, timeStamp='$timeStamp', stringStringMapdrop=$stringStringMapdrop, " +
                "isFromNmea=$isFromNmea, isSelected=$isSelected" +
                '}'
    }

    inner class OnDropdownlist(dropdownvalue: String, dropdownvalueId: String) {
        var dropdownvalue = dropdownvalue
        var dropdownvalueId = dropdownvalueId
    }

    fun getTitle(): String {
        return title1
    }
}
