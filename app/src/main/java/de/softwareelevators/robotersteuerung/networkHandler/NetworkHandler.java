package de.softwareelevators.robotersteuerung.networkHandler;

import android.bluetooth.BluetoothDevice;


/**
 * Ermöglicht dieser Anwendung die Kommunikation zu einem remote Gerät (einem Roboter).
 * Die Kommunikation ist entweder über Bluetooth: {@link BluetoothHandler}
 * oder über W-Lan: {@link WLanHandler} durch von dieser Klasse erbende Klassen möglich.
 */
public abstract class NetworkHandler
{
	/** Gibt an, ob diese Klasse mit einem Remote Gerät verbunden ist */
	private ConnectionState connectionState;
	private DataReceivedListener dataReceivedListener;
	private NetworkConnectionStateListener networkConnectionStateListener;


	public NetworkHandler()
	{
		connectionState = ConnectionState.NOT_CONNECTED;
	}

	/**
	 * Veranlasst dieses Objekt dazu Daten an das remote Gerät (den
	 * Roboter) zu senden
	 *
	 * @param data Die Daten, die an den empfänger gesendet werden
	 *             sollen
	 */
	public abstract void sendData(String data);

	/**
	 * Wird immer dann geworfen, wenn Daten von einem Remote Gerät (dem Roboter)
	 * empfangen wurden. Dieses Event dient dann der Verarbeitung dieser
	 * Daten.
	 *
	 * @param data Die vom Remote Gerät (dem Roboter) empfangenen Daten
	 */
	public void onDataReceived(String data)
	{
		dataReceivedListener.onDataReceived(data);
	}

	/**
	 * Muss durch die von dieser Klasse erbende Klasse aufgerufen werden, sobald
	 * eine Verbindung zu einem Remote Gerät hergestellt werden konnte
	 *
	 * @param connectedDevice Das Remote Gerät, zu dem eine Verbindung hergestellt
	 *                        wurde
	 */
	public void onConnectionEstablished(BluetoothDevice connectedDevice)
	{
		connectionState = ConnectionState.CONNECTED;

		networkConnectionStateListener.onConnectionsEstablished(connectedDevice);
	}


	/**
	 * Veranlasst dieses Objekt dazu eine Verbindung zu einem Remote
	 * Gerät (dem Roboter) auzubauen. Achtung: Nach diesem Befehl ist
	 * nicht unbedingt auch eine Verbindung aufgebaut. Es wird eben nur
	 * der Verbindungsaufbau versucht. Er kann aber nicht garantiert
	 * werden. Sollte eine Verbindung aufgebaut worden sein können,
	 * so wird {@link #onConnectionEstablished(BluetoothDevice)}
	 * geworfen.
	 */
	public void connect()
	{

	}


	/**
	 * Befehl um eine aufgebaute Verbindung zu einem Empfänger
	 * wieder zu schließen
	 */
	public void disconnect()
	{
		connectionState = ConnectionState.NOT_CONNECTED;

		networkConnectionStateListener.onDisconnect();
	}

	/**
	 * Sorgt dafür, dass das Objekt, welches hier diesen Befehl aufruft und den Parameter übergibt über diesen
	 * Parameter einen Listener erhält, der auf den Aufbau einer Verbindung, sowie auf dessen Beendigung reagieren
	 * kann
	 *
	 * @param networkConnectionStateListener
	 */
	public void setNetworkConnectionStateListener(NetworkConnectionStateListener networkConnectionStateListener)
	{
		this.networkConnectionStateListener = networkConnectionStateListener;
	}

	/**
	 * Sorgt dafür, dass dieses Objekt den hier übergebenen Parameter bei Zustandsänderungen, die
	 * durch diesen Parameter übermittelt werden können, informiert.
	 *
	 * @param dataReceivedListener
	 */
	public void setDataReceivedListener(DataReceivedListener dataReceivedListener)
	{
		this.dataReceivedListener = dataReceivedListener;
	}

	public boolean isConnected()
	{
		return connectionState.equals(ConnectionState.CONNECTED);
	}
}
