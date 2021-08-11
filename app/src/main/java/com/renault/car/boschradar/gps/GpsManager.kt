/*
 * 2021 Developed by Renault SW Labs,
 * an affiliate of RENAULT s.a.s. which holds all intellectual property rights.
 * Use of this software is subject to a specific license granted by Renault s.a.s.
 */

package com.renault.car.boschradar.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.OnNmeaMessageListener
import android.os.Handler
import android.os.HandlerThread
import android.util.Log

class GpsManager(
    private val mContext: Context,
    private val mUpdatePeriodMilliSeconds: Long = 1000
) {
    companion object {
        const val TAG = "Boschradar.GpsManager"
        const val LONGITUDE_PRECISION = 0.00000001
        const val LATITUDE_PRECISION = 0.00000001
        const val ALTITUDE_PRECISION = 0.001
        const val HORIZONTAL_STD_DEV_PRECISION = 0.001
        const val VERTICAL_STD_DEV_PRECISION = 0.001
        const val YAW_PRECISION = 0.01
        const val COMBINED_STD_DEV_PRECISION = 0.01
    }

    private var mLocationManager =
        mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val mGpsManagerOnNmeaMessageListener = GpsManagerOnNmeaMessageListener()
    private val mGpsManagerLocationListener = GpsManagerLocationListener()

    private val mHandlerThread = HandlerThread("GpsManager")
    private var mHandler: Handler? = null

    private var started = false


    private fun locationToWgs84(position: Location): GpsData {

        return GpsData(
            position.time,
            (position.longitude / LONGITUDE_PRECISION).toLong(),
            (position.latitude / LATITUDE_PRECISION).toLong(),
            (position.altitude / ALTITUDE_PRECISION).toLong(),
            (position.accuracy / HORIZONTAL_STD_DEV_PRECISION).toLong(),
            (position.verticalAccuracyMeters / VERTICAL_STD_DEV_PRECISION).toLong(),
            (position.bearing / YAW_PRECISION).toLong(),
            (position.bearingAccuracyDegrees / COMBINED_STD_DEV_PRECISION).toLong(),

            )
    }

    inner class GpsManagerLocationListener : LocationListener {

        override fun onLocationChanged(position: Location) {
            val locationData = locationToWgs84(position)
            Log.v(TAG, "onLocationChanged: $locationData")
        }

        override fun onProviderEnabled(provider: String) {
            Log.d(TAG, "onProviderEnabled: $provider")
            Log.d(TAG, "Starting collect locations")
        }

        override fun onProviderDisabled(provider: String) {
            //TODO need to handle if user disables location
            Log.d(TAG, "onProviderDisabled: $provider")
            Log.d(TAG, "Location will start when location get enabled")
        }
    }

    inner class GpsManagerOnNmeaMessageListener : OnNmeaMessageListener {
        override fun onNmeaMessage(message: String?, timestamp: Long) {

            Log.v(TAG, "onNmeaMessage: $message : timestamp = $timestamp")
        }
    }

    private fun startHandler() {
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper)
    }

    private fun isLocationEnabled(): Boolean {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun start() {

        if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "Access to location service denied, startGpsManager() abort")
            return
        }

        startHandler()

        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            mUpdatePeriodMilliSeconds,
            0f,
            mGpsManagerLocationListener,
            mHandler?.looper
        )

        mLocationManager.addNmeaListener(mGpsManagerOnNmeaMessageListener, mHandler)

        started = true
        Log.i(TAG, "GpsManager Started")
        if (!isLocationEnabled()) {
            Log.i(TAG, "Location will start when location get enabled")
        }
    }

    fun stop() {
        mLocationManager.removeUpdates(mGpsManagerLocationListener)
        Log.i(TAG, "GpsManager Stopped")
        started = false
    }
}
