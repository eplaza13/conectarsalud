package com.conectasalud.app.ui.auth

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.conectasalud.app.R
import com.conectasalud.app.data.repository.UserRepository
import com.conectasalud.app.databinding.FragmentLoginBinding
import com.conectasalud.app.utils.hide
import com.conectasalud.app.utils.show

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var userRepository: UserRepository

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(userRepository)
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.handleGoogleSignInResult(result.data)
        } else {
            Toast.makeText(requireContext(), "Inicio con Google cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRepository = UserRepository(requireContext())

        // If already logged in, go directly to home
        if (viewModel.isUserLoggedIn()) {
            findNavController().navigate(R.id.action_login_to_home)
            return
        }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.signInWithEmail(email, password)
        }

        binding.btnGoogle.setOnClickListener {
            val client = userRepository.getGoogleSignInClient()
            client.signOut() // force account chooser
            googleSignInLauncher.launch(client.signInIntent)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun observeViewModel() {
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.progressBar.show()
                    binding.btnSignIn.isEnabled = false
                    binding.btnGoogle.isEnabled = false
                }
                is AuthState.Success -> {
                    binding.progressBar.hide()
                    findNavController().navigate(R.id.action_login_to_home)
                }
                is AuthState.Error -> {
                    binding.progressBar.hide()
                    binding.btnSignIn.isEnabled = true
                    binding.btnGoogle.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.progressBar.hide()
                    binding.btnSignIn.isEnabled = true
                    binding.btnGoogle.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
