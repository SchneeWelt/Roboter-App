package de.softwareelevators.robotersteuerung.util;


import android.widget.Button;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import de.softwareelevators.robotersteuerung.steuerung.Joystick;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.steuerung.VertikalerRegler;

/**
 * Kümmert sich um die UI Elemente. Also Aktualiserungen
 * und durch UI Elemente ausgelöste Events. Events werden
 * in der Regel die MainAcitivity ausgelagert.
 * <p>
 * Ziel: UI Logik verbergen, aber wichtige Events weiterhin
 * gut sichtbar machen.
 */
public class UiHandler
{
	private final int GRÜN, GRAU;

	private MainActivity mainActivity;

	private Button connectButton;
	private TextView joystickInfo;
	private TextView verbindungsstatus;


	private float aktuellerLenkwinkel, aktuelleFahrgeschwindigkeit;


	public UiHandler(MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;

		GRÜN = ContextCompat.getColor(mainActivity, R.color.grün);
		GRAU = ContextCompat.getColor(mainActivity, R.color.grau);

		Joystick joystick = mainActivity.findViewById(R.id.joystick);
		joystick.setJoystickListener(mainActivity);

		VertikalerRegler vertikalerRegler = mainActivity.findViewById(R.id.ges_regler);
		vertikalerRegler.setVertikalerReglerListener(mainActivity);

		joystickInfo = mainActivity.findViewById(R.id.controll_info);
		joystickInfo.setText("...");

		connectButton = mainActivity.findViewById(R.id.connect_button);
		connectButton.setOnClickListener((view) -> mainActivity.getBluetoothController().connect());

		verbindungsstatus = mainActivity.findViewById(R.id.verbindungsstatus);
		verbindungsstatus.setText(R.string.nicht_verbunden);
		verbindungsstatus.setTextColor(GRAU);

		/* Sollte Deamon sein! Sollte keinen direkten Lambda bekommen, lieber Methodenreferenz */
		new Thread(() -> {
			mainActivity.runOnUiThread(() -> updateInfoDisplay(aktuellerLenkwinkel, aktuelleFahrgeschwindigkeit));

			try{
			Thread.sleep(500);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}).start();
	}


	/**
	 * Aktualisiert die UI Elemente nachdem sich ein BT Gerät mit diesem
	 * Gerät verbunden hat. Unteranderem werden neue onClick Listener
	 * vergeben und Text auf UI Elementen geändert.
	 */
	public void onConnect()
	{
		mainActivity.runOnUiThread(this::onConnect_ButtonUpdate);
		mainActivity.runOnUiThread(this::onConnect_StatusUpdate);
	}

	public void onDisconnect()
	{
		mainActivity.runOnUiThread(this::onDisconnect_ButtonUpdate);
		mainActivity.runOnUiThread(this::onDisconnect_StatusUpdate);
	}

	public void updateLenkwinkel(float lenkwinkel)
	{
		this.aktuellerLenkwinkel = lenkwinkel;
	}

	public void updateFahrgeschwindigkeit(float fahregeschwindigkeit)
	{
		this.aktuelleFahrgeschwindigkeit = fahregeschwindigkeit;
	}

	private void updateInfoDisplay(float lenkwinkel, float fahrgeschwindigkeit)
	{
		String info = String.format("Fahrgeschwindigkeit: %.2f%%\nLenkwinkel: %.2f°", fahrgeschwindigkeit, lenkwinkel);
		joystickInfo.setText(info);
	}

	private void onDisconnect_ButtonUpdate()
	{
		connectButton.setText(R.string.verbinden);
		connectButton.setOnClickListener((view) -> mainActivity.getBluetoothController().connect());
	}

	private void onDisconnect_StatusUpdate()
	{
		verbindungsstatus.setTextColor(GRAU);
		verbindungsstatus.setText(R.string.nicht_verbunden);
	}

	private void onConnect_ButtonUpdate()
	{
		connectButton.setText(R.string.verbindung_trennen);
		connectButton.setOnClickListener((view) -> mainActivity.getBluetoothController().disconnect());
	}

	private void onConnect_StatusUpdate()
	{
		verbindungsstatus.setText(R.string.verbunden);
		verbindungsstatus.setTextColor(GRÜN);
	}
}