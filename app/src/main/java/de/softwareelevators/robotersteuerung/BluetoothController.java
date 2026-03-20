package de.softwareelevators.robotersteuerung;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.core.app.ActivityCompat;
import de.softwareelevators.robotersteuerung.util.Ausgabe;
import de.softwareelevators.robotersteuerung.util.BtPermissionChecker;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


/** Erlaubt das Herstellen von Verbindungen zu anderen Geräten
 * über klassisches Bluetooth (nicht BLE)
 */
public class BluetoothController
{
	/**
	* Der name von dem BT Classic Gerät, mit dem sich dises
	* Gerät verbinden soll. Das Zielgerät muss für einen
	* erfolgreichen Verbindungsaufbau mit diesem Gerät gekoppelt
	* sein. */
	private String deviceName;

	/** Eine Schnittstelle zur BT Steuerung von diesem Gerät.
	* Über diese Schnittstelle kann unteranderem ausgelesen
	* werden, ob BT auf disem Gerät verfügbar oder eingeschaltet
	* ist. Zudem wird über diese Schnittstelle eine Liste als Set
	* verwaltet, mit der alle mit diesem Gerät gekoppelten BT
	* Gerät ausgelesen werden können. Das ist für den Verbindungsaufbau
	* zum Zielgerät sehr wichtig. */
	private BluetoothAdapter bluetoothAdapter;

	/** Das BT Gerät, mit dem diese Anwendung verbunden ist.
	* Ist dieses Objekt null, so besteht keine BT Verbindung
	* zu einem BT Gerät. */
	private BluetoothDevice connectedDevice;

	/** Der BT Socket, über den BT-Daten von diesem Gerät an
	den BT Socket des verbundenen BT Gerätes gesendet werden können.
	Er darf pro BT Gerät nur einmal für das jeweilige BT Gerät
	existieren. Ein Empfangen von Daten, die von dem anderen
	Gerät an dieses Gerät gesendet wurden, ist ebenfalls möglich. */
	private BluetoothSocket bluetoothSocket;


	private MainActivity mainActivity;

	private ConnectionNotifier connectionNotifier;
	private DataReceivedConnection dataReceivedConnection;


	public BluetoothController(String deviceName, DataReceivedConnection dataReceivedConnection, ConnectionNotifier connectionNotifier, MainActivity mainActivity)
	{
		this.deviceName = deviceName;
		this.mainActivity = mainActivity;
		this.connectionNotifier = connectionNotifier;
		this.dataReceivedConnection = dataReceivedConnection;
	}

	/**
	 * Dieser Befehl startet den Verbindungsaufbau zum Roboter.
	 * In dieser Methode wird sich dabei primär aber noch um
	 * die Erfüllung der Vorabkriterien gekümmert, die für den
	 * Verbindungsaufbau nötig sind. Je nach erfolgt wird in dieser
	 * Methode anschließend an andere Methoden weitergeleitet, die
	 * dann die Verbindung herstellen.
	 */
	public void connect()
	{
		Ausgabe.print("Starte Verbindungsaufbau zu Zielgerät mit Namen: " + deviceName);

		BluetoothManager bluetoothManager = mainActivity.getSystemService(BluetoothManager.class);
		bluetoothAdapter = bluetoothManager.getAdapter();

		if (bluetoothAdapter == null)
		{
			/* Gerät unterstützt kein Bluetooth -> Fehlermeldung ausgeben */
			Ausgabe.print("Fehler: Dieses Gerät unterstützt kein Bluetooth");

			Toast.makeText(mainActivity, "Achtung: Dieses Gerät unterstützt kein Bleutooth...", Toast.LENGTH_SHORT);

			return;
		}

		/* Was soll passieren, wenn BT nicht eingeschaltet ist: */
		if (!bluetoothAdapter.isEnabled())
		{
			/* Eine andere Anwendung starten - hier die Bluetooth Anwendung des Handys,
			um den Nutzer aufzufordern Bluetooth einzuschalten. Das Ergebnis seiner Aktion
			wird in einem Callback über den Result Code ausgegeben. */
			Intent enableBtRequest = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mainActivity.activityLauncher.launch(enableBtRequest, this::handleEnableBtResult);
			// this::handleEnableBtResult übergibt Referenz von handleEnableBtResult() an launch Methode
			// => Direkte Lambda spezifikation wird vermieden, was den Code lesbarer macht.
		} else
			zielgerätFinden();
	}

