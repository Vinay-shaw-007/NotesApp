package com.example.myapplication.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.models.UserRequest
import com.example.myapplication.ui.register.AuthViewModel
import com.example.myapplication.utils.NetworkResult
import com.example.myapplication.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val validationResult = validateInput()
            if (validationResult.first)
                authViewModel.loginUser(getUserRequest())
            else
                binding.txtError.text = validationResult.second
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        bindObserver()
    }
    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is NetworkResult.Success -> {
                    it.data?.token?.let { it1 -> tokenManager.saveToken(it1) }
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
            }
        }
    }
    private fun validateInput(): Pair<Boolean, String> {
        val user = getUserRequest()
        return authViewModel.validateUserInput(user.email, user.password, user.username, true)
    }
    private fun getUserRequest(): UserRequest {
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(email, password, "")
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}