package com.moo.mool.view.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.moo.mool.R
import com.moo.mool.databinding.FragmentProfileBinding
import com.moo.mool.util.*
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.collect

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val profileViewModel by activityViewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarProfileBoard, R.string.profile_change_nickname, requireActivity(), this)
        textWatcherEditText()
        setEtChangePasswordClickListener()
        setCheckPasswordChanged()
        setCheckDuplicateNickname()
        setChangeSaveClickListener()

        // TODO : 현재 닉네임 텍스트와 기존 닉네임이 다를때 뒤로가기누르면 경고 Dialog 띄우기
        return binding.root
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        }
        return true
    }

    private fun activeDuplicateCheck() {
        with(binding){
            with(btnDuplicateCheckEtNickname){
                isEnabled = true
                visibility = View.VISIBLE
            }
            tvMessageChangeCheckNickname.visibility = View.VISIBLE
        }
    }
    private fun deactiveDuplicateCheck() {
        with(binding){
            with(btnDuplicateCheckEtNickname){
                isEnabled = false
                visibility = View.INVISIBLE
            }
            tvMessageChangeCheckNickname.visibility = View.INVISIBLE
        }
    }

    private fun setEtChangePasswordClickListener() {
        profileViewModel.checkSocialEmail()
        with(binding){
            etProfileNickname.setText(profileViewModel.getCurrentUserNickname())
            etProfilePassword.setOnClickListener {
                setRequestCheckSocialEmailCollect()
            }
        }
    }

    private fun setRequestCheckSocialEmailCollect() {
        repeatOnLifecycle {
            profileViewModel.checkSocialEmailSuccess.collect{ socialEmailCheck ->
                if(socialEmailCheck) {
                    navigate(R.id.action_profileFragment_to_changePasswordFragment)
                } else {
                    ToastDefaultBlack.createToast(requireContext(), getString(R.string.profile_change_navigate_changePassword_fail_toast))?.show()
                }
            }
        }
    }
    private fun setCheckPasswordChanged() {
        with(binding.tvMessageChangeCheckEmailPassword){
            visibility = if(profileViewModel.checkPasswordChanged){
                View.VISIBLE
            } else{
                View.INVISIBLE
            }
        }
    }

    private fun setCheckDuplicateNickname() {
        binding.btnDuplicateCheckEtNickname.setOnClickListener {
            profileViewModel.duplicateCheckNickname(binding.etProfileNickname.text.toString().trim())
        }

        profileViewModel.duplicateCheckNicknameSuccess.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
                    R.string.profile_change_nickname_success_dialog_title, R.string.profile_change_nickname_success_dialog_description,
                    true, false, null, null,
                    {
                        deactiveDuplicateCheck()
                        with(binding.tvMessageChangeCheckNickname){
                            visibility = View.VISIBLE
                            setText(R.string.success_message_profile_change_nickname)
                            setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                        }
                        binding.etProfileNickname.isEnabled = false
                        ActiveButtonUtil.setButtonActive(requireContext(), binding.btnSave)
                    }, null
                )
                if(mAlertDialog != null && !mAlertDialog.isShowing) {
                    mAlertDialog.show()
                }
            } else if(it == false) {
                val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
                    R.string.profile_change_nickname_fail_dialog_title, R.string.profile_change_nickname_fail_dialog_description,
                    false, true, null, R.string.back,
                    null, null
                )
                if(mAlertDialog != null && !mAlertDialog.isShowing) {
                    mAlertDialog.show()
                }
            }
        })
        profileViewModel.duplicateCheckNicknameFailedMessage.observe(viewLifecycleOwner, Observer {
            Log.e("Duplicate Check Error", it.toString())
        })
    }

    private fun textWatcherEditText() {
        binding.etProfileNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(s.toString() != profileViewModel.getCurrentUserNickname() && s.toString().trim().isNotEmpty()) {
                    if(EdittextCount.getGraphemeCount(s.toString().trim()) > 8){
                        with(binding){
                            with(btnDuplicateCheckEtNickname){
                                isEnabled = false
                                visibility = View.INVISIBLE
                            }
                            with(tvMessageChangeCheckNickname){
                                setText(R.string.fail_message_email_signup_nickname_format)
                                visibility = View.VISIBLE
                            }
                        }
                    } else {
                        binding.tvMessageChangeCheckNickname.setText(R.string.message_profile_change_nickname)
                        activeDuplicateCheck()
                    }
                } else {
                    deactiveDuplicateCheck()
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setChangeSaveClickListener() {

        binding.btnSave.setOnClickListener {
            profileViewModel.updateNickname(binding.etProfileNickname.text.toString()) // 서버와 닉네임 연동하여 변경
            val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
                R.string.profile_change_all_success_dialog_title, R.string.profile_change_all_success_dialog_description,
                true, false, null, null,
                { (activity as MainActivity).onBackPressed() }, null
            )
            mAlertDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileViewModel.duplicateCheckNicknameSuccess.value = null // 이전 화면과 같은 Dialog 실행 방지
        profileViewModel.checkPasswordChanged = false
        _binding = null
    }
}