package com.moo.mool.view.setting

import android.app.AlertDialog
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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.moo.mool.BuildConfig
import com.moo.mool.R
import com.moo.mool.databinding.FragmentSettingBinding
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
        setToolbarDetail()
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
        (activity as MainActivity).onBackPressed()
        return true
    }

    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        this.setHasOptionsMenu(true)
    }

    private fun setHideUnusedFeature() { // TODO: Hide 처리된 기능들 구현
        binding.tvMessageSetting.visibility = View.GONE
        binding.btnSettingNotification.visibility = View.GONE
        binding.btnSettingMotion.visibility = View.GONE
        binding.btnSettingInformationNotice.visibility = View.GONE
    }

    private fun setToolbarDetail() {
        binding.toolbarSettingBoard.tvToolbarTitle.setText(R.string.setting)
        (activity as MainActivity).setSupportActionBar(binding.toolbarSettingBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }

    private fun deleteSharedPreferencesInfo() {
        val prefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = prefs.edit()
        editor.clear().commit()
    }

    private fun setNoticeClickListener() {
        binding.btnSettingInformationNotice.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_settingFragment_to_noticeListFragment)
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
        binding.tvSettingLogout.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
            val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
            val mAlertDialog = mBuilder.create()
            // Dialog 중복 실행 방지
            if (mAlertDialog != null && !mAlertDialog.isShowing) {
                mAlertDialog.show()

                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title)
                    .setText(R.string.setting_logout)
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description)
                    .setText(R.string.setting_logout_dialog_description)

                val confirmButton = mDialogView.findViewById<TextView>(R.id.tv_dialog_confirm)
                val cancelButton = mDialogView.findViewById<TextView>(R.id.tv_dialog_cancel)
                cancelButton.setOnClickListener {
                    mAlertDialog.dismiss()
                }
                confirmButton.setOnClickListener {
                    deleteSharedPreferencesInfo()
                    settingViewModel.deleteTempPostAll()
                    val loginIntent = Intent(activity, LoginActivity::class.java)
                    startActivity(loginIntent)
                    (activity as MainActivity).finish()

                    mAlertDialog.dismiss()
                }
            }
        }
    }

    private fun setDeleteAccountClickListener() {
        binding.tvSettingDeleteAccount.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
            val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
            val mAlertDialog = mBuilder.create()
            // Dialog 중복 실행 방지
            if (mAlertDialog != null && !mAlertDialog.isShowing) {
                mAlertDialog.show()

                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title)
                    .setText(R.string.setting_delete_account)
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description)
                    .setText(R.string.setting_delete_account_check_dialog_description)

                val confirmButton = mDialogView.findViewById<TextView>(R.id.tv_dialog_confirm)
                val cancelButton = mDialogView.findViewById<TextView>(R.id.tv_dialog_cancel)
                cancelButton.setOnClickListener {
                    mAlertDialog.dismiss()
                }
                confirmButton.setOnClickListener {
                    settingViewModel.deleteAccount()
                    settingViewModel.deleteTempPostAll()
                    mAlertDialog.dismiss()
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title)
                        .setText(R.string.setting_delete_account_complete)
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description)
                        .setText(R.string.setting_delete_account_complete_dialog_description)
                    mDialogView.findViewById<TextView>(R.id.tv_dialog_cancel).visibility =
                        View.GONE
                    mAlertDialog.setCancelable(false)
                    mAlertDialog.show()
                    deleteSharedPreferencesInfo()

                    // 메인 로그인 화면으로 돌아가도록 설정
                    val deleteAccountConfirmButton =
                        mDialogView.findViewById<TextView>(R.id.tv_dialog_confirm)
                    deleteAccountConfirmButton.setOnClickListener {
                        val loginIntent = Intent(activity, LoginActivity::class.java)
                        startActivity(loginIntent)
                        (activity as MainActivity).finish()
                    }
                }
            }
        }
    }

    private fun setNotificationSettingClickListener() {
        binding.btnSettingNotification.setOnClickListener {
            ToastDefaultBlack.createToast(
                requireContext(),
                getString(R.string.fail_navigate_page_developing)
            )?.show()
            // requireView().findNavController().navigate(R.id.action_settingFragment_to_settingNotificationFragment)
        }
    }

    private fun setMotionSettingClickListener() {
        binding.btnSettingMotion.setOnClickListener {
            ToastDefaultBlack.createToast(
                requireContext(),
                getString(R.string.fail_navigate_page_developing)
            )?.show()
            // requireView().findNavController().navigate(R.id.action_settingFragment_to_settingMotionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}