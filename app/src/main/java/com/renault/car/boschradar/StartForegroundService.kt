/*
 * Copyright (c) 2021 and later Renault S.A.S. / Nissan Motor Company, Limited
 * Developed by Renault S.A.S. / Nissan Motor Company, Limited and affiliates
 * which hold all intellectual property rights. Use of this software is subject
 * to a specific license granted by RENAULT S.A.S. / Nissan Motor Company, Limited.
 */

package com.renault.car.boschradar

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import java.util.Objects

const val NOTIFICATION_CHANNEL_ID = "com.renault.car.boschradar"

fun startForegroundService(context: Context, className: String): Notification {
    val chan =
        NotificationChannel(NOTIFICATION_CHANNEL_ID, className, NotificationManager.IMPORTANCE_NONE)
    chan.lightColor = Color.BLUE
    chan.lockscreenVisibility = Notification.VISIBILITY_SECRET
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    Objects.requireNonNull(manager, "manager is NULL").createNotificationChannel(chan)

    val notificationBuilder = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)

    return notificationBuilder.setOngoing(true)
        .setContentTitle("App is running in background")
        .setCategory(Notification.CATEGORY_SERVICE)
        .setVisibility(Notification.VISIBILITY_SECRET)
        .setSmallIcon(R.mipmap.ic_launcher)
        .build()
}
