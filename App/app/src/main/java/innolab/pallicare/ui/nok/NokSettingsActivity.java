package innolab.pallicare.ui.nok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import innolab.pallicare.R;
import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.io.SharedPrefHandler;
import innolab.pallicare.ui.BaseActivity;
import innolab.pallicare.ui.auth.WelcomeMessageActivity;

/**
 * Activity for all available settings of nok
 * - implements BaseActivity
 *
 * @author Klaus Schmidt
 */
public class NokSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_nok_settings_overview;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.settings_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_nok_setting;
    }

    /**
     * This lets the user logout.
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
