package com.apogee.geomaster.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apogee.geomaster.databinding.HomeScreenLayoutBinding

class HomeScreen : AppCompatActivity() {

    private lateinit var binding: HomeScreenLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeScreenLayoutBinding.inflate(layoutInflater)

    }
}