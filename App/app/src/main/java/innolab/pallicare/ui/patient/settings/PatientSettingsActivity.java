package innolab.pallicare.ui.patient.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import innolab.pallicare.R;
import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.io.SharedPrefHandler;
import innolab.pallicare.ui.BaseActivity;
import innolab.pallicare.ui.auth.LoginActivity;
import innolab.pallicare.ui.auth.WelcomeMessageActivity;
/**
 * Activity for all available settings
 * - implements BaseActivity
 *
 * @author Ulla Sternemann
 */
public class PatientSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * if the manage devices button is clicked ManageDevicesActivity is launched
     *
     * @param view the manage devices button (@id/button_device_settings)
     */
    public void launchManageDevicesActivity(View view) {
        Intent intent = new Intent(this, ManageDevicesActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_settings;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.settings_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_patient_settings;
    }

    /**
     * This button lets the user logout.
     * TODO implemented in each settings screen so far
     *
     * @param view ignore
     */
    public void launchLogout(View view) {
        new SharedPrefHandler().logout(getApplicationContext());
        PallicareRepository.getInstance(getApplication()).clearDB();
        Intent intent = new Intent(getApplicationContext(), WelcomeMessageActivity.class);
        //Delete the whole AppStack and start with the provided WelcomeMessageActivity
        finishAffinity();
        startActivity(intent);
    }

}
