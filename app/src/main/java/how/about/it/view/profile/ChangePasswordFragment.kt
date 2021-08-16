package how.about.it.view.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import how.about.it.R
import how.about.it.databinding.FragmentChangePasswordBinding
import how.about.it.view.main.MainActivity

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = requireNotNull(_binding)

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
        (activity as MainActivity).onBackPressed()
        return true
    }
    private fun setToolbarDetail() {
        binding.toolbarChangePasswordBoard.tvToolbarTitle.setText("비밀번호 변경")
        (activity as MainActivity).setSupportActionBar(binding.toolbarChangePasswordBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }

    private fun textWatcherEditText() {
        binding.etChangePasswordOld.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(checkOldPassword(s.toString().trim())){
                    binding.tvMessageChangePasswordCheckOld.visibility = View.INVISIBLE

                    if(checkNewPasswordFormat(binding.etChangePasswordNew.text.toString().trim()) &&
                            checkNewPasswordSame(binding.etChangePasswordNew.text.toString().trim()) &&
                            checkNewPassword(binding.etChangePasswordNew.text.toString().trim(), binding.etChangePasswordNewCheck.text.toString().trim())){
                        activeButtonChange()
                    } else { deactiveButtonChange() }

                } else{
                    binding.tvMessageChangePasswordCheckOld.setText(R.string.change_password_fail_old_password)
                    binding.tvMessageChangePasswordCheckOld.visibility = View.VISIBLE
                    deactiveButtonChange()}

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
                if(checkNewPasswordFormat(s.toString().trim())){ // 새 비밀번호 형식 검사 통과
                    if(checkNewPasswordSame(s.toString().trim())) { // 이전 비밀번호와 불일치 검사 통과
                        binding.tvMessageChangePasswordCheckNew.visibility = View.INVISIBLE
                        if(checkNewPassword(binding.etChangePasswordNew.text.toString().trim(), binding.etChangePasswordNewCheck.text.toString().trim())) {
                            binding.tvMessageChangePasswordCheckNewCheck.visibility = View.INVISIBLE
                            if(checkOldPassword(binding.etChangePasswordOld.text.toString().trim())){
                                activeButtonChange()
                            } else { deactiveButtonChange() }
                        } else {
                            binding.tvMessageChangePasswordCheckNewCheck.setText(R.string.change_password_fail_new_password_check)
                            binding.tvMessageChangePasswordCheckNewCheck.visibility = View.VISIBLE
                            activeButtonChange()
                        }
                    } else {
                        binding.tvMessageChangePasswordCheckNew.setText(R.string.change_password_fail_new_password_same)
                        binding.tvMessageChangePasswordCheckNew.visibility = View.VISIBLE
                    }
                } else{
                    binding.tvMessageChangePasswordCheckNew.setText(R.string.change_password_fail_new_password_format)
                    binding.tvMessageChangePasswordCheckNew.visibility = View.VISIBLE
                    deactiveButtonChange()}

                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtNewPassword.visibility = View.VISIBLE
                } else { binding.btnDeleteEtNewPassword.visibility = View.INVISIBLE }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        binding.etChangePasswordNewCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // 이전 비밀번호와 일치 검사
                if(checkNewPassword(binding.etChangePasswordNew.text.toString().trim(), s.toString().trim())){
                    binding.tvMessageChangePasswordCheckNewCheck.visibility = View.INVISIBLE
                    if( checkOldPassword(binding.etChangePasswordOld.text.toString().trim()) &&
                        checkNewPasswordFormat(binding.etChangePasswordNew.text.toString().trim()) &&
                        checkNewPasswordSame(binding.etChangePasswordNew.text.toString().trim())){
                        activeButtonChange()
                    } else { deactiveButtonChange() }

                } else{
                    binding.tvMessageChangePasswordCheckNewCheck.setText(R.string.change_password_fail_new_password_check)
                    binding.tvMessageChangePasswordCheckNewCheck.visibility = View.VISIBLE
                    deactiveButtonChange()}

                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtNewPasswordCheck.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteEtNewPasswordCheck.visibility = View.INVISIBLE }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })
    }

    private fun checkOldPassword(oldPassword: String) : Boolean {
        /** TODO : 현재 비밀번호와 입력한 비밀번호 일치 검사 **/
        return true
    }

    private fun checkNewPasswordFormat(newPassword : String) : Boolean {
        if(newPassword.trim().length >= 5) return true
        return false
    }

    private fun checkNewPasswordSame(newPassword: String) : Boolean {
        if(true /** TODO :  newPassword != 현재 비밀번호 **/){

            return true
        }
        return false
    }

    private fun checkNewPassword(newPassword: String, checkPassword: String) : Boolean {
        if(newPassword == checkPassword) return true
        return false
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
        }
    }

    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.setHasOptionsMenu(true)
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