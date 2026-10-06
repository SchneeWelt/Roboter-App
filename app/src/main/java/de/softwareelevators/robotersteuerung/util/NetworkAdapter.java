package de.softwareelevators.robotersteuerung.util;

import android.bluetooth.BluetoothDevice;
import de.softwareelevators.robotersteuerung.networkHandler.DataReceivedListener;
import de.softwareelevators.robotersteuerung.networkHandler.NetworkConnectionStateListener;

/**
 * Ein Wrapper, der die einfache Anbindung an einen {@link de.softwareelevators.robotersteuerung.networkHandler.NetworkHandler}
 * ermöglicht. Alternativ könnten aber auch alle Interfaces einzelnt implementiert werden.
 */
public class NetworkAdapter implements NetworkConnectionStateListener, DataReceivedListener
{
    @Override
    public void onDataReceived(String data)
    {

    }

    @Override
    public void onConnectionsEstablished(BluetoothDevice connectedDevice)
    {

    }

    @Override
    public void onDisconnect()
    {

    }
}
