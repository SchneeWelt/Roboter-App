package de.softwareelevators.robotersteuerung.util;


import android.os.Handler;
import android.os.Looper;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkAdapter;

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
	/** Die Daten, die bei einem Sendevorgang gesendet werden
	 * sollen. */
	private float lenkwinkel, fahrgeschwindigkeit;

	private float vorheriegerLenkwinkel, vorheriegeFahrgeschwindigkeit;


	private Handler handler;
	private BtSender btSender;

	public Sendebegrenzer(NetworkAdapter networkAdapter)
	{
		vorheriegerLenkwinkel = Integer.MIN_VALUE;
		vorheriegeFahrgeschwindigkeit = Integer.MIN_VALUE;

		btSender = new BtSender(networkAdapter);

		handler = new Handler(Looper.getMainLooper());
	}


	/**
	 * Startet den Sendevorgang und wiederhohlt ihn dann im gesetzten
	 * Intervall regelmäßig.
	 */
	public void start()
	{
		/* Methodenreferenz nutzen. Wichtig: Parameterliste der verwendeten Methode
		muss mit Parameterliste des erwarteten Lambdas übereinstimmen. Name der Methode
		ist egal, muss also nicht gleich sein. */

		handler.post(this::sendTask);
	}

	/** Beendet den wiederkehrenden Sendevorgang */
	public void stop()
	{
		handler.removeCallbacks(this::sendTask);
	}

	public void lenkwinkelAktualisieren(float lenkwinkel)
	{
		this.lenkwinkel = lenkwinkel;
	}

	public void fahrgeschwindigkeitAktualisieren(float fahrgeschwindigkeit)
	{
		this.fahrgeschwindigkeit = fahrgeschwindigkeit;
	}

	/** Hier wird definiert, wie die Daten an den Roboter gesendet werden sollen */
	private void sendTask()
	{
		/* Wertänderung seit letzem Send ermitteln */
		boolean lenkwinkelÄnderung = lenkwinkel != vorheriegerLenkwinkel;
		boolean fahrgeschwindigkeitsÄnderung = fahrgeschwindigkeit != vorheriegeFahrgeschwindigkeit;

		/* Nur bei Wertänderung senden -> Spamvermeidung */
		if (lenkwinkelÄnderung || fahrgeschwindigkeitsÄnderung)
		{
			btSender.steuerdatenSenden(lenkwinkel, fahrgeschwindigkeit);

			vorheriegerLenkwinkel = lenkwinkel;
			vorheriegeFahrgeschwindigkeit = fahrgeschwindigkeit;
		}

		handler.postDelayed(this::sendTask, 250);
	}
}
