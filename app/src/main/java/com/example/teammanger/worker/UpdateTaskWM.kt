package com.example.teammanger.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.domain.repositories.Repository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UpdateTaskWM @Inject constructor(
    @ApplicationContext private val context: Context,
    private val params: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val res = repository.updateTaskStatue(
            params.inputData.getString("due") ?: "",
            params.inputData.getBoolean("statue", false)
        )
        return when (res) {
            is com.example.domain.util.Result.Failure -> {
                Result.retry()
            }

            is com.example.domain.util.Result.Success -> {
                Result.success()
            }
        }
    }
}