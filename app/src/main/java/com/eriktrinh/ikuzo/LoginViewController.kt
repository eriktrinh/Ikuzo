package com.eriktrinh.ikuzo

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.oauth.AuthCallbacks
import com.eriktrinh.ikuzo.oauth.AuthService
import com.eriktrinh.ikuzo.oauth.Tokens
import com.eriktrinh.ikuzo.web.ServiceGenerator
import kotlinx.android.synthetic.main.controller_login_view.view.*
import retrofit2.Call

class LoginViewController(args: Bundle?) : Controller(args) {

    lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var uri: Uri

    companion object {
        private val KEY_URI = "URI"
        private val TAG = "LoginViewController"

        fun Bundle.putUri(uri: Uri): Bundle {
            putParcelable(KEY_URI, uri)
            return this
        }
    }

    constructor(uri: Uri) : this(Bundle().putUri(uri))

    init {
        uri = args?.getParcelable(KEY_URI) ?: Uri.EMPTY
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_login_view, container, false)

        webView = view.controller_login_web_view
        progressBar = view.controller_login_progress_bar

        progressBar.max = 100

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
        webView.loadUrl(uri.toString())

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
        call.enqueue(AuthCallbacks(activity, activity as AuthCallbacks.Callbacks))
    }
}