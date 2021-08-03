package how.about.it.view.profile

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
import how.about.it.R
import how.about.it.database.SharedManager
import how.about.it.databinding.FragmentProfileBinding
import how.about.it.view.main.MainActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val sharedManager : SharedManager by lazy { SharedManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.toolbarProfileBoard.tvToolbarTitle.text = "프로필 변경"
        (activity as MainActivity).setSupportActionBar(binding.toolbarProfileBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()

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
        }
        // TODO : 중복확인 버튼 클릭 시 Dialog 띄우기
        // TODO : 중복확인 버튼 클릭 후 완료시 중복확인 버튼 INVISIBLE 처리 후 닉네임 아래 메시지 변경
        // TODO : 중복확인 완료시 저장하기 버튼 활성화
        // TODO : 현재 닉네임 텍스트와 기존 닉네임이 다를때 뒤로가기누르면 경고 Dialog 띄우기


        binding.etProfilePassword.setOnClickListener {
            // TODO : 비밀번호 변경 화면 띄우기
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}