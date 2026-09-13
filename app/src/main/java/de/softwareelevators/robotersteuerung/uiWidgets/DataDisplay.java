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
        String info = String.format("Fahrgeschwindigkeit: %.2f%%\nLenkwinkel: %.2f°", fahrgeschwindigkeit * 100, lenkwinkel);
        setText(info);
    }

    private void setup(Context context)
    {
        setText("...");
    }
}
