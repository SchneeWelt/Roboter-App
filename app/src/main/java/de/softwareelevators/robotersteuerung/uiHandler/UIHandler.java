package de.softwareelevators.robotersteuerung.uiHandler;


import android.bluetooth.BluetoothDevice;
import android.view.View;
import de.softwareelevators.robotersteuerung.networkHandler.BluetootSender;
import de.softwareelevators.robotersteuerung.networkHandler.NetworkSender;
import de.softwareelevators.robotersteuerung.networkHandler.UdpSender;
import de.softwareelevators.robotersteuerung.uiWidgets.Joystick;
import de.softwareelevators.robotersteuerung.Main;
import de.softwareelevators.robotersteuerung.util.Ausgabe;
import de.softwareelevators.robotersteuerung.util.MathTools;
import de.softwareelevators.robotersteuerung.util.NetworkAdapter;
import de.softwareelevators.robotersteuerung.util.Sendebegrenzer;

/**
 * Ein Adapter für alle UI Elemente der App. Die Elemente existieren hier als
 * Objekte und dessen Daten werden hier verarbeitet
 */
@Deprecated
public class UIHandler extends NetworkAdapter implements Joystick.JoystickListener
{
	private final Main main;
	private final UIElemente uiElemente;
	private Sendebegrenzer sendebegrenzer;


	/** Der derzeit aktive {@link NetworkSender}. Kann durch die UI vom {@link UIHandler}
	 * ausgetauscht werden. Dieses Objekt wird von Objekten dieser Klasse zum Senden und Empfangen
	 * von Daten über ein Netzwerk verwendet */
	private NetworkSender networkSender;

	/**
	 * Die Klasse {@link NetworkSender} muss vor Erzeugung eines
	 * Objektes dieser Klasse als Objekt in {@link Main} intialisiert
	 * worden sein
	 *
	 * @param main
	 */
	public UIHandler(Main main)
	{
		this.main = main;

		uiElemente = new UIElemente(main, this);

		// Im Bluetooth Modus starten
		onNetworkHandlerChanged(new BluetootSender(main));
	}

	/**
	 * Wird immer dann geworfen, wenn der aktive {@link NetworkSender} durch das
	 * UI gewechselt wurde. Das kommt dem Umstellen von Bluetooth auf W-Lan bzw von
	 * W-Lan auf Bluetooth gleich
	 *
	 * @param networkSender Der neue Handler
	 */
	public void onNetworkHandlerChanged(NetworkSender networkSender)
	{
		// Den neuen NetworkHandler globale für die weitere Verwendung in dieser Klasse
		// speichern
		this.networkSender = networkSender;

		// Falls alter sendebegrenzer aktiv -> diesen Stoppen
		if (sendebegrenzer != null)
			sendebegrenzer.stop();

		updateIpButton(networkSender);

		/* Sendebegrenzer zum Senden der Daten mit neuem Handler initialisieren */
		sendebegrenzer = new Sendebegrenzer(networkSender);

		// Connect Button mit Networkhandler verknüpfen
		uiElemente.getConnectButton().setOnClickListener((l) -> {Ausgabe.print("Connect Button pressed"); networkSender.connect();});

		// Diesem Objekt ermöglichen auf Dateneingang, Verbindungsauf und -abbau
		// zu reagieren
		networkSender.setNetworkConnectionStateListener(this);
	}

	private void updateIpButton(NetworkSender networkSender)
	{
		// Sichtbarkeit IP Config Button ändern
		if (networkSender instanceof UdpSender)
		{
			uiElemente.getIpButton().setVisibility(View.VISIBLE);

			//
		}
		else
			uiElemente.getIpButton().setVisibility(View.INVISIBLE);
	}

	@Override
	public void onDataReceived(String data)
	{
		// Daten vom Roboter erhalten, was tun?:

		// Roboter kann aktuell keine Daten zurückschicken
		// Schnittstelle daher nicht implementiert
	}

	/**
	 * Wird geworfen, wenn sich ein remote Gerät mit diesem Gerät,
	 * dieser App verbunden hat
	 *
	 * @param connectedDevice
	 */
	@Override
	public void onConnectionEstablished(BluetoothDevice connectedDevice)
	{
		// Ui Aktualisieren
		main.getMainActivity().runOnUiThread(() -> uiElemente.getConnectionStateDisplay().onConnectionEstablished());
		main.getMainActivity().runOnUiThread(() -> uiElemente.getConnectButton().onConnectionEstablished(networkSender));

		// Sendebegrenzer für die nun kommende Datenübertragung starten
		sendebegrenzer.start();
	}

	@Override
	public void onDisconnect()
	{
		// Ui aktualisieren
		main.getMainActivity().runOnUiThread(() -> uiElemente.getConnectButton().onDisconect(networkSender));
		main.getMainActivity().runOnUiThread(() -> uiElemente.getConnectionStateDisplay().onDisconnect());

		// Sendebegrenzer stoppen, da jetzt ja kein Ziel mehr existiert, mit dem kommuniziert werden müsste
		sendebegrenzer.stop();
	}

	@Override
	public void onJoyStickMoved(float xPercent, float yPercent)
	{
		// Lenkwinkel berechnen. Wertebereich: [0;180] (rechte Hälfte), [-0;-180] (linke Hälfte)
		float lenkwinkel = MathTools.lenkwinkelBerechnen(xPercent, yPercent);

		// Wertebereich: [0; 1]
		float fahrgeschwindigkeit = MathTools.fahrgeschwindigkeitBerechnen(xPercent, yPercent);

		// Und in der UI aktualisieren
		uiElemente.getSteeringDataDisplay().updateContent(lenkwinkel, fahrgeschwindigkeit);

		// Wenn remote Geräte verfügbar
		if (networkSender.isConnected())
		{
			// Aktualisierte Daten an remote Gerät schicken
			sendebegrenzer.lenkwinkelAktualisieren(lenkwinkel);
			sendebegrenzer.fahrgeschwindigkeitAktualisieren(fahrgeschwindigkeit);

			Ausgabe.print("Aktualisier Daten: " + lenkwinkel + " | " + fahrgeschwindigkeit);
		}
	}
}