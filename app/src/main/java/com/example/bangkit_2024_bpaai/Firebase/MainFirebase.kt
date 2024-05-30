package com.example.bangkit_2024_bpaai.Firebase

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bangkit_2024_bpaai.Firebase.adapter.FirebaseMessageAdapter
import com.example.bangkit_2024_bpaai.Firebase.data.Message
import com.example.bangkit_2024_bpaai.R
import com.example.bangkit_2024_bpaai.databinding.ActivityMainFirebaseBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import java.util.Date

class MainFirebase : AppCompatActivity() {
    private lateinit var binding: ActivityMainFirebaseBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: FirebaseMessageAdapter
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Logout dari akun Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this, LoginFirebaseActivity::class.java))
            finish()
            return
        }

        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        sendMessage(firebaseUser, messagesRef)

        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messagesRef, Message::class.java) // menentukan data apa yang diambil dengan menggunakan DatabaseReference seperti yang kita buat sebelumnya
            .build()
        adapter = FirebaseMessageAdapter(options, firebaseUser.displayName)
        binding.messageRecyclerView.adapter = adapter

        if (Build.VERSION.SDK_INT >= 33) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu_firebase, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }
    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    private fun signOut() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@MainFirebase)

            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@MainFirebase, LoginFirebaseActivity::class.java))
            finish()
        }
    }

    private fun sendMessage(firebaseUser: FirebaseUser, messagesRef: DatabaseReference) {
        binding.sendButton.setOnClickListener {
            val friendlyMessage = Message(
                binding.messageEditText.text.toString(),
                firebaseUser.displayName.toString(),
                firebaseUser.photoUrl.toString(),
                Date().time
            )
            // fungsi push digunakan untuk membuat auto-generated key di dalam child messages
            messagesRef.push().setValue(friendlyMessage) { error, _ ->
                if (error != null) {
                    Toast.makeText(this, getString(R.string.send_error) + error.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, getString(R.string.send_success), Toast.LENGTH_SHORT).show()
                }
            }
            binding.messageEditText.setText("")
        }
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    companion object {
        const val MESSAGES_CHILD = "messages"
    }
}