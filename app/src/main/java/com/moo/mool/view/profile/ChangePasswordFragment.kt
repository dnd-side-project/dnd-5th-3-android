package com.moo.mool.view.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.moo.mool.R
import com.moo.mool.databinding.FragmentChangePasswordBinding
import com.moo.mool.util.ActiveButtonUtil
import com.moo.mool.util.ToolbarDecorationUtil
import com.moo.mool.util.popBackStack
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.ProfileViewModel
import java.util.regex.Pattern

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val profileViewModel by activityViewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarChangePasswordBoard, R.string.change_password, requireActivity(), this)
        textWatcherEditText()
        setDeleteEditTextButtonClickListener()
        setChangeButtonClickListener()

        return binding.root
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            popBackStack()
        }
        return true
    }

    private fun textWatcherEditText() {
        profileViewModel.enableChange.observe(viewLifecycleOwner, Observer {
            if(it==true &&
                checkNewPasswordFormat(binding.etChangePasswordNew.text.toString().trim()) &&// 조건 2
                checkNewPassword(binding.etChangePasswordNew.text.toString().trim(), binding.etChangePasswordNewCheck.text.toString().trim()) // 조건 4
            ) {
                ActiveButtonUtil.setButtonActive(requireContext(), binding.btnChange)
            } else {
                ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnChange)
            }
        })

        binding.etChangePasswordOld.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                profileViewModel.checkOldPassword(s.toString().trim())
                profileViewModel.checkNewPassword(binding.etChangePasswordNew.text.toString().trim())

                profileViewModel.checkOldPasswordSuccess.observe(viewLifecycleOwner, Observer { success -> // 조건 1
                    with(binding.tvMessageChangePasswordCheckOld){
                        if(success==true) {
                            visibility = View.INVISIBLE
                        } else {
                            setText(R.string.change_password_fail_old_password)
                            visibility = View.VISIBLE
                        }
                    }
                })

                with(binding.btnDeleteEtOldPassword){
                    visibility = if(!s.toString().trim().isNullOrBlank()){
                        View.VISIBLE
                    } else {
                        View.INVISIBLE
                    }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        binding.etChangePasswordNew.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                profileViewModel.checkOldPassword(binding.etChangePasswordOld.text.toString().trim())
                profileViewModel.checkNewPassword(s.toString().trim())

                profileViewModel.checkNewPasswordSuccess.observe(viewLifecycleOwner, Observer { success -> // 조건 3
                    with(binding.tvMessageChangePasswordCheckNew) {
                        if (success == true) {
                            // binding.tvMessageChangePasswordCheckNewCheck.visibility = View.INVISIBLE
                            if (checkNewPasswordFormat(s.toString().trim())) { // 조건 2
                                visibility = View.INVISIBLE
                            } else if(s.toString().trim().length < 8){
                                setText(R.string.change_password_fail_new_password_format)
                                visibility = View.VISIBLE
                            } else if(Pattern.matches("^(?=.*[A-Za-z])(?=.*[\\d]).{8,}\$", s.toString().trim())) {
                                if(!Pattern.matches("^[A-Za-z\\d\$@\$!%*#?&]*\$", s.toString().trim())){
                                    setText(R.string.fail_message_email_signup_password_format_character)
                                    visibility = View.VISIBLE
                                }
                            } else {
                                setText(R.string.fail_message_email_signup_password_format)
                                visibility = View.VISIBLE
                            }
                        } else {
                            setText(R.string.change_password_fail_new_password_same)
                            visibility = View.VISIBLE
                        }
                    }
                })

                with(binding.tvMessageChangePasswordCheckNewCheck) {
                    if(checkNewPassword(s.toString().trim(), binding.etChangePasswordNewCheck.text.toString().trim())) { // 조건 4
                        visibility = View.INVISIBLE
                    } else {
                        visibility = View.VISIBLE
                        setText(R.string.change_password_fail_new_password_check)
                    }
                }

                with(binding.btnDeleteEtNewPassword) {
                    visibility = if(!s.toString().trim().isNullOrBlank()){
                        View.VISIBLE
                    } else { View.INVISIBLE }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        binding.etChangePasswordNewCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                profileViewModel.checkOldPassword(binding.etChangePasswordOld.text.toString().trim())
                profileViewModel.checkNewPassword(binding.etChangePasswordNew.text.toString().trim())

                with(binding.tvMessageChangePasswordCheckNewCheck) {
                    if(checkNewPassword(binding.etChangePasswordNew.text.toString().trim(), s.toString().trim())) { //조건 4
                        visibility = View.INVISIBLE
                    } else {
                        setText(R.string.change_password_fail_new_password_check)
                        visibility = View.VISIBLE
                    }
                }

                with(binding.btnDeleteEtNewPasswordCheck){
                    visibility = if(!s.toString().trim().isNullOrBlank()){
                        View.VISIBLE
                    } else {
                        View.INVISIBLE
                    }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun checkNewPasswordFormat(newPassword : String) : Boolean {
        if(Pattern.matches("^(?=.*[A-Za-z])(?=.*[\\d]).{8,}\$", newPassword.trim())) {
            if(Pattern.matches("^[A-Za-z\\d\$@\$!%*#?&]*\$", newPassword.trim())) {
                return true
            }
        }
        return false
    }

    private fun checkNewPassword(newPassword: String, checkPassword: String) : Boolean {
        return newPassword == checkPassword
    }

    private fun setDeleteEditTextButtonClickListener() {
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtOldPassword, binding.etChangePasswordOld)
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtNewPassword, binding.etChangePasswordNew)
        ActiveButtonUtil.setClearButton(binding.btnDeleteEtNewPasswordCheck, binding.etChangePasswordNewCheck)
    }

    private fun setChangeButtonClickListener() {
        binding.btnChange.setOnClickListener {
            profileViewModel.updatePassword(binding.etChangePasswordNew.text.toString()) // 서버와 연동하여 비밀번호 변경

            with(binding){
                with(tvMessageChangePasswordCheckNewCheck){
                    setText(R.string.change_password_success)
                    setTextColor(resources.getColor(R.color.moomool_blue_0098ff, context?.theme))
                    visibility = View.VISIBLE
                }
                tvMessageChangePasswordCheckNew.visibility = View.INVISIBLE
                tvMessageChangePasswordCheckOld.visibility = View.INVISIBLE
                btnDeleteEtNewPasswordCheck.visibility = View.INVISIBLE
                btnDeleteEtNewPassword.visibility = View.INVISIBLE
                btnDeleteEtOldPassword.visibility = View.INVISIBLE
                etChangePasswordNewCheck.isEnabled = false
                etChangePasswordNew.isEnabled = false
                etChangePasswordOld.isEnabled = false
            }
            ActiveButtonUtil.setButtonDeactivate(requireContext(), binding.btnChange)
            popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}