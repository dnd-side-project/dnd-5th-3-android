package com.moo.mool.view.signup

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.moo.mool.R
import com.moo.mool.databinding.FragmentEmailSignupSetIdBinding
import com.moo.mool.view.login.LoginActivity
import com.moo.mool.viewmodel.SignupViewModel

class EmailSignupSetIDFragment : Fragment() {
    private var _binding : FragmentEmailSignupSetIdBinding?= null
    private val binding get() = _binding!!
    private val signupViewModel by activityViewModels<SignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailSignupSetIdBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding.toolbarSignupBoard.tvToolbarTitle.setText(R.string.signup)
        (activity as LoginActivity).setSupportActionBar(binding.toolbarSignupBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()

        setKeyboardDoneClick()
        setKeyboardHide()

        binding.btnNext.setOnClickListener {
            if(binding.etSignupEmailId.text.isNullOrBlank()) {
                Toast.makeText(activity, R.string.hint_email, Toast.LENGTH_SHORT).show()
            } else {
                signupViewModel.setEmail(binding.etSignupEmailId.text.toString().trim())
                // 비밀번호 작성 화면으로 이동
                (activity as LoginActivity).ReplaceEmailSignupSetPasswordFragment()
            }
        }

        binding.btnDeleteEtEmailId.setOnClickListener{
            binding.etSignupEmailId.setText("")
        }

        binding.etSignupEmailId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // 비밀번호, 비밀번호 확인 칸에 텍스트 작성시 아래에 안내 메시지 출력
                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtEmailId.visibility = View.VISIBLE
                    binding.tvMessageEmailIdCheck.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteEtEmailId.visibility = View.INVISIBLE
                    binding.tvMessageEmailIdCheck.visibility = View.INVISIBLE
                }

                // 안드로이드에서 제공하는 이메일 양식 검사 사용
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString().trim()).matches()) {
                    signupViewModel.duplicateCheckEmail(s.toString().trim())
                    signupViewModel.duplicateCheckEmailSuccess.observe(viewLifecycleOwner, Observer {
                        if(it) { /** 중복계정 확인 구문 **/
                            binding.tvMessageEmailIdCheck.setText(getString(R.string.success_message_email_signup_id))
                            binding.tvMessageEmailIdCheck.setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                            activeButtonNext()
                        } else {
                            binding.tvMessageEmailIdCheck.setText(getString(R.string.fail_message_email_signup_id_duplicate))
                            binding.tvMessageEmailIdCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                            deactiveButtonNext()
                        }
                    })
                    signupViewModel.signupFailedMessage.observe(viewLifecycleOwner, Observer {
                        Log.e("Duplicate Check Error", it.toString())
                    })
                } else {
                    // 형식 검사 실패시
                    binding.tvMessageEmailIdCheck.setText(getString(R.string.fail_message_email_signup_id_format))
                    binding.tvMessageEmailIdCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                    deactiveButtonNext()
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

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

    private fun activeButtonNext() {
        binding.btnNext.isEnabled = true
        binding.btnNext.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_enable))
        binding.btnNext.setTextColor(resources.getColorStateList(R.color.bluegray50_F9FAFC, context?.theme))
    }
    private fun deactiveButtonNext() {
        binding.btnNext.isEnabled = false
        binding.btnNext.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_disable))
        binding.btnNext.setTextColor(resources.getColorStateList(R.color.bluegray600_626670, context?.theme))
    }

    private fun setKeyboardDoneClick() {
        binding.etSignupEmailId.setOnEditorActionListener { _, actionId, event ->
            var handled = false

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSignupEmailId.windowToken, 0)
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