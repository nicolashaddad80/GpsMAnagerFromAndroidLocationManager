/*
 * 2021 Developed by Renault SW Labs,
 * an affiliate of RENAULT s.a.s. which holds all intellectual property rights.
 * Use of this software is subject to a specific license granted by Renault s.a.s.
 */

package com.renault.car.boschradar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.UserManager
import android.util.Log

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i("Boschradar.BootReceiver", "BootComplete intent received")
            if (userManager.isSystemUser) {
                val boschradarSystemServiceIntent = Intent(
                    context,
                    BoschradarSystemService::class.java
                )
                context.startForegroundService(boschradarSystemServiceIntent)
            } else {
                val boschradarUserServiceIntent = Intent(context, BoschradarUserService::class.java)
                context.startForegroundService(boschradarUserServiceIntent)
            }
        }
    }
}
