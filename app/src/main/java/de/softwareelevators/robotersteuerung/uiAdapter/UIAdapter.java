package de.softwareelevators.robotersteuerung.uiAdapter;


import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.steuerung.Joystick;
import de.softwareelevators.robotersteuerung.steuerung.Steuereinheit;

/**
 * Ein Adapter für alle UI Elemente der App. Die Elemente existieren hier als
 * Objekte und dessen Daten werden hier verarbeitet
 */
public class UIAdapter implements Joystick.JoystickListener
{
//	private float lenkwinkel, fahrgeschwindigkeit;

	private final MainActivity mainActivity;
	private final Steuereinheit steuereinheit;

	private final UIElemente uiElemente;


	/**
	 * @param steuereinheit
	 * @param mainActivity
	 */
	public UIAdapter(Steuereinheit steuereinheit, MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;
		this.steuereinheit = steuereinheit;

		uiElemente = new UIElemente(steuereinheit, mainActivity);
		uiElemente.initButtons();

//		steuereinheit.getNetworkAdapter();
	}

	@Override
	public void onJoyStickMoved(float xPercent, float yPercent)
	{
		/* Lenkwinkel berechnen. Wertebereich: [0;180] (rechte Hälfte), [-0;-180] (linke Hälfte) */
		float lenkwinkel = lenkwinkelBerechnen(xPercent, yPercent);
		float fahrgeschwindigkeit = fahrgeschwindigkeitBerechnen(xPercent, yPercent);

		updateInfoDisplay(lenkwinkel, fahrgeschwindigkeit);

		if (steuereinheit.getConnectionState().isConnectionEstablished())
			sendebegrenzer.lenkwinkelAktualisieren(lenkwinkel);
	}

	/**
	 * Dieses Event wird geworfen, sobald sich ein Gerät mit diesem
	 * System (Dieser App) verbindet.
	 * <p>
	 * Das Event wird dazu verwendet, die Widgets des UIAdapters
	 * zu aktualisieren
	 */
	public void onDeviceConnected()
	{
		mainActivity.runOnUiThread(this::onConnect_ButtonUpdate);
		mainActivity.runOnUiThread(this::onConnect_StatusUpdate);
	}

	public void onDisconnect()
	{
		mainActivity.runOnUiThread(this::onDisconnect_ButtonUpdate);
		mainActivity.runOnUiThread(this::onDisconnect_StatusUpdate);
	}

	public void fahrgeschwindigkeitAktualisieren(float fahregeschwindigkeit)
	{
		this.fahrgeschwindigkeit = fahregeschwindigkeit;

		updateInfoDisplay(lenkwinkel, fahrgeschwindigkeit);
	}

	private void updateInfoDisplay(float lenkwinkel, float fahrgeschwindigkeit)
	{
		String info = String.format("Fahrgeschwindigkeit: %.2f%%\nLenkwinkel: %.2f°", fahrgeschwindigkeit, lenkwinkel);

		uiElemente.getSteeringDataDisplay().setText(info);
	}

	// --

	private void onDisconnect_ButtonUpdate()
	{
		connectButton.setText(R.string.verbinden);
		connectButton.setOnClickListener((view) -> networkAdapter.connect());
	}

	private void onDisconnect_StatusUpdate()
	{
		verbindungsstatusdisplay.setTextColor(GRAU);
		verbindungsstatusdisplay.setText(R.string.nicht_verbunden);
	}

	private void onConnect_ButtonUpdate()
	{
		connectButton.setText(R.string.verbindung_trennen);
		connectButton.setOnClickListener((view) -> networkAdapter.disconnect());
	}

	private void onConnect_StatusUpdate()
	{
		verbindungsstatusdisplay.setText(R.string.verbunden);
		verbindungsstatusdisplay.setTextColor(GRÜN);
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