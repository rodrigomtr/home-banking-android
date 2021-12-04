package com.example.rodrigobrodrigues.accentureHomeBanking.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.Window

import com.example.rodrigobrodrigues.accentureHomeBanking.AbstractAsyncActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.dialogs.FailureDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.dialogs.SuccessDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.login.LoginActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.MatrixErrorDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.menu.PaymentsActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.models.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * Class that contains the app main page and main menu
 */
class MainActivity : AbstractAsyncActivity(), MainContract.View, NavigationView.OnNavigationItemSelectedListener, MatrixErrorDialog.MatrixErrorDialogListener {

    private var presenter: MainContract.Presenter? = null

    private val currentUser: User? = App.instance!!.user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        content_id.text = currentUser?.client?.firstName
        content_content.text = currentUser?.client?.firstName

        val sharedPreferences = getSharedPreferences(
                "com.example.rodrigobrodrigues.accentureHomeBanking",
                Context.MODE_PRIVATE)

        presenter = MainPresenter(this, sharedPreferences)
    }

    override fun onResume() {
        super.onResume()
        sendRegistrationToServer()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            showSignOutDialog()
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
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when (id) {
            R.id.nav_payments -> presenter?.checkMatrix()
            R.id.nav_signout -> showSignOutDialog()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showSignOutDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(R.string.sign_out_message)
                .setTitle(R.string.sign_out_title)
                .setPositiveButton(R.string.sign_out) { _, _ -> startActivity(Intent(this@MainActivity, LoginActivity::class.java)) }
                .setNegativeButton(R.string.cancel) { _, _ -> }
        dialog.create()
        dialog.show()
    }

    /**
     * Method that sends to server the refreshedToken
     * just in case that the App is running for the first time in the current device
     */
    private fun sendRegistrationToServer() {
        presenter?.sendNotificationToken()
    }

    override fun onCreateMatrixClick() {
        presenter?.createMatrix()
    }

    override fun startPayments() {
        startActivity(Intent(this@MainActivity, PaymentsActivity::class.java))
    }

    override fun showMatrixError() {
        val frag = MatrixErrorDialog()
        frag.show(supportFragmentManager, "MATRIX_ERROR")
    }

    override fun showMatrixCreatedDialog() {
        val frag = SuccessDialog.newInstance(getString(R.string.created_matrix),
                getString(R.string.verify_email))
        frag.show(supportFragmentManager, "MATRIX_SUCCESS")
    }

    override fun setError() {
        val failureDialog = FailureDialog.newInstance(getString(R.string.error_title),
                getString(R.string.error_matrix_create_subtitle) + "\n" +
                        getString(R.string.error_try_again))
        failureDialog.show(supportFragmentManager, "MAIN")
    }
}
