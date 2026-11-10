package de.softwareelevators.robotersteuerung.networkHandler;


import android.widget.Toast;
import de.softwareelevators.robotersteuerung.Main;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.util.Ausgabe;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/** Erlaubt das Herstellen von Verbindungen zu anderen Geräten
 * über WLan. Jeweils eine Verbindung pro App Instanz.
 * Anschließend ist die Datenübertragung zu dem verbundenen Gerät
 * möglich. */

//STA Modus im Roboter:
/*
	Der Roboter soll im STA Modus laufen. Heißt er kommuniziert über einen
	Router mit meinem Handy. Aktuell läuft er im AP Modus, da ist er selbst
	dann der Router, was extrem ineefizient ist und nur sehr geringe
	reichweiten hat.
	Hier der Code, den ich für den sta modus brauche:

	#include <WiFi.h>

const char* ssid = "DEIN_ROUTER_NAME";
const char* password = "DEIN_ROUTER_PASSWORT";

WiFiServer server(1234);

void setup() {
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }

  server.begin();
}

void loop() {
  WiFiClient client = server.available();
  if (client) {
    while (client.connected()) {
      if (client.available()) {
        String data = client.readString();
        // verarbeite Daten
      }
    }
  }
}



 */


public class WLanHandler extends NetworkHandler
{
	private Socket socket;
	private String mDnsHost;
	private MainActivity mainActivity;

	/**
	 * @param mDnsHost Der mDNS Namen von dem Gerät mit dem sich dieser Handler
	 *                 über {@link connect()} verbinden soll. Sollte etwas enhalten
	 *                 wie: "esp32.local". Das .local als Endung ist wichtig, damit
	 *                 der Router erkennt, dass ich hier einen Hostname über mDNS
	 *                 auflösen will. Ohne .local könnte der Router auf die Idee
	 *                 kommen nach dem Namen von einem Server über DNS zu suchen,
	 *                 was dann ohne Ergebnis resultieren würde.
	 */
	public WLanHandler(String mDnsHost, MainActivity mainActivity)
	{
		this.mDnsHost = mDnsHost;
		this.mainActivity = mainActivity;
	}

	/**
	 * Verbindet sich über mDNS zu einem Remote Gerät
	 */
	@Override
	public void connect()
	{
		super.connect();

		if (socket != null && !socket.isClosed())
			disconnect();

		/* Android erlaubt Netzwerkoperationen nicht auf dem Main Thread. Deshalb
		* separaten Thread verwenden */
		new Thread(() -> {
			try {

				Ausgabe.print("Verbinde...");

				// Löst erst den Hostname per mDNS auf und verbindet sich dann direkt
				// mit diesem auf dem angegebenen Port
				socket = new Socket(mDnsHost, 8000);

				Ausgabe.print("Verbindung zu Zielgerät erfolgreich hergestellt!");

				onConnectionEstablished(null);

				mainActivity.runOnUiThread(() -> Toast.makeText(mainActivity, "Verbindung hergestellt", Toast.LENGTH_SHORT).show());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();







	}

	@Override
	public void sendData(String data)
	{
		if (socket == null || socket.isClosed())
			throw new IllegalStateException("Socket ist nicht verbunden");

		new Thread(() ->
		{
			try
			{
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(data.getBytes(StandardCharsets.UTF_8));
				outputStream.flush();
			} catch (IOException e)
			{
				throw new RuntimeException(e);
			}

		}).start();
	}

	@Override
	public void disconnect()
	{
		super.disconnect();

		try
		{
			if (socket != null && !socket.isClosed())
				socket.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			socket = null;
		}
	}
}
