package de.softwareelevators.robotersteuerung.util;


import de.softwareelevators.robotersteuerung.BluetoothController;

/**
 * Kapselt Methoden, die zum Senden der BT Daten an den Roboter
 * benötigt werden.
 */
public class BtSender
{
	private BluetoothController bluetoothController;

	public BtSender(BluetoothController bluetoothController)
	{
		this.bluetoothController = bluetoothController;
	}


	/**
	 *
	 * @param lenkwinkel Wertebereich: [0;180], [-0;-180]
	 * @param fahrgeschwindigkeit Wertebereich: [0;100]
	 */
	public void steuerdatenSenden(float lenkwinkel, float fahrgeschwindigkeit)
	{
		/* Deadzone, um Zittern des Roboters zu verhinden */
		float deadzone = 5f;	// Entfernung von Joystick Center mindestens 5%
		if (fahrgeschwindigkeit < deadzone)
			bluetoothController.sendData(baueSteuerdaten(0, 0));
		else
			bluetoothController.sendData(baueSteuerdaten(lenkwinkel, fahrgeschwindigkeit));

		/* Debugdaten in der Konsole ausgeben */
		Ausgabe.blank();
		Ausgabe.print("Sende Steuerdaten an Roboter: ");
		Ausgabe.print("Lenkwinkel: " + lenkwinkel);
		Ausgabe.print("Fahrgeschwindigkeit: " + fahrgeschwindigkeit);
	}

	private String baueSteuerdaten(float lenkwinkel, float fahrgeschwindigkeit)
	{
		return lenkwinkel + "," + fahrgeschwindigkeit + "\n";
	}
}
