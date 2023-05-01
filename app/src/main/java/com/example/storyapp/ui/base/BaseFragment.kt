package com.example.storyapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.storyapp.UserPreferences
import com.example.storyapp.apiNetwork.ApiConfig
import com.example.storyapp.repository.BaseRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<viewModel: ViewModel, viewBinding: ViewBinding, baseRepository: BaseRepository>: Fragment() {

    protected lateinit var userPreferences: UserPreferences

    protected lateinit var binding: viewBinding
    protected lateinit var viewModel: viewModel
    protected val apiConfig = ApiConfig()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())

        binding = getViewBinding(inflater, container)
        val baseViewModel = BaseViewModel(getRepository())
        viewModel = ViewModelProvider(this, baseViewModel).get(getViewModel())

        lifecycleScope.launch { userPreferences.loginToken.first() }

        return binding.root
    }

    abstract fun getViewModel() : Class<viewModel>
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): viewBinding
    abstract fun getRepository() : baseRepository

}