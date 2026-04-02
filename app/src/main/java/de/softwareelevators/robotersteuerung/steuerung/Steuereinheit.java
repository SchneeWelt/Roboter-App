package de.softwareelevators.robotersteuerung.steuerung;

import android.bluetooth.BluetoothDevice;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.networkController.BluetoothController;
import de.softwareelevators.robotersteuerung.networkController.NetworkController;
import de.softwareelevators.robotersteuerung.util.Sendebegrenzer;
import de.softwareelevators.robotersteuerung.util.UiHandler;

public class Steuereinheit implements VertikalerRegler.VertikalerReglerListener, Joystick.JoystickListener, BluetoothController.DataReceivedConnection, BluetoothController.ConnectionStateNotifier
{
	private Joystick joystick;
	private UiHandler uiHandler;
	private VertikalerRegler regler;
	private Sendebegrenzer sendebegrenzer;
	private NetworkController networkController;


	private float fahrgeschwindigkeit;
	private float lenkwinkel;


	public Steuereinheit(Joystick joystick, VertikalerRegler regler, NetworkController networkController, MainActivity mainActivity)
	{
		this.regler = regler;
		this.joystick = joystick;
		this.networkController = networkController;

		uiHandler = new UiHandler(mainActivity);

		sendebegrenzer = new Sendebegrenzer(networkController);
		sendebegrenzer.start();
	}

	@Override
	public void onReglerMoved(float auslenkungProzent)
	{
		/* Wertebereich: [0; 100] */
		fahrgeschwindigkeit = auslenkungProzent * 100;

		uiHandler.updateFahrgeschwindigkeit(fahrgeschwindigkeit);
	}

	@Override
	public void onJoyStickMoved(float xPercent, float yPercent)
	{
		/* Lenkwinkel berechnen. Wertebereich: [0;180] (rechte Hälfte), [-0;-180] (linke Hälfte) */
		float lenkwinkel = lenkwinkelBerechnen(xPercent, yPercent);

		/* Wertebereich: [0; 100] */
//		float fahrgeschwindigkeit = fahrgeschwindigkeitBerechnen(xPercent, yPercent);

		uiHandler.updateLenkwinkel(lenkwinkel);

		/* Die neuen Daten in dem Sendebegrenzer aktualisieren. Er sendet sie dann, sobald er
		Zeit dafür hat. Wenn die bereits dort existierenden Daten äquivalten zu den neuen Daten
		sind wird nicht gesenet. */
		if (networkController.isConnected())
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
}
