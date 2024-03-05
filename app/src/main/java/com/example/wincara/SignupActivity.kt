package com.example.wincara

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        // Initialize database helper
        dbHelper = DBHelper(this)

        // Find views by their IDs
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnSignup = findViewById(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Save credentials to SQLite database
                saveCredentials(username, password)

                // Navigate to LoginActivity after signup
                navigateToLoginActivity()
            }
        }
    }

    private fun saveCredentials(username: String, password: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(UserContract.UserEntry.COLUMN_NAME_USERNAME, username)
            put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, password)
        }
        db.insert(UserContract.UserEntry.TABLE_NAME, null, values)
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finish SignupActivity to prevent going back when pressing back button
    }

    // Inner class for database helper
    class DBHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Not needed for this example
        }

        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "UserCredentials.db"

            private const val SQL_CREATE_ENTRIES = """
                CREATE TABLE ${UserContract.UserEntry.TABLE_NAME} (
                    ${UserContract.UserEntry._ID} INTEGER PRIMARY KEY,
                    ${UserContract.UserEntry.COLUMN_NAME_USERNAME} TEXT,
                    ${UserContract.UserEntry.COLUMN_NAME_PASSWORD} TEXT
                )
            """


        }
    }
}
