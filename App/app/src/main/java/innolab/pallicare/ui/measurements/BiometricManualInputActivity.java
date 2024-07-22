package innolab.pallicare.ui.measurements;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import innolab.pallicare.R;
import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.db.api.RetrofitInstance;
import innolab.pallicare.db.api.entity.ScaleMeasurementData;
import innolab.pallicare.db.entities.MeasurementBloodpressure;
import innolab.pallicare.db.entities.MeasurementScale;
import innolab.pallicare.io.SharedPrefHandler;
import innolab.pallicare.ui.BaseActivity;
import innolab.pallicare.util.InputValidation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//TODO: Klaus: Split to view and add. Add to patient.

/**
 * Activity for manual input of biometric measurement data
 * - implements BaseActivity
 *
 * @author Insa Suchantke and Ulla Sternemann
 */
public class BiometricManualInputActivity extends BaseActivity {

    /**
     * TextField to enter the weight
     */
    EditText editText_weight;

    /**
     * TextField to enter the systolic bp
     */
    EditText editText_systolic;

    /**
     * TextField to enter the diastolic bp
     */
    EditText editText_diastolic;
    //TODO so far only allowed to enter three numbers as weight? without , or .

    /**
     * Stores the weight
     */
    float weight;

    /**
     * Stores the diastolic bp
     */
    int diastolic;

    /**
     * stores the systolic bp
     */
    int systolic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editText_weight = findViewById(R.id.editText_measurement_input_weight); //TODO Remove workaround
        editText_systolic = findViewById(R.id.editText_measurement_input_bp_first);//TODO Remove workaround
        editText_diastolic = findViewById(R.id.editText_measurement_input_bp_second);//TODO Remove workaround


    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_measurement_input_manual;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.measurement_input_manual_activity_title;
    }

    /**
     * Saves the weight to the DB, if possible
     *
     * @param view
     */
    public void saveWeight(View view) {
        if (isWeightValueValid()) {
            int userID = PallicareRepository.getInstance(getApplication()).getCurrentPatient().getUserId();

            MeasurementScale measurementScale = new MeasurementScale(weight, 0, 0, 0, new Date(System.currentTimeMillis()), userID);
            //TODO adapt to use correct user id.
            PallicareRepository.getInstance(getApplication()).insertMeasurementScale(measurementScale);
            PallicareRepository.getInstance(getApplication()).saveWeightToServer(weight, getApplicationContext());


            editText_weight.setText("");
            weight = 0;

        } else {
            //TODO show error
        }

    }



    /**
     * Checks whether the entered string is a valid weight.
     *
     * @return true or false
     */
    private boolean isWeightValueValid() {
        String weightString = editText_weight.getText().toString();
        float weightValue = InputValidation.isStringValidFloat(weightString);
        //TODO Check better on a valid range?
        //TODO After saving to DB -> Assess if it is critical
        if (weightValue != 0) {
            weight = weightValue;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Saves the bp to the DB, if possible
     *
     * @param view
     */
    public void saveBP(View view) {
        boolean bothValid = true;
        //TODO check if value ok. Evtl mit einem & checken? Good code style dafür googlen.
        if (!isSystolicValueValid()) {
            //TODO Show error
            bothValid = false;
        }
        if (!isDiastolicValueValid()) {
            //TODO Show error
            bothValid = false;
        }

        if (bothValid) {
            int userID = PallicareRepository.getInstance(getApplication()).getCurrentPatient().getUserId();
            MeasurementBloodpressure measurementBloodpressure = new MeasurementBloodpressure(systolic, diastolic, new Date(System.currentTimeMillis()), userID);
            //TODO adapt to use correct user id.
            //TODO name repo method fors add/insert equal
            PallicareRepository.getInstance(getApplication()).addBloodpressureMeasurement(measurementBloodpressure);
            editText_systolic.setText("");
            editText_diastolic.setText("");
            systolic = 0;
            diastolic = 0;
        }


    }

    /**
     * Checks whether the entered string is a valid diastolic bp.
     *
     * @return true or false
     */
    private boolean isDiastolicValueValid() {
        String diastolicString = editText_diastolic.getText().toString();
        int diastolicValue = InputValidation.isStringValidInt(diastolicString);
        //TODO Check better on a valid range?
        if (diastolicValue != 0) {
            diastolic = diastolicValue;
            return true;
        } else {
            return false;
        }
    }


    /**
     * Checks whether the entered string is a valid systolic bp.
     *
     * @return true or false
     */
    private boolean isSystolicValueValid() {
        String systolicString = editText_systolic.getText().toString();
        int systolicValue = InputValidation.isStringValidInt(systolicString);
        //TODO Check better on a valid range?
        if (systolicValue != 0) {
            systolic = systolicValue;
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_biometric_manual_input;
    }
}
