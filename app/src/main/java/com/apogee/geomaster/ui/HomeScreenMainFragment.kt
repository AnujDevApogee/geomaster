package com.apogee.geomaster.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ViewPagerAdapter
import com.apogee.geomaster.bluetooth.ResponseHandling
import com.apogee.geomaster.databinding.HomeScreenMainFragmentLayoutBinding
import com.apogee.geomaster.model.HomeScreenOption
import com.apogee.geomaster.repository.BleConnectionRepository
import com.apogee.geomaster.ui.device.DeviceFragment
import com.apogee.geomaster.ui.projects.ProjectsFragment
import com.apogee.geomaster.ui.survey.SurveyFragment
import com.apogee.geomaster.ui.tools.ToolsFragment
import com.apogee.geomaster.utils.RotateDownPageTransformer
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.toastMsg
import com.apogee.geomaster.viewmodel.BleConnectionViewModel
import com.apogee.updatedblelibrary.BleService
import com.apogee.updatedblelibrary.Utils.BleResponse
import com.apogee.updatedblelibrary.Utils.BleResponseListener
import com.apogee.updatedblelibrary.Utils.OnSerialRead
import com.bumptech.glide.util.Util
import kotlinx.coroutines.launch
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import java.lang.Exception
import kotlin.math.log

class HomeScreenMainFragment : Fragment(R.layout.home_screen_main_fragment_layout) ,BleResponseListener{

    private lateinit var binding: HomeScreenMainFragmentLayoutBinding
    private lateinit var viewPagerAdaptor: ViewPagerAdapter
    private val bleConnectionViewModel: BleConnectionViewModel by activityViewModels()
    private var service: BleService? = null
    private val menuItem = arrayOf(
        CbnMenuItem(
            R.drawable.ic_folder,
            R.drawable.anim_folder_vector,
            R.id.projectsFragment
        ),
        CbnMenuItem(
            R.drawable.ic_device,
            R.drawable.avd_ic_device,
            R.id.deviceFragment
        ),
        CbnMenuItem(
            R.drawable.ic_survey,
            R.drawable.avd_anim_survey,
            R.id.surveyFragment
        ),
        CbnMenuItem(
            R.drawable.ic_setting,
            R.drawable.avd_setting,
            R.id.toolsFragment
        ),
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeScreenMainFragmentLayoutBinding.bind(view)
        setUpBottomNav()
        setUpAdaptor()
        setUpBottomNavClick()
        getResponse()
        Log.i("Current_Page", "onViewCreated: Page ${binding.mainFragmentViewPager.currentItem}")
    }

