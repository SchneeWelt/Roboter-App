package de.softwareelevators.robotersteuerung.uiAdapter;


import android.bluetooth.BluetoothDevice;
import android.widget.Button;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.networkAdapter.DataReceivedListener;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkAdapter;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkConnectionStateListener;
import de.softwareelevators.robotersteuerung.steuerung.Joystick;
import de.softwareelevators.robotersteuerung.steuerung.Main;
import de.softwareelevators.robotersteuerung.util.Sendebegrenzer;

/**
 * Ein Adapter für alle UI Elemente der App. Die Elemente existieren hier als
 * Objekte und dessen Daten werden hier verarbeitet
 */
public class UIAdapter implements Joystick.JoystickListener, NetworkConnectionStateListener, DataReceivedListener
{
	private final Main main;
	private final UIElemente uiElemente;
	private final Sendebegrenzer sendebegrenzer;

	/**
	 * @param main
	 * @param networkAdapter Über dieses Objekt kann diese Klasse
	 *                       Daten senden
	 */
	public UIAdapter(Main main, NetworkAdapter networkAdapter)
	{
		this.main = main;

		uiElemente = new UIElemente(main, main.getMainActivity(), this);

		/* Sendebegrenzer zum senden der Daten initialisieren und starten */
		sendebegrenzer = new Sendebegrenzer(networkAdapter);

		// Dem UI Handler ermöglichen auf Dateneingang und Verbindungsauf und -abbau
		// zu reagieren
		networkAdapter.setDataReceivedListener(this);
		networkAdapter.setNetworkConnectionStateListener(this);
	}

	@Override
	public void onDataReceived(String data)
	{
		// Daten vom Roboter erhalten, was tun?:
	}

	@Override
	public void onJoyStickMoved(float xPercent, float yPercent)
	{
		/* Lenkwinkel berechnen. Wertebereich: [0;180] (rechte Hälfte), [-0;-180] (linke Hälfte) */
		float lenkwinkel = lenkwinkelBerechnen(xPercent, yPercent);
		float fahrgeschwindigkeit = fahrgeschwindigkeitBerechnen(xPercent, yPercent);

		updateSteeringDataDisplay(lenkwinkel, fahrgeschwindigkeit);

		if (main.getConnectionStateStorage().isConnectionEstablished())
		{
			sendebegrenzer.lenkwinkelAktualisieren(lenkwinkel);
			sendebegrenzer.fahrgeschwindigkeitAktualisieren(fahrgeschwindigkeit);
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

		main.getConnectionStateStorage().setConnectionEstablished(true);

		sendebegrenzer.start();
	}

	@Override
	public void onDisconnect()
	{
		main.getMainActivity().runOnUiThread(this::onDisconnect_ButtonUpdate);
		main.getMainActivity().runOnUiThread(this::onDisconnect_StatusUpdate);

		main.getConnectionStateStorage().setConnectionEstablished(false);

		sendebegrenzer.stop();
	}

	// ----------------------------------------------------------------------------------------

	private void updateSteeringDataDisplay(float lenkwinkel, float fahrgeschwindigkeit)
	{
		// Wäre eigentlich cooler, würde die text aktualisierung auch event basiert laufen

		String info = String.format("Fahrgeschwindigkeit: %.2f%%\nLenkwinkel: %.2f°", fahrgeschwindigkeit, lenkwinkel);
		uiElemente.getSteeringDataDisplay().setText(info);
	}

	private void onDisconnect_ButtonUpdate()
	{
		Button connectButton = uiElemente.getConnectButton();

		connectButton.setText(R.string.verbinden);
		connectButton.setOnClickListener((view) -> main.getNetworkAdapter().connect());
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