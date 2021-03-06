package com.moo.mool.view.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.moo.mool.R
import com.moo.mool.databinding.FragmentEmailSignupSetPasswordBinding
import com.moo.mool.util.ActiveButtonUtil
import com.moo.mool.util.HideKeyBoardUtil
import com.moo.mool.util.ToolbarDecorationUtil
import com.moo.mool.util.navigate
import com.moo.mool.view.login.LoginActivity
import com.moo.mool.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class EmailSignupSetPasswordFragment : Fragment() {
    private var _binding : FragmentEmailSignupSetPasswordBinding?= null
    private val binding get() = _binding!!
    private val signupViewModel by activityViewModels<SignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailSignupSetPasswordBinding.inflate(layoutInflater, container, false)

        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarSignupBoard, R.string.signup, requireActivity(), this)
        textWatcherEditText()
        setEtClearClickListener()
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
        binding.etSignupEmailPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                with(binding){
                    // ????????????, ???????????? ?????? ?????? ????????? ????????? ????????? ?????? ????????? ??????
                    if(!s.toString().trim().isNullOrBlank()){
                        btnDeleteEtEmailPassword.visibility = View.VISIBLE
                        tvMessageSignupPasswordFormat.visibility = View.VISIBLE
                    } else {
                        btnDeleteEtEmailPassword.visibility = View.INVISIBLE
                        tvMessageSignupPasswordFormat.visibility = View.INVISIBLE
                    }

                    if(s.toString().trim().length >= 8) {
                        if(Pattern.matches("^(?=.*[A-Za-z])(?=.*[\\d]).{8,}\$", s.toString().trim())) {
                            if(Pattern.matches("^[A-Za-z\\d\$@\$!%*#?&]*\$", s.toString().trim())) {
                                with(tvMessageSignupPasswordFormat) {
                                    // ?????? ?????? ?????????
                                    text = getString(R.string.success_message_email_signup_password)
                                    setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                                }

                                with(tvMessageSignupPasswordCheck) {
                                    // ???????????? ?????? ?????? ???????????? ??????
                                    // '???????????? ?????? ?????? ???????????????' ?????? ??? '????????????' ?????? ?????????
                                    visibility = View.VISIBLE
                                    if(s.toString().trim().equals(etSignupEmailPasswordCheck.text.toString().trim())) {
                                        text = getString(R.string.success_message_email_signup_password_check)
                                        setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                                        ActiveButtonUtil.setButtonActive(requireContext(), binding.btnNext)
                                    } else { // ???????????? ?????????
                                        text = getString(R.string.fail_message_email_signup_password_check)
                                        setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                                        ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                                    }
                                }

                            } else {
                                // ???????????? ?????? ?????? ?????????
                                tvMessageSignupPasswordCheck.visibility = View.INVISIBLE
                                tvMessageSignupPasswordFormat.text = getString(R.string.fail_message_email_signup_password_format_character)
                                tvMessageSignupPasswordFormat.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                                ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                            }
                        } else {
                            // ????????? ??? ?????? ?????? ?????? ?????????
                            tvMessageSignupPasswordCheck.visibility = View.INVISIBLE
                            tvMessageSignupPasswordFormat.text = getString(R.string.fail_message_email_signup_password_format)
                            tvMessageSignupPasswordFormat.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                            ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                        }
                    } else {
                        // ????????? ?????? ?????? ?????? ?????????
                        tvMessageSignupPasswordCheck.visibility = View.INVISIBLE
                        tvMessageSignupPasswordFormat.text = getString(R.string.fail_message_email_signup_password_format_length)
                        tvMessageSignupPasswordFormat.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                        ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                    }
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        binding.etSignupEmailPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                with(binding){
                    // ????????????, ???????????? ?????? ?????? ????????? ????????? ????????? ?????? ????????? ??????
                    if(!s.toString().trim().isNullOrBlank()){
                        btnDeleteEtEmailPasswordCheck.visibility = View.VISIBLE
                        tvMessageSignupPasswordCheck.visibility = View.VISIBLE
                    } else {
                        btnDeleteEtEmailPasswordCheck.visibility = View.INVISIBLE
                        tvMessageSignupPasswordCheck.visibility = View.INVISIBLE
                    }

                    if(etSignupEmailPassword.text.toString().trim().length >= 8) {
                        if(Pattern.matches("^(?=.*[A-Za-z])(?=.*[\\d]).{8,}\$", etSignupEmailPassword.text.toString().trim())) {
                            if(Pattern.matches("^[A-Za-z\\d\$@\$!%*#?&]*\$", etSignupEmailPassword.text.toString().trim())) {
                                with(tvMessageSignupPasswordFormat) {
                                    // ?????? ?????? ?????????
                                    text = getString(R.string.success_message_email_signup_password)
                                    setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                                }
                                with(tvMessageSignupPasswordCheck){
                                    // ???????????? ?????? ?????? ???????????? ??????
                                    // '???????????? ?????? ?????? ???????????????' ?????? ??? '????????????' ?????? ?????????
                                    visibility = View.VISIBLE
                                    if(s.toString().trim().equals(binding.etSignupEmailPassword.text.toString().trim())) {
                                        text = getString(R.string.success_message_email_signup_password_check)
                                        setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                                        ActiveButtonUtil.setButtonActive(requireContext(), binding.btnNext)
                                    } else { // ???????????? ?????????
                                        text = getString(R.string.fail_message_email_signup_password_check)
                                        setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                                        ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                                    }
                                }
                            } else {
                                // ???????????? ?????? ?????? ?????????
                                tvMessageSignupPasswordCheck.visibility = View.INVISIBLE
                                tvMessageSignupPasswordFormat.text = getString(R.string.fail_message_email_signup_password_format_character)
                                tvMessageSignupPasswordFormat.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                                ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                            }
                        } else {
                            // ????????? ??? ?????? ?????? ?????? ?????????
                            tvMessageSignupPasswordCheck.visibility = View.INVISIBLE
                            tvMessageSignupPasswordFormat.text = getString(R.string.fail_message_email_signup_password_format)
                            tvMessageSignupPasswordFormat.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                            ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                        }
                    } else {
                        // ????????? ?????? ?????? ?????? ?????????
                        tvMessageSignupPasswordCheck.visibility = View.INVISIBLE
                        tvMessageSignupPasswordFormat.text = getString(R.string.fail_message_email_signup_password_format_length)
                        tvMessageSignupPasswordFormat.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                        ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
                    }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setEtClearClickListener() {
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtEmailPasswordCheck, binding.etSignupEmailPasswordCheck)
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtEmailPassword, binding.etSignupEmailPassword)
        binding.btnDeleteEtEmailPassword.setOnClickListener{
            // X?????? ?????? ?????? ???????????? ?????? ?????? ?????????????????? ???????????? ?????? ???????????? ????????? ?????? ????????????
            binding.tvMessageSignupPasswordCheck.setText(getString(R.string.fail_message_email_signup_password_check))
            binding.tvMessageSignupPasswordCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
            ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnNext)
        }
    }

    private fun setNextClickListener() {
        binding.btnNext.setOnClickListener {
            if(binding.etSignupEmailPassword.text.isNullOrBlank() || binding.etSignupEmailPasswordCheck.text.isNullOrBlank()) {
                Toast.makeText(activity, R.string.hint_password, Toast.LENGTH_SHORT).show()
            } else {
                signupViewModel.setPassword(binding.etSignupEmailPassword.text.toString().trim())
                navigate(R.id.action_emailSignupSetPasswordFragment_to_emailSignupSetNicknameFragment)
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