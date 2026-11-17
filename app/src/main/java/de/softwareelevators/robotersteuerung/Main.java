package de.softwareelevators.robotersteuerung;

import de.softwareelevators.robotersteuerung.uiHandler.UIHandler;
import de.softwareelevators.robotersteuerung.util.Ausgabe;


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

		new UIHandler(mainActivity);

		/* Nach allen möglichen Runtime permissions fragen. Der Trick: Wird eine Permmission einmal
		erlaubt, so merkt sich das die App und solange diese dann nicht neu gestartet order zurückgesetzt
		wird, muss nie wieder diese Permission neu eingeholt werden. Heißt der nachfolgende Teil nervt einmal
		und dann nie wieder */

		Ausgabe.print("Hohle App Berechtigungen...");
		if (getMainActivity().hohleBtBerechtigungen())
			Ausgabe.print("Alle App Berechtigungen erteilt!");
		else
			Ausgabe.print("App Berechtitungen verweigert. Neustart empfohlen");
	}



	public MainActivity getMainActivity()
	{
		return mainActivity;
	}
}
