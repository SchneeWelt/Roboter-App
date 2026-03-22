package de.softwareelevators.robotersteuerung.util;


import android.os.Handler;
import android.os.Looper;
import de.softwareelevators.robotersteuerung.networkController.BluetoothController;
import de.softwareelevators.robotersteuerung.networkController.NetworkController;

/**
 * Diese Hilfklasse verwendet das Produce-Consumer Pattern,
 * um die Datensenderate an den Roboter zu begrenzen.
 * Daten, die diese Hilfsklasse bekommt werden nur alle
 * x milli Sekunden an den Roboter gesendet.
 * <p>
 * Das Pattern puffert wohl auch Daten, die zwar schon
 * existieren, aber wegen der zeitlichen Limitierung
 * noch nicht gesendet werden können. Mache ich hier nicht.
 * Man würde aber wohl eine Queu verwenden.
 *
 */
public class Sendebegrenzer
{
	/** Die Daten, die beim einem Sendevorgang gesendet werden
	 * sollen. */
	private float lenkwinkel, fahrgeschwindigkeit;

	private float vorheriegerLenkwinkel, vorheriegeFahrgeschwindigkeit;


	private Handler handler;
	private BtSender btSender;

	public Sendebegrenzer(NetworkController networkController)
	{
		vorheriegerLenkwinkel = Integer.MIN_VALUE;
		vorheriegeFahrgeschwindigkeit = Integer.MIN_VALUE;

		btSender = new BtSender(networkController);

		handler = new Handler(Looper.getMainLooper());
	}

	public void start()
	{
		/* Methodenreferenz nutzen. Wichtig: Parameterliste der verwendeten Methode
		muss mit Parameterliste des erwarteten Lambdas übereinstimmen. Name der Methode
		ist egal, muss also nicht gleich sein. */

		handler.post(this::sendTask);
	}

	public void stop()
	{
		handler.removeCallbacks(this::sendTask);
	}


	/**
	 * Hier werden die Daten an diese Klasse übergeben, die von dieser
	 * Klasse via BT an den Roboter gesendet werden sollen. Die hier
	 * übergebenen Daten werden beim nächsten Sendeversuch gesendet.
	 * Ein Buffering der Daten findet nicht statt, heißt jedes neue
	 * Datum überschreibt seinen entsprechenden Vorgänger.
	 *
	 * @param lenkwinkel
	 * @param fahrgeschwindigkeit
	 */
	public void datenAktualisieren(float lenkwinkel, float fahrgeschwindigkeit)
	{
		this.lenkwinkel = lenkwinkel;
		this.fahrgeschwindigkeit = fahrgeschwindigkeit;
	}

	private void sendTask()
	{
		/* Nur bei Wertänderung senden -> Spamvermeidung */
		boolean lenkwinkelÄnderung = lenkwinkel != vorheriegerLenkwinkel;
		boolean fahrgeschwindigkeitsÄnderung = fahrgeschwindigkeit != vorheriegeFahrgeschwindigkeit;

		if (lenkwinkelÄnderung || fahrgeschwindigkeitsÄnderung)
		{
			btSender.steuerdatenSenden(lenkwinkel, fahrgeschwindigkeit);

			vorheriegerLenkwinkel = lenkwinkel;
			vorheriegeFahrgeschwindigkeit = fahrgeschwindigkeit;
		}

		handler.postDelayed(this::sendTask, 250);
	}
}
