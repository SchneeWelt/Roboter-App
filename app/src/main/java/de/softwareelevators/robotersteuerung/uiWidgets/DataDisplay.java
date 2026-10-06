package de.softwareelevators.robotersteuerung.uiWidgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

/**
 * Zeigt wichtige Daten in echtzeit auf dem UI an
 */
public class DataDisplay extends androidx.appcompat.widget.AppCompatTextView
{
    public DataDisplay(Context context)
    {
        super(context);

        setup(context);
    }

    public DataDisplay(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        setup(context);
    }

    public DataDisplay(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setup(context);
    }

    public void updateContent(float lenkwinkel, float fahrgeschwindigkeit)
    {
        // Mal 100, dann kann ich nämlich nen % Zeichen dahinter setzen
        float fahgeschwindigkeit_display = fahrgeschwindigkeit * 100;

        String info = String.format("Fahrgeschwindigkeit: %.2f%%\nLenkwinkel: %.2f°", fahgeschwindigkeit_display, lenkwinkel);
        setText(info);
    }

    private void setup(Context context)
    {
        setText("...");
    }
}
