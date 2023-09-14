package com.apogee.geomaster.adaptor

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.LayoutDataSourceBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.utils.MyPreference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.StringBuilder
import kotlin.collections.HashMap


class DataSourceAdapter( var context : Context) : RecyclerView.Adapter<DataSourceAdapter.RecordViewHolder>()  {

    var clickListerner: ClickListerner? = null

    var dataSourceList : ArrayList<HashMap<String, String>>? = null
    var list : ArrayList<Int> = ArrayList()
    var clickPos = -1
    var isFromRadio = true
    var dbTask = DatabaseRepsoitory(context)
    var temp_device_name=""
    lateinit var sharedPreferences: MyPreference
    var protocolKey: ArrayList<String> = ArrayList()
    var protocolValue: ArrayList<String> = ArrayList()
    var rs232Key: ArrayList<String> = ArrayList()
    var rs232Value: ArrayList<String> = ArrayList()

    interface ClickListerner {
        fun onSuccess(pos: Int)
        fun onDelete(id : String?)
    }

    fun setListerner(clickListerner: ClickListerner) {
        this.clickListerner = clickListerner
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecordViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding : LayoutDataSourceBinding = LayoutDataSourceBinding.inflate(layoutInflater, viewGroup, false)

        return RecordViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {


        val disp = StringBuilder()
        var ssid=""
        var ssid_password=""
        sharedPreferences=  MyPreference.getInstance(context)
        temp_device_name  = sharedPreferences.getStringData(Constants.DEVICE_NAME).toString()

        val gson = Gson()
        val json1: String = sharedPreferences.getStringData(Constants.TRIMBLE_ProtocolKey).toString()
        val json2: String = sharedPreferences.getStringData(Constants.TRIMBLE_ProtocolValue).toString()
        val rs232json1: String = sharedPreferences.getStringData(Constants.TRIMBLE_rs232Key).toString()
        val rs232json2: String = sharedPreferences.getStringData(Constants.TRIMBLE_rs232Value).toString()

        val type1 = object : TypeToken<ArrayList<String>>() {}.getType()
      if(json1!=null)
      {
         try {
             val mySet1: ArrayList<String> = gson.fromJson(json1, type1)
             val mySet2: ArrayList<String> = gson.fromJson(json2, type1)
             protocolKey =ArrayList(mySet1)
             protocolValue =ArrayList(mySet2)

         }catch (e: Exception)
         {

         }
      }
        val rs232Type = object : TypeToken<ArrayList<String>>() {}.getType()
          if(rs232json1!=null)
          {
             try {
                 val mySet1: ArrayList<String> = gson.fromJson(rs232json1, rs232Type)
                 val mySet2: ArrayList<String> = gson.fromJson(rs232json2, rs232Type)
                 rs232Key =ArrayList(mySet1)
                 rs232Value =ArrayList(mySet2)

             }catch (e: Exception)
             {

             }
          }

        for ((key, value) in dataSourceList!![position].entries) {



            try {
                if(key!= "param_id" && key!="operation"){
                    if(key.contains("Protocol") && temp_device_name.contains("NAVIK300")){
                        val temp_key =protocolValue.indexOf(value)
                        var temp_value = protocolKey.get(temp_key)
                        Log.d(TAG, "onBindViewHoldertemp_value: "+temp_value + "\n" + temp_key)
                        disp.append("$key : ${temp_value.trim()}\r\n\r\n")
                    } else if(key.contains("RS232 Baudrate") && temp_device_name.contains("NAVIK300")){
                        val temp_key =rs232Value.indexOf(value)
                        var temp_value = rs232Key.get(temp_key)
                        Log.d(TAG, "onBindViewHolder: "+temp_value)
                        disp.append("$key : ${temp_value.trim()}\r\n\r\n")
                    }
                    else
                    {
                        val output = StringBuilder()
                        var i = 0
                        while (i < value.length) {
                            val str: String = value.substring(i, i + 2)

                            output.append(str.toInt(16).toChar())
                            i += 2
                        }
                        disp.append("$key : ${output.trim()}\r\n\r\n")

                  }
                }

            }catch (ex :Exception){

            }



        }


       /* Log.d("TAG", "onBindViewHolder: "+ssid+"==="+ssid_password)
        dbTask.open()
        var result =dbTask.insertSSIDPassword(ssid.replace("'","\'").toString(),ssid_password.toString())
        if(result)
        {
            Log.d("TAG", "onBindViewHolder: "+"SSID Inserted")
        }else
        {
            Log.d("TAG", "onBindViewHolder: "+"SSID already exist")
        }*/
        Log.d("check===", disp.toString())
        holder.binding.tvData.text = disp

        if(clickPos == position){
            holder.binding.cvdata.setBackgroundColor(ContextCompat.getColor(context!!,R.color.light_gray))
        }else{
            holder.binding.cvdata.setBackgroundColor(ContextCompat.getColor(context!!,R.color.white))
        }

        holder.binding.cvdata.setOnClickListener {
            if(clickListerner != null){
                clickListerner!!.onSuccess(position)
            }
            clickPos = position
            notifyDataSetChanged()
        }

        holder.binding.ibdelete.setOnClickListener{
            if(clickListerner != null){
                clickListerner!!.onDelete(dataSourceList!![position]["param_id"])
            }
            dataSourceList!!.removeAt(position)
            notifyDataSetChanged()
        }

     holder.binding.ivEdit.setOnClickListener{
         Log.d("TAG", "onBindViewHolder: "+dataSourceList!![position])
     }
    }

    override fun getItemCount(): Int {
        if(dataSourceList != null){
            return   dataSourceList!!.size
        }else{
            return 0
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(context: Context, dataSourceList : ArrayList<HashMap<String, String>>?, isFromRadio : Boolean){
        this.context = context
        this.dataSourceList = dataSourceList
        this.isFromRadio = isFromRadio
        notifyDataSetChanged()
    }



    class RecordViewHolder(val binding: LayoutDataSourceBinding) : RecyclerView.ViewHolder(binding.root)




}