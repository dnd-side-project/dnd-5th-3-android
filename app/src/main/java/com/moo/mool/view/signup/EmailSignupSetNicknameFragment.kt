package com.moo.mool.view.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.moo.mool.R
import com.moo.mool.databinding.FragmentEmailSignupSetNicknameBinding
import com.moo.mool.util.*
import com.moo.mool.view.login.LoginActivity
import com.moo.mool.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EmailSignupSetNicknameFragment : Fragment() {
    private var _binding : FragmentEmailSignupSetNicknameBinding?= null
    private val binding get() = _binding!!
    private val signupViewModel by activityViewModels<SignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailSignupSetNicknameBinding.inflate(layoutInflater, container, false)

        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarSignupBoard, R.string.signup, requireActivity(), this)
        textWatcherEditText()
        setEtClearClickListener()
        setEtEditorActionListener(binding.etSignupEmailNickname)
        setNextClickListener()
        setPolicyInformationClickListener()

        signupViewModel.signupSuccess.observe(viewLifecycleOwner, Observer {
            if(it) { // 회원가입이 성공할 경우 자동 로그인
                signupViewModel.login()
                setRequestLoginCollect()
            }
        })
        signupViewModel.signupFailedMessage.observe(viewLifecycleOwner, Observer {
            Log.e("Signup Error", it.toString())
        })
        signupViewModel.loginFailedMessage.observe(viewLifecycleOwner, Observer {
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
        binding.etSignupEmailNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                with(binding) {
                    // 비밀번호, 비밀번호 확인 칸에 텍스트 작성시 아래에 안내 메시지 출력
                    if(!s.toString().trim().isNullOrBlank()){
                        btnDeleteEtEmailNickname.visibility = View.VISIBLE
                        tvMessageEmailNicknameCheck.visibility = View.VISIBLE
                    } else {
                        btnDeleteEtEmailNickname.visibility = View.INVISIBLE
                        tvMessageEmailNicknameCheck.visibility = View.INVISIBLE
                        ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                    }
                }

                with(binding.tvMessageEmailNicknameCheck) {
                    if(EdittextCount.getGraphemeCount(s.toString().trim()) <= 8 && !s.toString().trim().isNullOrBlank()) {
                        signupViewModel.duplicateCheckNickname(s.toString().trim())
                        signupViewModel.duplicateCheckNicknameSuccess.observe(viewLifecycleOwner, Observer {
                            if(it) { /** 중복계정 확인 구문 **/
                                text = getString(R.string.success_message_email_signup_nickname)
                                setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                                ActiveButtonUtil.setButtonActive(requireContext(), binding.btnNext)
                            } else {
                                text = getString(R.string.fail_message_email_signup_nickname_duplicate)
                                setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                                ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                            }
                        })
                        signupViewModel.signupFailedMessage.observe(viewLifecycleOwner, Observer {
                            Log.e("Duplicate Check Error", it.toString())
                        })
                    } else {
                        // 형식 검사 실패시
                        text = getString(R.string.fail_message_email_signup_nickname_format)
                        setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                        ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                    }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setEtClearClickListener() {
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtEmailNickname, binding.etSignupEmailNickname)
    }

    private fun setEtEditorActionListener(editText: EditText) {
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

    private fun setNextClickListener() {
        binding.btnNext.setOnClickListener {
            if(binding.etSignupEmailNickname.text.isNullOrBlank()) {
                Toast.makeText(activity, R.string.hint_nickname, Toast.LENGTH_SHORT).show()
            } else {
                signupViewModel.setNickname(binding.etSignupEmailNickname.text.toString().trim())
                signupViewModel.signup()
            }
        }
    }

    private fun setRequestLoginCollect() {
        repeatOnLifecycle {
            signupViewModel.loginSuccess.collect { loginSuccess ->
                if (loginSuccess) { // 자동 로그인 성공시 바로 메인 화면으로 이동
                    navigate(R.id.action_emailSignupSetNicknameFragment_to_mainActivity)
                    (activity as LoginActivity).finish()
                }
            }
        }
    }

    private fun setPolicyInformationClickListener() {
        lateinit var browserIntent : Intent
        binding.tvMessageEmailInformationTos.setOnClickListener {
            browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/e52905d447194a25ae20ab6f5f3322ed"))
            startActivity(browserIntent)
        }

        binding.tvMessageEmailInformationPrivacy.setOnClickListener {
            browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/6c40dee8c16e491f83507438d5de1493"))
            startActivity(browserIntent)
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