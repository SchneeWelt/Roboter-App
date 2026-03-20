package de.softwareelevators.robotersteuerung;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import de.softwareelevators.robotersteuerung.util.ActivityLauncher;
import de.softwareelevators.robotersteuerung.util.Sendebegrenzer;
import de.softwareelevators.robotersteuerung.util.UiHandler;

public class MainActivity extends AppCompatActivity implements Joystick.JoystickListener, BluetoothController.DataReceivedConnection, BluetoothController.ConnectionNotifier
{
	/* Ermöglicht das starten anderer Anwendungen - genannt Intents - durch diese Anwendung. Damit ist
	* es beispielsweise möglich, den Nutzer um das Einschalten von Bluetooth zu bitten. */
	public final ActivityLauncher<Intent, ActivityResult> activityLauncher = ActivityLauncher.registerActivityForResult(this);

	private UiHandler uiHandler;
	private Sendebegrenzer sendebegrenzer;
	private BluetoothController bluetoothController;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.activity_main);

		setup();
	}

	private void setup()
	{
		/* DeviceName könnte ich mir auch über ein Intent besorgen. Ich schreibe eine
		zweite Klasse, die von Activity erbt und starte diese dann über ein Intent bei
		Start dieser Anwendung.
		Auf diesem Overlay wird nach dem Gerätenamen des Gerätes gefragt, mit dem
		sich diese Anwendung verbinden soll. Oben links in der Anwendung könnte ein
		quardratischer Button zu sehen sein, mit dem dieses Intent erneut geöffnet wird
		und somit das Verbinden zu einem anderen Gerät möglich ist. */

		uiHandler = new UiHandler(this);

		bluetoothController = new BluetoothController("ESP32", this,this, this);

		sendebegrenzer = new Sendebegrenzer(bluetoothController);
		sendebegrenzer.start();
	}

	@Override
	public void onJoyStickMoved(float xPercent, float yPercent)
	{
		/* Lenkwinkel berechnen. Wertebereich: [0;180] (rechte Hälfte), [-0;-180] (linke Hälfte) */
		float lenkwinkel = lenkwinkelBerechnen(xPercent, yPercent);

		/* Wertebereich: [0; 100] */
		float fahrgeschwindigkeit = fahrgeschwindigkeitBerechnen(xPercent, yPercent);

		uiHandler.updateJoystickInfo(lenkwinkel, fahrgeschwindigkeit);

		/* Setze fahrgeschwindigkeit auf 1001 wenn die Sprint Taste gedrückt ist */
		fahrgeschwindigkeit = uiHandler.checkSprinting(fahrgeschwindigkeit);

		/* Die neuen Daten in den Sendebegrenzer aktualisieren. Er sendet sie dann, sobald er
		Zeit dafür hat. Wenn die bereits dort existierenden Daten äquivalten zu den neuen Daten
		sind wird nicht gesenet. */
		if (bluetoothController.isConnected())
			sendebegrenzer.datenAktualisieren(lenkwinkel, fahrgeschwindigkeit);
	}

	@Override
	public void onDataReceived(String data)
	{
		/* Wird aufgerufen, wenn über den BluetoothController - also vom Roboter - Daten empfangen wurden */
	}

	@Override
	public void onConnect(BluetoothDevice connectedDevice)
	{
		/* Wird ausgeführt, wenn sich ein BT Gerät mit diesem Gerät verbindet */

		uiHandler.onConnect();
	}

	@Override
	public void onDisconnect()
	{
		uiHandler.onDisconnect();
	}

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

	private float fahrgeschwindigkeitBerechnen(float xPercent, float yPercent)
	{
		float fahrgeschwindigkeit = (float) Math.sqrt(xPercent * xPercent + yPercent * yPercent) * 100;

		/* Rundungsfehler beheben. Ein Ergebnis von 100.00015 oder ähnlichem ist möglicht. Aber
		nicht erwünscht. */
		if (fahrgeschwindigkeit > 100)
			fahrgeschwindigkeit = 100;

		return fahrgeschwindigkeit;
	}

	private float lenkwinkelBerechnen(float xPercent, float yPercent)
	{
		float lenkwinkel = (float) Math.toDegrees(Math.atan2(yPercent, xPercent));

		/* Koordinatensystem drehen: oben: 0°, 90°: rechts, 180°: unten, -90°:links */
		lenkwinkel += 90;

		/* Verschobenes Koordinatensystem jetzt noch normalisieren, damit die Werte innerhalb
		von +-180 Grad bleiben - also so sind, wie oben beschrieben */
		if (lenkwinkel > 180) lenkwinkel -= 360;
		if (lenkwinkel < -180) lenkwinkel += 360;

		return lenkwinkel;
	}

	public BluetoothController getBluetoothController()
	{
		return bluetoothController;
	}
}