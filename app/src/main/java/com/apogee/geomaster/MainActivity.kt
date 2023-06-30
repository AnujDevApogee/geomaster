package com.apogee.geomaster

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.apogee.geomaster.Repository.RepositoryClass
import com.apogee.geomaster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    val RESULTCODE=500
    private val myViewModel = LoginViewModel("Sibin", "123456", repositoryClass = RepositoryClass())
//    lateinit var testSocket: SocketActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.viewModel = myViewModel
        activityMainBinding.lifecycleOwner = this
        activityMainBinding.executePendingBindings()
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