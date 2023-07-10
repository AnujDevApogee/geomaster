package com.apogee.geomaster.ui.testing

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.TestingLayoutBinding
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.viewmodel.RecordsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TestingFragment : Fragment(R.layout.testing_layout) {

    private lateinit var binding: TestingLayoutBinding
    private val viewModel: RecordsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TestingLayoutBinding.bind(view)
        getData()
        binding.getDataBtn.setOnClickListener {
            viewModel.getTblRecords()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recordsTable.collectLatest { res ->
                    when (res) {
                        is ApiResponse.Error -> {
                            Log.i(
                                "API_RESPONSE_Final",
                                "Error: ${res.data ?: res.exception?.localizedMessage}"
                            )
                        }

                        is ApiResponse.Loading -> {
                            Log.i("API_RESPONSE_FINAL", "Loading: ${res.data}")
                        }

                        is ApiResponse.Success -> {
                            Log.i("API_RESPONSE_FINAL", "Success: ${res.data}")
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                (res.data as List<String>?) ?: emptyList()
                            )
                            adapter.setDropDownViewResource(
                                android.R.layout
                                    .simple_spinner_dropdown_item
                            )

                            binding.spinnerView.adapter = adapter
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

    }
}