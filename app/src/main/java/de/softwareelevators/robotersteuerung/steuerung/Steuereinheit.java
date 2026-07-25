package de.softwareelevators.robotersteuerung.steuerung;

import android.bluetooth.BluetoothDevice;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.networkController.BluetoothController;
import de.softwareelevators.robotersteuerung.networkController.NetworkController;
import de.softwareelevators.robotersteuerung.networkController.WLanController;
import de.softwareelevators.robotersteuerung.util.Sendebegrenzer;
import de.softwareelevators.robotersteuerung.util.UiUpdater;

public class Steuereinheit implements VertikalerRegler.SliderListener, Joystick.JoystickListener, NetworkController.DataReceivedListener, NetworkController.NetworConnectionStateListener
{
	/** True, wenn Verbindung zu Roboter über NetworkController besteht. */
	private boolean verbindungHergestellt;

	private Joystick joystick;
	private UiUpdater uiUpdater;
	private Sendebegrenzer sendebegrenzer;
	private VertikalerRegler geschwindigkeitsregler;


	public Steuereinheit(Joystick joystick, VertikalerRegler geschwindigkeitsregler, MainActivity mainActivity)
	{
		this.joystick = joystick;
		this.geschwindigkeitsregler = geschwindigkeitsregler;

		/* Netzwerkcontroller einrichten */
//		wLanController = new WLanController();
		NetworkController activeNetworkController = new BluetoothController("ESP32", this,this, mainActivity);;

		/* Steuerelemtschnittstellen der Views mit der Steuereinheit verbinden */
		joystick.setJoystickListener(this);
		geschwindigkeitsregler.setSliderListener(this);

		baue ges regler zurückl

		/* UI Handler initialisieren */
		uiUpdater = new UiUpdater(activeNetworkController, mainActivity);

		/* Sendebegrenzer zum senden der Daten initialisieren und starten */
		sendebegrenzer = new Sendebegrenzer(activeNetworkController);
		sendebegrenzer.start();
	}

	@Override
	public void onSliderMoved(float sliderPositionPercent)
	{
		/* Wertebereich: [0; 100] */
		float fahrgeschwindigkeit = sliderPositionPercent * 100;

		uiUpdater.fahrgeschwindigkeitAktualisieren(fahrgeschwindigkeit);
		sendebegrenzer.fahrgeschwindigkeitAktualisieren(fahrgeschwindigkeit);
	}

	@Override
	public void onJoyStickMoved(float xPercent, float yPercent)
	{
		/* Lenkwinkel berechnen. Wertebereich: [0;180] (rechte Hälfte), [-0;-180] (linke Hälfte) */
		float lenkwinkel = lenkwinkelBerechnen(xPercent, yPercent);

		uiUpdater.lenkwinkeltAktualisieren(lenkwinkel);

		if (verbindungHergestellt)
			sendebegrenzer.lenkwinkelAktualisieren(lenkwinkel);
	}

	@Override
	public void onDataReceived(String data)
	{
		/* Wird aufgerufen, wenn über den BluetoothController - also vom Roboter - Daten empfangen wurden */
	}

	@Override
	public void onConnect(BluetoothDevice connectedDevice)
	{
		uiUpdater.onConnect();

		verbindungHergestellt = true;
	}

	@Override
	public void onDisconnect()
	{
		uiUpdater.onDisconnect();

		verbindungHergestellt = false;
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
}
