package de.softwareelevators.robotersteuerung.steuerung;

import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.networkAdapter.BluetoothAdapter;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkAdapter;
import de.softwareelevators.robotersteuerung.uiAdapter.UIAdapter;
import de.softwareelevators.robotersteuerung.util.ConnectionStateStorage;


/**
 * Besteht aus Adaptern und bildet die oberste Ebene der App. Wird direkt
 * aus der MainActivity heraus gestartet. Dient als Abstraktionsschicht
 * zwischen Anwendungslogik und der standart Android App Logik schicht
 * aus {@link MainActivity}
 */
public class Main
{
	private MainActivity mainActivity;
	private final UIAdapter uiAdapter;
	private final NetworkAdapter networkAdapter;


	private final ConnectionStateStorage connectionStateStorage;


//	private Sendebegrenzer sendebegrenzer;
//	private VertikalerRegler geschwindigkeitsregler;

	public Main(MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;

		/* Netzwerkcontroller einrichten */
//		networkAdapter = new WLanAdapter();
		networkAdapter = new BluetoothAdapter("ESP32", mainActivity);;

		die app sollte für bt jetzt endlich wieder funktionieren! -> testen!

		// Steuerung initialisieren
		uiAdapter = new UIAdapter(this, networkAdapter);

		// Hierrüber kann der UI Adapter erkennen, ob eine Verbindung zu einem Remote Gerät besteht
		connectionStateStorage = new ConnectionStateStorage();
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

	public NetworkAdapter getNetworkAdapter()
	{
		return networkAdapter;
	}

	public ConnectionStateStorage getConnectionStateStorage()
	{
		return connectionStateStorage;
	}

	public MainActivity getMainActivity()
	{
		return mainActivity;
	}
}
