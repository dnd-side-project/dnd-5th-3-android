package how.about.it.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import how.about.it.R
import how.about.it.database.SharedManager
import how.about.it.databinding.FragmentEmailPasswordResetBinding

class EmailPasswordResetFragment : Fragment() {
    private var _binding : FragmentEmailPasswordResetBinding?= null
    private val binding get() = _binding!!
    private val sharedManager : SharedManager by lazy { SharedManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailPasswordResetBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding.toolbarLoginBoard.tvToolbarTitle.text = "비밀번호 초기화"
        (activity as LoginActivity).setSupportActionBar(binding.toolbarLoginBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as LoginActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.btnResetEmail.setOnClickListener {
            if(binding.etLoginEmailId.text.isNullOrBlank()) {
                Toast.makeText(activity, "이메일를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                // TODO : 이메일 인증 코드 작성
            }
        }

        binding.btnDeleteEtEmailId.setOnClickListener{
            binding.etLoginEmailId.setText("")
        }

        binding.etLoginEmailId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrBlank()){
                    activeButtonResetPassword()
                    binding.btnDeleteEtEmailId.visibility = View.VISIBLE
                } else {
                    deactiveButtonResetPassword()
                    binding.btnDeleteEtEmailId.visibility = View.INVISIBLE
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.isNullOrBlank()){
                    activeButtonResetPassword()
                    binding.btnDeleteEtEmailId.visibility = View.VISIBLE
                } else {
                    deactiveButtonResetPassword()
                    binding.btnDeleteEtEmailId.visibility = View.INVISIBLE
                }
            }
        })


        return view
    }

    fun activeButtonResetPassword() {
        binding.btnResetEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_enable))
        binding.btnResetEmail.setTextColor(resources.getColorStateList(R.color.bluegray50_F9FAFC, context?.theme))
    }
    fun deactiveButtonResetPassword() {
        binding.btnResetEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_disable))
        binding.btnResetEmail.setTextColor(resources.getColorStateList(R.color.bluegray600_626670, context?.theme))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}