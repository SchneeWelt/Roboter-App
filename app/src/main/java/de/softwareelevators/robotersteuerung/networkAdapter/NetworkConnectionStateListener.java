package de.softwareelevators.robotersteuerung.networkAdapter;

import android.bluetooth.BluetoothDevice;

public interface NetworkConnectionStateListener
{
    /** Wird geworfen, wenn eine Verbindung zu einem Remotegerät
     * hergestellt wurde
     *
     * @param connectedDevice
     */
    void onConnect(BluetoothDevice connectedDevice);


    /** Wird geworfen, wenn eine bestehende Verbindung zu einem Gerät
     * über ein Netzwerk mit diesem NetworkController beendet wurde.
     */
    void onDisconnect();
}
