public interface NetworkConnectionStateListener
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