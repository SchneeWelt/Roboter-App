package de.softwareelevators.robotersteuerung.util;


import de.softwareelevators.robotersteuerung.networkAdapter.NetworkAdapter;

/**
 * Kapselt Methoden, die zum Senden der Daten an das verbundene
 * Gerät (den Roboter) verwendet werden. Wird vom {@link Sendebegrenzer}
 * verwendet, um Daten an das verbundene Gerät zu senden
 */
public class SendingWrapper
{
	private NetworkAdapter networkAdapter;

	public SendingWrapper(NetworkAdapter networkAdapter)
	{
		this.networkAdapter = networkAdapter;
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
			networkAdapter.sendData(baueSteuerdaten(0, 0));
		else
			networkAdapter.sendData(baueSteuerdaten(lenkwinkel, fahrgeschwindigkeit));

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
