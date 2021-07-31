package how.about.it.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import how.about.it.R
import how.about.it.database.SharedManager
import how.about.it.database.User
import how.about.it.databinding.FragmentEmailLoginBinding
import how.about.it.model.RequestLogin
import how.about.it.model.ResponseLogin
import how.about.it.network.RequestToServer
import how.about.it.view.ToolbarActivity
import how.about.it.view.main.MainActivity
import retrofit2.Callback
import retrofit2.Response

class EmailLoginFragment : Fragment() {
    private var _binding : FragmentEmailLoginBinding?= null
    private val binding get() = _binding!!
    private val sharedManager : SharedManager by lazy { SharedManager(requireContext()) }


    val _loginActivity = activity

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailLoginBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        // val requestToServer = RequestToServer

        binding.toolbarLoginBoard.tvToolbarTitle.text = "로그인"
        (activity as LoginActivity).setSupportActionBar(binding.toolbarLoginBoard.toolbarBoard)
        (activity as LoginActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as LoginActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.btnLoginEmail.setOnClickListener {
            if (binding.etLoginEmailId.text.isNullOrBlank() || binding.etLoginEmailPassword.text.isNullOrBlank()) {
                Toast.makeText(activity, "이메일과 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 임시 더미데이터 저장
                val currentUser = User().apply {
                    accessToken = "Temp-AccessToken"
                    refreshToken = "Temp-RefreshToken"
                    nickname = "닉네임부분"
                }
                sharedManager.saveCurrentUser(currentUser)
                val loginIntent = Intent(activity, MainActivity::class.java)
                startActivity(loginIntent)
                (activity as LoginActivity).finish() // 로그인 액티비티 종료

                /* 서버와 연동하는 부분
                // 로그인 요청
                requestToServer.service.requestLogin(
                    RequestLogin(
                        userId = binding.etLoginEmailId.text.toString(),
                        password = binding.etLoginEmailPassword.text.toString()
                    )   //로그인 정보를 전달
                ).enqueue(object : Callback<ResponseLogin> {
                    override fun onFailure(call: retrofit2.Call<ResponseLogin>, t: Throwable) {
                        Log.d("통신 실패", "${t.message}")
                    }

                    override fun onResponse(
                        call: retrofit2.Call<ResponseLogin>,
                        response: Response<ResponseLogin>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()!!.success) {
                                Log.d("성공", response.body()!!.token.toString())
                                // 로그인 성공 후 토큰 저장 및 화면 전환
                                val currentUser = User().apply {
                                    accessToken = response.body()!!.token!!.accessToken
                                    refreshToken = response.body()!!.token!!.refreshToken
                                }
                                sharedManager.saveCurrentUser(currentUser) // SharedPreference에 저장

                                val loginIntent = Intent(activity, MainActivity::class.java)
                                startActivity(loginIntent)
                            } else {
                                Log.d("실패", "실패")
                                Toast.makeText(activity, "이메일과 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                })*/
            }
        }

            binding.tvMessagePasswordReset.setOnClickListener {
                // TODO : 이메일 계정 패스워드 분실 시 패스워드 초기화 하는 Fragment로 이동하는 코드 작성
            }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}