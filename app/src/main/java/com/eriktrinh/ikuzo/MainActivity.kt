package com.eriktrinh.ikuzo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.eriktrinh.ikuzo.ui.LoginActivity
import com.eriktrinh.ikuzo.ui.SeriesController
import com.eriktrinh.ikuzo.ui.UnauthenticatedController
import com.eriktrinh.ikuzo.utils.AuthUtils
import com.eriktrinh.ikuzo.utils.MeUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private val TAG = "MainActivity"
        private val REQUEST_LOGIN = 0
    }

    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = drawer_layout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        router = Conductor.attachRouter(this, content_main, savedInstanceState)

        reinitHeaderAndControllers()
    }

    private fun setNavHeaderTitle(title: String) {
        val navHeader = drawer_layout.nav_view.getHeaderView(0)
        navHeader.nav_header_title.text = title
        navHeader.invalidate()
    }

    private fun loadUserImage(url: String) {
        val navHeader = drawer_layout.nav_view.getHeaderView(0)
        Picasso.with(this)
                .load(url)
                .fit()
                .centerInside()
                .into(navHeader.imageView)
    }

    private fun reinitHeaderAndControllers() {
        val imageUrl = MeUtils.getMyImageUrl(this)
        if (imageUrl != null) loadUserImage(imageUrl)
        setNavHeaderTitle(MeUtils.getMyDisplayName(this) ?: "Login")

        router.setRoot(RouterTransaction.with(SeriesController()))
        drawer_layout.nav_view.setCheckedItem(R.id.nav_browse)
        if (!AuthUtils.isAuthorized(this)) {
            showUnauthenticatedScreen()
            showLogin()
        } else {
//            AuthUtils.setMe(this, { recreate() }, { showLogin() })
        }
    }

    override fun onBackPressed() {
        val drawer = drawer_layout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        when (item.itemId) {
            R.id.action_settings -> {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        if (!AuthUtils.isAuthorized(this)) {
            return true
        }
        val drawer = drawer_layout
        when (item.itemId) {
            R.id.nav_browse -> {
                if (!item.isChecked) {
                    router.setRoot(RouterTransaction.with(SeriesController()))
                }
            }
            R.id.nav_slideshow -> {
            }
            R.id.nav_manage -> {
            }
            R.id.nav_share -> {
            }
            R.id.nav_send -> {
            }
            R.id.nav_logout -> {
                AuthUtils.clearPreferences(this)
                showUnauthenticatedScreen()
                showLogin()
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            Log.i(TAG, "onActivityResult Cancelled")
            return
        }
        when (requestCode) {
            REQUEST_LOGIN -> {
                Log.i(TAG, "LoginActivity OK")
                router.setRoot(RouterTransaction.with(SeriesController()))
                AuthUtils.setMe(this, { reinitHeaderAndControllers() }, { AuthUtils.clearPreferences(this); showUnauthenticatedScreen(); showLogin() })
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showUnauthenticatedScreen() {
        drawer_layout.closeDrawer(GravityCompat.START)
        router.setRoot(RouterTransaction.with(UnauthenticatedController()))
    }

    private fun showLogin() {
        val i = LoginActivity.newIntent(this)
        startActivityForResult(i, REQUEST_LOGIN)
    }
}