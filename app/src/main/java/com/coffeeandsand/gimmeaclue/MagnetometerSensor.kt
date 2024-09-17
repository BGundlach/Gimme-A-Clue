package com.coffeeandsand.gimmeaclue

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class MagnetometerSensor @Inject constructor(
    @ApplicationContext context: Context
) : AndroidSensor(
    context,
    PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    Sensor.TYPE_MAGNETIC_FIELD
)
