package de.softwareelevators.robotersteuerung.steuerung;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.networkAdapter.BluetoothHandler;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkHandler;
import de.softwareelevators.robotersteuerung.uiAdapter.UIHandler;
import de.softwareelevators.robotersteuerung.util.Ausgabe;

import java.util.ArrayList;
import java.util.List;


/**
 * Besteht aus Adaptern und bildet die oberste Ebene der App. Wird direkt
 * aus der MainActivity heraus gestartet. Dient als Abstraktionsschicht
 * zwischen Anwendungslogik und der standart Android App Logik schicht
 * aus {@link MainActivity}
 */
public class Main
{
	private MainActivity mainActivity;

	public Main(MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;

		new UIHandler(this);

		/* Nach allen möglichen Runtime permissions fragen. Der Trick: Wird eine Permmission einmal
		erlaubt, so merkt sich das die App und solange diese dann nicht neu gestartet order zurückgesetzt
		wird, muss nie wieder diese Permission neu eingeholt werden. Heißt der nachfolgende Teil nervt einmal
		und dann nie wieder */

		if (hohleBerechtigungen())
			Ausgabe.print("Alle Berechtigungen erteilt");
		else
			Ausgabe.print("App Berechtitungen verweigert. Neustart empfohlen");
	}

	/**
	 * @return true, wenn keine Permission fehlte und der Programmablauf also nicht
	 * neu gestartet werden musss
	 */
	public boolean hohleBerechtigungen()
	{
		String[] permissions =
		{
				Manifest.permission.BLUETOOTH_CONNECT,
				Manifest.permission.BLUETOOTH_SCAN,
				Manifest.permission.BLUETOOTH_ADVERTISE,
//				Manifest.permission.NEARBY_WIFI_DEVICES
		};

		List<String> missing = new ArrayList<>();

		for (String p : permissions)
			if (ActivityCompat.checkSelfPermission(mainActivity, p)
					!= PackageManager.PERMISSION_GRANTED) {
				missing.add(p);
			}

		if (!missing.isEmpty())
		{
			ActivityCompat.requestPermissions(
					mainActivity,
					missing.toArray(new String[0]),
					1001
			);

			return false; // Permission fehlt → warten
		}

		return true; // Alle Permissions vorhanden
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

	public boolean hohleBtScanBerechtigung()
	{
		// Auch das Intent braucht eine Erlaubnis geöffnet zu werden...
		if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(
					mainActivity,
					new String[]{Manifest.permission.BLUETOOTH_SCAN},
					1001
			);

			return true; // WICHTIG: Intent erst starten, wenn Permission da ist. Deshalb hier "warten" -> zweiter Methodenaufruf erforderlich
			// möglich durch erneutes anklicken des connect buttons?
		}

		return false;
	}

	public MainActivity getMainActivity()
	{
		return mainActivity;
	}
}
