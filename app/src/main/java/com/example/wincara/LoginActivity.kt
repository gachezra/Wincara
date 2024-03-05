package com.example.wincara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var dbHelper: SignupActivity.DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Initialize database helper
        dbHelper = SignupActivity.DBHelper(this)

        // Find views
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // Retrieve stored credentials from the SQLite database
            val storedCredentials = readStoredCredentials(username)

            if (storedCredentials != null && storedCredentials.second == password) {
                // Credentials match, proceed to welcome activity
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Credentials don't match, show error message
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readStoredCredentials(username: String): Pair<String, String>? {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            UserContract.UserEntry.COLUMN_NAME_PASSWORD
        )

        val selection = "${UserContract.UserEntry.COLUMN_NAME_USERNAME} = ?"
        val selectionArgs = arrayOf(username)

        val cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToNext()) {
            val passwordIndex = cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_PASSWORD)
            val storedPassword = cursor.getString(passwordIndex)
            Pair(username, storedPassword)
        } else {
            null
        }
    }
}
