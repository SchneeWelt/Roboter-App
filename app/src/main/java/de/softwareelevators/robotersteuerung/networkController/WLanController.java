package de.softwareelevators.robotersteuerung.networkController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/** Erlaubt das Herstellen von Verbindungen zu anderen Geräten
 * über WLan. Jeweils eine Verbindung pro COntroller Instanz.
 * Anschließend ist die Datenübertragung zu dem verbundenen Gerät
 * möglich. */
public class WLanController extends NetworkController
{
	/**
	 * Dises Objekt sendet an den ESP32
	 */
	private OutputStream outputStream;

	/** Dieser Stream erhölt Daten vom ESP32 */
	private InputStream inputStream;

	/** Die Verbindung zum ESP32 */
	private Socket socket;


	/* Auch der WLan Controller wird vermutlich nicht stark genug sein.
	Was ich wirklich brauche ist eine Verbindung über Funk */

	@Override
	public void connect()
	{
		Thread connection = new Thread(() ->
			{
				try
				{
					// Verbindung zum ESP32 herstellen. Das Handy muss hierfür
					// bereits im WLan des ESP32 eingewählt sein
					socket = new Socket(getHostName(), getHostPort());

					// Streams holen
					outputStream = socket.getOutputStream();
					inputStream = socket.getInputStream();

				} catch (Exception e) {
					e.printStackTrace();
				}
		});

		connection.start();
	}

	@Override
	public void disconnect()
	{
		if (outputStream != null)
		{
			try
			{
				outputStream.flush();
				outputStream.close();

				outputStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (inputStream != null)
		{
			try
			{
				inputStream.close();

				inputStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        try
		{
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public void sendData(String data)
	{
		/* Daten Senden und Daten lesen sollte wohl immer in eigenen Threads
		erfolgen, da sich sonst das lesen und das schreiben gegenseitig blockiert */

		Thread sendingThread = new Thread(() ->
			{
				try
				{
					outputStream.write(data.getBytes(StandardCharsets.UTF_8));
					outputStream.flush();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		);

		sendingThread.start();
	}

	private void receiveData()
	{
//		new Thread(() ->
//			{
//				while(inputStream.available())
//				{
//
//				}
//			}
//		);
	}

	protected String getHostName()
	{
		// Die nachfolgende Adresse ist wohl die standart Adresse bei einem
		// durch einen Accsess Point über ESP32 bereitgestelltem Netzwerk
		// für den Dienst, den ich verwenden will

		/* Zum Verständnis: Im AP (Accsess Point) Modus ist der ESP32
		sowohl Router als auch Client. Aus diesem Grund wird hier auch eine
		Host IP Adresse benötigt, da auf dem ESP32 eben nicht nur der
		Router läuft, sondern eben auch ein Dienst, der (hoffentlich)
		über die unten stehende IP Adresse angesprochen wird */

		return "192.168.4.1";
	}

	protected int getHostPort()
	{
		return 1234;
	}
}
