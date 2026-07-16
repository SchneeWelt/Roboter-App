public interface DataReceivedListener
{
    /** Wird immer dann geworfen, wenn dieser NetworkController Daten
     * von dem mit ihm verbundenem Gerät erhalten hat. Speziell beim Roboter
     * würde das also bedeuten, dass dieser Daten an den Client, also die
     * Roboter Steuerungs App, gesendet hat und diese Daten nun hier angekommen
     * sind.
     *
     * @param data
     */
    void onDataReceived(String data);
}