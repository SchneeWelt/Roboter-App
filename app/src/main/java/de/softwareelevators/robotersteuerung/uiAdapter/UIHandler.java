package de.softwareelevators.robotersteuerung.uiAdapter;


import android.bluetooth.BluetoothDevice;
import android.widget.Button;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkHandler;
import de.softwareelevators.robotersteuerung.steuerung.Joystick;
import de.softwareelevators.robotersteuerung.steuerung.Main;
import de.softwareelevators.robotersteuerung.util.Ausgabe;
import de.softwareelevators.robotersteuerung.util.NetworkAdapter;
import de.softwareelevators.robotersteuerung.util.Sendebegrenzer;

/**
 * Ein Adapter für alle UI Elemente der App. Die Elemente existieren hier als
 * Objekte und dessen Daten werden hier verarbeitet
 */
public class UIHandler extends NetworkAdapter implements Joystick.JoystickListener
{
	private final Main main;
	private final UIElemente uiElemente;
	private Sendebegrenzer sendebegrenzer;

	/** Objekte dieser Klasse senden Daten über dieses Objekt */
	private NetworkHandler networkHandler;

	/**
	 * Die Klasse {@link NetworkHandler} muss vor Erzeugung eines
	 * Objektes dieser Klasse als Objekt in {@link Main} intialisiert
	 * worden sein
	 *
	 * @param main
	 */
	public UIHandler(Main main)
	{
		this.main = main;

		uiElemente = new UIElemente(main, this);

		onNetworkHandlerChanged(main.getNetworkHandler());
	}

	/**
	 * Wird immer dann geworfen, wenn der aktive {@link NetworkHandler} durch das
	 * UI gewechselt wurde. Das kommt dem Umstellen von Bluetooth auf W-Lan bzw von
	 * W-Lan auf Bluetooth gleich
	 *
	 * @param networkHandler Der neue Adapter
	 */
	public void onNetworkHandlerChanged(NetworkHandler networkHandler)
	{
		// Den neuen NetworkHandler globale für die weitere Verwendung in dieser Klasse
		// speichern
		this.networkHandler = networkHandler;

//		rufe methode über neues ui element auf. Einen toggle button will ich haben.
//		dieser neue button muss vor diesem aufruf einen networkAdapter.disconnect();
//		befehl ausführen

		if (sendebegrenzer != null)
			sendebegrenzer.stop();

		/* Sendebegrenzer zum Senden der Daten initialisieren und starten */
		sendebegrenzer = new Sendebegrenzer(networkHandler);
		sendebegrenzer.start();	// Macht diese Zeile hier sinn?

		// Dem UI Handler ermöglichen auf Dateneingang und Verbindungsauf und -abbau
		// zu reagieren
		networkHandler.setDataReceivedListener(this);
		networkHandler.setNetworkConnectionStateListener(this);
	}

	@Override
	public void onDataReceived(String data)
	{
		// Daten vom Roboter erhalten, was tun?:

		// Roboter kann aktuell keine Daten zurückschicken
		// Schnittstelle daher nicht implementiert
	}

	@Override
	public void onJoyStickMoved(float xPercent, float yPercent)
	{
		/* Lenkwinkel berechnen. Wertebereich: [0;180] (rechte Hälfte), [-0;-180] (linke Hälfte) */
		float lenkwinkel = lenkwinkelBerechnen(xPercent, yPercent);
		float fahrgeschwindigkeit = fahrgeschwindigkeitBerechnen(xPercent, yPercent);

		updateSteeringDataDisplay(lenkwinkel, fahrgeschwindigkeit);

		if (networkHandler.isConnected())
		{
			sendebegrenzer.lenkwinkelAktualisieren(lenkwinkel);
			sendebegrenzer.fahrgeschwindigkeitAktualisieren(fahrgeschwindigkeit);

			Ausgabe.print("Aktualisier Daten: " + lenkwinkel + " | " + fahrgeschwindigkeit);
		}
	}


	/**
	 * Wird geworfen, wenn sich eine remote Gerät mit diesem Gerät,
	 * dieser App verbunden hat
	 *
	 * @param connectedDevice
	 */
	@Override
	public void onConnect(BluetoothDevice connectedDevice)
	{
		main.getMainActivity().runOnUiThread(this::onConnect_ButtonUpdate);
		main.getMainActivity().runOnUiThread(this::onConnect_StatusUpdate);

//		sendebegrenzer.start();
	}

	@Override
	public void onDisconnect()
	{
		main.getMainActivity().runOnUiThread(this::onDisconnect_ButtonUpdate);
		main.getMainActivity().runOnUiThread(this::onDisconnect_StatusUpdate);

		sendebegrenzer.stop();
	}

	// ----------------------------------------------------------------------------------------

	private void updateSteeringDataDisplay(float lenkwinkel, float fahrgeschwindigkeit)
	{
		// Wäre eigentlich cooler, würde die text aktualisierung auch event basiert laufen

		uiElemente.getSteeringDataDisplay().updateContent(lenkwinkel, fahrgeschwindigkeit);
	}

	private void onDisconnect_ButtonUpdate()
	{
		Button connectButton = uiElemente.getConnectButton();

		connectButton.setText(R.string.verbinden);
		connectButton.setOnClickListener((view) -> main.getNetworkHandler().connect());
	}

	private void onDisconnect_StatusUpdate()
	{
		uiElemente.getConnectionStateDisplay().onDisconnect();
	}

	private void onConnect_ButtonUpdate()
	{
		Button connectButton = uiElemente.getConnectButton();
//		connectButton.onConnect();

//		auch button braucht eigene klasse

		connectButton.setText(R.string.verbindung_trennen);
//		connectButton.setOnClickListener((view) -> networkAdapter.disconnect());
	}

	private void onConnect_StatusUpdate()
	{
//		verbindungsstatusdisplay.setText(R.string.verbunden);
//		verbindungsstatusdisplay.setTextColor(GRÜN);
	}

	private float lenkwinkelBerechnen(float xPercent, float yPercent)
	{
		float lenkwinkel = (float) Math.toDegrees(Math.atan2(yPercent, xPercent));

		/* Koordinatensystem drehen: oben: 0°, 90°: rechts, 180°: unten, -90°:links.
		 * Das ist richtig so und sorgt dafür, dass geradeausfahren gleichbedeutend
		 * zu Joystick nach oben bewegen ist. */
		lenkwinkel += 90;

		/* Verschobenes Koordinatensystem jetzt noch normalisieren, damit die Werte innerhalb
		von +-180 Grad bleiben - also so sind, wie oben beschrieben */
		if (lenkwinkel > 180) lenkwinkel -= 360;
		if (lenkwinkel < -180) lenkwinkel += 360;

		return lenkwinkel;
	}

	private float fahrgeschwindigkeitBerechnen(float xPercent, float yPercent)
	{
		return (float) Math.sqrt(xPercent * xPercent + yPercent * yPercent);
	}
}