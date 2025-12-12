package com.example.greenquest.apiParameters

data class ApiError(
    val username: List<String>? = null,
    val password: List<String>? = null,
    val non_field_errors: List<String>? = null
)
