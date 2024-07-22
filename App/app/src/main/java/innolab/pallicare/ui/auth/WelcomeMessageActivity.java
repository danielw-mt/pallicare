package innolab.pallicare.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import innolab.pallicare.R;

/**
 * This class will show a welcome screen when installing the app.
 * Different welcome text depending on patient or next of kin --> clicking on a tab
 *
 * @author Insa Suchantke
 */

public class WelcomeMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_message);

        //The tab layout to select the type: patient and NoK --> get the reference of TabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout_welcome_message);

        //TODO: In zwei Screens aufteilen? Einen mit Auswahl Patient/NOK und dann die Textanzeige?
        //TODO auf jeden Fall Schriftgröße vom TabText vergrößern/anpassen.
        TextView tvWelcomeMessage = findViewById(R.id.tv_welcome_message_text);

        // Load welcome messages from strings
        String tvWelcomeTextPatient = getString(R.string.welcome_message_patient);
        String tvWelcomeTextNok = getString(R.string.welcome_message_nok);

        // Listener to be notified when any tab's selection state has been changed
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // Called when a tab enters the selected state
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // position 0 is patient, position 1 is nok
                int position = tab.getPosition();
                if (position == 0) {
                    // set text of tvWelcomeMessage to tvWelcomeTextPatient
                    tvWelcomeMessage.setText(tvWelcomeTextPatient);

                } else if (position == 1) {
                    // set text of tvWelcomeMessage to tvWelcomeTextNok
                    tvWelcomeMessage.setText(tvWelcomeTextNok);
                }
            }

            // Called when a tab exits the selected state
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            /*TODO: Insa Is the content of onTabReselected really necessary? I tested it,
                and the screen works with this method empty. If you think the implementation is
                necessary it would be good style of code to remove redundancy of this method and
                onTabSelected and extract the content to its own private method*/

            // Called when a tab that is already selected is chosen again by the user
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // position 0 is patient, position 1 is nok
                int position = tab.getPosition();
                if (position == 0) {
                    // set text of tvWelcomeMessage to tvWelcomeTextPatient
                    tvWelcomeMessage.setText(tvWelcomeTextPatient);

                } else if (position == 1) {
                    // set text of tvWelcomeMessage to tvWelcomeTextNok
                    tvWelcomeMessage.setText(tvWelcomeTextNok);
                }
            }
        });
    }

    /**
     * launches PermissionCallPhoneActivity if corresponding button is clicked
     *
     * @param view the begin button (@id/btn_welcome_message_begin)
     */

    public void launchPermissionCallPhoneActivity(View view) {
        //TODO Insa: Check before which permissions have not been set yet (e.g. in case of logout) and request / display only those.
        Intent intent = new Intent(this, PermissionInformationActivity.class);
        startActivity(intent);
    }
}
