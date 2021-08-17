package how.about.it.view.setting

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import how.about.it.R
import how.about.it.databinding.FragmentSettingBinding
import how.about.it.view.login.LoginActivity
import how.about.it.view.main.MainActivity

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = requireNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater)
        setToolbarDetail()
        setLogoutClickListener()
        setDeleteAccountClickListener()

        return binding.root
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        (activity as MainActivity).onBackPressed()
        return true
    }
    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.setHasOptionsMenu(true)
    }
    private fun setToolbarDetail() {
        binding.toolbarSettingBoard.tvToolbarTitle.setText(R.string.setting)
        (activity as MainActivity).setSupportActionBar(binding.toolbarSettingBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }

    private fun setLogoutClickListener() {
        binding.tvSettingLogout.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
            val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
            val mAlertDialog = mBuilder.create()
            // Dialog 중복 실행 방지
            if(mAlertDialog != null && !mAlertDialog.isShowing){
                mAlertDialog.show()

                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.setting_logout)
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.setting_logout_dialog_description)

                val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
                val cancelButton = mDialogView.findViewById<Button>(R.id.btn_dialog_cancel)
                cancelButton.setOnClickListener {
                    mAlertDialog.dismiss()
                }
                confirmButton.setOnClickListener {
                    val prefs: SharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(requireContext())
                    val editor = prefs.edit()
                    editor.clear().commit()

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

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}