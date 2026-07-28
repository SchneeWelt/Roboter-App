package de.softwareelevators.robotersteuerung.steuerung;

import android.bluetooth.BluetoothDevice;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkAdapter;
import de.softwareelevators.robotersteuerung.networkAdapter.WLanAdapter;
import de.softwareelevators.robotersteuerung.util.UiUpdater;


/**
 * Besteht aus Adaptern und bildet die oberste Ebene der App. Wird direkt
 * aus der MainActivity heraus gestartet
 */
public class Steuereinheit implements VertikalerRegler.SliderListener, Joystick.JoystickListener, NetworkAdapter.DataReceivedListener, NetworkAdapter.NetworConnectionStateListener
{
	/** True, wenn Verbindung zu Roboter über NetworkController besteht. */
	private boolean verbindungHergestellt;

	private final UiUpdater uiUpdater;
	private final NetworkAdapter networkAdapter;


//	private Sendebegrenzer sendebegrenzer;
//	private VertikalerRegler geschwindigkeitsregler;

	public Steuereinheit(MainActivity mainActivity)
	{
		/* Netzwerkcontroller einrichten */
		networkAdapter = new WLanAdapter();
//		networkAdapter = new BluetoothController("ESP32", this,this, mainActivity);;

		/* Steuerelemtschnittstellen der Views mit der Steuereinheit verbinden */
//		joystick.setJoystickListener(this);
//		geschwindigkeitsregler.setSliderListener(this);

		/* UI Handler initialisieren */
		uiUpdater = new UiUpdater(this);

		/* Sendebegrenzer zum senden der Daten initialisieren und starten */
//		sendebegrenzer = new Sendebegrenzer(activeNetworkController);
//		sendebegrenzer.start();
	}


//	public Steuereinheit(Joystick joystick, VertikalerRegler geschwindigkeitsregler, MainActivity mainActivity)
//	{
//		this.joystick = joystick;
//		this.geschwindigkeitsregler = geschwindigkeitsregler;
//
//		/* Netzwerkcontroller einrichten */
////		wLanController = new WLanController();
//		NetworkController activeNetworkController = new BluetoothController("ESP32", this,this, mainActivity);;
//
//		/* Steuerelemtschnittstellen der Views mit der Steuereinheit verbinden */
//		joystick.setJoystickListener(this);
//		geschwindigkeitsregler.setSliderListener(this);
//
//		/* UI Handler initialisieren */
//		uiUpdater = new UiUpdater(activeNetworkController, mainActivity);
//
//		/* Sendebegrenzer zum senden der Daten initialisieren und starten */
//		sendebegrenzer = new Sendebegrenzer(activeNetworkController);
//		sendebegrenzer.start();
//	}

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