	/**
	 * Steuert was als Antwort auf die BT Enable Request an den Nutzer gesendet
	 * werden soll. Startet bei erfolgreicher BT Aktivierung den Verbindungsaubau
	 * zum Roboter.
	 *
	 * @param result
	 */
	private void handleEnableBtResult(ActivityResult result)
	{
		int resultCode = result.getResultCode();

		if (resultCode == Activity.RESULT_OK)
		{
			Ausgabe.print("Bluetooth wurde aktiviert...");

			zielgerätFinden();
		}
		else if (resultCode == Activity.RESULT_CANCELED)
			Toast.makeText(mainActivity, "Bitte für Stuerung Bluetooth aktivieren...", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(mainActivity, "FEHLER: Unbekannte Antwort...", Toast.LENGTH_SHORT).show();

		// Toast.LENGTH_SHORT: Anzeigedauer etwa 2 Sekunden, Long etwa 3.5 Sekunden. Das ist alles
	}


	private void zielgerätFinden()
	{
		/* Über alle gekoppelten Geräte iterieren. Wenn dort der im
		Feld deviceName gespeicherte Name vorkommt: Lese die MAC Adresse
		von diesem Gerät aus und verbinde mit diesem Gerät */

		/* Android schreibt an dieser Stelle das Überprüfen der Bluetooth Berechtigungen vor */
		if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
		{
			BtPermissionChecker.requestBluetoothPermission(mainActivity);
			return;
		}

		/* Alle gekoppelten BT Geräte vom Betriebssystem holen */
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		if (pairedDevices == null || pairedDevices.isEmpty())
		{
			Ausgabe.print("Es sind keine Geräte gekoppelt, verbindungsaufbau fehlgeschlagen. Koppel bitte zunächst ein BT-Gerät mit diesem Gerät.");
			return;
		}

		/* Aus der Menge der gekoppelten BT Geräte jenes finden, dass so heißt, wie das Gerät mit dem
		sich diese Anwendung verbinden soll. */
		BluetoothDevice targetDevice = null;
		Ausgabe.print("Gehe alle gekoppelten Geräte durch...");
		for (BluetoothDevice device : pairedDevices)
		{
			/* Statusbericht über Verbindungsversuche ausgeben */
			Ausgabe.print(" - Gekoppeltes Gerät: " + device.getName() + " (" + device.getAddress() + ")");

			if (device.getName() != null && device.getName().equals(deviceName))
			{
				Ausgabe.print("Zielgerät gefunden: " + device.getName() + " (" + device.getAddress() + ")");
				targetDevice = device;
				break;
			}
		}

		if (targetDevice == null)
		{
			Ausgabe.print("Das gesuchte Gerät: " + deviceName + " konnte nicht gefunden werden");
			Toast.makeText(mainActivity, "Das gesuchte Gerät: " + deviceName + " konnte nicht gefunden werden", Toast.LENGTH_SHORT).show();
			return;
		}

		connectToTargetDevice(targetDevice);
	}

	private void connectToTargetDevice(BluetoothDevice targetDevice)
	{
		/* Diese UUID gibt dem Zielgerät zu verstehen, dass es über das Serial Port
		Profile - also über die Serielle Schnittstelle - mit diesem Gerät kommunizieren
		soll. */
		final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

		/* Achtung: socket.connect() blockiert den jeweils Zuständigen Thread für mehrere Sekunden.
		Wenn ich diesen Aufruf also im Main Thread ausführe, friert das unteranderem meine UI ein.
		Deshalb ist ein nebenläufiger Thread für die Aktivierung unbedingt erforderlich. */

		/* Android schreibt an dieser Stelle das Überprüfen der Bluetooth Berechtigungen vor. Achtung:
		* Überprüfungen müssen im Main Thread durchgeführt werden. */
		if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
				ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
		{
			BtPermissionChecker.requestBluetoothPermission(mainActivity);
			return;
		}

		new Thread(() ->
			{
				try
				{
					bluetoothSocket = targetDevice.createRfcommSocketToServiceRecord(SPP_UUID);

					/* Laufende Geräte Suche deaktivieren, da das Suchen von neuen BT Geräten
					Rechenkapazitäten blockiert, die ich für den Verbindungsaufbau zum
					Zielgerät benötige. */
					if (bluetoothAdapter.isDiscovering())
						bluetoothAdapter.cancelDiscovery();

					Ausgabe.print("Verbinde...");

					bluetoothSocket.connect();

					Ausgabe.print("Verbindung zu Zielgerät erfolgreich hergestellt!");

					mainActivity.runOnUiThread(() -> Toast.makeText(mainActivity, "Verbindung hergestellt", Toast.LENGTH_SHORT).show());

					connectionNotifier.onConnect(connectedDevice);
				} catch (Exception e)
				{
					Ausgabe.print("Verbindungsaufbau fehlgeschlagen: " + e.getMessage());
					e.printStackTrace();
				}
			}
		).start();
	}

	public void disconnect()
	{
		if (!isConnected())
			return;

		try
		{
			bluetoothSocket.close();

			connectionNotifier.onDisconnect();

			mainActivity.runOnUiThread(() -> Toast.makeText(mainActivity, "Verbindung getrennt", Toast.LENGTH_SHORT).show());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		bluetoothSocket = null;
	}

	public void sendData(String data)
	{
		if (!isConnected())
			return;

		/* Kurz zum Gedanklichen Bild: Es gibt zwei Sockets. Einmal den
		Socket, der mit diesem Gerät verbunden ist und einmal den Socket,
		der mit dem Zielgerät verbunden ist. Beide Sockets sind über zwei
		Streams miteinander verbunden - sehen aus wie zwei lange Seile.
		Der Socket in dieser Anwendung ist derr Socket, der mit diesem Gerät
		verbunden ist. Sein Outputstream sendet Daten direkt an den Socket des
		anderen Geräts. Der Inputstream von diesem Socket erhält entsprechend
		Daten, die vom anderen Gerät an dieses Gerät gesendet wurden. */

		try
		{
			OutputStream outputStream = bluetoothSocket.getOutputStream();
			outputStream.write(data.getBytes());
			outputStream.flush();
		} catch (Exception e)
		{
			Ausgabe.print("Senden fehlgeschlagen: " + e.getMessage());
		}
	}

	public boolean isConnected()
	{
		return bluetoothSocket != null && bluetoothSocket.isConnected();
	}

	public interface DataReceivedConnection
	{
		void onDataReceived(String data);
	}

	public interface ConnectionNotifier
	{
		/** Wird geworfen, wenn eine Verbindung zu einem BT Gerät
		 * hergestellt wurde.
		 *
		 * @param connectedDevice
		 */
		void onConnect(BluetoothDevice connectedDevice);

		void onDisconnect();
	}
}
