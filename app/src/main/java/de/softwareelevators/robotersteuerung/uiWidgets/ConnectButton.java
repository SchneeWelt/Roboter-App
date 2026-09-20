package de.softwareelevators.robotersteuerung.uiWidgets;

import android.content.Context;
import android.util.AttributeSet;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.networkHandler.NetworkHandler;

public class ConnectButton extends androidx.appcompat.widget.AppCompatButton
{
    public ConnectButton(Context context)
    {
        super(context);
    }

    public ConnectButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ConnectButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Definiert was im Fall eines erfolgreichen Verbindugnsaufbaus mit diesem
     * Button passieren soll
     *
     * @param networkHandler Der derzeit aktive {@link NetworkHandler}
     */
    public void onConnectionEstablished(NetworkHandler networkHandler)
    {
        // Dem Nutzer signalisieren, dass ein erneutes drücken des Buttons die Verbindung
        // wieder trennt
        setText(R.string.verbindung_trennen);

        // Den eigentlichen Verbindungsabbruch ermöglichen
		setOnClickListener((view) -> networkHandler.disconnect());
    }

    /**
     * @param networkHandler Der derzeit aktive {@link NetworkHandler}
     */
    public void onDisconect(NetworkHandler networkHandler)
    {
        setText(R.string.verbinden);
        setOnClickListener((view) -> networkHandler.connect());
    }
}
