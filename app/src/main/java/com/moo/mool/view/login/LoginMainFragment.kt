package com.moo.mool.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.moo.mool.R
import com.moo.mool.databinding.FragmentLoginMainBinding
import com.moo.mool.model.ResponseLogin
import com.moo.mool.util.navigate
import com.moo.mool.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginMainFragment : Fragment() {
    private var _binding: FragmentLoginMainBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginMainBinding.inflate(inflater, container, false)
        setGoogleLogin()
        setEmailLoginClickListener()
        setEmailSignupClickListener()
        return binding.root
    }

    private fun setEmailLoginClickListener() {
        binding.btnEmailLogin.setOnClickListener {
            navigate(R.id.action_loginMainFragment_to_emailLoginFragment)
        }
    }
    private fun setEmailSignupClickListener() {
        binding.tvSignup.setOnClickListener {
            navigate(R.id.action_loginMainFragment_to_emailSignupSetIDFragment)
        }
    }

    private fun setGoogleLogin() {
        object : WebChromeClient() {
            override fun onConsoleMessage(cmsg: ConsoleMessage): Boolean {
                val googleLoginWebviewResponseHtml = cmsg.message().toString()
                val startEmailIndex = googleLoginWebviewResponseHtml.indexOf("\"email\":\"")
                val startNameIndex = googleLoginWebviewResponseHtml.indexOf("\"name\":\"")
                val startAccessTokenIndex = googleLoginWebviewResponseHtml.indexOf("\"accessToken\":\"")
                val startRefreshTokenIndex = googleLoginWebviewResponseHtml.indexOf("\"refreshToken\":\"")

                val googleEmail = googleLoginWebviewResponseHtml.substring(startEmailIndex+9, startNameIndex-2)
                val googleNickname = googleLoginWebviewResponseHtml.substring(startNameIndex+8, startAccessTokenIndex-2)
                val googleAccessToken = googleLoginWebviewResponseHtml.substring(startAccessTokenIndex+15, startRefreshTokenIndex-2)
                val googleRefreshToken = googleLoginWebviewResponseHtml.substring(startRefreshTokenIndex+16, googleLoginWebviewResponseHtml.length-2)

                loginViewModel.setGoogleLoginResponse(
                    ResponseLogin(googleEmail, googleNickname, googleAccessToken, googleRefreshToken)
                )

                navigate(R.id.action_loginMainFragment_to_mainActivity)
                binding.webviewGoogleLogin.clearHistory()
                CookieManager.getInstance().removeAllCookies(null)
                (activity as LoginActivity).finish()

                return true
            }
        }.also { webviewParsing ->
            binding.webviewGoogleLogin.webChromeClient = webviewParsing }

        val mWebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if(Pattern.matches("^(https://moomool.shop/login/oauth2/code/google)(.*)", request?.url.toString().trim())){
                    binding.webviewGoogleLogin.visibility = View.GONE
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                view?.loadUrl("javascript:console.log(document.body.getElementsByTagName('pre')[0].innerHTML);");
                super.onPageFinished(view, url)
            }
        }

        binding.layoutGoogleLogin.setOnClickListener {
            with(binding.webviewGoogleLogin){
                apply {
                    webViewClient = mWebViewClient
                    settings.javaScriptEnabled = true
                    settings.userAgentString = "Mozilla/5.0 AppleWebKit/535.19 Chrome/56.0.0 Mobile Safari/535.19"
                    loadUrl("https://moomool.shop/oauth2/authorization/google")
                    this.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}