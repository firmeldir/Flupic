package com.example.flupic.domain.auth

import com.example.flupic.data.auth.AuthUserDataSource
import com.example.flupic.domain.UseCase
import javax.inject.Inject

class AuthenticationCompletedUseCase @Inject constructor(
    private val authUserDataSource: AuthUserDataSource
) : UseCase<Unit, Boolean>(){

    override suspend fun execute(parameters: Unit): Boolean {
        val basicUserInfo = authUserDataSource.getBasicUserInfo()
        return basicUserInfo.isSignedIn()
    }
}