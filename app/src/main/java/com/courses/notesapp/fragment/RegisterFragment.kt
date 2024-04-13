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
import com.courses.notesapp.databinding.FragmentRegisterBinding
import com.courses.notesapp.models.UserRequest
import com.courses.notesapp.util.ErrorHandling
import com.courses.notesapp.util.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)


        //todo if the token is present then move to the main screen.
        if(tokenManager.getToken() != null){
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val validateResult = validateInput()
            if (validateResult.first) {   //todo set the first as true and second is false in the viewmodel validation function.
                viewModel.registerUser(getUserRequest())
            } else {
                binding.txtError.text = validateResult.second
            }
        }

        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        bindObservers()
    }

    //todo common function for all click get text to string
    private fun getUserRequest(): UserRequest{
        val email = binding.txtEmail.text.toString().trim()
        val passowrd = binding.txtPassword.text.toString()
        val username = binding.txtUsername.text.toString().trim()
        return UserRequest(email, passowrd, username)
    }

    //todo for the validation.
    private fun validateInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return viewModel.validateCredentials(userRequest.username, userRequest.email, userRequest.password, false)
    }

    private fun bindObservers() {
        viewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ErrorHandling.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ErrorHandling.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    binding.progressBar.visibility = View.GONE
                }

                is ErrorHandling.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.txtError.text = it.message

                }
            }
        }

    }


    //todo when my fragment is destroy then free up the views. performance benefit.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}