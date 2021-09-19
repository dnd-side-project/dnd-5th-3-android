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
        setToolbarDetail()
        textWatcherEditText()
        setDeleteEditTextButtonClickListener()
        setChangeButtonClickListener()

        return binding.root
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        requireView().findNavController().popBackStack()
        return true
    }

    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        this.setHasOptionsMenu(true)
    }

    private fun setToolbarDetail() {
        binding.toolbarChangePasswordBoard.tvToolbarTitle.setText(R.string.change_password)
        (activity as MainActivity).setSupportActionBar(binding.toolbarChangePasswordBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }

    private fun textWatcherEditText() {
        profileViewModel.enableChange.observe(viewLifecycleOwner, Observer {
            if(it==true &&
                checkNewPasswordFormat(binding.etChangePasswordNew.text.toString().trim()) &&// 조건 2
                checkNewPassword(binding.etChangePasswordNew.text.toString().trim(), binding.etChangePasswordNewCheck.text.toString().trim()) // 조건 4
            ) {
                activeButtonChange()
            } else {
                deactiveButtonChange()
            }
        })

        binding.etChangePasswordOld.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                profileViewModel.checkOldPassword(s.toString().trim())
                profileViewModel.checkNewPassword(binding.etChangePasswordNew.text.toString().trim())

                profileViewModel.checkOldPasswordSuccess.observe(viewLifecycleOwner, Observer { // 조건 1
                    if(it==true) {
                        binding.tvMessageChangePasswordCheckOld.visibility = View.INVISIBLE
                    } else {
                        binding.tvMessageChangePasswordCheckOld.setText(R.string.change_password_fail_old_password)
                        binding.tvMessageChangePasswordCheckOld.visibility = View.VISIBLE
                    }
                })

                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtOldPassword.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteEtOldPassword.visibility = View.INVISIBLE }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        binding.etChangePasswordNew.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                profileViewModel.checkOldPassword(binding.etChangePasswordOld.text.toString().trim())
                profileViewModel.checkNewPassword(s.toString().trim())

                profileViewModel.checkNewPasswordSuccess.observe(viewLifecycleOwner, Observer { // 조건 3
                    if(it==true) {
                        // binding.tvMessageChangePasswordCheckNewCheck.visibility = View.INVISIBLE
                        if(checkNewPasswordFormat(s.toString().trim())) { // 조건 2
                            binding.tvMessageChangePasswordCheckNew.visibility = View.INVISIBLE
                        } else if(Pattern.matches("^[^\$@\$!%*#?&]*\$", s.toString().trim())){
                            binding.tvMessageChangePasswordCheckNew.setText(R.string.fail_message_email_signup_password_format_character)
                            binding.tvMessageChangePasswordCheckNew.visibility = View.VISIBLE
                        } else {
                            binding.tvMessageChangePasswordCheckNew.setText(R.string.change_password_fail_new_password_format)
                            binding.tvMessageChangePasswordCheckNew.visibility = View.VISIBLE
                        }
                    } else {
                        binding.tvMessageChangePasswordCheckNew.setText(R.string.change_password_fail_new_password_same)
                        binding.tvMessageChangePasswordCheckNew.visibility = View.VISIBLE
                    }
                })

                if(checkNewPassword(s.toString().trim(), binding.etChangePasswordNewCheck.text.toString().trim())) { // 조건 4
                    binding.tvMessageChangePasswordCheckNewCheck.visibility = View.INVISIBLE
                } else {
                    binding.tvMessageChangePasswordCheckNewCheck.visibility = View.VISIBLE
                    binding.tvMessageChangePasswordCheckNewCheck.setText(R.string.change_password_fail_new_password_check)
                }

                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtNewPassword.visibility = View.VISIBLE
                } else { binding.btnDeleteEtNewPassword.visibility = View.INVISIBLE }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        binding.etChangePasswordNewCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                profileViewModel.checkOldPassword(binding.etChangePasswordOld.text.toString().trim())
                profileViewModel.checkNewPassword(binding.etChangePasswordNew.text.toString().trim())

                if(checkNewPassword(binding.etChangePasswordNew.text.toString().trim(), s.toString().trim())) { //조건 4
                    binding.tvMessageChangePasswordCheckNewCheck.visibility = View.INVISIBLE
                } else {
                    binding.tvMessageChangePasswordCheckNewCheck.setText(R.string.change_password_fail_new_password_check)
                    binding.tvMessageChangePasswordCheckNewCheck.visibility = View.VISIBLE
                }

                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtNewPasswordCheck.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteEtNewPasswordCheck.visibility = View.INVISIBLE }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
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
        binding.btnDeleteEtOldPassword.setOnClickListener{
            binding.etChangePasswordOld.setText("")
        }
        binding.btnDeleteEtNewPassword.setOnClickListener {
            binding.etChangePasswordNew.setText("")
        }
        binding.btnDeleteEtNewPasswordCheck.setOnClickListener {
            binding.etChangePasswordNewCheck.setText("")
        }
    }

    private fun setChangeButtonClickListener() {
        binding.btnChange.setOnClickListener {
            profileViewModel.updatePassword(binding.etChangePasswordNew.text.toString()) // 서버와 연동하여 비밀번호 변경

            binding.tvMessageChangePasswordCheckNewCheck.setText(R.string.change_password_success)
            binding.tvMessageChangePasswordCheckNewCheck.setTextColor(resources.getColor(R.color.moomool_blue_0098ff, context?.theme))
            binding.tvMessageChangePasswordCheckNewCheck.visibility = View.VISIBLE
            binding.tvMessageChangePasswordCheckNew.visibility = View.INVISIBLE
            binding.tvMessageChangePasswordCheckOld.visibility = View.INVISIBLE
            binding.btnDeleteEtNewPasswordCheck.visibility = View.INVISIBLE
            binding.btnDeleteEtNewPassword.visibility = View.INVISIBLE
            binding.btnDeleteEtOldPassword.visibility = View.INVISIBLE
            binding.etChangePasswordNewCheck.isEnabled = false
            binding.etChangePasswordNew.isEnabled = false
            binding.etChangePasswordOld.isEnabled = false
            deactiveButtonChange()
            requireView().findNavController().popBackStack()
        }
    }

    private fun activeButtonChange() {
        binding.btnChange.isEnabled = true
        binding.btnChange.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_enable))
        binding.btnChange.setTextColor(resources.getColorStateList(R.color.bluegray50_F9FAFC, context?.theme))
    }
    private fun deactiveButtonChange() {
        binding.btnChange.isEnabled = false
        binding.btnChange.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_disable))
        binding.btnChange.setTextColor(resources.getColorStateList(R.color.bluegray600_626670, context?.theme))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}