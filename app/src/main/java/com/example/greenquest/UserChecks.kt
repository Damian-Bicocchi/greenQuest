package com.example.greenquest

object UserChecks {

    fun isValidUsername(username: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9._-]+$")


        return regex.matches(username)
    }

    fun isValidUsernameCharacterLength(username: String): Boolean {
        return username.length in 3..20
    }
    fun isValidPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z]*)(?=.*[A-Z]).{8,}$")
        return regex.matches(password)
    }

    fun isValidPasswordConfirmation(password: String, confirm: String): Boolean {
        return password == confirm
    }

    fun isFieldsFilled(vararg campos: String): Boolean {
        for (campo in campos) {
            if (campo.isBlank()) {
                return false
            }
        }
        return true
    }
}