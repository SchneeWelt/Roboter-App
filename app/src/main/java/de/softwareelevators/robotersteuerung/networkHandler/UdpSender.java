package de.softwareelevators.robotersteuerung.networkHandler;


import android.widget.Toast;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.util.Ausgabe;
import de.softwareelevators.robotersteuerung.util.PortConfiguration;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Erlaubt das Herstellen von Verbindungen zu anderen Geräten
 * über WLan. Pro Klasseninstanz ist genau eine Verbindung zu
 * einem externen Gerät möglich. Zur Kommunikation über WLan
 * wird das UDP Protokoll verwendet, dieses ist deutlich schneller
 * als das TCP Protokoll und ein Paketverlusst ist heutzutage
 * unwahrscheinlich.
 */

public class UdpSender extends NetworkSender
{
	/** Diese Flage gibt an, ob dieses Objekt sich bereits mit einem
	 * Server verbunden hat.
	 */
	private boolean connected;

	/** Ermöglicht die drahtlose Kommunikation über WLan / UDP */
	private DatagramSocket udpSocket;

	/** Die als mDNS Kodierte Adresse des Servers, zu dem dieses Objekt
	 * eine Verbindung aufbauen soll
	 */
	private final String mDnsTarget;

	/** IP Adresse und Port zu einem anderen Gerät in einem Netzwerk (auch Internet).
	 * Wird in diese Klasse aus einer mDNS Adresse abgeleitet. */
	private InetAddress inetAddress;

	/**
	 * Dieses Objekt hat intern einen Menge von Threads, die für dieses Objekt
	 * arbeiten können. (Bei newSingleThreadExecutor handelt es sich um genau
	 * einen Thread). Die Idee ist Threads nicht immer neu erzeugen zu müssen,
	 * wenn ein Aufgabe ansteht. Es ist viel schneller und vermeidet RaceConditions,
	 * wenn immer die gleichen Threads eines solchen Service Objektes die Arbeit
	 * erledigen und direkt den nächsten Task anfangen, sobald diese fertig sind
	 */
	private ExecutorService threadExecutor;

	/**
	 * @param mDnsTarget Die als mDNS codierte Adresse von dem Gerät mit dem sich diese
	 *                 Klasse über {@link connect()} verbinden soll. Dieser Parameter sollte
	 *                 etwas enhalten wie: "esp32.local". Das .local als Endung ist wichtig, damit
	 *                 der Router erkennt, dass es sich hier um einen Hostname handelt, der über mDNS
	 *                 aufgelöst werden muss. Ohne .local könnte der Router auf die Idee kommen nach dem
	 *                 Namen von einem Server über DNS zu suchen, was dann ohne ein Ergebnis resultieren
	 *                  würde.
	 * @param mainActivity Backlink zum Programmstart
	 */
	public UdpSender(String mDnsTarget, MainActivity mainActivity)
	{
		super(mainActivity);

		this.mDnsTarget = mDnsTarget;

		connected = false;

		threadExecutor = Executors.newSingleThreadExecutor();
	}

	/**
	 * Verbindet sich über mDNS zu dem Server eiens Remote Geräts.
	 */
	@Override
	public void connect()
	{
		if (connected)
		{
			Ausgabe.print("[" + getClass().getName() + "] Eine Verbindung zu einem Server besteht bereits." +
					"Erneuter Verbindungsauffbau abgebrochen!");

			return;
		}


		/* Android erlaubt Netzwerkoperationen nicht auf dem Main Thread. Deshalb
		* separaten Thread verwenden */
		threadExecutor.submit(() ->
			{
				try {

					Ausgabe.print("Initialisiere Verbindung");

					/* "Leeres" Objekt, welches jetzt aber UDP Pakete an einen Server senden kann */
					udpSocket = new DatagramSocket();

					/* mDNS Adresse in InetAdresse auflösen */
					inetAddress = InetAddress.getByName(mDnsTarget);

					Ausgabe.print("Verbindung zu Zielgerät hergestellt!");

					onConnectionEstablished();

					getMainActivity().runOnUiThread(() -> Toast.makeText(getMainActivity(), "Verbindung zu: " + mDnsTarget + " hergestellt", Toast.LENGTH_SHORT).show());

					connected = true;

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		);
	}


	/**
	 * Sendet Daten an einen im Vorfeld "aufgeklärten" Server.
	 *
	 * @param message Die Daten, die an den Empfänger (Server) gesendet werden
	 *             	  sollen. Bitte beachten: Hier wird UPD zum Senden
	 *             	  der Daten verwendet, daher sollten die einzelnen
	 *             	  Packages nicht zu groß werden. Die Package größe
	 *             	  hängt direkt mit diesem Parameter zusammen. Also
	 *             	  einfach versuchen hier keine Bücher im Parameter
	 *             	  zu kodieren sondern immer nur kurze Sätze oder
	 *             	  noch besser nur einzelne Zeichen zu senden.
	 */
	@Override
	public void sendData(String message)
	{
		if (!connected)
		{
			Ausgabe.print("[" + getClass().getName() + "] Es beseht noch keine Verbindung zu einem Server! Senden der Daten abgebrochen....");
			return;
		}

		threadExecutor.submit(() ->
			{
				try
				{
					/* Zu sendende Daten in ein Byte Array parsen */
					byte[] data = message.getBytes(StandardCharsets.UTF_8);

					/* Ein UDP Paket erstellen */
					DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, PortConfiguration.W_LAN_PORT);

					/* Und übers Netzwerk verschicken */
					udpSocket.send(packet);

				} catch (IOException e)
				{
					throw new RuntimeException(e);
				}

			}
		);
	}

	@Override
	public void disconnect()
	{
		if (!connected)
		{
			Ausgabe.print("[" + getClass().getName() + "] Es besteht keine Verbindung zu einem Server!\n Daher muss auch keine Verbindung geschlossen werden.\nAbbruch...");
			return;
		}

		super.disconnect();

		if (udpSocket != null && !udpSocket.isClosed())
			udpSocket.close();

		connected = false;

		udpSocket = null;
	}
}
