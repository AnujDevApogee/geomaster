package com.apogee.geomaster.View

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.apogee.geomaster.MainActivity
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.SplashActivityBinding
import com.apogee.geomaster.service.ApiService
import com.apogee.geomaster.service.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener

class SplashScreen : AppCompatActivity() {
    private val TAG: String?=SplashScreen::class.java.simpleName
    var sharedPreferences: SharedPreferences? = null
    lateinit var binding: SplashActivityBinding
    var responseString=""
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@SplashScreen)
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.splash_activity)

    }
    override fun onResume() {
        super.onResume()
         responseString= sharedPreferences!!.getString(Constants.RESPONSE_STRING,"").toString()

        if( responseString.equals("")) {
            Log.d(TAG, "onResume: ")
            val intent = Intent(this, ApiService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        Glide.with(this).asGif().load(R.raw.survey).listener(object :
            RequestListener<GifDrawable?> {
            override fun onLoadFailed(@Nullable e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<GifDrawable?>?, isFirstResource: Boolean): Boolean {
                return false
            }
            override fun onResourceReady(resource: GifDrawable?, model: Any?, target: com.bumptech.glide.request.target.Target<GifDrawable?>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                resource!!.setLoopCount(1)
                resource.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        //do whatever after specified number of loops complete
                        Log.d("checkkk==", false.toString())
                        val intents = Intent(this@SplashScreen, MainActivity::class.java)
                        startActivity(intents)
                        finish()
                    }
                })
                return false
            }
        }).into(binding!!.ivGif)


    }

}