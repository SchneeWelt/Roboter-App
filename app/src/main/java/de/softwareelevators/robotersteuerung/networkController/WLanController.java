package de.softwareelevators.robotersteuerung.networkController;


/** Erlaubt das Herstellen von Verbindungen zu anderen Geräten
 * über WLan. Jeweils eine Verbindung pro COntroller Instanz.
 * Anschließend ist die Datenübertragung zu dem verbundenen Gerät
 * möglich. */
public class WLanController extends NetworkController
{
//	Ich will auf WLan umsteigen (Doppelte Reichweite). Der ESP32 Sketch
//	dafür ist fertig. Es muss nur noch ein WLan Kontroller in dieser App
//	gebaut werden, so dass diese APP auch WLan Daten an den ESP32 senden
//	kann.
//
//	das hier wär wohl irgendwie der code:

//		new Thread(() -> {
//			try {
//				// Verbindung zum ESP32 herstellen
//				Socket socket = new Socket("192.168.4.1", 1234);		// Hierfür muss das Handy bereits im WLan des EPS32 sein
//
//				// Streams holen
//				OutputStream out = socket.getOutputStream();
//				InputStream in = socket.getInputStream();
//
//				// Beispiel: Ein Byte senden
//				out.write(42); // z.B. Steuerbefehl
//
//				// Beispiel: Antwort lesen
//				int received = in.read();
//				System.out.println("ESP32 sendet: " + received);
//
//				// Verbindung offen lassen oder später schließen
//				// socket.close();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}).start();



//	ja sind wirklich nur 50 zeilen....


	@Override
	public void sendData(String data)
	{

	}
}
