package how.about.it.view.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import how.about.it.R
import how.about.it.database.SharedManager
import how.about.it.databinding.FragmentEmailSignupSetIdBinding
import how.about.it.view.login.LoginActivity
import how.about.it.viewmodel.SignupViewModel

class EmailSignupSetIDFragment : Fragment() {
    private var _binding : FragmentEmailSignupSetIdBinding?= null
    private val binding get() = _binding!!
    private val signupViewModel by activityViewModels<SignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailSignupSetIdBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding.toolbarSignupBoard.tvToolbarTitle.text = "회원가입"
        (activity as LoginActivity).setSupportActionBar(binding.toolbarSignupBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()

        binding.btnNext.setOnClickListener {
            if(binding.etSignupEmailId.text.isNullOrBlank()) {
                Toast.makeText(activity, "이메일를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                signupViewModel.setEmail(binding.etSignupEmailId.text.toString())
                // 비밀번호 작성 화면으로 이동
                (activity as LoginActivity).ReplaceEmailSignupSetPasswordFragment()
            }
        }

        binding.btnDeleteEtEmailId.setOnClickListener{
            binding.etSignupEmailId.setText("")
        }

        binding.etSignupEmailId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // 비밀번호, 비밀번호 확인 칸에 텍스트 작성시 아래에 안내 메시지 출력
                if(!s.toString().trim().isNullOrBlank()){
                    binding.btnDeleteEtEmailId.visibility = View.VISIBLE
                    binding.tvMessageEmailIdCheck.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteEtEmailId.visibility = View.INVISIBLE
                    binding.tvMessageEmailIdCheck.visibility = View.INVISIBLE
                }

                // TODO : 이메일 형식 확인 코드 작성
                if(s.toString().trim().length >= 5) {
                    // 형식 검사 성공시
                        // 서버 연동전 중복계정 검사 제외
                        binding.tvMessageEmailIdCheck.setText(getString(R.string.success_message_email_signup_id))
                        binding.tvMessageEmailIdCheck.setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                        activeButtonNext()

                    // TODO : 서버 연동 처리
                        /* 서버 연동 부분 주석처리
                        if(/** 중복계정 확인 구문 **/) {
                            // 중복 계정 검사 성공시
                            binding.tvMessageEmailIdCheck.setText(getString(R.string.success_message_email_signup_id))
                            binding.tvMessageEmailIdCheck.setTextColor(resources.getColorStateList(R.color.moomool_blue_0098ff, context?.theme))
                            activeButtonNext()
                        } else {
                            // 중복 계정 검사 실패시
                            binding.tvMessageEmailIdCheck.setText(getString(R.string.fail_message_email_signup_id_duplicate))
                            binding.tvMessageEmailIdCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
                            deactiveButtonNext()
                        } */
                } else {
                    // 형식 검사 실패시
                    binding.tvMessageEmailIdCheck.setText(getString(R.string.fail_message_eamil_signup_id_format))
                    binding.tvMessageEmailIdCheck.setTextColor(resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme))
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