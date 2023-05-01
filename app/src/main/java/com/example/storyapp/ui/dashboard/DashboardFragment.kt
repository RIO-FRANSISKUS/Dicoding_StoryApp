package com.example.storyapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.apiNetwork.Resource
import com.example.storyapp.apiNetwork.StoryApi
import com.example.storyapp.databinding.FragmentDashboardBinding
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.ui.base.BaseFragment
import com.example.storyapp.ui.base.StoryAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DashboardFragment : BaseFragment<DashboardViewModel, FragmentDashboardBinding, StoryRepository>() {

    private lateinit var storyAdapter: StoryAdapter
    val TAG = "DashboardAdapter"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserStories()
        setUpRecycleView()


        viewModel.userStory.observe(viewLifecycleOwner){
            when (it){
                is Resource.SucsessResponse -> {
                    it.value.let {
                        storyAdapter.differ.submitList(it.listStory)
                    }
                }
                is Resource.FailResponse -> {
                    it.errorBody.let {
                        val token = runBlocking { userPreferences.loginToken.first()}
                        Log.e(TAG,token.toString() )
                    }
                }
                else -> {
                    Toast.makeText(requireContext(), "gagal", Toast.LENGTH_SHORT).show()
                }
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
        val api = apiConfig.apiClient(StoryApi::class.java, token)
        return StoryRepository(api)
    }


}