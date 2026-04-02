package de.softwareelevators.robotersteuerung.networkController;

import android.bluetooth.BluetoothDevice;

public abstract class NetworkController
{
	public abstract void sendData(String data);

	public abstract void connect();

	public abstract void disconnect();



	public interface DataReceivedListener
	{
		/** Wird immer dann geworfen, wenn dieser NetworkController Daten
		 * von dem mit ihm verbundenem Gerät erhalten hat. Speziell beim Roboter
		 * würde das also bedeuten, dass dieser Daten an den Client, also die
		 * Roboter Steuerungs App, gesendet hat und diese Daten nun hier angekommen
		 * sind.
		 *
		 * @param data
		 */
		void onDataReceived(String data);
	}

	public interface NetworConnectionStateListener
	{
		/** Wird geworfen, wenn eine Verbindung zu einem Netzwerkgerät
		 * hergestellt wurde (Gerät wurde dabei entweder über BT oder
		 * über WLan verbunden).
		 *
		 * @param connectedDevice
		 */
		void onConnect(BluetoothDevice connectedDevice);


		/** Wird geworfen, wenn eine bestehende Verbindung zu einem Gerät
		 * über ein Netzwerk mit diesem NetworkController beendet wurde.
		 */
		void onDisconnect();
	}
}
