package com.moo.mool.view.login

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.moo.mool.R
import com.moo.mool.databinding.FragmentEmailPasswordResetBinding
import com.moo.mool.util.*
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EmailPasswordResetFragment : Fragment() {
    private var _binding: FragmentEmailPasswordResetBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailPasswordResetBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = this@EmailPasswordResetFragment
            vm = loginViewModel
        }
        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarLoginBoard, R.string.reset_password, requireActivity(), this)
        textWatcherEditText()
        setEtClearClickListener()
        setResetPasswordClickListener()
        setResetPasswordCollect()

        setEditTextEditorActionListener(binding.etLoginEmailId)

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
                with(binding.btnDeleteEtEmailId){
                    if (!s.isNullOrBlank()) {
                        ActiveButtonUtil.setButtonActive(requireContext(), binding.btnResetEmail)
                        visibility = View.VISIBLE
                    } else {
                        ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnResetEmail)
                        visibility = View.INVISIBLE
                    }
                }

                with(binding.tvMessageEmailIdCheck) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString().trim()).matches()) {
                        visibility = View.INVISIBLE
                        ActiveButtonUtil.setButtonActive(requireContext(), binding.btnResetEmail)
                    } else {
                        // 형식 검사 실패시
                        visibility = View.VISIBLE
                        text = getString(R.string.fail_message_email_signup_id_format)
                        setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                        ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnResetEmail)
                    }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setEtClearClickListener() {
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtEmailId, binding.etLoginEmailId)
    }

    private fun setResetPasswordClickListener() {
        binding.btnResetEmail.setOnClickListener {
            if (binding.etLoginEmailId.text.isNullOrBlank()) {
                ToastDefaultBlack.createToast(requireContext(), getString(R.string.hint_email))?.show()
            } else {
                loginViewModel.resetPassword(binding.etLoginEmailId.text.toString().trim())
                loadingDialog = LoadingDialogUtil.showLoadingIcon(requireContext())
            }
        }
    }

    private fun setEditTextEditorActionListener(editText: EditText) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    HideKeyBoardUtil.hide(requireContext(), editText)
                    true
                }
                else -> false
            }
        }
    }

    private fun setResetPasswordCollect() {
        val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
            R.string.reset_password_dialog_title, R.string.reset_password_dialog_description,
            true, false, null, null,
            { (activity as LoginActivity).onBackPressed() }, null
        )

        repeatOnLifecycle {
            with(loginViewModel){
                resetPasswordSuccess.collect() { resetPasswordSuccess ->
                    if(resetPasswordSuccess){
                        LoadingDialogUtil.hideLoadingIcon(loadingDialog)
                        mAlertDialog.show()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HideKeyBoardUtil.hideTouchDisplay(requireActivity(), view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LoadingDialogUtil.hideLoadingIcon(loadingDialog)
        _binding = null
    }
}