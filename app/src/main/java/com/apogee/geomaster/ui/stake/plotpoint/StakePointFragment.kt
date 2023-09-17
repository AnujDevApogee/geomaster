package com.apogee.geomaster.ui.stake.plotpoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.MainStakePlotPointLayoutBinding
import com.apogee.geomaster.model.SurveyModel
import com.apogee.geomaster.repository.FakeStakePointRepository
import com.example.stakemodual.StakePointFragment
import com.example.stakemodual.utils.MockStakePointImpl

class StakePointFragment : Fragment(), MockStakePointImpl {

    private lateinit var binding: MainStakePlotPointLayoutBinding

    private val dataSet by lazy {
        FakeStakePointRepository(requireActivity().application, this)
    }

    private lateinit var mainStakeFragment: StakePointFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainStakePlotPointLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainStakeFragment = StakePointFragment()
        inflateFragment(mainStakeFragment)
        dataSet.fakeStakePoint()
        dataSet.getLocation()
    }

    private fun inflateFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.main_stake_fragment, fragment).commit()
    }

    override fun receivePoint(surveyModel: SurveyModel) {
        mainStakeFragment.receivePoint(surveyModel)
    }

    override fun stakePoint(hashMap: HashMap<String, Any>) {
        mainStakeFragment.stakePoint(hashMap)
    }

}