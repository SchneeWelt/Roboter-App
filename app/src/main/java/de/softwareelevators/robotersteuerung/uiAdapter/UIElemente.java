package de.softwareelevators.robotersteuerung.uiAdapter;


import android.widget.Button;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.steuerung.Joystick;
import de.softwareelevators.robotersteuerung.steuerung.Steuereinheit;
import de.softwareelevators.robotersteuerung.uiWidgets.ConnectionStateDisplay;
import de.softwareelevators.robotersteuerung.uiWidgets.SteeringDataDisplay;

/**
 * Ein Wrapper, der zunächst alle verwendeten UIElemente über die jeweilige
 * ID der Elemente findet, sie als Referenz speichert und anschließend
 * für den UIAdapter nutzbar macht
 */
public class UIElemente
{
    private Joystick joystick;
    private Button connectButton;
    private SteeringDataDisplay steeringDataDisplay; //steuerdaten2stuerdatendisplay;
    private ConnectionStateDisplay connectionStateDisplay;

    private Steuereinheit steuereinheit;

    public UIElemente(Steuereinheit steuereinheit, MainActivity mainActivity)
    {
        this.steuereinheit = steuereinheit;

        joystick = mainActivity.findViewById(R.id.joystick);
        connectButton = mainActivity.findViewById(R.id.connect_button);
        steeringDataDisplay = mainActivity.findViewById(R.id.steuerdaten_display);
        connectionStateDisplay = mainActivity.findViewById(R.id.verbindungsstatus);
    }

    public void initButtons()
    {
        connectButton.setOnClickListener((view) -> steuereinheit.getNetworkAdapter().connect());
    }

    public SteeringDataDisplay getSteeringDataDisplay()
    {
        return steeringDataDisplay;
    }
}
