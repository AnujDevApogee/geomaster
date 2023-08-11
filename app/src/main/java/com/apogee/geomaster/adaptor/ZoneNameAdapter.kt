package com.apogee.geomaster.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ProjectionZoneNameBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory


class ZoneNameAdapter(var context: Context?, var list : ArrayList<String>?, var onItemClickListner: OnItemClickListner) : RecyclerView.Adapter<ZoneNameAdapter.RecordViewHolder>()  {
//    var dbTask: DatabaseOperation? = null
    var TAG= "ZoneNameAdapter"
    private lateinit var dbControl: DatabaseRepsoitory




    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecordViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding :  ProjectionZoneNameBinding= ProjectionZoneNameBinding.inflate(layoutInflater, viewGroup, false)
        return RecordViewHolder(binding)
//        dbControl = DatabaseRepsoitory(this.requireContext())

    }
    var lastPos=-1
    open interface OnItemClickListner {
        fun onItemClick(name: String)
    }
    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        var zName=""
        var model=list!!.get(position)
        Log.d("TAG", "onBindViewHolder: "+model)
        holder.binding.zoneName.setText(model)

        if(lastPos==position) {
            holder.binding.zoneName.setBackgroundColor(context!!.getColor(R.color.md_theme_light_onPrimary))
        }else {
            holder.binding.zoneName.setBackgroundColor(context!!.getColor(android.R.color.transparent))
        }
        holder.binding.delBtn.setOnClickListener{
            try{
                 zName=holder.binding.zoneName.text.toString()
                Log.d(TAG, "onBindViewHolder:zName---- $zName")

                removeItem(position)
            }catch (e:Exception){
                Log.d(TAG, "onBindViewHolderException: ${e.message}")
            }

        }
        holder.binding.editBtn.setOnClickListener {
            try{
/*                var intent = Intent(context, Projection::class.java)
                intent.putExtra("edit", true)
                intent.putExtra("zoneNameID", model.split(",")[0])
                context!!.startActivity(intent)*/
            }catch (e:Exception){
                Log.d(TAG, "onBindViewHolder:editBtn ")
            }
        }

        holder.itemView.setOnClickListener {
            lastPos=position
            onItemClickListner.onItemClick(model)
            notifyDataSetChanged()
        }


    }


    override fun getItemCount(): Int {
        return list!!.size
    }
    class RecordViewHolder(val binding: ProjectionZoneNameBinding) : RecyclerView.ViewHolder(binding.root)

    fun removeItem(position: Int) {
        list!!.removeAt(position)
        notifyItemRemoved(position)
    }
}