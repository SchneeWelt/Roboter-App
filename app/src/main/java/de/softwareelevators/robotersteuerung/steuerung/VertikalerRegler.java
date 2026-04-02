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
import de.softwareelevators.robotersteuerung.R;

public class VertikalerRegler extends View	// aka Slider
{
	private SliderListener sliderListener;


	private Paint basePaint, cursorPaint;

	private float cursorX, cursorY;
	private float cursorWidth, cursorHeight;
	private int baseWidth, baseHeight;

	/* Die (y) Position vom Cursor, die 0% Auslenkung entspricht */
	private float restingPosition;

	/** Die (y) Position vom Cursor, die 100% Auslenkung entspricht */
	private float limitPosition;

	public VertikalerRegler(Context context, @Nullable @org.jspecify.annotations.Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		setup(context);
	}

	public VertikalerRegler(Context context, @Nullable @org.jspecify.annotations.Nullable AttributeSet attrs)
	{
		super(context, attrs);

		setup(context);
	}

	public VertikalerRegler(Context context)
	{
		super(context);

		setup(context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		baseWidth = w;
		baseHeight = h;

		cursorWidth = baseWidth;
		cursorHeight = baseHeight * 0.03f;

		restingPosition = baseHeight * 0.97f - cursorHeight;	// Ruhposition untere Kante des Cursors
		limitPosition = baseHeight * 0.04f;	// Ruhposition obere Kante des Cursors

		cursorX = 0;
		cursorY = restingPosition;
	}

	@Override
	protected void onDraw(@NonNull @org.jspecify.annotations.NonNull Canvas canvas)
	{
		super.onDraw(canvas);

		/* Abrundung der Kanten */
		float radiusBase = 25;
		float radiusCursor = 10f;

		/* Base zeichen */
		canvas.drawRoundRect(0, 0, baseWidth, baseHeight, radiusBase, radiusBase, basePaint);

		/* Cursor zeichnen */
		float seitenabstand = baseWidth * 0.055f;
		canvas.drawRoundRect(cursorX + seitenabstand, cursorY - cursorHeight / 2, cursorWidth - seitenabstand, cursorY + cursorHeight, radiusCursor, radiusCursor, cursorPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		cursorY = event.getY();

		if (cursorY < limitPosition)
			cursorY = limitPosition;
		else if (cursorY > restingPosition)
			cursorY = restingPosition;

		if (event.getAction() == MotionEvent.ACTION_UP)
			cursorY = restingPosition;

		float b = limitPosition;
		float a = restingPosition;
		float x = cursorY;
		sliderListener.onSliderMoved((x - a) / (b - a));

		invalidate();

		return true;
	}

	private void setup(Context context)
	{
		int basePaintColor =  ContextCompat.getColor(context, R.color.orange);

		basePaint = new Paint();
		basePaint.setColor(basePaintColor);
		basePaint.setStrokeWidth(5);
		basePaint.setAntiAlias(true);
		basePaint.setStyle(Paint.Style.STROKE);

		cursorPaint = new Paint();
		cursorPaint.setColor(basePaintColor);
	}

	public void setSliderListener(SliderListener sliderListener)
	{
		this.sliderListener = sliderListener;
	}

	public interface SliderListener
	{
		/**
		 *
		 * @param sliderPositionPercent Die neue Position des Reglers in
		 *                              Prozent.
		 */
		void onSliderMoved(float sliderPositionPercent);
	}
}
