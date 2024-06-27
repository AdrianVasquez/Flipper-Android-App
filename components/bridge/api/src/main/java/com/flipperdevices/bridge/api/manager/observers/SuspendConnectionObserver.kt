package com.flipperdevices.bridge.api.manager.observers

import android.bluetooth.BluetoothDevice
import no.nordicsemi.android.ble.annotation.DisconnectionReason

interface SuspendConnectionObserver {
    suspend fun onDeviceConnecting(device: BluetoothDevice) = Unit

    suspend fun onDeviceConnected(device: BluetoothDevice) = Unit

    suspend fun onDeviceFailedToConnect(
        device: BluetoothDevice,
        @DisconnectionReason reason: Int
    ) = Unit

    suspend fun onDeviceReady(device: BluetoothDevice) = Unit

    suspend fun onDeviceDisconnecting(device: BluetoothDevice) = Unit

    suspend fun onDeviceDisconnected(
        device: BluetoothDevice,
        @DisconnectionReason reason: Int
    ) = Unit
}
