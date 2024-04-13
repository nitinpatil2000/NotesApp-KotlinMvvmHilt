package com.courses.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.courses.notesapp.viewmodel.AuthViewModel
import com.courses.notesapp.R
import com.courses.notesapp.databinding.FragmentLoginBinding
import com.courses.notesapp.models.UserRequest
import com.courses.notesapp.util.ErrorHandling
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment:Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLogin.setOnClickListener {
            val validationInput = validationUserInput()
            if (validationInput.first) {
                viewModel.loginUser(getInputValidation())
            } else {
                binding.txtError.text = validationInput.second
            }
        }

        observeLoginState()

    }


    private fun getInputValidation(): UserRequest {
        val email = binding.txtEmail.text.toString().trim()
        val password = binding.txtPassword.text.toString()

        return UserRequest(email, password, "")
    }

    private fun validationUserInput(): Pair<Boolean, String> {
        val userRequest = getInputValidation()
        return viewModel.validateCredentials(
            userRequest.username,
            userRequest.email,
            userRequest.password,
            true
        )
    }

    private fun observeLoginState() {
        viewModel.userResponseLiveData.observe(viewLifecycleOwner){
            when (it) {
                is ErrorHandling.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ErrorHandling.Success -> {
                    //todo token

                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                is ErrorHandling.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.txtError.text = it.message
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}