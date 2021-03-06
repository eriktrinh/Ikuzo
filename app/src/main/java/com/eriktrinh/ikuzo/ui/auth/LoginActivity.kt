package com.eriktrinh.ikuzo.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.callback.AuthObserver
import com.eriktrinh.ikuzo.web.service.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), AuthObserver.Delegate {
    override fun onAuthenticationFailure() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAuthenticated() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private lateinit var router: Router

    companion object {
        private val uri: Uri = Uri.parse(ServiceGenerator.BASE_URL)
                .buildUpon()
                .appendPath("auth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", AuthService.CLIENT_ID)
                .appendQueryParameter("redirect_uri", AuthService.REDIRECT_URI)
                .appendQueryParameter("response_type", "code")
                .build()

        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        router = Conductor.attachRouter(this, controller_container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(LoginController(uri)))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
