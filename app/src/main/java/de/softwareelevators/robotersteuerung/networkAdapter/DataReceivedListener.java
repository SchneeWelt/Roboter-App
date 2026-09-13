package de.softwareelevators.robotersteuerung.networkAdapter;


/** Ermöglicht eine Reaktion externer Klassen auf den Erhalt von Daten von
 * einem Remote Gerät (dem Roboter) zu reagieren. Das Wort extern bedeutet
 * Klassen, die nicht vom Type: {@link NetworkAdapter} sind
 */
public interface DataReceivedListener
{
    /**
     * Was soll passieren, wenn durch einen {@link NetworkAdapter} Daten von
     * einem Remote Gerät erhalten wurden. Das was passieren soll ist in dieser
     * Methode zu definieren
     *
     * @param data Die vom Remote Gerät (dem Roboter) empfangenen Daten
     */

    void onDataReceived(String data);
}
