package de.softwareelevators.robotersteuerung.uiAdapter;


import android.widget.Button;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.steuerung.Joystick;
import de.softwareelevators.robotersteuerung.steuerung.Main;
import de.softwareelevators.robotersteuerung.uiWidgets.ConnectionStateDisplay;
import de.softwareelevators.robotersteuerung.uiWidgets.SteeringDataDisplay;

/**
 * Ein Wrapper, der zunächst alle verwendeten UI Elemente über die jeweilige
 * ID der Elemente findet, sie als Referenz speichert und anschließend
 * für den {@link UIAdapter} nutzbar macht
 */
public class UIElemente
{
    private Joystick.JoystickListener joystickListener;

    private Joystick joystick;
    private Button connectButton;
    private SteeringDataDisplay steeringDataDisplay; //steuerdaten2stuerdatendisplay;
    private ConnectionStateDisplay connectionStateDisplay;

    private Main main;

    public UIElemente(Main main, MainActivity mainActivity, Joystick.JoystickListener joystickListener)
    {
        this.main = main;

        joystick = mainActivity.findViewById(R.id.joystick);
        connectButton = mainActivity.findViewById(R.id.connect_button);
        steeringDataDisplay = mainActivity.findViewById(R.id.steuerdaten_display);
        connectionStateDisplay = mainActivity.findViewById(R.id.verbindungsstatus);

        connectButton.setOnClickListener((view) -> main.getNetworkAdapter().connect());

        joystick.setJoystickListener(joystickListener);
    }

    public SteeringDataDisplay getSteeringDataDisplay()
    {
        return steeringDataDisplay;
    }

    public ConnectionStateDisplay getConnectionStateDisplay()
    {
        return connectionStateDisplay;
    }

    public Button getConnectButton()
    {
        return connectButton;
    }
}
