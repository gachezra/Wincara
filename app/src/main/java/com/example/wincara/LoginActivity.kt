package com.example.wincara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Find views
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // Retrieve stored credentials from signup file
            val storedCredentials = readStoredCredentials()

            if (storedCredentials != null && storedCredentials.first == username && storedCredentials.second == password) {
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

    private fun readStoredCredentials(): Pair<String, String>? {
        return try {
            val fileInputStream: FileInputStream = openFileInput("credentials.txt")
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val username = bufferedReader.readLine().removePrefix("Username: ")
            val password = bufferedReader.readLine().removePrefix("Password: ")
            bufferedReader.close()
            Pair(username, password)
        } catch (e: Exception) {
            null
        }
    }
}
