package com.example.flupic.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flupic.result.Result
import kotlinx.coroutines.*

/**
 *
 * @param parameters the input parameters to run the use case with
 * @param result the MutableLiveData where the result is posted to
 *
 */

abstract class UseCase<in P, R> {

    operator fun invoke(parameters: P, result: MutableLiveData<Result<R>>, scope: CoroutineScope = GlobalScope) {
        result.value = Result.Loading //TODO: add data to Result.Loading to avoid glitches
        try {
            scope.launch { withContext(Dispatchers.Default) {
                try {

                    result.postValue(Result.Success(execute(parameters)))

                } catch (e: Exception) {

                    Log.e(TAG, e.message.toString())
                    result.postValue(Result.Error(e))
                }
            }
            }
        } catch (e: Exception) {

            Log.e(TAG, e.message.toString())
            result.postValue(Result.Error(e))
        }
    }

    operator fun invoke(parameters: P, scope: CoroutineScope = GlobalScope): LiveData<Result<R>> {
        val liveCallback: MutableLiveData<Result<R>> = MutableLiveData()
        this(parameters, liveCallback, scope)
        return liveCallback
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R


    companion object{
        private const val TAG = "TAG UseCase"
    }
}

operator fun <R> UseCase<Unit, R>.invoke(scope: CoroutineScope = GlobalScope): LiveData<Result<R>> = this(Unit, scope)
operator fun <R> UseCase<Unit, R>.invoke(result: MutableLiveData<Result<R>>, scope: CoroutineScope = GlobalScope) = this(Unit, result, scope)

