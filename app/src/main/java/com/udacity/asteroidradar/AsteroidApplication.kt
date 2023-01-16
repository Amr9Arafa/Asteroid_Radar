package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.work.DeletePreviousListAsteroidWorker
import com.udacity.asteroidradar.work.DeleteTodayAsteroidWorker
import com.udacity.asteroidradar.work.RefreshAsteroidListWorker
import com.udacity.asteroidradar.work.RefreshTodayASteroidWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        val repeatingRefreshListRequest =
            PeriodicWorkRequestBuilder<RefreshAsteroidListWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshAsteroidListWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRefreshListRequest
        )


        val repeatingRefreshTodayAsteroidRequest =
            PeriodicWorkRequestBuilder<RefreshTodayASteroidWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshTodayASteroidWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRefreshTodayAsteroidRequest
        )


        val repeatingDeleteAsteroidListRequest =
            PeriodicWorkRequestBuilder<DeletePreviousListAsteroidWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            DeletePreviousListAsteroidWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingDeleteAsteroidListRequest
        )


        val repeatingDeleteTodayAsteroidRequest =
            PeriodicWorkRequestBuilder<DeleteTodayAsteroidWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            DeleteTodayAsteroidWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingDeleteTodayAsteroidRequest
        )
    }

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */


    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }
}