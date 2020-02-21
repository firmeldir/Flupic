package com.example.flupic.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flupic.result.Result
import kotlinx.coroutines.*


/**
 * Executes business logic synchronously or asynchronously using a [Scheduler].
 */
abstract class UseCase<in P, R> {

    companion object{
        private const val TAG = "TAG UseCase"
    }

    /** Executes the use case asynchronously and places the [Result] in a MutableLiveData
     *
     * @param parameters the input parameters to run the use case with
     * @param result the MutableLiveData where the result is posted to
     *
     */
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

    /** Executes the use case asynchronously and returnsx a [Result] in a new LiveData object.
     *
     * @return an observable [LiveData] with a [Result].
     *
     * @param parameters the input parameters to run the use case with
     */
    operator fun invoke(parameters: P, scope: CoroutineScope = GlobalScope): LiveData<Result<R>> {
        val liveCallback: MutableLiveData<Result<R>> = MutableLiveData()
        this(parameters, liveCallback, scope)
        return liveCallback
    }

    /** Executes the use case synchronously  */
//    fun executeNow(parameters: P): Result<R> {
//        return try {
//            Result.Success(execute(parameters))
//        } catch (e: Exception) {
//            Result.Error(e)
//        }
//    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}

operator fun <R> UseCase<Unit, R>.invoke(scope: CoroutineScope = GlobalScope): LiveData<Result<R>> = this(Unit, scope)
operator fun <R> UseCase<Unit, R>.invoke(result: MutableLiveData<Result<R>>, scope: CoroutineScope = GlobalScope) = this(Unit, result, scope)

