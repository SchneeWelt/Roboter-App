package de.softwareelevators.robotersteuerung.uiWidgets;

import android.content.Context;
import android.util.AttributeSet;
import de.softwareelevators.robotersteuerung.R;
import de.softwareelevators.robotersteuerung.networkHandler.NetworkSender;

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
     * @param networkSender Der derzeit aktive {@link NetworkSender}
     */
    public void onConnectionEstablished(NetworkSender networkSender)
    {
        // Dem Nutzer signalisieren, dass ein erneutes drücken des Buttons die Verbindung
        // wieder trennt
        setText(R.string.verbindung_trennen);

        // Den eigentlichen Verbindungsabbruch ermöglichen
		setOnClickListener((view) -> networkSender.disconnect());
    }

    /**
     * @param networkSender Der derzeit aktive {@link NetworkSender}
     */
    public void onDisconect(NetworkSender networkSender)
    {
        setText(R.string.verbinden);
        setOnClickListener((view) -> networkSender.connect());
    }
}
