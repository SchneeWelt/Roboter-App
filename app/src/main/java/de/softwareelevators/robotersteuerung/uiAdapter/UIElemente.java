package de.softwareelevators.robotersteuerung.uiAdapter;


import android.widget.*;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.networkAdapter.BluetoothHandler;
import de.softwareelevators.robotersteuerung.networkAdapter.NetworkHandler;
import de.softwareelevators.robotersteuerung.networkAdapter.WLanHandler;
import de.softwareelevators.robotersteuerung.steuerung.Joystick;
import de.softwareelevators.robotersteuerung.steuerung.Main;
import de.softwareelevators.robotersteuerung.uiWidgets.ConnectionStateDisplay;
import de.softwareelevators.robotersteuerung.uiWidgets.DataDisplay;

/**
 * Ein Wrapper, der zunächst alle verwendeten UI Elemente über die jeweilige
 * ID der Elemente findet, sie als Referenz speichert und anschließend
 * für den {@link UIHandler} nutzbar macht
 */
public class UIElemente
{
    private Main main;
    private Joystick joystick;
    private UIHandler uiHandler;
    private Button connectButton;
    private ToggleButton toggleButton;
    private DataDisplay dataDisplay; //steuerdaten2stuerdatendisplay;
    private ConnectionStateDisplay connectionStateDisplay;


    public UIElemente(Main main, UIHandler uiHandler)
    {
        this.main = main;
        this.uiHandler = uiHandler;

        MainActivity mainActivity = main.getMainActivity();

        joystick = mainActivity.findViewById(R.id.joystick);
        connectButton = mainActivity.findViewById(R.id.connect_button);
        dataDisplay = mainActivity.findViewById(R.id.steuerdaten_display);
        connectionStateDisplay = mainActivity.findViewById(R.id.verbindungsstatus);

        toggleButton = mainActivity.findViewById(R.id.mode_switcher);
        toggleButton.setOnCheckedChangeListener(this::onToggle);

        connectButton.setOnClickListener((view) -> main.getNetworkHandler().connect());

        joystick.setJoystickListener(uiHandler);
    }

    private void onToggle(CompoundButton b, boolean a)
    {
        NetworkHandler newAdapter = null;

        if (a)  // W-Lan Modus aktivieren
        {
            newAdapter = new WLanHandler();

            uiHandler.onNetworkHandlerChanged(newAdapter);

            Toast.makeText(main.getMainActivity(), "W-Lanmodus aktiviert", Toast.LENGTH_SHORT).show();
        } else // Bluetoothmodus aktivieren
        {
            newAdapter = new BluetoothHandler(main);

            uiHandler.onNetworkHandlerChanged(newAdapter);

            Toast.makeText(main.getMainActivity(), "Bluetoothmodus aktiviert", Toast.LENGTH_SHORT).show();
        }

        main.setNetworkHandler(newAdapter);
    }

    public DataDisplay getSteeringDataDisplay()
    {
        return dataDisplay;
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
