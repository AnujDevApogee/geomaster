package com.apogee.geomaster

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
import com.apogee.geomaster.databinding.ActivityMainBinding
import com.apogee.geomaster.utils.toastMsg
import com.apogee.geomaster.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val RESULTCODE = 500
    private val myViewModel: LoginViewModel by viewModels()

    //    lateinit var testSocket: SocketActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.viewModel = myViewModel
        activityMainBinding.lifecycleOwner = this
        activityMainBinding.executePendingBindings()
        lifecycleScope.launch {

            myViewModel.msgEvent.collectLatest { msg ->
                msg.let {
                    toastMsg(it)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("toastMessage")
        fun runMe(view: View, message: String?) {
            if (!message.isNullOrEmpty()) {
                Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}