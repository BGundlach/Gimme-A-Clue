package com.coffeeandsand.gimmeaclue


import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accelerometerSensor : AccelerometerSensor,
    private val magnetometerSensor : MagnetometerSensor,
) : ViewModel() {
    fun Float.radToDegrees() : Int {
        return (Math.toDegrees(this.toDouble()).toInt() + 360) % 360
    }
    private val _orientation = MutableStateFlow(0)
    val orientation : StateFlow<Int> get() = _orientation

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    fun startSensors(){
        accelerometerSensor.startListening()
        magnetometerSensor.startListening()
        initListeners()
    }

    fun stopSensors(){
        accelerometerSensor.stopListening()
        magnetometerSensor.stopListening()
    }

    private fun initListeners() {
        accelerometerSensor.setOnSensorValuesChangedListener { accelerometerReading ->
            this.accelerometerReading[0] = accelerometerReading[0]
            this.accelerometerReading[1] = accelerometerReading[1]
            this.accelerometerReading[2] = accelerometerReading[2]
            computeOrientation()
        }
        magnetometerSensor.setOnSensorValuesChangedListener { magnetometerReading ->
            this.magnetometerReading[0] = magnetometerReading[0]
            this.magnetometerReading[1] = magnetometerReading[1]
            this.magnetometerReading[2] = magnetometerReading[2]
        }
    }

    private fun computeOrientation() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        // "rotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        // "orientationAngles" now has up-to-date information.

        val (_ , _ , y) = orientationAngles

        // Values used here , are the ones needed by me , you may or may not need the same values
        val orientationState = when (y.radToDegrees()) {
            in 226..314 -> 0
            in 315..359, in 0..44 -> 1
            in 136..225 -> -1
            else -> 0
        }

        viewModelScope.launch {
            _orientation.emit(orientationState)
        }
    }
}