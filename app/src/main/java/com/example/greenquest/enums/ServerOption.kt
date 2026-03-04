package com.example.greenquest.enums

import android.content.Context
import com.example.greenquest.R

enum class ServerOption {
    DEFAULT,
    CUSTOM;

    fun getString(context: Context): String = when (this) {
        DEFAULT -> context.getString(R.string.default_server)
        CUSTOM  -> context.getString(R.string.custom_server)
    }
}