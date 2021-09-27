package com.moo.mool.view.login

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.moo.mool.R
import com.moo.mool.databinding.FragmentEmailLoginBinding
import com.moo.mool.model.RequestLogin
import com.moo.mool.util.*
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class EmailLoginFragment : Fragment() {
    private var _binding: FragmentEmailLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailLoginBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = this@EmailLoginFragment
            vm = loginViewModel
        }

        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarLoginBoard, R.string.login, requireActivity(), this)
        textWatcherEditText()
        setEtClearClickListener()
        setLoginEmailClickListener()
        setEmailPasswordResetClickListener()

        loginViewModel.loginFailedMessage.observe(viewLifecycleOwner, Observer {
            Log.e("Login Error", it.toString())
        })

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as LoginActivity).onBackPressed()
        }
        return true
    }

    private fun textWatcherEditText() {
        binding.etLoginEmailId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank() && !binding.etLoginEmailPassword.text.isNullOrBlank()) {
                    ActiveButtonUtil.setButtonActive(requireContext(), binding.btnLoginEmail)
                } else {
                    ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnLoginEmail)
                }

                with(binding.btnDeleteEtEmailId) {
                    visibility = if (!s.isNullOrBlank()) {
                        View.VISIBLE
                    } else { View.INVISIBLE }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
        binding.etLoginEmailPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank() && !binding.etLoginEmailId.text.isNullOrBlank()) {
                    ActiveButtonUtil.setButtonActive(requireContext(), binding.btnLoginEmail)
                } else {
                    ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnLoginEmail)
                }

                with(binding.btnDeleteEtEmailPassword) {
                    visibility = if (!s.isNullOrBlank()) {
                        View.VISIBLE
                    } else { View.INVISIBLE }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setEtClearClickListener() {
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtEmailId, binding.etLoginEmailId)
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtEmailPassword, binding.etLoginEmailPassword)
    }

    private fun setLoginEmailClickListener() {
        val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
            R.string.login_fail_dialog_title, R.string.login_fail_dialog_description,
            true, false, null, null,
            null, null
        )
        binding.btnLoginEmail.setOnClickListener {
            if (binding.etLoginEmailId.text.isNullOrBlank() || binding.etLoginEmailPassword.text.isNullOrBlank()) {
                ToastDefaultBlack.createToast(requireContext(), "이메일과 비밀번호를 모두 입력하세요.")?.show()
            } else {
                loginViewModel.login(
                    RequestLogin(
                        binding.etLoginEmailId.text.toString(),
                        binding.etLoginEmailPassword.text.toString()
                    )
                )
                setRequestLoginCollect(mAlertDialog)
            }
        }
    }

    private fun setEmailPasswordResetClickListener() {
        binding.tvMessagePasswordReset.setOnClickListener {
            navigate(R.id.action_emailLoginFragment_to_emailPasswordResetFragment)
        }
    }

    private fun setRequestLoginCollect(mAlertDialog: AlertDialog) {
        loginViewModel.responseLogin.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                repeatOnLifecycle {
                    loginViewModel.loginSuccess.collect { loginSuccess ->
                        if (loginSuccess) {
                            navigate(R.id.action_emailLoginFragment_to_mainActivity)
                            (activity as LoginActivity).finish()
                        } else {
                            // Dialog 중복 실행 방지
                            if (mAlertDialog != null && !mAlertDialog.isShowing) {
                                mAlertDialog.show()
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HideKeyBoardUtil.hideTouchDisplay(requireActivity(), view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}