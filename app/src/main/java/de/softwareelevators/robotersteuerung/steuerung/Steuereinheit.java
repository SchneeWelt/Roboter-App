package de.softwareelevators.robotersteuerung.steuerung;

import android.bluetooth.BluetoothDevice;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkAdapter;
import de.softwareelevators.robotersteuerung.networkAdapter.WLanAdapter;
import de.softwareelevators.robotersteuerung.uiAdapter.UIAdapter;
import de.softwareelevators.robotersteuerung.util.ConnectionState;


/**
 * Besteht aus Adaptern und bildet die oberste Ebene der App. Wird direkt
 * aus der MainActivity heraus gestartet
 */
public class Steuereinheit
{

	private final UIAdapter uiAdapter;
	private final NetworkAdapter networkAdapter;
	private final ConnectionState connectionState;


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
		uiAdapter = new UIAdapter(this, mainActivity);

		/* Sendebegrenzer zum senden der Daten initialisieren und starten */
//		sendebegrenzer = new Sendebegrenzer(activeNetworkController);
//		sendebegrenzer.start();

		connectionState = new ConnectionState();
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

		uiAdapter.fahrgeschwindigkeitAktualisieren(fahrgeschwindigkeit);
		sendebegrenzer.fahrgeschwindigkeitAktualisieren(fahrgeschwindigkeit);
	}

	@Override
	public void onDataReceived(String data)
	{
		/* Wird aufgerufen, wenn über den BluetoothController - also vom Roboter - Daten empfangen wurden */
	}

	@Override
	public void onConnect(BluetoothDevice connectedDevice)
	{
		uiAdapter.onDeviceConnected();

		connectionState.setConnectionEstablished(true);
	}

	@Override
	public void onDisconnect()
	{
		uiAdapter.onDisconnect();

		connectionState.setConnectionEstablished(false);
	}

	public NetworkAdapter getNetworkAdapter()
	{
		return networkAdapter;
	}

	public UIAdapter getUiAdapter()
	{
		return uiAdapter;
	}

	public ConnectionState getConnectionState()
	{
		return connectionState;
	}
}
