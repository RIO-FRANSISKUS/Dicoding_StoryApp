package com.example.storyapp.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storyapp.R
import com.example.storyapp.apiNetwork.StoryApi
import com.example.storyapp.databinding.FragmentMapBinding
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.ui.base.BaseFragment
import com.example.storyapp.ui.dashboard.DashboardViewModel
import com.example.storyapp.util.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MapFragment : BaseFragment<DashboardViewModel, FragmentMapBinding, StoryRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.actionLogout.setOnClickListener {
            showLogoutDialog()
        }

        binding.toMapButton.setOnClickListener {
            requireActivity().startNewActivity(MapsActivity::class.java)
        }
    }

    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentMapBinding.inflate(inflater, container, false)

    override fun getRepository(): StoryRepository {
        val token = runBlocking { userPreferences.loginToken.first()}
        val api = apiConfig.apiClient(StoryApi::class.java,token)
        return StoryRepository(api)
    }

}