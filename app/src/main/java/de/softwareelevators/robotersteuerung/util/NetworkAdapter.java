package de.softwareelevators.robotersteuerung.util;

import android.bluetooth.BluetoothDevice;
import de.softwareelevators.robotersteuerung.networkAdapter.DataReceivedListener;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkConnectionStateListener;

public class NetworkAdapter implements NetworkConnectionStateListener, DataReceivedListener
{
    @Override
    public void onDataReceived(String data)
    {

    }

    @Override
    public void onConnect(BluetoothDevice connectedDevice)
    {

    }

    @Override
    public void onDisconnect()
    {

    }
}
