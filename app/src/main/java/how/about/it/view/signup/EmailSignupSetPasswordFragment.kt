package how.about.it.view.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import how.about.it.R
import how.about.it.database.SharedManager
import how.about.it.databinding.FragmentEmailSignupSetPasswordBinding
import how.about.it.view.login.LoginActivity
import how.about.it.viewmodel.SignupViewModel

class EmailSignupSetPasswordFragment : Fragment() {
    private var _binding : FragmentEmailSignupSetPasswordBinding?= null
    private val binding get() = _binding!!
    private val signupViewModel by activityViewModels<SignupViewModel>()
    private val sharedManager : SharedManager by lazy { SharedManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailSignupSetPasswordBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding.toolbarSignupBoard.tvToolbarTitle.text = "회원가입"
        (activity as LoginActivity).setSupportActionBar(binding.toolbarSignupBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()

        binding.btnNext.setOnClickListener {
            if(binding.etSignupEmailPassword.text.isNullOrBlank() || binding.etSignupEmailPasswordCheck.text.isNullOrBlank()) {
                Toast.makeText(activity, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                    signupViewModel.setPassword(binding.etSignupEmailPassword.text.toString())
                // 닉네임 작성 화면으로 이동
                (activity as LoginActivity).ReplaceEmailSignupSetNicknameFragment()
            }
        }

        binding.btnDeleteEtEmailPassword.setOnClickListener{
            binding.etSignupEmailPassword.setText("")
            // X버튼 누른 경우 비밀번호 확인 아래 안내메시지와 다음단계 버튼 업데이트 안되는 현상 임시조치
            binding.tvMessageSignupPasswordCheck.setText(getString(R.string.fail_message_email_signup_password_check))
            binding.tvMessageSignupPasswordCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
            deactiveButtonNext()
        }
        binding.btnDeleteEtEmailPasswordCheck.setOnClickListener{
            binding.etSignupEmailPasswordCheck.setText("")
        }
        binding.etSignupEmailPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { afterTextChanged(s as Editable?) }
            override fun afterTextChanged(s: Editable?) {
                // 비밀번호, 비밀번호 확인 칸에 텍스트 작성시 아래에 안내 메시지 출력
                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtEmailPassword.visibility = View.VISIBLE
                    binding.tvMessageSignupPasswordFormat.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteEtEmailPassword.visibility = View.INVISIBLE
                    binding.tvMessageSignupPasswordFormat.visibility = View.INVISIBLE
                }

                // TODO : 비밀번호 형식 확인 코드 작성
                if(s.toString().trim().length >= 5) {
                    // 형식 검사 성공시
                    binding.tvMessageSignupPasswordFormat.setText(getString(R.string.success_message_email_signup_password))
                    binding.tvMessageSignupPasswordFormat.setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))

                    // 비밀번호 확인 칸과 일치하는 경우
                    // '비밀번호 확인 아래 안내메시지' 변경 및 '계속하기' 버튼 활성화
                    if(s.toString().trim().equals(binding.etSignupEmailPasswordCheck.text.toString().trim())) {
                        binding.tvMessageSignupPasswordCheck.setText(getString(R.string.success_message_email_signup_password_check))
                        binding.tvMessageSignupPasswordCheck.setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                        activeButtonNext()
                    } else { // 비밀번호 불일치
                        binding.tvMessageSignupPasswordCheck.setText(getString(R.string.fail_message_email_signup_password_check))
                        binding.tvMessageSignupPasswordCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                        deactiveButtonNext()
                    }
                } else {
                    // 형식 검사 실패시
                    binding.tvMessageSignupPasswordFormat.setText(getString(R.string.fail_message_email_signup_password_format))
                    binding.tvMessageSignupPasswordFormat.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        binding.etSignupEmailPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { afterTextChanged(s as Editable?) }
            override fun afterTextChanged(s: Editable?) {
                // 비밀번호, 비밀번호 확인 칸에 텍스트 작성시 아래에 안내 메시지 출력
                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtEmailPasswordCheck.visibility = View.VISIBLE
                    binding.tvMessageSignupPasswordCheck.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteEtEmailPasswordCheck.visibility = View.INVISIBLE
                    binding.tvMessageSignupPasswordCheck.visibility = View.INVISIBLE
                }

                if(s.toString().trim().equals(binding.etSignupEmailPassword.text.toString().trim())) {
                    // 비밀번호 확인 칸과 일치하는 경우 '계속하기' 버튼 활성화
                    binding.tvMessageSignupPasswordCheck.setText(getString(R.string.success_message_email_signup_password_check))
                    binding.tvMessageSignupPasswordCheck.setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                    activeButtonNext()
                } else {
                    // 비밀번호 불일치
                    binding.tvMessageSignupPasswordCheck.setText(getString(R.string.fail_message_email_signup_password_check))
                    binding.tvMessageSignupPasswordCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                    deactiveButtonNext()
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        (activity as LoginActivity).onBackPressed()
        return true
    }

    private fun showBackButton() {
        (activity as LoginActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.setHasOptionsMenu(true)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}