package com.flipperdevices.bridge.impl.utils

import android.bluetooth.BluetoothDevice
import java.lang.reflect.InvocationTargetException

object RemoveBondHelper {
    fun removeBand(device: BluetoothDevice): Result<Boolean> = runCatching {
        val removeBond = device.javaClass.getMethod("removeBond")
        return@runCatching removeBond.invoke(device) as Boolean
    }
}
