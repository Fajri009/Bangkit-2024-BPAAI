package com.example.bangkit_2024_bpaai.Firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.bangkit_2024_bpaai.R
import com.example.bangkit_2024_bpaai.databinding.ActivityLoginFirebaseBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.*
import kotlinx.coroutines.launch

class LoginFirebaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginFirebaseBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

    // agar tampil sebuah UI untuk memilih opsi akun login ketika pengguna mengeklik tombol
    private fun signIn() {
        val credentialManager = CredentialManager.create(this) //import from androidx.CredentialManager

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // untuk hanya menampilkan akun dari pengguna yang sudah terlebih dahulu terotorisasi dengan aplikasi
            .setServerClientId(getString(R.string.your_web_client_id)) // untuk proses autentikasi dengan proyek Firebase
            .build()

        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                // untuk menampilkan akun pengguna
                val result: GetCredentialResponse = credentialManager.getCredential( //import from androidx.CredentialManager
                    request = request,
                    context = this@LoginFirebaseActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Process Login dengan Firebase Auth
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@LoginFirebaseActivity, MainFirebase::class.java))
            finish()
        }
    }

    // untuk memeriksa status login setiap kali membuka halaman login
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}