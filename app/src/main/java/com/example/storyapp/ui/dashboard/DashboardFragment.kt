package com.example.storyapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.apiNetwork.Resource
import com.example.storyapp.apiNetwork.StoryApi
import com.example.storyapp.databinding.FragmentDashboardBinding
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.ui.base.BaseFragment
import com.example.storyapp.ui.base.StoryAdapter
import com.example.storyapp.util.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DashboardFragment : BaseFragment<DashboardViewModel, FragmentDashboardBinding, StoryRepository>() {

    private lateinit var storyAdapter: StoryAdapter
    private val TAG = "DashboardAdapter"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.paginationProgressBar.visible(false)
        viewModel.getUserStories()
        setUpRecycleView()

        storyAdapter.setOnItemClickListener {
            val bundle = bundleOf("detailUserStory" to it)
            findNavController().navigate(R.id.action_dashboardFragment_to_detalStoryFragment, bundle)
        }

        viewModel.userStory.observe(viewLifecycleOwner){
            when (it){
                is Resource.SucsessResponse -> {
                    binding.paginationProgressBar.visible(false)
                    it.value.let {
                        storyAdapter.differ.submitList(it.listStory)
                    }
                }
                is Resource.FailResponse -> {
                    it.errorBody.let {
                        Log.e(TAG, "error" )
                    }
                }

                is Resource.Loading -> {
                    binding.paginationProgressBar.visible(true)
                }
            }

            binding.actionLogout.setOnClickListener{
                showLogoutDialog()
            }
        }
    }


    private fun setUpRecycleView(){
        storyAdapter = StoryAdapter()
        binding.rvStory.apply {
            adapter = storyAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =  FragmentDashboardBinding.inflate(inflater, container, false)



    override fun getRepository(): StoryRepository {
        val token = runBlocking { userPreferences.loginToken.first()}
        val api = apiConfig.apiClient(StoryApi::class.java,token)
        return StoryRepository(api)
    }


}