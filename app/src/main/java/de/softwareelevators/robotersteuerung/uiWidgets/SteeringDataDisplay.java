package de.softwareelevators.robotersteuerung.uiWidgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

public class SteeringDataDisplay extends androidx.appcompat.widget.AppCompatTextView
{
    public SteeringDataDisplay(Context context)
    {
        super(context);

        setup(context);
    }

    public SteeringDataDisplay(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        setup(context);
    }

    public SteeringDataDisplay(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setup(context);
    }

    private void setup(Context context)
    {
        setText("...");
    }
}
