package de.softwareelevators.robotersteuerung.util;


/**
 * Speichert global ab, ob eine Verbindung zu einem Remote
 * gerät (bspw einem Roboter) besteht. Diese Klasse ist zentral
 * und wird sowohl von {@link de.softwareelevators.robotersteuerung.networkAdapter.NetworkAdapter}
 * Instanzen verwendet, als auch von der {@link de.softwareelevators.robotersteuerung.uiAdapter.UIAdapter}
 * Klasse.
 *
 * wird jetzt in {@link de.softwareelevators.robotersteuerung.networkAdapter.NetworkAdapter} direkt
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
