package de.softwareelevators.robotersteuerung.steuerung;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.networkAdapter.BluetoothAdapter;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkAdapter;
import de.softwareelevators.robotersteuerung.uiAdapter.UIAdapter;


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

	/** Der derzeit aktive Network Adapter. Kann durch die UI vom {@link UIAdapter}
	 * getausch werden */
	private NetworkAdapter networkAdapter;


//	private final ConnectionStateStorage connectionStateStorage;


//	private Sendebegrenzer sendebegrenzer;
//	private VertikalerRegler geschwindigkeitsregler;

	public Main(MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;


		/* Netzwerkcontroller einrichten */
//		networkAdapter = new WLanAdapter();
		// Über dieses Objekt läuft die Kommunikatino zum Roboter
		networkAdapter = new BluetoothAdapter(this);

//		die app sollte für bt jetzt endlich wieder funktionieren! -> testen!

		// Dieses Objekt verwendet das obere Objekt, um Daten an den Roboter
		// zu senden. Dafür ermöglicht es die spezifikation dazu, was gesendet
		// werden solle
		uiAdapter = new UIAdapter(this, networkAdapter);

		// Hierrüber kann der UI Adapter erkennen, ob eine Verbindung zu einem Remote Gerät besteht
//		connectionStateStorage = new ConnectionStateStorage();

		/* Nach allen möglichen Runtime permissions fragen. Der Trick: Wird eine Permmission einmal
		erlaubt, so merkt sich das die App und solange diese dann nicht neu gestartet order zurückgesetzt
		wird, muss nie wieder diese Permission neu eingeholt werden. Heißt der nachfolgende Teil nervt einmal
		und dann nie wieder */

		hohleBtConnectBerechtigung();
	}

	/**
	 * Nach der Berechtigungsanfrage vergeht noch etwas zeit, bis die berechtigung erteilt wurde. Man kann
	 * da dann mit callbacks drauf reagieren, sollte aber in jedemfall erstmal mit return aus dem aktuellen
	 * Programmablauf rausspringen
	 *
	 * @return true, wenn die berechtigung geholt werden musste und der weitere Programmablauf daher
	 * gestoppt werden sollte
	 */
	public boolean hohleBtConnectBerechtigung()
	{
		// Auch das Intent braucht eine Erlaubnis geöffnet zu werden...
		if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.BLUETOOTH_CONNECT)	!= PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(
					mainActivity,
					new String[]{Manifest.permission.BLUETOOTH_CONNECT},
					1001
			);

			return true; // WICHTIG: Intent erst starten, wenn Permission da ist. Deshalb hier "warten" -> zweiter Methodenaufruf erforderlich
			// möglich durch erneutes anklicken des connect buttons?
		}

		return false;
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

//	public ConnectionStateStorage getConnectionStateStorage()
//	{
//		return connectionStateStorage;
//	}

	public MainActivity getMainActivity()
	{
		return mainActivity;
	}

	public void setNetworkAdapter(NetworkAdapter networkAdapter)
	{
		this.networkAdapter = networkAdapter;
	}
}
