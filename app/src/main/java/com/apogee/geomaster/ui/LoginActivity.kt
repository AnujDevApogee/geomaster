package com.apogee.geomaster.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ActivityMainBinding
import com.apogee.geomaster.utils.isInvalidString
import com.apogee.geomaster.utils.toastMsg
import com.apogee.geomaster.viewmodel.LoginViewModel
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
                myViewModel.msgEvent.collect { msg ->
                    if (!isInvalidString(msg)) {
                        toastMsg(msg)
                    }
                }
            }

        }
    }




}