package com.example.storyapp.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.storyapp.R
import com.example.storyapp.UserPreferences
import com.example.storyapp.apiNetwork.AuthenticationApi
import com.example.storyapp.apiNetwork.Resource
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.repository.AuthenticationRepository
import com.example.storyapp.ui.base.BaseFragment
import com.example.storyapp.ui.dashboard.DashboardActivity
import com.example.storyapp.util.enable
import com.example.storyapp.util.startNewActivity
import com.example.storyapp.util.visible

class LoginFragment : BaseFragment<AuthenticationViewModel, FragmentLoginBinding, AuthenticationRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnimation()

        binding.loginProgressBar.visible(false)
        binding.loginButton.enable(false)

        val userPreferences = UserPreferences(requireContext())

        viewModel.loginResponse.observe(viewLifecycleOwner){

            binding.loginProgressBar.visible(false)
            when(it){
                is Resource.SucsessResponse -> {
                    userPreferences.setToken(it.value.loginResult.token)
                    viewModel.saveTokenVm(it.value.loginResult.token)
                    requireActivity().startNewActivity(DashboardActivity::class.java)
                    Toast.makeText(requireContext(), R.string.login_result, Toast.LENGTH_SHORT).show()

                }
                is Resource.FailResponse -> {
                    Toast.makeText(requireContext(), "Gagal melakukan Login", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    binding.loginProgressBar.visible(true)
                }
            }

        }

        binding.apply {
            edLoginPassword.addTextChangedListener {
                val email = edLoginEmail.text.toString().trim()
                val password = edLoginPassword.text.toString().trim()

                loginButton.enable(email.isNotEmpty() && it.toString().isNotEmpty() && password.length > 7)
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

    fun playAnimation(){
        binding.apply {
            val animImage = ObjectAnimator.ofFloat(imageView, View.ALPHA, 1f).setDuration(500)
            val animTitle = ObjectAnimator.ofFloat(titleTextView, View.ALPHA, 1f).setDuration(500)
            val animMess = ObjectAnimator.ofFloat(messageTextView, View.ALPHA, 1f).setDuration(500)
            val animEmail = ObjectAnimator.ofFloat(emailTextView, View.ALPHA, 1f).setDuration(500)
            val animEmailed = ObjectAnimator.ofFloat(edLoginEmail, View.ALPHA, 1f).setDuration(500)
            val animPass = ObjectAnimator.ofFloat(passwordTextView, View.ALPHA, 1f).setDuration(500)
            val animPassed = ObjectAnimator.ofFloat(edLoginPassword, View.ALPHA, 1f).setDuration(500)
            val animMessTv = ObjectAnimator.ofFloat(messageTextViewRegister, View.ALPHA, 1f).setDuration(500)
            val animRegister = ObjectAnimator.ofFloat(toRegisterFragmentBtn, View.ALPHA, 1f).setDuration(500)

            val together1 = AnimatorSet().apply {
                playTogether(animTitle, animMess)
            }

            val together2 = AnimatorSet().apply {
                playTogether(animEmail, animEmailed)
            }

            val together3 = AnimatorSet().apply {
                playTogether(animPass, animPassed)
            }
            val together4 = AnimatorSet().apply {
                playTogether(animMessTv,animRegister)
            }

            AnimatorSet().apply {
                playSequentially(animImage,together1,together2, together3, together4)
                start()
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