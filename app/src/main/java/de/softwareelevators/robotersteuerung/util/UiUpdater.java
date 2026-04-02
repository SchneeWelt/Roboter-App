package de.softwareelevators.robotersteuerung.util;


import android.widget.Button;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.networkController.NetworkController;

/**
 * Kümmert sich um die aktualisierung der auf der UI angezeigten
 * Daten. Übernimt aus diesem Grund auch die Eventverarbeitung
 * von kleineren Elementen der UI. Joystick und Geschindigkeitsregler
 * sind aber ganz klar ausgenommen.
 */
public class UiUpdater
{
	private final int GRÜN, GRAU;

	private MainActivity mainActivity;

	private Button connectButton;
	private TextView stuerdatendisplay;
	private TextView verbindungsstatusdisplay;
	private NetworkController networkController;


	private float lenkwinkel, fahrgeschwindigkeit;


	/**
	 *
	 * @param networkController Der verwendete NetworkController. Wird über diese Klasse
	 *                          aktiviert und deaktiviert.
	 * @param mainActivity
	 */
	public UiUpdater(NetworkController networkController, MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;
		this.networkController = networkController;

		GRÜN = ContextCompat.getColor(mainActivity, R.color.grün);
		GRAU = ContextCompat.getColor(mainActivity, R.color.grau);

		stuerdatendisplay = mainActivity.findViewById(R.id.steuerdaten_display);
		stuerdatendisplay.setText("...");

		connectButton = mainActivity.findViewById(R.id.connect_button);
		connectButton.setOnClickListener((view) -> networkController.connect());

		verbindungsstatusdisplay = mainActivity.findViewById(R.id.verbindungsstatus);
		verbindungsstatusdisplay.setText(R.string.nicht_verbunden);
		verbindungsstatusdisplay.setTextColor(GRAU);
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

	public void lenkwinkeltAktualisieren(float lenkwinkel)
	{
		this.lenkwinkel = lenkwinkel;

		updateInfoDisplay(lenkwinkel, fahrgeschwindigkeit);
	}

	public void fahrgeschwindigkeitAktualisieren(float fahregeschwindigkeit)
	{
		this.fahrgeschwindigkeit = fahregeschwindigkeit;

		updateInfoDisplay(lenkwinkel, fahrgeschwindigkeit);
	}

	private void updateInfoDisplay(float lenkwinkel, float fahrgeschwindigkeit)
	{
		String info = String.format("Fahrgeschwindigkeit: %.2f%%\nLenkwinkel: %.2f°", fahrgeschwindigkeit, lenkwinkel);
		stuerdatendisplay.setText(info);
	}

	// --

	private void onDisconnect_ButtonUpdate()
	{
		connectButton.setText(R.string.verbinden);
		connectButton.setOnClickListener((view) -> networkController.connect());
	}

	private void onDisconnect_StatusUpdate()
	{
		verbindungsstatusdisplay.setTextColor(GRAU);
		verbindungsstatusdisplay.setText(R.string.nicht_verbunden);
	}

	private void onConnect_ButtonUpdate()
	{
		connectButton.setText(R.string.verbindung_trennen);
		connectButton.setOnClickListener((view) -> networkController.disconnect());
	}

	private void onConnect_StatusUpdate()
	{
		verbindungsstatusdisplay.setText(R.string.verbunden);
		verbindungsstatusdisplay.setTextColor(GRÜN);
	}
}