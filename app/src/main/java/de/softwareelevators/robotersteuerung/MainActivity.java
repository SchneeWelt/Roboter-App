package de.softwareelevators.robotersteuerung;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import de.softwareelevators.robotersteuerung.steuerung.Steuereinheit;
import de.softwareelevators.robotersteuerung.util.ActivityLauncher;

public class MainActivity extends AppCompatActivity
{
	/* Ermöglicht das starten anderer Anwendungen - genannt Intents - durch diese Anwendung. Damit ist
	 * es beispielsweise möglich, den Nutzer um das Einschalten von Bluetooth zu bitten. */
	public final ActivityLauncher<Intent, ActivityResult> activityLauncher = ActivityLauncher.registerActivityForResult(this);

	/*
		in meiner android sdk fehlt mit das emulator package. Ohne das kann ich keine vms betreiben.
		ich muss irgendwie noch an dieses package ran kommen.

	 */


	/**
	 * Verantwortlich für das Stuern des Roboters. Aktualisiert auch die GUI
	 */
	private Steuereinheit steuereinheit;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		/*
			Bei diesem Projekt handelt es sich um ein Git Projekt, in welchem
			unterschiedliche Features auf unterschiedlichen Branches implementiert
			werden. Aktuell existiert der main Branch - genannt Master - und ein
			Branch, auf dem das Sprinting Feature wieder entfernt wurde. Mit
			git checkout kann zwischen beiden Branches gewechselt werden. Mit
			git branch kann der aktuell ausgewählte Branch angezeigt werden.
		 */


		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.activity_main);

		setup();
	}

	private void setup()
	{
		/* DeviceName könnte ich mir auch über ein Intent besorgen. Ich schreibe eine
		zweite Klasse, die von Activity erbt und starte diese dann über ein Intent bei
		Start dieser Anwendung.
		Auf diesem Overlay wird nach dem Gerätenamen des Gerätes gefragt, mit dem
		sich diese Anwendung verbinden soll. Oben links in der Anwendung könnte ein
		quardratischer Button zu sehen sein, mit dem dieses Intent erneut geöffnet wird
		und somit das Verbinden zu einem anderen Gerät möglich ist. */

		steuereinheit = new Steuereinheit(findViewById(R.id.joystick), findViewById(R.id.ges_regler), this);
	}

	// --

	@Override
	protected void onResume()
	{
		super.onResume();

		enableImmersiveMode();
	}

	private void enableImmersiveMode()
	{
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		);
	}
}