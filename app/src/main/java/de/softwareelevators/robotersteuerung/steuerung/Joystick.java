package de.softwareelevators.robotersteuerung.steuerung;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;


public class Joystick extends View
{
	private Paint basePaint;
	private Paint hatPaint;

	private float centerX, centerY;
	private float baseRadius, hatRadius;
	private float hatX, hatY;

	private JoystickListener joystickListener;


	public Joystick(Context context)
	{
		super(context);

		setup(context);
	}

	public Joystick(Context context, @Nullable @org.jspecify.annotations.Nullable AttributeSet attrs)
	{
		super(context, attrs);

		setup(context);
	}

	public Joystick(Context context, @Nullable @org.jspecify.annotations.Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		setup(context);
	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		/* Dieses Event wird geworfen, sobald Android die Maße für dieses View kennt.
		Das ist typischerweise kurz nach Initialisierung der App der Fall. Durch Verwendung
		dieses Events ist es möglicht die genaue Dimension eines Views zu ermitteln, wenn
		dieses über Androids Layout System auf dem Bildschirm platziert und dimensioniert
		wurde. */

		super.onSizeChanged(w, h, oldw, oldh);

		centerX = w / 2f;
		centerY = h / 2f;

		baseRadius = Math.min(w, h) / 3f;
		hatRadius = baseRadius / 3f;

		hatX = centerX;
		hatY = centerY;
	}

	@Override
	protected void onDraw(@NonNull @org.jspecify.annotations.NonNull Canvas canvas)
	{
		super.onDraw(canvas);

		canvas.drawCircle(centerX, centerY, baseRadius, basePaint);

		canvas.drawCircle(hatX, hatY, hatRadius, hatPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();

		float dx = x - centerX;
		float dy = y - centerY;
		float distance = (float) Math.sqrt(dx * dx + dy * dy);

		if (distance < baseRadius)
		{
			hatX = x;
			hatY = y;
		} else
		{
			float ratio = baseRadius / distance;

			hatX = centerX + dx * ratio;
			hatY = centerY + dy * ratio;
		}

		/* Gibt Befehl zum Neuzeichnen des Views, Veranlasst also
		* einmal den Aufruf von onDraw() */
		invalidate();

		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			hatX = centerX;
			hatY = centerY;
			invalidate();
		}

		if (joystickListener != null)
		{
			float xPercent = (hatX - centerX) / baseRadius;
			float yPercent = (hatY - centerY) / baseRadius;

			joystickListener.onJoyStickMoved(xPercent, yPercent);
		}

		return true;
	}

	public void setJoystickListener(JoystickListener joystickListener)
	{
		this.joystickListener = joystickListener;
	}

	private void setup(Context context)
	{
		int basePaintColor =  ContextCompat.getColor(context, R.color.orange);

		basePaint = new Paint();
		basePaint.setColor(basePaintColor);
		basePaint.setStrokeWidth(5);
		basePaint.setAntiAlias(true);
		basePaint.setStyle(Paint.Style.STROKE);

		hatPaint = new Paint();
		hatPaint.setColor(basePaintColor);
	}


	public interface JoystickListener
	{
		void onJoyStickMoved(float xPercent, float yPercent);
	}
}