    private fun setUpBottomNavClick() {
        binding.navView.setOnMenuItemClickListener { _, position ->
            try {
                Log.i("VIEW_PAGER", "onResume: ITEM CLICKED  at $position")
                binding.mainFragmentViewPager.currentItem = position
            } catch (e: Exception) {
                Log.i("VIEW_PAGER", "onResume: $e")
            }
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        super.onStart()

    }



    private fun setUpBottomNav() {
        binding.navView.setMenuItems(menuItem)
    }

    override fun onPause() {
        super.onPause()
        binding.mainFragmentViewPager.currentItem=0
    }

    fun <T> onChildFragmentResponse(response: T) {
        activity?.toastMsg("$response")
        if (response is HomeScreenOption && response.navId != -1) {
            findNavController().safeNavigate(response.navId)
        }
    }

    fun changeToBottom(pos: Int) {
        try {
            binding.navView.onMenuItemClick(pos)
        } catch (e: Exception) {
            binding.navView.onMenuItemClick(0)
        }
    }



    private fun getResponse() {

        lifecycleScope.launch {

            bleConnectionViewModel.bleResponse.collect {
                if (it != null) {
                    when (it) {
                        is BleResponse.OnConnected ->{} //binding.pbBle.isVisible = false
                        is BleResponse.OnConnectionClose -> Log.d(ContentValues.TAG, "getResponse: " + it.message)
                        is BleResponse.OnDisconnected -> Log.d(ContentValues.TAG, "getResponse: " + it.message)
                        is BleResponse.OnError -> Log.d(ContentValues.TAG, "getResponse: " + it.message)
                        is BleResponse.OnLoading ->{} //binding.pbBle.isVisible = true

                        is BleResponse.OnReconnect -> Log.d(ContentValues.TAG, "getResponse: " + it.message)
                        is BleResponse.OnResponseRead -> {
                            Log.d(ContentValues.TAG, "getResponse: " + it.response)
                            when(it.response){
                                is OnSerialRead.onSerialNmeaRead -> {
                                   // ResponseHandling(requireContext()).validateResponse()


                                }
                                is OnSerialRead.onSerialProtocolRead ->{
                                    Log.d("BLE_HOME_INFO", "getResponse: " + it.response)
                                }
                                is OnSerialRead.onSerialResponseRead ->{
                                    Log.d("BLE_HOME_INFO", "getResponse: " + it.response)
                                }
                            }
                           // findNavController().safeNavigate(R.id.action_bluetoothscandevicefragment_to_homeScreenMainFragment)
                        }
                        is BleResponse.OnResponseWrite -> Log.d(
                            ContentValues.TAG,
                            "getResponse: " + it.isMessageSend
                        )

                    }
                }
            }

        }

    }


  /*  fun getCommand()
    {
        val allSkyViewCmd   = "Enable_GGA";
        val operationIdList = dbTask.getOperationIdForSkyView(allSkyViewCmd)
        val newCommandList  = dbTask.getCommandDataForSkyView(operationIdList.toString().replace("[", "").replace("]", ""), sharedPreferences!!.getString(Constants.DGPS_DEVICE_ID,"")!!.toInt())
        newCommandFormatList= dbTask.getCommandFormatListForSkyView(operationIdList.toString().replace("[", "").replace("]", ""),  sharedPreferences!!.getString(Constants.DGPS_DEVICE_ID,"")!!.toInt())
        responseList.clear()
        commandList.clear()
        if (sharedPreferences!!.getString(Constants.HEADER_LENGTH, null) != null) {
            headerLength = sharedPreferences!!.getString(Constants.HEADER_LENGTH, null)!!
                .toInt()
        }

        headerName=sharedPreferences!!.getString(Constants.HEADER_NAME,"").toString()
        for (i in newCommandList.indices) {
            val commandId = newCommandList[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1]
            val commandName = newCommandList[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0]
            dbTask.open()
            val resList = dbTask.getResponseId(commandId.trim { it <= ' ' }
                .toInt())
            responseList.addAll(resList)
            commandList.add(commandName)
        }

        commandCount = 0
        commandCounter = 0

        var msg = ""
        val data: ByteArray
        if (newCommandFormatList[commandCount] == "hex") {
            val sb = java.lang.StringBuilder()
            TextUtil.toHexString(sb, TextUtil.fromHexString(commandList[commandCount]))
            TextUtil.toHexString(sb, newline.toByteArray())
            msg = sb.toString()
            data = TextUtil.fromHexString(msg)
        } else {
            msg = commandList[commandCount]
            data = (msg + newline).toByteArray()
        }
    }
*/
    private fun setUpAdaptor() {
        viewPagerAdaptor = ViewPagerAdapter(this)
        viewPagerAdaptor.setFragment(ProjectsFragment())
        viewPagerAdaptor.setFragment(DeviceFragment())
        viewPagerAdaptor.setFragment(SurveyFragment())
        viewPagerAdaptor.setFragment(ToolsFragment())
        binding.mainFragmentViewPager.adapter = viewPagerAdaptor
       binding.mainFragmentViewPager.setPageTransformer(RotateDownPageTransformer())
    }

    override fun onResponse(res: BleResponse) {
        Log.d("TAG", "onResponse: "+res.toString())
    }
}