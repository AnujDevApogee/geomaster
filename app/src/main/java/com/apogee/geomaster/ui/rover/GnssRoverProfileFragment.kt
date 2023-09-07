package com.apogee.geomaster.ui.rover

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentGnssRoverProfileBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.updatedblelibrary.Utils.BleResponse
import com.apogee.updatedblelibrary.Utils.BleResponseListener


class GnssRoverProfileFragment : Fragment(),TextToSpeech.OnInitListener,BleResponseListener
     {

         val device_id=56
         val dgps_id=6
         var isRTKPPK = false
         var modeWork = ""
         var mode = ""
         var opid = 0
         private lateinit var dbControl: DatabaseRepsoitory
         var deviceInfotimerHandler = Handler(Looper.getMainLooper())

         private lateinit var binding:FragmentGnssRoverProfileBinding

         override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
             super.onViewCreated(view, savedInstanceState)
             binding =  FragmentGnssRoverProfileBinding.bind(view)
             dbControl = DatabaseRepsoitory(requireContext())



                 if (isRTKPPK) {


        binding.triggerPoint.visibility = View.VISIBLE
        opid = dbControl.getOperationId(getString(R.string.rtk_ppk))
        deviceInfotimerHandler.postDelayed(deviceInfoRunnable, 1000)
    }
    else {
                     opid = dbControl.getOperationId(getString(R.string.rover))
    }
             //getcommandforparsing(opid,device_id)



             binding.done.setOnClickListener{

             }

         }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gnss_rover_profile, container, false)
    }

    override fun onInit(p0: Int) {
    }


         override fun onResponse(res: BleResponse) {
             if (res != null) {
                 when (res) {
                     is BleResponse.OnConnected ->{}
//                         binding.pbBle.isVisible = false


                     is BleResponse.OnConnectionClose -> Log.d(ContentValues.TAG, "getResponse: " + res.message)
                     is BleResponse.OnDisconnected -> Log.d(ContentValues.TAG, "getResponse: " + res.message)
                     is BleResponse.OnError -> Log.d(ContentValues.TAG, "getResponse: " + res.message)
                     is BleResponse.OnLoading ->{

                     }
//                         binding.pbBle.isVisible = true


                     is BleResponse.OnReconnect -> Log.d(ContentValues.TAG, "getResponse: " + res.message)
                     is BleResponse.OnResponseRead -> {
                         Log.d(ContentValues.TAG, "getResponse: " + res.response)
//                         findNavController().safeNavigate(R.id.action_bluetoothscandevicefragment_to_homeScreenMainFragment)
                     }


                     is BleResponse.OnResponseWrite -> Log.d(
                         ContentValues.TAG,
                         "getResponse: " + res.isMessageSend
                     )

                 }
             }


         }

         var deviceInfoCounter = 0

         var deviceInfoRunnable: Runnable = object : Runnable {
             override fun run() {
                 if (isRTKPPK) {
                     deviceInfoCounter++

                     if (modeWork == "PPK" || modeWork == "$mode + PPK") {
                         //progressDialog.dismiss()
                         binding.done.isEnabled = false
                         binding.done.isClickable = false
                         binding.done.isFocusable = false
                         binding.done.setBackgroundResource(R.drawable.buttondesign1)
                         deviceInfotimerHandler.removeCallbacks(this)
                     } else if (modeWork != "PPK" || modeWork != "$mode + PPK") {
                         //progressDialog.dismiss()
                         binding.triggerPoint.isEnabled = false
                         binding.triggerPoint.isClickable = false
                         binding.triggerPoint.isFocusable = false
                         binding.triggerPoint.setBackgroundResource(R.drawable.buttondesign1)
                         deviceInfotimerHandler.removeCallbacks(this)
                     }

                     if (deviceInfoCounter < 6) {
                         deviceInfotimerHandler.postDelayed(this, 1000)
                     } else {
//                         if (progressDialog.isShowing) {
//                             progressDialog.dismiss()
//                         }
                     }
                 }


             }
         }


        /* fun getcommandforparsing(opid: Int, oppid: Int) {
             if (opid > 0) {
                 gnssdelay = dbTask.delaylist(opid, dgps_id)
                 gnsscommands = dbTask.commandforparsinglist(opid, dgps_id)
                 gnnsFormatCommands = dbTask.commandformatparsinglist(opid, dgps_id)
                 Log.d(
                     TAG,
                     "NewListgetcommandforparsing: $gnsscommands \n ${gnsscommands.size} \n $opid "
                 )
             } else if (oppid > 0) {
                 radiodelay = dbTask.delaylist(oppid, dgps_id)
                 radiocommands = dbTask.commandforparsinglist(oppid, dgps_id)
                 radioFormatCommands = dbTask.commandformatparsinglist(oppid, dgps_id)
                 Log.d(TAG, "NewListgetcommandforparsing: $oppid\n$radiocommands")

             }
         }*/



     }