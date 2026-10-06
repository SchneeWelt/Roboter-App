package de.softwareelevators.robotersteuerung.uiHandler;


import android.widget.*;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.networkHandler.BluetoothHandler;
import de.softwareelevators.robotersteuerung.networkHandler.NetworkHandler;
import de.softwareelevators.robotersteuerung.networkHandler.WLanHandler;
import de.softwareelevators.robotersteuerung.uiWidgets.*;
import de.softwareelevators.robotersteuerung.Main;

/**
 * Ein Wrapper, der zunächst alle verwendeten UI Elemente über die jeweilige
 * ID der Elemente findet, sie als Referenz speichert und anschließend
 * für den {@link UIHandler} nutzbar macht. Implementierungen für die einzelnen
 * Widgets sollen in dieser Klasse eigentlich nicht vorkommen!
 */
public class UIElemente
{
    private final Main main;
    private final Joystick joystick;
    private final IpButton ipButton;
    private final UIHandler uiHandler;
    private final DataDisplay dataDisplay;
    private final ConnectButton connectButton;
    private final ConnectionStateDisplay connectionStateDisplay;


    public UIElemente(Main main, UIHandler uiHandler)
    {
        this.main = main;
        this.uiHandler = uiHandler;

        MainActivity mainActivity = main.getMainActivity();

        joystick = mainActivity.findViewById(R.id.joystick);
        ipButton = mainActivity.findViewById(R.id.ip_button);
        connectButton = mainActivity.findViewById(R.id.connect_button);
        dataDisplay = mainActivity.findViewById(R.id.steuerdaten_display);
        connectionStateDisplay = mainActivity.findViewById(R.id.verbindungsstatus);

        ToggleButton toggleButton = mainActivity.findViewById(R.id.mode_switcher);
        toggleButton.setOnCheckedChangeListener(this::onToggle);

        joystick.setJoystickListener(uiHandler);
    }

    private void onToggle(CompoundButton b, boolean newState)
    {
        NetworkHandler newAdapter = null;

        if (newState)  // W-Lan Modus aktivieren
        {
            uiHandler.onNetworkHandlerChanged(new WLanHandler());

            Toast.makeText(main.getMainActivity(), "W-Lanmodus aktiviert", Toast.LENGTH_SHORT).show();
        } else // Bluetoothmodus aktivieren
        {
            uiHandler.onNetworkHandlerChanged(new BluetoothHandler(main));

            Toast.makeText(main.getMainActivity(), "Bluetoothmodus aktiviert", Toast.LENGTH_SHORT).show();
        }
    }

    public DataDisplay getSteeringDataDisplay()
    {
        return dataDisplay;
    }

    public ConnectionStateDisplay getConnectionStateDisplay()
    {
        return connectionStateDisplay;
    }

    public ConnectButton getConnectButton()
    {
        return connectButton;
    }
}
