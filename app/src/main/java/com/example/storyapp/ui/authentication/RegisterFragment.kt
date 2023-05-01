package com.example.storyapp.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.storyapp.R
import com.example.storyapp.apiNetwork.Resource
import com.example.storyapp.apiNetwork.AuthenticationApi
import com.example.storyapp.ui.dashboard.startNewActivity
import com.example.storyapp.databinding.FragmentRegisterBinding
import com.example.storyapp.repository.AuthenticationRepository
import com.example.storyapp.ui.base.BaseFragment

class RegisterFragment : BaseFragment<AuthenticationViewModel, FragmentRegisterBinding, AuthenticationRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registerResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.SucsessResponse -> {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                    requireActivity().startNewActivity(AuthenticationActivity::class.java)
                    Toast.makeText(requireContext(), R.string.register_result, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.apply {
            registerButton.setOnClickListener{
                val name = edRegisterName.text.toString().trim()
                val email = edRegisterEmail.text.toString().trim()
                val password = edRegisterPassword.text.toString().trim()

                viewModel.hitRegister(name, email, password)
            }

            toLoginFragmentBtn.setOnClickListener{
                val loginFragment = LoginFragment()
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView3, loginFragment, LoginFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    override fun getViewModel(): Class<AuthenticationViewModel> = AuthenticationViewModel::class.java

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getRepository() = AuthenticationRepository(apiConfig.apiClient(AuthenticationApi::class.java), userPreferences)


}