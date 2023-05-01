package com.example.storyapp.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.storyapp.R
import com.example.storyapp.apiNetwork.Resource
import com.example.storyapp.apiNetwork.AuthenticationApi
import com.example.storyapp.ui.dashboard.DashboardActivity
import com.example.storyapp.ui.dashboard.enable
import com.example.storyapp.ui.dashboard.startNewActivity
import com.example.storyapp.ui.dashboard.visible
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.repository.AuthenticationRepository
import com.example.storyapp.ui.base.BaseFragment

class LoginFragment : BaseFragment<AuthenticationViewModel, FragmentLoginBinding, AuthenticationRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginProgressBar.visible(false)
        binding.loginButton.enable(false)
        binding.passwordWarning.visible(false)

        viewModel.loginResponse.observe(viewLifecycleOwner){

            binding.loginProgressBar.visible(false)
            when(it){
                is Resource.SucsessResponse -> {
                    viewModel.saveTokenVm(it.value.loginResult.token)
                    requireActivity().startNewActivity(DashboardActivity::class.java)
                    Toast.makeText(requireContext(), R.string.login_result, Toast.LENGTH_SHORT).show()

                }
                else -> {
                    Toast.makeText(requireContext(), "gagal", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.apply {
            edLoginPassword.addTextChangedListener {
                val email = edLoginEmail.text.toString().trim()
                val password = edLoginPassword.text.toString().trim()

                passwordWarning.visible(password.length < 8)
                loginButton.enable(email.isNotEmpty() && it.toString().isNotEmpty())
            }

            loginButton.setOnClickListener{
                val email = edLoginEmail.text.toString().trim()
                val password = edLoginPassword.text.toString().trim()
                loginProgressBar.visible(true)
                viewModel.hitLogin(email, password)
            }

            toRegisterFragmentBtn.setOnClickListener{
                val registerFragment = RegisterFragment()
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView3, registerFragment, RegisterFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    override fun getViewModel() = AuthenticationViewModel::class.java

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =  FragmentLoginBinding.inflate(inflater, container, false)

    override fun getRepository() = AuthenticationRepository(apiConfig.apiClient(AuthenticationApi::class.java), userPreferences)

}