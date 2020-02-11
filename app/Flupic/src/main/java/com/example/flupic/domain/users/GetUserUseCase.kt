package com.example.flupic.domain.users

import com.example.flupic.data.users.UsersRepository
import com.example.flupic.domain.UseCase
import com.example.flupic.model.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) : UseCase<Unit, User>(){

    //TODO change to get profile with changing array of subs

    override suspend fun execute(parameters: Unit): User {
        return usersRepository.getUser(false)
    }
}

class ForceUpdateUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) : UseCase<Unit, User>(){

    override suspend fun execute(parameters: Unit): User {
        return usersRepository.getUser(true)
    }
}