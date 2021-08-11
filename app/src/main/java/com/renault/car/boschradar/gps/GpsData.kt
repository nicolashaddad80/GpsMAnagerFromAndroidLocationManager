/*
 * 2021 Developed by Renault SW Labs,
 * an affiliate of RENAULT s.a.s. which holds all intellectual property rights.
 * Use of this software is subject to a specific license granted by Renault s.a.s.
 */

package com.renault.car.boschradar.gps

import java.security.InvalidParameterException

data class GpsData(
    var timeStamp: Long? = null,
    val longitude: Long? = null,
    val latitude: Long? = null,
    val altitude: Long? = null,
    val horizontalStdDev: Long? = null,
    val verticalStdDev: Long? = null,
    val yaw: Long? = null,
    val combinedStdDev: Long? = null,
    val satelliteSystemCount: Long? = null,
    val satelliteSystemHDop: Long? = null,
    val satelliteSystemVDop: Long? = null,
    val satellitesystemPDop: Long? = null
)

data class GpsSensorData(
    val id: Long? = null,
    val primaryId: String? = null,
    val secondaryId: String? = null,
    val softwareVersion: Version? = null,
    val antennaZOffset: Long? = null,
    val satelliteSystem: List<SatelliteSystem>? = null
)

/** Version
 *
 * Describe application version.
 *
 * This is needed to match probing layout and application version.
 * The Version follows the Semantic versioning MAJOR.MINOR.PATCHl
 *
 * @param[_major] major number
 * @param[_minor] minor number
 * @param[_patch] patch number
 *
 * @note all values must be between 0 and 255
 */
class Version(major: Int, minor: Int, patch: Int) {
    private val major: Int
    private val minor: Int
    private val patch: Int

    init {
        if (major !in 0..255 || minor !in 0..255 || patch !in 0..255) {
            throw InvalidParameterException("major, minor, and patch values must be between 0 and 255.")
        }

        this.major = major
        this.minor = minor
        this.patch = patch
    }

    override fun toString(): String = "$major.$minor.$patch"
}

enum class SatelliteSystem(val num: Int) {
    UNKNOWN_SATELLITE_SYSTEM(0),
    GPS(1),
    GLONASS(2),
    GALILEO(3),
    BEIDOU_1(4),
    BEIDOU_2(5),
    NAVIC(6),
    QZSS(7)
}
