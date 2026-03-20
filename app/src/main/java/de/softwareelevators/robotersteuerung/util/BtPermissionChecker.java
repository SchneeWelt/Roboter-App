package de.softwareelevators.robotersteuerung.util;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import de.softwareelevators.robotersteuerung.MainActivity;

public class BtPermissionChecker
{
	/**
	 * Die Verwendung dieser Methode ist nur dann möglich, wenn über @SuppressLint("MissingPersmission")
	 * Der Linter über den existierenden Permission Check informiert wird.
	 * <p>
	 * Der Linter ist ein Code Prüfer, der unabhängig vom Compiler meinen Code vor dem Ausführen
	 * und während dem Schreiben prüft und mich über grobe Fehler informiert.
	 *
	 * @param mainActivity
	 * @return
	 */
	public static boolean hasBluetoothPermission(MainActivity mainActivity)
	{
		return ActivityCompat.checkSelfPermission(mainActivity,	Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
	}

	public static void requestBluetoothPermission(MainActivity mainActivity)
	{
		Ausgabe.print("Frage nach Berechtigungen...");

		String[] permissions = new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN};

		ActivityCompat.requestPermissions(mainActivity,	permissions,1001);
	}

	private BtPermissionChecker()
	{

	}
}
