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
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.moo.mool.R
import com.moo.mool.databinding.FragmentEmailSignupSetNicknameBinding
import com.moo.mool.util.HideKeyBoardUtil
import com.moo.mool.view.login.LoginActivity
import com.moo.mool.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

        setToolbarDetail()
        textWatcherEditText()
        setEtClearClickListener()
        setEtEditorActionListener(binding.etSignupEmailNickname)
        setNextClickListener()

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
        (activity as LoginActivity).onBackPressed()
        return true
    }
    private fun showBackButton() {
        (activity as LoginActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as LoginActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        this.setHasOptionsMenu(true)
    }
    private fun setToolbarDetail() {
        binding.toolbarSignupBoard.tvToolbarTitle.setText(R.string.signup)
        (activity as LoginActivity).setSupportActionBar(binding.toolbarSignupBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }

    private fun textWatcherEditText() {
        binding.etSignupEmailNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // 비밀번호, 비밀번호 확인 칸에 텍스트 작성시 아래에 안내 메시지 출력
                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtEmailNickname.visibility = View.VISIBLE
                    binding.tvMessageEmailNicknameCheck.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteEtEmailNickname.visibility = View.INVISIBLE
                    binding.tvMessageEmailNicknameCheck.visibility = View.INVISIBLE
                    deactiveButtonNext()
                }

                // TODO : 닉네임 형식 확인 코드 작성
                if(s.toString().trim().length <= 8 && !s.toString().trim().isNullOrBlank()) {
                    signupViewModel.duplicateCheckNickname(s.toString().trim())
                    signupViewModel.duplicateCheckNicknameSuccess.observe(viewLifecycleOwner, Observer {
                        if(it) { /** 중복계정 확인 구문 **/
                            binding.tvMessageEmailNicknameCheck.setText(getString(R.string.success_message_email_signup_nickname))
                            binding.tvMessageEmailNicknameCheck.setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                            activeButtonNext()
                        } else {
                            binding.tvMessageEmailNicknameCheck.setText(getString(R.string.fail_message_email_signup_nickname_duplicate))
                            binding.tvMessageEmailNicknameCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                            deactiveButtonNext()
                        }
                    })
                    signupViewModel.signupFailedMessage.observe(viewLifecycleOwner, Observer {
                        Log.e("Duplicate Check Error", it.toString())
                    })
                } else {
                    // 형식 검사 실패시
                    binding.tvMessageEmailNicknameCheck.setText(getString(R.string.fail_message_email_signup_nickname_format))
                    binding.tvMessageEmailNicknameCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                    deactiveButtonNext()
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })
    }

    private fun setEtClearClickListener() {
        binding.btnDeleteEtEmailNickname.setOnClickListener{
            binding.etSignupEmailNickname.setText("")
        }
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signupViewModel.loginSuccess
                    .collect { loginSuccess ->
                        if (loginSuccess) { // 자동 로그인 성공시 바로 메인 화면으로 이동
                            requireView().findNavController().navigate(R.id.action_emailSignupSetNicknameFragment_to_mainActivity)
                            (activity as LoginActivity).finish()
                        } }
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