/*
 * Copyright (c) 2021 and later Renault S.A.S. / Nissan Motor Company, Limited
 * Developed by Renault S.A.S. / Nissan Motor Company, Limited and affiliates
 * which hold all intellectual property rights. Use of this software is subject
 * to a specific license granted by RENAULT S.A.S. / Nissan Motor Company, Limited.
 */

package com.renault.car.boschradar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

class ServiceDebugActivity : Activity() {

    companion object {
        const val TAG = "Boschradar.ServiceDebugActivity"
        private val PERMISSION_ID = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //startForegroundServiceAsUser(Intent(this, BoschradarSystemService::class.java), UserHandle.SYSTEM)

        // check for permission and if not already granted request them
        if (!checkPermission()) {
            Log.i(TAG, "Requesting permission")
            requestPermission()
        } else {
            startForegroundService(Intent(this, BoschradarUserService::class.java))
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "We have the permission for location access")
                startForegroundService(Intent(this, BoschradarUserService::class.java))
            }
        }
    }
}
