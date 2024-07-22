package innolab.pallicare.ui.patient.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import innolab.pallicare.R;
import innolab.pallicare.ui.BaseActivity;
import innolab.pallicare.ui.measurements.BiometricViewSelectionActivity;
import innolab.pallicare.ui.measurements.SelectMeasurementInputActivity;
import innolab.pallicare.ui.patient.questionnaire.QuestionnaireMidos1P1Info;
import innolab.pallicare.ui.psychometrical_output.SufferingsOverviewActivity;
import innolab.pallicare.ui.patient.settings.PatientSettingsActivity;

/**
 * This activity hold the overview of the main functions of this app
 * - extends {@link BaseActivity}
 *
 * @author Ulla Sternemann and Insa Suchantke
 */
public class PatientOverviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideBackButton();
        hideHomeButton();
        super.onCreate(savedInstanceState);
    }

    /**
     * Launches QuestionnaireActivity when questionnaire button is clicked
     *
     * @param view the button view which was clicked (@id/btn_patient_overview_psychometric_input)
     */
    public void launchQuestionnaireActivity(View view) {
        Intent intent = new Intent(this, QuestionnaireMidos1P1Info.class);
        startActivity(intent);
    }

    /**
     * Launches SettingsPatientOverviewActivity when corresponding button is clicked
     *
     * @param view the button view which was clicked (@id/btn_patient_overview_psychometric_view)
     */
    public void launchQuestionnaireOverviewActivity(View view) {
        Intent intent = new Intent(this, SufferingsOverviewActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param view the settings button (@id/btn_patient_overview_settings)
     */
    public void launchSettingsActivity(View view) {
        Intent intent = new Intent(this, PatientSettingsActivity.class);
        startActivity(intent);
    }

    /**
     * launches BiometricViewSelectionActivity if corresponding button is clicked
     *
     * @param view the view measurement button (@id/btn_patient_overview_biometric_view)
     */
    public void launchMeasurementActivity(View view) {
        Intent intent = new Intent(this, BiometricViewSelectionActivity.class);
        startActivity(intent);
    }



    /**
     * launches SelectMeasurementInputActivity if corresponding button is clicked
     *
     * @param view the add measurement button (@id/btn_patient_overview_biometric_input)
     */
    public void launchSelectMeasurementInputActivity(View view) {
        Intent intent = new Intent(this, SelectMeasurementInputActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_overview;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.overview_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_patient_overview;
    }
}
