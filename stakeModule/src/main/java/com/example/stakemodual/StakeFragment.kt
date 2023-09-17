package com.example.stakemodual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stakemodual.databinding.MainStakeLayoutBinding
import com.example.stakemodual.utils.StakePointItemClickPoint

class StakeFragment(private val listener: StakePointItemClickPoint) : Fragment() {


    private lateinit var binding: MainStakeLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = MainStakeLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainButtonClicked.setOnClickListener {
            listener.getItemClicked(binding.mainButtonClicked)
        }
    }

}