package com.apogee.geomaster.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ActivityMainBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.isInvalidString
import com.apogee.geomaster.utils.toastMsg
import com.apogee.geomaster.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    //  val RESULTCODE = 500
    private val myViewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.viewModel = myViewModel
        activityMainBinding.lifecycleOwner = this
        activityMainBinding.executePendingBindings()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                myViewModel.msgEvent.collectLatest {  msg ->
                    if (!isInvalidString(msg)) {
                        toastMsg(msg)
                        val intent=Intent(this@LoginActivity, HomeScreen::class.java)
                        startActivity(intent)
                        finishAffinity()

                    }
                }
            }
        }


    }




}