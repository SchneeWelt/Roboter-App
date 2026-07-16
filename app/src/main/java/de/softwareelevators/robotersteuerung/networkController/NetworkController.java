package de.softwareelevators.robotersteuerung.networkController;

import android.bluetooth.BluetoothDevice;

public abstract class NetworkController
{
	public abstract void sendData(String data);

	public abstract void connect();

	public abstract void disconnect();
}
