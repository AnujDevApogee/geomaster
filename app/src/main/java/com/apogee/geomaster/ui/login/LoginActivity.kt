package com.apogee.geomaster.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ActivityMainBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.closeKeyboard
import com.apogee.geomaster.utils.isInvalidString
import com.apogee.geomaster.utils.openKeyBoard
import com.apogee.geomaster.utils.toastMsg
import com.apogee.geomaster.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    //  val RESULTCODE = 500
    private val myViewModel: LoginViewModel by viewModels()


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                myViewModel.msgEvent.collectLatest { msg ->
                    if (!isInvalidString(msg)) {
                        toastMsg(msg)
                        val intent = Intent(this@LoginActivity, HomeScreen::class.java)
                        startActivity(intent)
                        finishAffinity()

                    }
                }
            }
        }


        val anim=AnimationUtils.loadAnimation(this, R.anim.enter_anim_layout)

        binding.cardLyt.startAnimation(anim)

        binding.loginBtn.setOnClickListener {
            myViewModel.onLoginClicked()
        }

    }


    override fun onStart() {
        super.onStart()
        openKeyBoard(binding.userNm)
    }


    override fun onPause() {
        super.onPause()
        closeKeyboard(binding.userNm)
    }


}