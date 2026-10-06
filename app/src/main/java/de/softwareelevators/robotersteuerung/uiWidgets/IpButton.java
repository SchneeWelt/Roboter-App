package de.softwareelevators.robotersteuerung.uiWidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import de.softwareelevators.robotersteuerung.MainActivity;
import de.softwareelevators.robotersteuerung.R;

public class IpButton extends AppCompatButton
{
    private MainActivity mainActivity;

    public IpButton(@NonNull Context context)
    {
        super(context);

        setup(context);
    }

    public IpButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        setup(context);
    }

    public IpButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setup(context);
    }

    private void setup(Context context)
    {
        this.mainActivity = (MainActivity) context;

        setOnClickListener(this::onButtonClicked);
    }

    private void onButtonClicked(View view)
    {
        // Pop Up öffnen, um die Ziel IP adresse abzufragen
        openPopUp();
    }

    private void openPopUp()
    {
        BottomSheetDialog dialog = new BottomSheetDialog(mainActivity);

        // Der Inhalt des PopUps
        View dialogView = mainActivity.getLayoutInflater().inflate(R.layout.ip_pop_up, null);

        EditText ipInput = dialogView.findViewById(R.id.input_ip);
        EditText portInput = dialogView.findViewById(R.id.input_port);

        Button okButton = dialogView.findViewById(R.id.btn_ok);
        okButton.setOnClickListener((l) ->
            {
                String ip = ipInput.getText().toString();
                String portStr = portInput.getText().toString();

                Integer port = null;
                try
                {
                    port = Integer.parseInt(portStr);
                } catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }



                // Dialog schließen sobald der OK Button gedrückt wird
                dialog.dismiss();
            }
        );

        dialog.setContentView(dialogView);
        dialog.show();
    }
}
