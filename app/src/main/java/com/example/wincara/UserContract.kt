package com.example.wincara

import android.provider.BaseColumns

object UserContract {
    // Inner class that defines the table contents
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "users"
            const val _ID = BaseColumns._ID
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_PASSWORD = "password"
        }
    }
}
