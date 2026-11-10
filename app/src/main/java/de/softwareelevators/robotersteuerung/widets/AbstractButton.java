package de.softwareelevators.robotersteuerung.widets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import de.softwareelevators.robotersteuerung.MainActivity;

/**
 * Stellt abstrakt einen clickbaren Button dar. Die Eventvorschrift für das
 * anklicken eines solchen Buttons ist dabei in einer von dieser Klasse erbenden
 * Klasse fest zu implementieren.
 * <p>
 *     Ich habe mich gegen die Verwendung einer einzigen Buttonklasse und der
 *     damit einhergehenden Verwendung von Lambdas entschieden, da es durchaus
 *     möglich ist, dass zwischen zwei konkreten Buttons weitere Unterschiede
 *     als nur das verhalten bei onClick bestehen. Diese Unterschiede können
 *     so leicht in den erbenden Klassen implementiert werden
 * </p>
 * <p>
 * </p>
 */
public abstract class AbstractButton extends AppCompatButton
{
	public AbstractButton(@NonNull Context context)
	{
		super(context);

		setup();
	}

	public AbstractButton(@NonNull Context context, @Nullable AttributeSet attrs)
	{
		super(context, attrs);

		setup();
	}

	public AbstractButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		setup();
	}

	private void setup()
	{
		setOnClickListener(this::onButtonPressed);

		onSetup();
	}

	/**
	 *  Wird bei initialisierung der Klasse geworfen. Kommt einem einheitlichen Konstruktoraufruf
	 *  gleich
	 */
	protected abstract void onSetup();

	/**
	 * Wird immer dann geworfen, wenn dieser Button angeklickt wurde.
	 *
	 * @param view Das View, dass angeklickt wurde - also dieser Button
	 */
	protected abstract void onButtonPressed(View view);

//	/**
//	 * @return Das zentrale {@link OscSender} Objekt, das von dieser Anwendung zum senden
//	 * von Osc Daten verwendet wird
//	 */
//	protected OscSender getOscSender()
//	{
//		return ((MainActivity) getContext()).getOscSender();
//	}

	// getContext und dann Cast auf MainActivity. So kommt man an wichtige anderen Klassen rann!
}
