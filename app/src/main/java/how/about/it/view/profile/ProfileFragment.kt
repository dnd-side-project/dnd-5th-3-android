package how.about.it.view.profile

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.database.SharedManager
import how.about.it.databinding.FragmentProfileBinding
import how.about.it.view.main.MainActivity
import how.about.it.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val sharedManager : SharedManager by lazy { SharedManager(requireContext()) }
    private val profileViewModel by activityViewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.toolbarProfileBoard.tvToolbarTitle.setText(R.string.profile_change_nickname)
        (activity as MainActivity).setSupportActionBar(binding.toolbarProfileBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()

        setEtChangePasswordClickListener()

        binding.etProfileNickname.setText(sharedManager.getCurrentUser().nickname.toString())
        binding.etProfileEmail.setText(sharedManager.getCurrentUser().email.toString())
        val originalNickname = sharedManager.getCurrentUser().nickname.toString()

        // 닉네임 TEXT가 바뀌면 중복확인 버튼 + 아래에 메시지 나타나기
        binding.etProfileNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(s.toString() != originalNickname) {
                    activeDuplicateCheck()
                } else {
                    deactiveDuplicateCheck()
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?)  }
        })

        binding.btnDuplicateCheckEtNickname.setOnClickListener {
            // TODO : Dialog 띄우기 코드 개선 필요
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
            val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
            val mAlertDialog = mBuilder.create()
            // Dialog 확인, 취소버튼 설정
            val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
            val cancelButton = mDialogView.findViewById<Button>(R.id.btn_dialog_cancel)
            cancelButton.text = resources.getText(R.string.back)
            mAlertDialog.show()

            profileViewModel.duplicateCheckNickname(binding.etProfileNickname.text.toString().trim())
            profileViewModel.duplicateCheckNicknameSuccess.observe(viewLifecycleOwner, Observer {
                if(it) {
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.profile_change_nickname_success_dialog_title)
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.profile_change_nickname_success_dialog_description)

                    mDialogView.findViewById<ConstraintLayout>(R.id.layout_dialog_cancel).visibility = View.GONE
                    confirmButton.setOnClickListener {
                        deactiveDuplicateCheck()
                        binding.tvMessageChangeCheckNickname.visibility = View.VISIBLE
                        binding.tvMessageChangeCheckNickname.setText(R.string.success_message_profile_change_nickname)
                        binding.tvMessageChangeCheckNickname.setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                        binding.etProfileNickname.isEnabled = false
                        activeButtonSave()
                        mAlertDialog.dismiss()
                    }
                } else {
                    // Dialog 제목 및 내용 설정
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.profile_change_nickname_fail_dialog_title)
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.profile_change_nickname_fail_dialog_description)

                    mDialogView.findViewById<ConstraintLayout>(R.id.layout_dialog_confirm).visibility = View.GONE

                    cancelButton.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                }
            })
            profileViewModel.duplicateCheckNicknameFailedMessage.observe(viewLifecycleOwner, Observer {
                Log.e("Duplicate Check Error", it.toString())
            })
        }
        // TODO : 현재 닉네임 텍스트와 기존 닉네임이 다를때 뒤로가기누르면 경고 Dialog 띄우기

        binding.btnSave.setOnClickListener {
            // TODO : Dialog 띄우기 코드 개선 필요
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            // Dialog 제목 및 내용 설정
            mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.profile_change_all_success_dialog_title)
            mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.profile_change_all_success_dialog_description)
            mDialogView.findViewById<ConstraintLayout>(R.id.layout_dialog_cancel).visibility = View.GONE

            // Dialog 확인, 취소버튼 설정
            val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)

            // Dialog 확인 버튼을 클릭 한 경우
            confirmButton.setOnClickListener {
                // TODO : 변경내용 서버와 통신하여 저장하는 코드 작성
                mAlertDialog.dismiss()
                // 저장 후 바로 메인화면으로 되돌아가기 위해 OnBackPressed 사용
                (activity as MainActivity).onBackPressed()
            }
        }

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

    private fun activeDuplicateCheck() {
        binding.btnDuplicateCheckEtNickname.isEnabled = true
        binding.btnDuplicateCheckEtNickname.visibility = View.VISIBLE
        binding.tvMessageChangeCheckNickname.visibility = View.VISIBLE
    }

    private fun deactiveDuplicateCheck() {
        binding.btnDuplicateCheckEtNickname.isEnabled = false
        binding.btnDuplicateCheckEtNickname.visibility = View.INVISIBLE
        binding.tvMessageChangeCheckNickname.visibility = View.INVISIBLE
    }

    private fun activeButtonSave() {
        binding.btnSave.isEnabled = true
        binding.btnSave.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_enable))
        binding.btnSave.setTextColor(resources.getColorStateList(R.color.bluegray50_F9FAFC, context?.theme))
    }
    private fun deactiveButtonSave() {
        binding.btnSave.isEnabled = false
        binding.btnSave.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_disable))
        binding.btnSave.setTextColor(resources.getColorStateList(R.color.bluegray600_626670, context?.theme))
    }

    private fun setEtChangePasswordClickListener() {
        binding.etProfilePassword.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}