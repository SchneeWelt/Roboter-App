package de.softwareelevators.robotersteuerung.util;

/**
 * Stellt Methoden zur Lenkwinkel berechnung
 */
public class MathTools
{
    public static float lenkwinkelBerechnen(float xPercent, float yPercent)
    {
        float lenkwinkel = (float) Math.toDegrees(Math.atan2(yPercent, xPercent));

        /* Koordinatensystem drehen: oben: 0°, 90°: rechts, 180°: unten, -90°:links.
         * Das ist richtig so und sorgt dafür, dass geradeausfahren gleichbedeutend
         * zu Joystick nach oben bewegen ist. */
        lenkwinkel += 90;

		/* Verschobenes Koordinatensystem jetzt noch normalisieren, damit die Werte innerhalb
		von +-180 Grad bleiben - also so sind, wie oben beschrieben */
        if (lenkwinkel > 180) lenkwinkel -= 360;
        if (lenkwinkel < -180) lenkwinkel += 360;

        return lenkwinkel;
    }

    public static float fahrgeschwindigkeitBerechnen(float xPercent, float yPercent)
    {
        return (float) Math.sqrt(xPercent * xPercent + yPercent * yPercent);
    }
}
