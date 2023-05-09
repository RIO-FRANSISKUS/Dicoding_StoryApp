package com.example.storyapp.ui.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storyapp.apiNetwork.StoryApi
import com.example.storyapp.databinding.FragmentUploadStoryBinding
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.ui.base.BaseFragment
import com.example.storyapp.ui.dashboard.DashboardViewModel
import com.example.storyapp.util.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UploadStoryFragment : BaseFragment<DashboardViewModel, FragmentUploadStoryBinding, StoryRepository>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toUploadButton.setOnClickListener{
            requireActivity().startNewActivity(UploadStoryActivity::class.java)
        }

        binding.actionLogout.setOnClickListener{
            showLogoutDialog()
        }
    }

    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUploadStoryBinding.inflate(inflater, container, false)

    override fun getRepository(): StoryRepository {
        val token = runBlocking { userPreferences.loginToken.first()}
        val api = apiConfig.apiClient(StoryApi::class.java,token)
        return StoryRepository(api)
    }

}