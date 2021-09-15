package com.rey.dumbdumb.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rey.dumbdumb.DumbDumbApp.Companion.TAG
import com.rey.dumbdumb.databinding.FragmentAuthenticationBinding
import com.rey.dumbdumb.presentation.viewmodel.AuthenticationViewModel
import com.rey.lib.cleanarch.domain.usecase.external.ICoroutineProvider
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.asExecutor
import javax.inject.Inject

class AuthenticationFragment : Fragment() {

    companion object {
        private val bitwiseAuthenticationCombination =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            else BIOMETRIC_STRONG
    }

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding get() = _binding!!

    @Inject
    lateinit var coroutine: ICoroutineProvider

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AuthenticationViewModel by activityViewModels {
        viewModelFactory
    }
    private lateinit var biometricPromptInfo: BiometricPrompt.PromptInfo
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
        initBiometric()
        initBiometricPromptInfo()
        initBiometricPrompt()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getEncryptedCredential()
    }

    private fun initListeners() = with(binding) {
        buttonLogin.setOnClickListener {
            viewModel.authenticate(
                (inputUsername.editText?.text ?: "").toString(),
                (inputPassword.editText?.text ?: "").toString()
            )
        }
    }

    private fun initObservers() = with(viewLifecycleOwner) {
        viewModel.name.observe(this) {
            val direction =
                AuthenticationFragmentDirections.actionAuthenticationFragmentToNavHome(it)
            findNavController().navigate(direction)
        }

        viewModel.encryptionCipher.observe(this) {
            biometricPrompt.authenticate(biometricPromptInfo, BiometricPrompt.CryptoObject(it))
        }

        viewModel.token.observe(this) {
            try {
                if (it != null)
                    viewModel.getEncryptionCipher()
                else
                    throw IllegalStateException("token is empty")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "initObservers: Failed to authenticate $e")
            }
        }

        viewModel.decryptionCipher.observe(this) {
            biometricPrompt.authenticate(biometricPromptInfo, BiometricPrompt.CryptoObject(it))
        }

        viewModel.encryptedCredential.observe(this) {
            with(binding) {
                val enableForm = it == null
                inputUsername.isEnabled = enableForm
                inputPassword.isEnabled = enableForm
                buttonLogin.isEnabled = enableForm
            }

            try {
                if (it != null)
                    viewModel.getDecryptionCipher(it.initializationVector)
                else
                    throw IllegalStateException("credential is empty")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "initObservers: Failed to decrypt $e")
            }
        }
    }

    private fun initBiometricPrompt() {
        val executor = coroutine.default().asExecutor()
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.e(TAG, "onAuthenticationError: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                viewModel.authenticate()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.e(TAG, "onAuthenticationFailed: ")
            }
        }

        biometricPrompt = BiometricPrompt(this, executor, callback)
    }

    private fun initBiometricPromptInfo() {
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Put your finger on the biometric sensor for logging in")
            .setAllowedAuthenticators(bitwiseAuthenticationCombination)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)
            builder.setNegativeButtonText("Cancel")

        biometricPromptInfo = builder.build()
    }

    private fun initBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(bitwiseAuthenticationCombination)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d(TAG, "App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e(TAG, "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e(TAG, "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e(TAG, "BIOMETRIC_ERROR_NONE_ENROLLED")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // Prompts the user to create credentials that your app accepts.
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        )
                    }
                    startActivityForResult(enrollIntent, 1)
                }
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Log.e(TAG, "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED")
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Log.e(TAG, "BIOMETRIC_ERROR_UNSUPPORTED")
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Log.d(TAG, "BIOMETRIC_STATUS_UNKNOWN")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}