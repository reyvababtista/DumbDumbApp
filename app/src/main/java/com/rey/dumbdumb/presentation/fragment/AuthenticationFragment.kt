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
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rey.dumbdumb.databinding.FragmentAuthenticationBinding
import com.rey.dumbdumb.presentation.viewmodel.AuthenticationViewModel
import dagger.android.support.AndroidSupportInjection
import java.nio.charset.Charset
import java.util.*
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

        initBiometric()
        initBiometricPromptInfo()
        initBiometricPrompt()
        initObservers()
    }

    private fun initObservers() = with(viewLifecycleOwner) {
        viewModel.cipher.observe(this) {
            try {
                biometricPrompt.authenticate(biometricPromptInfo, BiometricPrompt.CryptoObject(it))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initBiometricPrompt() {
        biometricPrompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(requireContext()),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(),
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        requireContext(),
                        "Authentication succeeded!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val encryptedInfo: ByteArray? =
                        result.cryptoObject?.cipher?.doFinal("foo".toByteArray(Charset.defaultCharset()))
                    if (encryptedInfo != null) {
                        Log.d(
                            "MY_APP_TAG",
                            "Encrypted information: " + Arrays.toString(encryptedInfo)
                        )
                        Toast.makeText(
                            requireContext(),
                            Arrays.toString(encryptedInfo),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToNavHome())
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        binding.buttonLogin.setOnClickListener {
            viewModel.getChiper()
        }
    }

    private fun initBiometricPromptInfo() {
        biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Subtitle")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(bitwiseAuthenticationCombination).build()
    }

    private fun initBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(bitwiseAuthenticationCombination)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("MY_APP_TAG", "BIOMETRIC_ERROR_NONE_ENROLLED")
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
                Log.e("MY_APP_TAG", "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED")
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Log.e("MY_APP_TAG", "BIOMETRIC_ERROR_UNSUPPORTED")
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Log.d("MY_APP_TAG", "BIOMETRIC_STATUS_UNKNOWN")
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