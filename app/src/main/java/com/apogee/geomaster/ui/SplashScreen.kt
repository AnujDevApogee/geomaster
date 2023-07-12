package com.apogee.geomaster.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.apogee.databasemodule.TableCreator
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.SplashActivityBinding
import com.apogee.geomaster.service.ApiService
import com.apogee.geomaster.service.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener

class SplashScreen : AppCompatActivity() {
    private val TAG: String? = SplashScreen::class.java.simpleName
    var sharedPreferences: SharedPreferences? = null

    lateinit var binding: SplashActivityBinding

    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@SplashScreen)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val resp = sharedPreferences!!.getString(Constants.RESPONSE_STRING,null)
        Log.d(TAG, "onCreate: Constant${sharedPreferences!!.getString(Constants.RESPONSE_STRING, null)}")

        Log.d(TAG, "onCreate: "+resp)
        if (resp == null) {
            Log.d(
                TAG,
                "onCreate:APISERVICE Splash Oncreate ${
                    sharedPreferences!!.getString(
                        Constants.RESPONSE_STRING,
                        null
                    )
                }"
            )
            isNetworkConnectionAvailable

            val intent = Intent(this, ApiService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
        if (sharedPreferences!!.getBoolean("firstrun", true)) {
            Glide.with(this).asGif().load(R.raw.survey).listener(object :
                RequestListener<GifDrawable?> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<GifDrawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<GifDrawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource!!.setLoopCount(1)
                    resource.registerAnimationCallback(object :
                        Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            //do whatever after specified number of loops complete
                            Log.d("checkkk==", false.toString())
                            val intents = Intent(this@SplashScreen, StartActivity::class.java)
                            startActivity(intents)
                            finish()
                        }
                    })
                    return false
                }
            }).into(binding.ivGif)
            sharedPreferences!!.edit().putBoolean("firstrun", false).apply()

        } else {
            val intents = Intent(this@SplashScreen, StartActivity::class.java)
            startActivity(intents)
            finish()
        }


    }

    override fun onResume() {
        super.onResume()


    }


    val isNetworkConnectionAvailable: Unit
        @SuppressLint("MissingPermission")
        get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null &&
                    activeNetwork.isConnected
            if (isConnected) {
                Log.d("Network", "Connected")
            } else {
                checkNetworkConnection()
                Log.d("Network", "Not Connected")
            }
        }

    /*CheckNetworkConnection*/
    private fun checkNetworkConnection() {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.noconnection)
        builder.setTitle(getText(R.string.no_internet_connection))
        builder.setMessage(getText(R.string.please_turn_on_internet_connection_to_continue))
        builder.setNegativeButton(getText(R.string.ok)) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }


}