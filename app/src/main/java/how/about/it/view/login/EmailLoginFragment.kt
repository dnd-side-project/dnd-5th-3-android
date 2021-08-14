package how.about.it.view.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import how.about.it.R
import how.about.it.databinding.FragmentEmailLoginBinding
import how.about.it.model.RequestLogin
import how.about.it.repository.LoginRepository
import how.about.it.view.main.MainActivity
import how.about.it.viewmodel.LoginViewModel
import how.about.it.viewmodel.LoginViewModelFactory

class EmailLoginFragment : Fragment() {
    private var _binding : FragmentEmailLoginBinding?= null
    private val binding get() = _binding!!
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailLoginBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(LoginRepository(requireContext()))).get(LoginViewModel::class.java)

        binding.toolbarLoginBoard.tvToolbarTitle.text = "로그인"
        (activity as LoginActivity).setSupportActionBar(binding.toolbarLoginBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as LoginActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.btnLoginEmail.setOnClickListener {
            if (binding.etLoginEmailId.text.isNullOrBlank() || binding.etLoginEmailPassword.text.isNullOrBlank()) {
                Toast.makeText(activity, "이메일과 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(RequestLogin(binding.etLoginEmailId.text.toString(), binding.etLoginEmailPassword.text.toString()))
            }
        }

        loginViewModel.loginSuccess.observe(viewLifecycleOwner, Observer {
            if(it) {
                val loginIntent = Intent(activity, MainActivity::class.java)
                startActivity(loginIntent)
                (activity as LoginActivity).finish()
            } else {
                binding.tvMessageFailEmailCheck.visibility = View.VISIBLE
                binding.tvMessageFailPasswordCheck.visibility = View.VISIBLE
                binding.tvMessagePasswordReset.visibility = View.VISIBLE
            }
        })
        loginViewModel.loginFailedMessage.observe(viewLifecycleOwner, Observer {
            Log.e("Login Error", it.toString())
        })

        binding.tvMessagePasswordReset.setOnClickListener {
            (activity as LoginActivity).ReplaceEmailPasswordResetFragment()
        }

        binding.btnDeleteEtEmailId.setOnClickListener{
            binding.etLoginEmailId.setText("")
        }
        binding.btnDeleteEtEmailPassword.setOnClickListener {
            binding.etLoginEmailPassword.setText("")
        }

        binding.etLoginEmailId.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrBlank() && !binding.etLoginEmailPassword.text.isNullOrBlank()){
                        activeButtonLoginEmail()
                } else { deactiveButtonLoginEmail() }

                if(!s.isNullOrBlank()){
                    binding.btnDeleteEtEmailId.visibility = View.VISIBLE
                } else { binding.btnDeleteEtEmailId.visibility = View.INVISIBLE }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })
        binding.etLoginEmailPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrBlank() && !binding.etLoginEmailId.text.isNullOrBlank()){
                    activeButtonLoginEmail()
                } else { deactiveButtonLoginEmail() }

                if(!s.isNullOrBlank()){
                    binding.btnDeleteEtEmailPassword.visibility = View.VISIBLE
                } else { binding.btnDeleteEtEmailPassword.visibility = View.INVISIBLE }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        return view
    }

    fun activeButtonLoginEmail() {
        binding.btnLoginEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_enable))
        binding.btnLoginEmail.setTextColor(resources.getColorStateList(R.color.bluegray50_F9FAFC, context?.theme))
    }
    fun deactiveButtonLoginEmail() {
        binding.btnLoginEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_default_disable))
        binding.btnLoginEmail.setTextColor(resources.getColorStateList(R.color.bluegray600_626670, context?.theme))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}