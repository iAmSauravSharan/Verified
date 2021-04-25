package com.coronawarriors.verified.ui.form

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.coronawarriors.verified.R
import com.coronawarriors.verified.data.DataRepository
import com.coronawarriors.verified.databinding.ActivityFormBinding
import com.coronawarriors.verified.enums.FormType
import com.coronawarriors.verified.enums.FormType.ADD_FORM
import com.coronawarriors.verified.enums.FormType.SEARCH_FORM
import com.coronawarriors.verified.ui.details.DetailsActivity
import com.coronawarriors.verified.util.Constants.FORM_TYPE
import com.coronawarriors.verified.util.Constants.RC_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FormActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mBinding: ActivityFormBinding
    private val mViewModel: FormViewModel by viewModels()
    @Inject lateinit var repository: DataRepository
    private lateinit var auth: FirebaseAuth


    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        getIntentData()
        setFormLayout()
    }

    private fun setFormLayout() {
        mBinding.vgUploadDetails.visibility =
            if (mViewModel.getFormType() == SEARCH_FORM) View.GONE else View.VISIBLE

        if (!repository.isLoggedIn) {
            val mySnackbar = Snackbar.make(
                mBinding.svMain,
                R.string.want_to_contribute, Snackbar.LENGTH_SHORT
            )
            mySnackbar.setAction(R.string.login) {
                loginListener()
            }
            mySnackbar.show()
        }
    }

    private fun loginListener() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
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
                        val intent = Intent(this, FormActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Toast.makeText(this, resources.getString(R.string.login_successful),
                    Toast.LENGTH_SHORT).show()
                repository.accountId = account.id
                repository.accountIdToken = account.idToken
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, resources.getString(R.string.signin_error),
                    Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getIntentData() {
        if (intent != null && intent.hasExtra(FORM_TYPE))
        mViewModel.setFormType(intent.getSerializableExtra(FORM_TYPE) as FormType)
        mBinding.tvChangeCountry.text = String.format("%s %s%s",
            resources.getString(R.string.not_from),
            repository.country?.toLowerCase(),
            resources.getString(R.string.change_country)
        )
        mBinding.tvProceed.text =
            when {
                mViewModel.getFormType() == SEARCH_FORM -> resources.getString(R.string.search)
                mViewModel.getFormType() == ADD_FORM -> resources.getString(R.string.upload)
                else -> resources.getString(R.string.update)
            }
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_form)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
    }

    private fun launchDetailsPage() {
        startActivity(Intent(this, DetailsActivity::class.java))
        finish()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, resources.getString(R.string.signin_error),
            Toast.LENGTH_SHORT).show()
    }
}