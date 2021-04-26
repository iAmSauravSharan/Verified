package com.coronawarriors.verified.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.coronawarriors.verified.R
import com.coronawarriors.verified.data.DataRepository
import com.coronawarriors.verified.databinding.ActivitySettingsBinding
import com.coronawarriors.verified.enums.SettingsUIClickListener.*
import com.coronawarriors.verified.ui.splash.SplashActivity
import com.coronawarriors.verified.util.Constants
import com.coronawarriors.verified.util.Utilities
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var repository: DataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        updateLoginUI(repository.isLoggedIn, false)
        subscribeToClickListener()
    }

    private fun subscribeToClickListener() {
        viewModel.clickListener.observe(this, Observer {
            when (it) {
                LOGIN -> loginListener()
                LOGOUT -> updateLoginUI(false)
                CONTACT_US -> Utilities.sendContactUsIntent(this)
                SHARE_US -> Utilities.sendShareIntent(this)
            }
        })
    }

    private fun updateLoginUI(isLoggedIn: Boolean, redirect: Boolean = true) {
        binding.tvLogIn.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                if (isLoggedIn)
                    R.drawable.red_border_round_rectangle
                else R.drawable.blue_border_round_rectangle
            )
        )
        binding.tvLogIn.text =
            resources.getString(if (!isLoggedIn) R.string.login else R.string.log_out)
        binding.tvLogIn.setTextColor(
            ContextCompat.getColor(
                this,
                if (isLoggedIn) R.color.red else R.color.blue
            )
        )
        binding.tvLoginMessage.text =
            if (isLoggedIn)
                (String.format(
                    "%s, %s",
                    resources.getString(R.string.welcome),
                    repository.userName
                ))
            else resources.getString(R.string.want_to_contribute)
        if (redirect) redirectToSplashScreen()
    }

    private fun redirectToSplashScreen() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initialize() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.viewModel = viewModel

        binding.include.ivBack.setOnClickListener { onBackPressed() }
        binding.include.tvScreenName.text = resources.getString(R.string.settings)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
    }

    private fun loginListener() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(
                        this, resources.getString(R.string.login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this, resources.getString(R.string.login_successful),
                        Toast.LENGTH_SHORT
                    ).show()
                    repository.isLoggedIn = true
                    repository.userName = acct?.displayName
                    updateLoginUI(true)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Toast.makeText(
                    this, resources.getString(R.string.login_successful),
                    Toast.LENGTH_SHORT
                ).show()
                repository.accountId = account.id
                repository.accountIdToken = account.idToken
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(
                    this, resources.getString(R.string.signin_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}