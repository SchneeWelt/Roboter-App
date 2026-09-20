package de.softwareelevators.robotersteuerung.util;


import de.softwareelevators.robotersteuerung.networkAdapter.NetworkHandler;
import de.softwareelevators.robotersteuerung.uiAdapter.UIHandler;

/**
 * Speichert global ab, ob eine Verbindung zu einem Remote
 * gerät (bspw einem Roboter) besteht. Diese Klasse ist zentral
 * und wird sowohl von {@link NetworkHandler}
 * Instanzen verwendet, als auch von der {@link UIHandler}
 * Klasse.
 *
 * wird jetzt in {@link NetworkHandler} direkt
 * gemacht. Muss also nicht mehr über eine externe klasse gespeichert werden
 */
@Deprecated
public class ConnectionStateStorage
{
    private boolean connectionEstablished;

    public void setConnectionEstablished(boolean connectionEstablished)
    {
        this.connectionEstablished = connectionEstablished;
    }

    /**
     * @return True, wenn eine Verbindung zu einem Remote Gerät (dem Roboter)
     * besteht
     */
    public boolean isConnectionEstablished()
    {
        return connectionEstablished;
    }
}
