package innolab.pallicare.ui.measurements;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import innolab.pallicare.R;
import innolab.pallicare.ui.BaseActivity;

/**
 * This class will display an overview of all gathered measurements like weight, bloodpressure etc.
 * extends {@link BaseActivity} with hidden bottom buttons
 *
 * @author Patrick Höfner
 */
//TODO GIve activity better name. Not specific / very similar to "SelectMeasurementInputActivity"
public class BiometricViewSelectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Override BaseActivity functions
     */
    @Override
    protected int getLayoutID() {
        return R.layout.activity_measurement;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.deviceData_activity_title;
    }

    public void launchWeightActivity(View view) {
        Intent intent = new Intent(this, WeightViewActivity.class);
        startActivity(intent);
    }

    public void launchBloodPressureActivity(View view) {
        Intent intent = new Intent(this, BloodPressureViewActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_biometric_view;
    }
}
