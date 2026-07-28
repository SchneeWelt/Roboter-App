package de.softwareelevators.robotersteuerung.networkAdapter;

public interface DataReceivedListener
{
    /**
     * Wird immer dann geworfen, wenn Daten von einem verbundenen Gerät
     * empfangen wurden. Dieses Event dient dann der Verarbeitung dieser
     * Daten.
     * <p>
     * Speziell beim Roboter würde das also bedeuten, dass dieser Daten
     * an den Client, also die Roboter Steuerungs App (Diese Anwendung),
     * gesendet hat.
     *
     * @param data Die vom Sender empfangenen Daten
     */

    void onDataReceived(String data);
}
