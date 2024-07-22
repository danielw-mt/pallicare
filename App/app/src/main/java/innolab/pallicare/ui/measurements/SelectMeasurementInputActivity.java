package innolab.pallicare.ui.measurements;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import innolab.pallicare.R;
import innolab.pallicare.ui.BaseActivity;

/**
 * This activity is used to measure manually or automatically
 * - extends {@link BaseActivity}
 *
 * @author Insa Suchantke
 */

//TODO: INSA add to wiki and describe why it gets removed
//TODO to be deleted, go to subsequent activities from overview directly
//TODO Maybe rename?
//TODO GIve activity better name. Not specific / very similar to "BiometricViewSelectionActivity"
public class SelectMeasurementInputActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_select_measurement_input;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.select_measurement_input_activity_title;
    }

    public void launchDeviceMeasurementActivity(View view){
        Intent intent = new Intent(this, ScaleMeasurementActivity.class);
        startActivity(intent);
    }
    /**
     * This intent started by pressing Manual on the screen.
     *
     * @param view Button @+id/button_selectMeasurementInput_manuall
     */
    public void launchMeasurementInputManualActivity(View view) {
        Intent intent = new Intent(this, BiometricManualInputActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_select_measurement_input;
    }
}
