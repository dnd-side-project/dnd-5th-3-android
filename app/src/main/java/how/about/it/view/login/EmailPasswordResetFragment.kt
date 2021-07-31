package how.about.it.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import how.about.it.R
import how.about.it.database.SharedManager
import how.about.it.databinding.FragmentEmailPasswordResetBinding

class EmailPasswordRestFragment : Fragment() {
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

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}