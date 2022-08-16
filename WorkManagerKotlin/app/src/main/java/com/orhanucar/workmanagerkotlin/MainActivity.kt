package com.orhanucar.workmanagerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = Data.Builder().putInt("intKey",1).build()
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .build()

        /*
        * One Time Request
        * val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
        *     .setConstraints(constraints)
        *     .setInputData(data)
        *     //.setInitialDelay(5,TimeUnit.SECONDS)
        *     //.addTag("myTag")
        *     .build()

        * WorkManager.getInstance(this).enqueue(myWorkRequest)

         */

        // Periodic Request
        val myWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDatabase>(15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this,
            Observer {
                if (it.state == WorkInfo.State.RUNNING) {
                    println("running")

                } else if (it.state == WorkInfo.State.FAILED) {
                    println("failed")

                } else if (it.state == WorkInfo.State.SUCCEEDED) {
                    println("succeeded")
                }
            })

        //WorkManager.getInstance(this).cancelAllWork()

        /*   Chaining - One Time Request

        val oneTimeWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .enqueue()

         */
    }
}