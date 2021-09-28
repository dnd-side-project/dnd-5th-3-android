package com.moo.mool.view.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.moo.mool.BuildConfig
import com.moo.mool.R
import com.moo.mool.databinding.FragmentSettingBinding
import com.moo.mool.util.DefaultDialogUtil
import com.moo.mool.util.ToolbarDecorationUtil
import com.moo.mool.util.navigate
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.view.login.LoginActivity
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val settingViewModel by activityViewModels<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater)
        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarSettingBoard, R.string.setting, requireActivity(), this)
        setLogoutClickListener()
        setDeleteAccountClickListener()
        setHideUnusedFeature()
        setNotificationSettingClickListener()
        setMotionSettingClickListener()
        setInquiryClickListener()
        setNoticeClickListener()
        setCurrentAppVersion()
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        }
        return true
    }

    private fun setHideUnusedFeature() { // TODO: Hide 처리된 기능들 구현
        with(binding){
            View.GONE.also {
                tvMessageSetting.visibility = it
                btnSettingNotification.visibility = it
                btnSettingMotion.visibility = it
                btnSettingInformationNotice.visibility = it
            }
        }
    }

    private fun deleteSharedPreferencesInfo() {
        val prefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = prefs.edit()
        editor.clear().commit()
    }

    private fun setNoticeClickListener() {
        binding.btnSettingInformationNotice.setOnClickListener {
            navigate(R.id.action_settingFragment_to_noticeListFragment)
        }
    }

    private fun setInquiryClickListener() {
        binding.btnSettingInformationInquiry.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // only email apps should handle this
                putExtra(Intent.EXTRA_EMAIL, arrayOf("moomool.official@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "[문의사항]")
                putExtra(
                    Intent.EXTRA_TEXT, String.format(
                        "[기본 사항] \nApp Version : ${BuildConfig.VERSION_NAME}\nDevice : ${Build.MODEL}\nAndroid(SDK) : ${Build.VERSION.SDK_INT}(${Build.VERSION.RELEASE})\n\n문의 내용 : "
                    )
                );
            }
            val chooser = Intent.createChooser(intent, "Select an email app to open")

            try {
                startActivity(chooser)
            } catch (e: ActivityNotFoundException) {
                e.stackTrace
            }
        }
    }

    private fun setCurrentAppVersion() {
        binding.appVersion = BuildConfig.VERSION_NAME
    }

    private fun setLogoutClickListener() {
        val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
            R.string.setting_logout, R.string.setting_logout_dialog_description,
            true, true, null,null, {
                deleteSharedPreferencesInfo()
                settingViewModel.deleteTempPostAll()
                val loginIntent = Intent(activity, LoginActivity::class.java)
                startActivity(loginIntent)
                (activity as MainActivity).finish()
            }, null
        )

        binding.tvSettingLogout.setOnClickListener {
            // Dialog 중복 실행 방지
            if (mAlertDialog != null && !mAlertDialog.isShowing) {
                mAlertDialog.show()
            }
        }
    }

    private fun setDeleteAccountClickListener() {
        val mAlertDialogFarewell = DefaultDialogUtil.createDialog(requireContext(),
            R.string.setting_delete_account_complete, R.string.setting_delete_account_complete_dialog_description,
            true, false, null,null, {
                val loginIntent = Intent(activity, LoginActivity::class.java)
                startActivity(loginIntent)
                (activity as MainActivity).finish()
            }, null
        )

        val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
            R.string.setting_delete_account, R.string.setting_delete_account_check_dialog_description,
            true, true, null,null, {
                settingViewModel.deleteAccount()
                settingViewModel.deleteTempPostAll()
                mAlertDialogFarewell.show()
                deleteSharedPreferencesInfo()
            }, null
        )

        binding.tvSettingDeleteAccount.setOnClickListener {
            if (mAlertDialog != null && !mAlertDialog.isShowing) {
                mAlertDialog.show()
            }
        }
    }

    private fun setNotificationSettingClickListener() {
        binding.btnSettingNotification.setOnClickListener {
            ToastDefaultBlack.createToast(requireContext(), getString(R.string.fail_navigate_page_developing))?.show()
            // navigate(R.id.action_settingFragment_to_settingNotificationFragment)
        }
    }

    private fun setMotionSettingClickListener() {
        binding.btnSettingMotion.setOnClickListener {
            ToastDefaultBlack.createToast(requireContext(), getString(R.string.fail_navigate_page_developing))?.show()
            // navigate(R.id.action_settingFragment_to_settingMotionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}