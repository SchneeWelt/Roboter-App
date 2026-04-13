package de.softwareelevators.robotersteuerung.networkController;


import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/** Erlaubt das Herstellen von Verbindungen zu anderen Geräten
 * über WLan. Jeweils eine Verbindung pro App Instanz.
 * Anschließend ist die Datenübertragung zu dem verbundenen Gerät
 * möglich. */
public class WLanController extends NetworkController
{
	private Socket socket;


	@Override
	public void connect()
	{
		/* Verbindung zum ESP32 herstellen - nur möglich, wenn Handy mit ESP32-WLan verbunden */
		try
		{
			if (socket != null && !socket.isClosed())
				disconnect();

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
