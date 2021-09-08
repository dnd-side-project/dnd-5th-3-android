package com.moo.mool.view.login

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.moo.mool.R
import com.moo.mool.databinding.FragmentEmailLoginBinding
import com.moo.mool.model.RequestLogin
import com.moo.mool.util.HideKeyBoardUtil
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmailLoginFragment : Fragment() {
    private var _binding : FragmentEmailLoginBinding?= null
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

        setToolbarDetail()
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
        (activity as LoginActivity).onBackPressed()
        return true
    }
    private fun showBackButton() {
        (activity as LoginActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as LoginActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        this.setHasOptionsMenu(true)
    }
    private fun setToolbarDetail() {
        binding.toolbarLoginBoard.tvToolbarTitle.setText(R.string.login)
        (activity as LoginActivity).setSupportActionBar(binding.toolbarLoginBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }

    private fun textWatcherEditText() {
        binding.etLoginEmailId.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrBlank() && !binding.etLoginEmailPassword.text.isNullOrBlank()){
                    activeButtonLoginEmail()
                } else { deactiveButtonLoginEmail() }

                if(!s.isNullOrBlank()){
                    binding.btnDeleteEtEmailId.visibility = View.VISIBLE
                } else { binding.btnDeleteEtEmailId.visibility = View.INVISIBLE }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })
        binding.etLoginEmailPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrBlank() && !binding.etLoginEmailId.text.isNullOrBlank()){
                    activeButtonLoginEmail()
                } else { deactiveButtonLoginEmail() }

                if(!s.isNullOrBlank()){
                    binding.btnDeleteEtEmailPassword.visibility = View.VISIBLE
                } else { binding.btnDeleteEtEmailPassword.visibility = View.INVISIBLE }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })
    }

    private fun setEtClearClickListener() {
        binding.btnDeleteEtEmailId.setOnClickListener{
            binding.etLoginEmailId.setText("")
        }
        binding.btnDeleteEtEmailPassword.setOnClickListener {
            binding.etLoginEmailPassword.setText("")
        }
    }

    private fun setLoginEmailClickListener() {
        binding.btnLoginEmail.setOnClickListener {
            if (binding.etLoginEmailId.text.isNullOrBlank() || binding.etLoginEmailPassword.text.isNullOrBlank()) {
                ToastDefaultBlack.createToast(requireContext(), "이메일과 비밀번호를 모두 입력하세요.")?.show()
            } else {
                loginViewModel.login(RequestLogin(binding.etLoginEmailId.text.toString(), binding.etLoginEmailPassword.text.toString()))
                setRequestLoginCollect()
            }
        }
    }

    private fun setEmailPasswordResetClickListener() {
        binding.tvMessagePasswordReset.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_emailLoginFragment_to_emailPasswordResetFragment)
        }
    }

    private fun activeButtonLoginEmail() {
        binding.btnLoginEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_enable))
        binding.btnLoginEmail.setTextColor(resources.getColorStateList(R.color.bluegray50_F9FAFC, context?.theme))
    }
    private fun deactiveButtonLoginEmail() {
        binding.btnLoginEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_disable))
        binding.btnLoginEmail.setTextColor(resources.getColorStateList(R.color.bluegray600_626670, context?.theme))
    }

    private fun setRequestLoginCollect() {
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
        val mAlertDialog = mBuilder.create()

        loginViewModel.responseLogin.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        loginViewModel.loginSuccess
                            .collect { loginSuccess ->
                                if (loginSuccess) {
                                    requireView().findNavController()
                                        .navigate(R.id.action_emailLoginFragment_to_mainActivity)
                                    (activity as LoginActivity).finish()
                                } else {
                                    // Dialog 중복 실행 방지
                                    if (mAlertDialog != null && !mAlertDialog.isShowing) {
                                        mAlertDialog.show()

                                        mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title)
                                            .setText(R.string.login_fail_dialog_title)
                                        mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description)
                                            .setText(R.string.login_fail_dialog_description)

                                        val confirmButton =
                                            mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
                                        mDialogView.findViewById<ConstraintLayout>(R.id.layout_dialog_cancel).visibility =
                                            View.GONE
                                        confirmButton.setOnClickListener {
                                            mAlertDialog.dismiss()
                                        }
                                    }
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