/*
 * Copyright (c) 2021 and later Renault S.A.S. / Nissan Motor Company, Limited
 * Developed by Renault S.A.S. / Nissan Motor Company, Limited and affiliates
 * which hold all intellectual property rights. Use of this software is subject
 * to a specific license granted by RENAULT S.A.S. / Nissan Motor Company, Limited.
 */

package com.renault.car.boschradar

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import com.renault.car.boschradar.gps.GpsManager

class BoschradarUserService : Service() {

    companion object {
        const val TAG = "Boschradar.UserService"
    }

    private lateinit var thread: HandlerThread
    private lateinit var handler: Handler

    private lateinit var gpsManager: GpsManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(20, startForegroundService(this, javaClass.simpleName))
        Log.i(TAG, "Boschradar User Service Started")

        handler.post {
            gpsManager = GpsManager(applicationContext)
            gpsManager.start()
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        thread = HandlerThread("BoschradarUserTasks")
        thread.start()
        handler = Handler(thread.looper)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.post {

            if (this::gpsManager.isInitialized) {
                gpsManager.stop()
            }
        }
        thread.quitSafely()
    }
}
