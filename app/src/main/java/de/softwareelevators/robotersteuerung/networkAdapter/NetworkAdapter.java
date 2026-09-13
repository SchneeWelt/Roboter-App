package de.softwareelevators.robotersteuerung.networkAdapter;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;


/**
 * Definiert wird diese Anwendung mit einem Remote Gerät (einem Roboter)
 * kommuniziert. Möglich ist die Kommunikation über Bluetooth: {@link BluetoothAdapter}
 * oder über W-Lan: {@link WLanAdapter}
 */
public abstract class NetworkAdapter
{
	private DataReceivedListener dataReceivedListener;
	private NetworkConnectionStateListener networkConnectionStateListener;


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
		networkConnectionStateListener.onConnect(connectedDevice);
	}


	/**
	 * Veranlasst dieses Objekt dazu eine Verbindung zu einem Remote
	 * Gerät (dem Roboter) auzubauen.
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

	public void setDataReceivedListener(DataReceivedListener dataReceivedListener)
	{
		this.dataReceivedListener = dataReceivedListener;
	}
}
