package com.example.flupic.domain.auth

import com.example.flupic.data.auth.AuthUserDataSource
import com.example.flupic.data.auth.BasicUserInfo
import com.example.flupic.domain.MediatorUseCase
import com.example.flupic.result.Result
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Some enterprise auth logic : subscriptions, notifications etc
 */

@Singleton
class ObserveAuthUserUseCase @Inject constructor(
    private val authUserDataSource: AuthUserDataSource
) : MediatorUseCase<Unit, BasicUserInfo>(){

    override fun execute(parameters: Unit) {
        authUserDataSource.startListening()
    }

    init {
        result.addSource(authUserDataSource.getObservableBasicUserInfo()){
            if(it is Result.Success) result.postValue(Result.Success(it.data))
            else if(it is Result.Error)
                result.postValue(Result.Error(Exception("Authentication Error")))
        }
    }
}