package de.softwareelevators.robotersteuerung.networkHandler;

import de.softwareelevators.robotersteuerung.MainActivity;


/**
 * Ermöglicht dieser Anwendung die Kommunikation zu einem remote Gerät (einem Roboter).
 * Die Kommunikation ist entweder über Bluetooth: {@link BluetootSender}
 * oder über W-Lan: {@link UdpSender} durch von dieser Klasse erbende Klassen möglich.
 * <p>
 *     Diese Klasse und dessen Kinder fungierem dabei als Sender von Daten - sendet diese
 *     also immer an einen Server, der auf einen Verbindungsaufbau wartet und dann ankommende Daten
 *     verarbeitet und Antworten zurückschickt.
 * </p>
 */
public abstract class NetworkSender
{
	/** Ein Backlink auf den Programmstart und damit auf einen wichtigen Datenspeicher */
	private MainActivity mainActivity;

	/** Gibt an, ob diese Klasse mit einem Remote Gerät verbunden ist */
	private ConnectionState connectionState;
	private NetworkConnectionStateListener networkConnectionStateListener;

	public NetworkSender(MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;

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
	 * Ein Event, welches intern andere Komponenten über den erfolgreichen
	 * Verbindungsaufbau zu einem Server informiert
	 * <p>
	 * Muss durch die von dieser Klasse erbende Klasse aufgerufen werden, sobald
	 * eine Verbindung zu einem Server hergestellt wurde.
	 */
	public void onConnectionEstablished()
	{
		connectionState = ConnectionState.CONNECTED;

		networkConnectionStateListener.onConnectionEstablished();
	}

	/**
	 * Veranlasst dieses Objekt dazu eine Verbindung zu einem Server
	 * (dem Roboter) aufzubauen. Achtung: Nach diesem Befehl ist
	 * nicht unbedingt auch eine Verbindung aufgebaut. Es wird eben nur
	 * der Verbindungsaufbau versucht. Er kann aber nicht garantiert
	 * werden. Sollte eine Verbindung aufgebaut worden sein können,
	 * so wird {@link #onConnectionEstablished()} geworfen.
	 */
	public abstract void connect();


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

	public boolean isConnected()
	{
		return connectionState.equals(ConnectionState.CONNECTED);
	}

	protected MainActivity getMainActivity()
	{
		return mainActivity;
	}
}
