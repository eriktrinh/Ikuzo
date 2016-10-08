package com.eriktrinh.ikuzo.ui.auth

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.ani.Tokens
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.callback.AuthCallbacks
import com.eriktrinh.ikuzo.web.service.AuthService
import kotlinx.android.synthetic.main.controller_login_view.view.*
import retrofit2.Call

class LoginController(args: Bundle?) : Controller(args) {


    companion object {
        private val KEY_URI = "ARGS_URI"
        private val TAG = "LoginController"

        private fun Bundle.putUri(uri: Uri): Bundle {
            putParcelable(KEY_URI, uri)
            return this
        }
    }

    constructor(uri: Uri) : this(Bundle().putUri(uri))

    lateinit var webView: WebView
    private var uri: Uri

    init {
        uri = args?.getParcelable(KEY_URI) ?: Uri.EMPTY
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_login_view, container, false)

        webView = view.controller_login_web_view

        @Suppress webView.settings.javaScriptEnabled = true
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                val activity = activity as AppCompatActivity
                activity.supportActionBar?.subtitle = title
            }
        })
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (request != null && request.url.path.contains(AuthService.REDIRECT_URI)) {
                    val code = request.url.getQueryParameter("code")
                    if (code != null) {
                        Log.i(TAG, "Got authorization code: $code")
                        setTokens(code)
                    }
                    return true
                }
                return false
            }
        })
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.domStorageEnabled = true
        webView.settings.saveFormData = false
        CookieManager.getInstance().removeAllCookies { webView.loadUrl(uri.toString()) }

        return view
    }

    override fun handleBack(): Boolean {
        if (webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.handleBack()
    }

    private fun setTokens(code: String) {
        val authService: AuthService = ServiceGenerator.createService(AuthService::class.java)
        val call: Call<Tokens> = authService.accessTokenByCode(code)
        call.enqueue(AuthCallbacks(activity, activity as AuthCallbacks.Delegate))
    }
}