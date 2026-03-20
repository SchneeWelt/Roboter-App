package de.softwareelevators.robotersteuerung.util;


import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import de.softwareelevators.robotersteuerung.Joystick;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;

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


	public UiHandler(MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;

		GRÜN = ContextCompat.getColor(mainActivity, R.color.grün);
		GRAU = ContextCompat.getColor(mainActivity, R.color.grau);

		Joystick joystick = mainActivity.findViewById(R.id.joystick);
		joystick.setJoystickListener(mainActivity);

		joystickInfo = mainActivity.findViewById(R.id.joystick_info);
		joystickInfo.setText("...");

		connectButton = mainActivity.findViewById(R.id.connect_button);
		connectButton.setOnClickListener((view) -> mainActivity.getBluetoothController().connect());

		verbindungsstatus = mainActivity.findViewById(R.id.verbindungsstatus);
		verbindungsstatus.setText(R.string.nicht_verbunden);
		verbindungsstatus.setTextColor(GRAU);
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

	public void updateJoystickInfo(float lenkwinkel, float fahrgeschwindigkeit)
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