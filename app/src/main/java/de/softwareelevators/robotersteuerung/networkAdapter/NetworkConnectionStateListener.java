package de.softwareelevators.robotersteuerung.networkAdapter;

import android.bluetooth.BluetoothDevice;

/** Ermöglicht eine Reaktion externer Klassen auf das aufbauen und schließen
 * einer Verbindung zu einem Remote Gerät (dem Roboter). Das Wort extern bedeutet
 * Klassen, die nicht vom Type: {@link NetworkAdapter} sind
 */
public interface NetworkConnectionStateListener
{
    /** Wird geworfen, wenn eine Verbindung zu einem Remot Gerät (dem
     * Roboter) hergestellt wurde
     *
     * @param connectedDevice
     */
    void onConnect(BluetoothDevice connectedDevice);


    /** Wird geworfen, wenn eine bestehende Verbindung zu einem Gerät
     * über ein Netzwerk mit diesem NetworkController beendet wurde.
     */
    void onDisconnect();
}
