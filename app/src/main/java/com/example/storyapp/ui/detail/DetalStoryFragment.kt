package com.example.storyapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.storyapp.apiNetwork.Resource
import com.example.storyapp.apiNetwork.StoryApi
import com.example.storyapp.databinding.FragmentDetalStoryBinding
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.response.Story
import com.example.storyapp.ui.base.BaseFragment
import com.example.storyapp.ui.dashboard.DashboardViewModel
import com.example.storyapp.util.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DetalStoryFragment : BaseFragment<DashboardViewModel, FragmentDetalStoryBinding, StoryRepository>() {

    private lateinit var data: Story

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelable("detailUserStory")!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailProgressBar.visible(false)
        val id = data.id
        viewModel.getDetailStory(id)

        binding.apply {
                        Glide.with(requireContext())
                            .load(data.photoUrl)
                            .into(ivDetailPhoto)

                        tvDetailName.text = data.name
                        tvDetailDescription.text = data.description
                    }

        viewModel.userDetailStory.observe(viewLifecycleOwner){
            when (it){
                is Resource.SucsessResponse -> {
                    binding.detailProgressBar.visible(false)

                }

                is Resource.FailResponse -> {
                    binding.detailProgressBar.visible(false)
                }

                is Resource.Loading -> {
                    binding.detailProgressBar.visible(true)
                }
            }
        }

    }

    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentDetalStoryBinding.inflate(inflater, container, false)

    override fun getRepository(): StoryRepository {
        val token = runBlocking { userPreferences.loginToken.first()}
        val api = apiConfig.apiClient(StoryApi::class.java, token)
        return StoryRepository(api)
    }

}