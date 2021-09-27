package com.moo.mool.view.signup

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
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.moo.mool.R
import com.moo.mool.databinding.FragmentEmailSignupSetIdBinding
import com.moo.mool.util.ActiveButtonUtil
import com.moo.mool.util.HideKeyBoardUtil
import com.moo.mool.util.ToolbarDecorationUtil
import com.moo.mool.util.navigate
import com.moo.mool.view.login.LoginActivity
import com.moo.mool.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailSignupSetIDFragment : Fragment() {
    private var _binding : FragmentEmailSignupSetIdBinding?= null
    private val binding get() = _binding!!
    private val signupViewModel by activityViewModels<SignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailSignupSetIdBinding.inflate(layoutInflater, container, false)

        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarSignupBoard, R.string.signup, requireActivity(), this)
        textWatcherEditText()
        setEtClearClickListener()
        setEtEditorActionListener()
        setNextClickListener()

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
        binding.etSignupEmailId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // 비밀번호, 비밀번호 확인 칸에 텍스트 작성시 아래에 안내 메시지 출력
                with(binding){
                    if(!s.toString().trim().isNullOrBlank()){
                        btnDeleteEtEmailId.visibility = View.VISIBLE
                        tvMessageEmailIdCheck.visibility = View.VISIBLE
                    } else {
                        btnDeleteEtEmailId.visibility = View.INVISIBLE
                        tvMessageEmailIdCheck.visibility = View.INVISIBLE
                    }
                }

                with(binding.tvMessageEmailIdCheck) {
                    // 안드로이드에서 제공하는 이메일 양식 검사 사용
                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString().trim()).matches()) {
                        signupViewModel.duplicateCheckEmail(s.toString().trim())
                        signupViewModel.duplicateCheckEmailSuccess.observe(viewLifecycleOwner, Observer {
                            if(it) { /** 중복계정 확인 구문 **/
                                text = getString(R.string.success_message_email_signup_id)
                                setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                                ActiveButtonUtil.setButtonActive(requireContext(), binding.btnNext)
                            } else {
                                text = getString(R.string.fail_message_email_signup_id_duplicate)
                                setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                                ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                            }
                        })
                        signupViewModel.signupFailedMessage.observe(viewLifecycleOwner, Observer {
                            Log.e("Duplicate Check Error", it.toString())
                        })
                    } else {
                        // 형식 검사 실패시
                        text = getString(R.string.fail_message_email_signup_id_format)
                        setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                        ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                    }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setEtClearClickListener() {
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtEmailId, binding.etSignupEmailId)
    }

    private fun setEtEditorActionListener() {
        binding.etSignupEmailId.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    HideKeyBoardUtil.hide(requireContext(), binding.etSignupEmailId)
                    true
                }
                else -> false
            }
        }
    }

    private fun setNextClickListener() {
        binding.btnNext.setOnClickListener {
            if(binding.etSignupEmailId.text.isNullOrBlank()) {
                Toast.makeText(activity, R.string.hint_email, Toast.LENGTH_SHORT).show()
            } else {
                signupViewModel.setEmail(binding.etSignupEmailId.text.toString().trim())
                navigate(R.id.action_emailSignupSetIDFragment_to_emailSignupSetPasswordFragment)
            }
        }
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