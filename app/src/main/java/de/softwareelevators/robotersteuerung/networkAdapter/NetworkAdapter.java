package de.softwareelevators.robotersteuerung.networkAdapter;

import android.bluetooth.BluetoothDevice;


/**
 * Der Teil meiner App, der sich mit der Netzwerkkommunikation
 * beschäftigt.
 */
public abstract class NetworkAdapter
{
	/** Die Verbindung zu einer Schnittstelle, über die Daten
	 * empfangen werden können
	 */
	protected DataReceivedListener dataReceivedListener;

	/**
	 * Der Befehl um Daten über den jeweils durch die erbende Klasse
	 * implementierten Standart
	 *
	 * @param data Die Daten, die an den empfänger gesendet werden
	 *             sollen
	 */
	public abstract void sendData(String data);


	/**
	 * Befehl um eine Verbindung zu einem Empfänger
	 * aufzubauen
	 */
	public abstract void connect();


	/**
	 * Befehl um eine aufgebaute Verbindung zu einem Empfänger
	 * wieder zu schließen
	 */
	public abstract void disconnect();


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
