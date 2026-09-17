package de.softwareelevators.robotersteuerung.networkAdapter;


import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
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


public class WLanAdapter extends NetworkAdapter
{
	private Socket socket;

	public WLanAdapter()
	{
    }

	@Override
	public void connect()
	{
		super.connect();

		/* Verbindung zum ESP32 herstellen - nur möglich, wenn Handy mit ESP32-WLan verbunden */
		try
		{
			if (socket != null && !socket.isClosed())
				disconnect();

//			ich sollte mich hier über mDNS verbinden. Dann muss ich die IP Adresse des EPS32 nicht kennen,
//			die müsste ich andernfalls ja herausfinden. Auf dieser seite:

		socket = new Socket("esp32robot.local", 1234);

//			andere seite:
//
//			#include <ESPmDNS.h>
//
//			MDNS.begin("esp32robot");
//
//			falls das nicht funzt: IP Adresse über serial monitor ausgeben lassen oder:
//
//			Der klassische udp broadcast. Der ist am aufwändigsten zu implementieren. Hier ausschnitte:
//
//			esp 32
//			udp.beginPacket("255.255.255.255", 4210);
//			udp.print(WiFi.localIP());
//			udp.endPacket();
//
//			app
//			DatagramPacket packet = new DatagramPacket(buf, buf.length);
//			socket.receive(packet);
//			String espIp = new String(packet.getData());



			socket = new Socket("192.168.4.1", 1234);
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendData(String data)
	{
		if (socket == null || socket.isClosed())
			throw new IllegalStateException("Socket ist nicht verbunden");

		// Thread wird sowieso gar nicht benötigt, da dieser Befehl hier nicht durch einen
		// UI Thread aufgerufen wird.

		new Thread(() ->		// Das hier mit dem eigenen Thread geht so nicht. Es muss einen Handler geben, d
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
