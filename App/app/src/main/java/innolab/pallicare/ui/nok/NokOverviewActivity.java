package innolab.pallicare.ui.nok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import innolab.pallicare.R;
import innolab.pallicare.ui.BaseActivity;

/**
 * This activity shows the homescreen in nok mode
 * - extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class NokOverviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideBackButton();
        hideHomeButton();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_nok_homescreen;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.overview_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_nok_home;
    }

    /**
     * Sends an intent to change to the Nok settings activity.
     *
     * @param view ignore
     */
    public void launchNokSettingsView(View view) {
        Intent intent = new Intent(getApplicationContext(), NokSettingsActivity.class);
        startActivity(intent);
    }
}
