package com.moo.mool.view.login

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moo.mool.R
import com.moo.mool.databinding.FragmentEmailPasswordResetBinding
import com.moo.mool.repository.LoginRepository
import com.moo.mool.util.LoadingDialogUtil
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.viewmodel.LoginViewModel
import com.moo.mool.viewmodel.LoginViewModelFactory

class EmailPasswordResetFragment : Fragment() {
    private var _binding : FragmentEmailPasswordResetBinding?= null
    private val binding get() = _binding!!
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var loadingDialog : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailPasswordResetBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(LoginRepository(requireContext()))).get(LoginViewModel::class.java)

        setToolbarDetail()
        textWatcherEditText()
        setResetPasswordClickListener()

        // TODO : Dialog 띄우기 코드 개선 필요
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
        val mAlertDialog = mBuilder.create()
        mAlertDialog.setCancelable(false)

        loginViewModel.resetPasswordSuccess.observe(viewLifecycleOwner, Observer {
            if(it){
                LoadingDialogUtil.hideLoadingIcon(loadingDialog)
                // Dialog 제목 및 내용 설정
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.reset_password_dialog_title)
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.reset_password_dialog_description)

                // Dialog 확인, 취소버튼 설정
                val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
                mDialogView.findViewById<ConstraintLayout>(R.id.layout_dialog_cancel).visibility = View.GONE

                // Dialog 확인 버튼을 클릭 한 경우
                confirmButton.setOnClickListener {
                    mAlertDialog.dismiss()
                    // 비밀번호 초기화 화면을 그냥 바로 빠져나가기 위해서 onBackPressed()
                    (activity as LoginActivity).onBackPressed()
                }
                mAlertDialog.show()
            }
        })

        setKeyboardDoneClick()
        setKeyboardHide()

        binding.btnDeleteEtEmailId.setOnClickListener{
            binding.etLoginEmailId.setText("")
        }

        return view
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
        binding.toolbarLoginBoard.tvToolbarTitle.setText(R.string.reset_password)
        (activity as LoginActivity).setSupportActionBar(binding.toolbarLoginBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }

    private fun textWatcherEditText() {
        binding.etLoginEmailId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrBlank()){
                    activeButtonResetPassword()
                    binding.btnDeleteEtEmailId.visibility = View.VISIBLE
                } else {
                    deactiveButtonResetPassword()
                    binding.btnDeleteEtEmailId.visibility = View.INVISIBLE
                }

                if(android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString().trim()).matches()) {
                    binding.tvMessageEmailIdCheck.visibility = View.INVISIBLE
                    activeButtonResetPassword()
                } else {
                    // 형식 검사 실패시
                    binding.tvMessageEmailIdCheck.visibility = View.VISIBLE
                    binding.tvMessageEmailIdCheck.setText(getString(R.string.fail_message_email_signup_id_format))
                    binding.tvMessageEmailIdCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                    deactiveButtonResetPassword()
                }

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged( s as Editable? ) }
        })
    }

    private fun setResetPasswordClickListener() {
        binding.btnResetEmail.setOnClickListener {
            if(binding.etLoginEmailId.text.isNullOrBlank()) {
                ToastDefaultBlack.createToast(requireContext(), getString(R.string.hint_email))?.show()
            } else {
                loginViewModel.resetPassword(binding.etLoginEmailId.text.toString().trim())
                loadingDialog = LoadingDialogUtil.showLoadingIcon(requireContext())
            }
        }
    }

    fun activeButtonResetPassword() {
        binding.btnResetEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_enable))
        binding.btnResetEmail.setTextColor(resources.getColorStateList(R.color.bluegray50_F9FAFC, context?.theme))
    }
    fun deactiveButtonResetPassword() {
        binding.btnResetEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_disable))
        binding.btnResetEmail.setTextColor(resources.getColorStateList(R.color.bluegray600_626670, context?.theme))
    }

    private fun setKeyboardDoneClick() {
        binding.etLoginEmailId.setOnEditorActionListener { _, actionId, event ->
            var handled = false

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etLoginEmailId.windowToken, 0)
                handled = true
            }
            handled
        }
    }
    private fun setKeyboardHide() {
        binding.root.setOnTouchListener { _, event ->
            val inputMethodManager : InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}