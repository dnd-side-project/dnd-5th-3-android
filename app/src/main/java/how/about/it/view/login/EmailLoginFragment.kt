package how.about.it.view.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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

        binding.toolbarLoginBoard.tvToolbarTitle.setText(R.string.login)
        (activity as LoginActivity).setSupportActionBar(binding.toolbarLoginBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as LoginActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
        val mAlertDialog = mBuilder.create()

        setKeyboardHide()

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
                // Dialog 중복 실행 방지
                if(mAlertDialog != null && !mAlertDialog.isShowing){
                    mAlertDialog.show()

                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.login_fail_dialog_title)
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.login_fail_dialog_description)

                    val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
                    mDialogView.findViewById<ConstraintLayout>(R.id.layout_dialog_cancel).visibility = View.GONE
                    confirmButton.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                }
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

    private fun setKeyboardHide() {
        binding.root.setOnTouchListener { _, event ->
            val inputMethodManager : InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}