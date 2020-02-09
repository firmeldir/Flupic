package com.example.flupic.util.exception

import androidx.annotation.StringRes

class ResourceableException(@StringRes val stringResourceId: Int, message: String? = null) : Exception(message)