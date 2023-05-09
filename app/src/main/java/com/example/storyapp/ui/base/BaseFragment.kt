package com.example.storyapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.storyapp.UserPreferences
import com.example.storyapp.apiNetwork.ApiConfig
import com.example.storyapp.apiNetwork.StoryApi
import com.example.storyapp.repository.BaseRepository
import com.example.storyapp.ui.authentication.AuthenticationActivity
import com.example.storyapp.util.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<viewModel: BaseViewModel, viewBinding: ViewBinding, baseRepository: BaseRepository>: Fragment() {

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
        val baseViewModel = ViewModelFactory(getRepository())
        viewModel = ViewModelProvider(this, baseViewModel).get(getViewModel())

        lifecycleScope.launch { userPreferences.loginToken.first() }

        return binding.root
    }

    fun logout() = lifecycleScope.launch {
        val token = userPreferences.loginToken.first()
        val api = apiConfig.apiClient(StoryApi::class.java,token)
        viewModel.logout(api)
        userPreferences.clearUserLogin()
        requireActivity().startNewActivity(AuthenticationActivity::class.java)
    }

    fun showLogoutDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage("Anda yakin ingin Logout?")
            setPositiveButton("Ya") { _, _ ->
                logout()
            }
            setNegativeButton("Tidak", null)
            create()
            show()
        }
    }

    abstract fun getViewModel() : Class<viewModel>
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): viewBinding
    abstract fun getRepository() : baseRepository

}