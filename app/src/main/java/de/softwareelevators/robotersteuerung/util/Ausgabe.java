package de.softwareelevators.robotersteuerung.util;

import android.util.Log;

import java.util.ArrayList;

/**
 * Diese Klasse ermöglicht standart Ausgabe zum Debuggen.
 */

public class Ausgabe
{
    private static final String LOG_TAG = "DEBUG";

    /**
     * Fügt einfach nur eine Leere Zeile ein.
     * Praktisch für die Textformatierung.
     */
    public static void blank()
    {
        Log.d(LOG_TAG, "");
    }

    public static void print(String debugMessage)
    {
        Log.d(LOG_TAG,"[debug] " + debugMessage);
    }

    public static <A> void print(ArrayList<A> eingabe)
    {
        for (A a : eingabe)
            Log.d(LOG_TAG, a.toString());
    }

    public static <A> void print(A[] eingabe)
    {
        for (int i = 0; i < eingabe.length; i ++)
            Log.d(LOG_TAG, eingabe[i].toString());
    }

    public static void print(Number eingabe)
    {
        print("" + eingabe);
    }

    public static void print(Object eingabe)
    {
        print("" + eingabe);
    }

    private Ausgabe()
    {
    }
}
