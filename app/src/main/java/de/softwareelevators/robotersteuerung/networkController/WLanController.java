package de.softwareelevators.robotersteuerung.networkController;


import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/** Erlaubt das Herstellen von Verbindungen zu anderen Geräten
 * über WLan. Jeweils eine Verbindung pro App Instanz.
 * Anschließend ist die Datenübertragung zu dem verbundenen Gerät
 * möglich. */
public class WLanController extends NetworkController
{
	private Socket socket;

	hier sind noch einnige Fehler drinn. Gebe Copilot diese Klasse und lass sie von ihm bewerten.

	@Override
	public void connect()
	{
		/* Verbindung zum ESP32 herstellen - nur möglich, wenn Handy mit ESP32-WLan verbunden */
		try
		{
			socket = new Socket("192.168.4.1", 1234);
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}


	}

	@Override
	public void sendData(String data)
	{
		new Thread(() ->
		{
			try
			{
				OutputStream outputStream = socket.getOutputStream();

				outputStream.write(data.getBytes());
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
		try
		{
			socket.close();
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
