package de.softwareelevators.robotersteuerung.networkController;

import android.bluetooth.BluetoothDevice;

public abstract class NetworkController
{
	public abstract void sendData(String data);


	public interface DataReceivedConnection
	{
		void onDataReceived(String data);
	}

	public interface ConnectionStateNotifier
	{
		/** Wird geworfen, wenn eine Verbindung zu einem Netzwerkgerät
		 * hergestellt wurde (Gerät wurde dabei entweder über BT oder
		 * über WLan verbunden).
		 *
		 * @param connectedDevice
		 */
		void onConnect(BluetoothDevice connectedDevice);

		void onDisconnect();
	}
}
