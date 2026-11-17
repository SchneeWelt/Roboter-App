package de.softwareelevators.robotersteuerung;
import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import de.softwareelevators.robotersteuerung.util.ActivityLauncher;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
	/* Ermöglicht das starten anderer Anwendungen - genannt Intents - durch diese Anwendung. Damit ist
	 * es beispielsweise möglich, den Nutzer um das Einschalten von Bluetooth zu bitten. */
	public final ActivityLauncher<Intent, ActivityResult> activityLauncher = ActivityLauncher.registerActivityForResult(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);

		new Main(this);
	}

	/* DeviceName könnte ich mir auch über ein Intent besorgen. Ich schreibe eine
		zweite Klasse, die von Activity erbt und starte diese dann über ein Intent bei
		Start dieser Anwendung.
		Auf diesem Overlay wird nach dem Gerätenamen des Gerätes gefragt, mit dem
		sich diese Anwendung verbinden soll. Oben links in der Anwendung könnte ein
		quardratischer Button zu sehen sein, mit dem dieses Intent erneut geöffnet wird
		und somit das Verbinden zu einem anderen Gerät möglich ist. */

	// --

	@Override
	protected void onResume()
	{
		super.onResume();

		enableImmersiveMode();
	}

	private void enableImmersiveMode()
	{
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		);
	}

	// --- Berechtigungen -------------------------------------

	/**
	 * Aufruf hohlt vom Betriebssystem wesentliche Berechtigungen für das
	 * Senden und Empfangen von Daten über Bluetooth.
	 *
	 * @return true, wenn keine Permission fehlte und der Programmablauf also nicht
	 * neu gestartet werden musss
	 */
	public boolean hohleBtBerechtigungen()
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
			if (ActivityCompat.checkSelfPermission(this, p)
					!= PackageManager.PERMISSION_GRANTED) {
				missing.add(p);
			}

		if (!missing.isEmpty())
		{
			ActivityCompat.requestPermissions(
					this,
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
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)	!= PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(
					this,
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
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(
					this,
					new String[]{Manifest.permission.BLUETOOTH_SCAN},
					1001
			);

			return true; // WICHTIG: Intent erst starten, wenn Permission da ist. Deshalb hier "warten" -> zweiter Methodenaufruf erforderlich
			// möglich durch erneutes anklicken des connect buttons?
		}

		return false;
	}
}