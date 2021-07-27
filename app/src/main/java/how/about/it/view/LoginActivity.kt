package how.about.it.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import how.about.it.R
import how.about.it.databinding.ActivityLoginBinding
import how.about.it.model.UserProfile


class LoginActivity : AppCompatActivity() {
    // Google Sign In API와 호출할 구글 로그인 클라이언트
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val TAG ="LoginActivity"

    private lateinit var loginViewBinding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        loginViewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginViewBinding.root)

        // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다.
        // DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // Your server's client ID, not your Android client ID.
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val gsa = GoogleSignIn.getLastSignedInAccount(this@LoginActivity)
        if (gsa != null && gsa.id != null) { // 이미 로그인 한 경우 토큰으로 로그인 처리
            Log.d(TAG,"already Login")
            mGoogleSignInClient.silentSignIn().addOnCompleteListener(this, OnCompleteListener<GoogleSignInAccount?> { task -> handleSignInResult(task) })
        }

        loginViewBinding.btnGoogleLogin.setOnClickListener {
            GoogleAccountSignIn()
        }
        loginViewBinding.btnGoogleLogout.setOnClickListener {
            GoogleAccountsignOut()
        }
        loginViewBinding.btnEmailLogin.setOnClickListener {
            EmailAccountSignIn()
        }
    }

    var googleSigninResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> val data: Intent? = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        handleSignInResult(task)
    }
    private fun GoogleAccountSignIn() {
        // 로그인이 되어있지 않은 경우 (앱에 연동되어있지 않은 경우)
            val googleSignInIntent = mGoogleSignInClient.signInIntent
            googleSigninResultLauncher.launch(googleSignInIntent)
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account : GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)

            if (account != null) {
                // Signed in successfully, show authenticated UI.
                val loginIntent = Intent(this@LoginActivity, MainActivity::class.java)
                loginIntent.putExtra("idToken", account.idToken) // 메인으로 넘겨줄 데이터 (아이디 토큰)
                startActivity(loginIntent)

                // TODO : 백엔드 서버에 ID Token 전송 후 인증
                //RetrofitBuilder.api.userProfileRequest(RequestLogin).enqueue(object : Callback<ResponseLogin>)
/*
                val idToken = account.idToken
                val personId = account.id
                val personName = account.displayName
                val personEmail = account.email
                val personPhoto: Uri? = account.photoUrl

                val userProfile = UserProfile(
                    "$personId",
                    "$idToken",
                    "$personEmail",
                    "$personName",
                    "$personPhoto"
                )
                Log.d(TAG, "handleSignInResult:personName $personName")
                Log.d(TAG, "handleSignInResult:personEmail $personEmail")
                Log.d(TAG, "handleSignInResult:personId $personId")
                Log.d(TAG, "handleSignInResult:personPhoto $personPhoto") */
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
            // updateUI(null)
        }
        finish()
    }

    private fun EmailAccountSignIn() {
        // Email로 로그인하는 Fragment로 이동
        val emailLoginFragment = EmailLoginFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout_login_email_fragment, emailLoginFragment)
        transaction.commit()
    }

    private fun GoogleAccountsignOut() { // 단순히 구글 계정 로그아웃 하려는 경우, 앱에 연결된 계정 삭제
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                Log.d(TAG, "LOGOUT GOOGLE")
            }
    }
    private fun revokeAccess() { // 사용자가 회원 삭제를 요청하는 경우, 앱이 Google API에서 얻은 정보 삭제
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                // Update your UI here
            }
    }


}