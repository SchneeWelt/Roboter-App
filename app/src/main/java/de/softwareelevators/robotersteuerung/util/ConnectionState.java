package de.softwareelevators.robotersteuerung.util;

public class ConnectionState
{
    /** True, wenn Verbindung zu Roboter über
     * NetworkController besteht */
    private boolean connectionEstablished;

    public void setConnectionEstablished(boolean connectionEstablished)
    {
        this.connectionEstablished = connectionEstablished;
    }

    public boolean isConnectionEstablished()
    {
        return connectionEstablished;
    }
}
