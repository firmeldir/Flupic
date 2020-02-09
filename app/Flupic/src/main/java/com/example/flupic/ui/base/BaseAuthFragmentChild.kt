package com.example.flupic.ui.base

import androidx.fragment.app.Fragment
import com.example.flupic.ui.auth.AuthFragment
import java.lang.IllegalStateException

abstract class BaseAuthFragmentChild : Fragment(){

    protected val authFragment : AuthFragment
        get() = parentFragment?.parentFragment as? AuthFragment
            ?: throw IllegalStateException("Cannot use this fragment without the auth fragment")
}