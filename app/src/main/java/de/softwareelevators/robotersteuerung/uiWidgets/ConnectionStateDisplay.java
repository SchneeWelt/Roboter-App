package de.softwareelevators.robotersteuerung.uiWidgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import de.softwareelevators.robotersteuerung.R;

/**
 * Ein Wrapper für ein TextView.
 */
public class ConnectionStateDisplay extends androidx.appcompat.widget.AppCompatTextView
{
    public ConnectionStateDisplay(Context context)
    {
        super(context);

        setup(context);
    }

    public ConnectionStateDisplay(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        setup(context);
    }

    public ConnectionStateDisplay(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setup(context);
    }

    private void setup(Context context)
    {
        setText(R.string.nicht_verbunden);

        setTextColor(ContextCompat.getColor(context, R.color.grau));
    }
}
