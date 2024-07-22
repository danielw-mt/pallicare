package innolab.pallicare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import innolab.pallicare.R;
import innolab.pallicare.ui.nok.NokOverviewActivity;
import innolab.pallicare.ui.patient.emergency.EmergencyActivity;
import innolab.pallicare.ui.patient.home.PatientOverviewActivity;

/**
 * This class is the base class of all activities with a toolbar and the bottom bar.
 * Instead of extending your activities with AppCompatActivity please use now BaseActivity.
 * Therefore you have to implement the two abstract methods getLayoutTitleDescriptor and getLayoutID.
 *
 * @author Klaus Schmidt
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Flag to indicate whether the app is used by the patient or for a next of kin
     */
    private static boolean isPatientMode = false;

    @SuppressWarnings("FieldCanBeLocal")
    protected static String LOG_TAG = "PalliCare:CurrentActivity";

    /**
     * The actionBar / toolbar at the top
     */
    ActionBar supportActionBar;

    /**
     * If this flag is set, the home button will be hidden
     */
    private boolean isHomeButtonHidden = false;

    /**
     * If this flag is set, the alarm button will be hidden
     */
    private boolean isAlarmButtonHidden = false;

    /**
     * If this flag is set, the back button will be hidden
     */
    private boolean hideBackButton = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, this.getLocalClassName());

        //sets the base view
        setContentView();

        //setsUp the ActionBar / toolbar
        setUpActionBar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add the toolbar menu, to display the help button
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Custom setContentView method, to additionally inflate the child layout inside the FrameLayout placeholder.
     * Calls the default super method setContentView.
     * The childLayout for the FrameLayout is loaded during runtime with the getLayoutID method.
     * If the BottomButtons are disabled they will be hidden in the layout. (e.g. in the alarm screen)
     */
    public void setContentView() {
        super.setContentView(R.layout.activity_base);
        FrameLayout frameLayout = findViewById(R.id.base_class_container);
        getLayoutInflater().inflate(getLayoutID(), frameLayout, true);

        if (!isPatientMode) {
            hideAlarmButton();
        }

        //If both bottom buttons are disabled, hide their parent container otherwise just hide a single button
        if (isAlarmButtonHidden && isHomeButtonHidden) {
            findViewById(R.id.base_bottom_panel).setVisibility(View.GONE);
        } else if (isAlarmButtonHidden) {
            findViewById(R.id.base_card_right).setVisibility(View.GONE);
        } else if (isHomeButtonHidden) {
            findViewById(R.id.base_card_left).setVisibility(View.GONE);
        }
    }


    /**
     * Make the home button to be hidden. Call this method before super.onCreate in the child class.
     */
    protected void hideHomeButton() {
        isHomeButtonHidden = true;
    }

    /**
     * Make the alarm button to be hidden. Call this method before super.onCreate in the child class.
     */
    protected void hideAlarmButton() {
        isAlarmButtonHidden = true;
    }


    /**
     * Call this method before super.onCreate in the child class to hide both of the bottom buttons (e.g. in emergency screen)
     */
    protected void hideBottomButtons() {
        hideHomeButton();
        hideAlarmButton();
    }

    /**
     * Call this method before super.onCreate in the child class to hide the back button (e.g. in the overview screen)
     */
    protected void hideBackButton() {
        hideBackButton = true;
    }

    /**
     * Displays the actionbar if possible, sets the title which is received by getLayoutTitleDescriptor, and adds the back button unless it is suppressed.
     */
    private void setUpActionBar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (!hideBackButton) {
                supportActionBar.setDisplayHomeAsUpEnabled(true);
                supportActionBar.setDisplayShowHomeEnabled(true);
            }
            String title = getString(getLayoutTitleDescriptor());
            supportActionBar.setTitle(title);
        }

    }

    /**
     * Set a string as the toolbar title. Please use only in exceptional cases and always after calling super.onCreate in a child class.
     *
     * @param title the new title
     */
    @SuppressWarnings("unused") //TODO: Patrick remove if used
    public void setToolbarTitle(String title) {
        supportActionBar.setTitle(title);
    }

    /**
     * This method is called, if the back button of the action bar is pressed, and causes the back to return to the previous activity.
     *
     * @return true
     */
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * This intent started by one of the bottom buttons will lead to the main menu of the specified user type
     *
     * @param view ignore
     */
    public void launchHomeActivity(View view) {

        Intent intent;
        if (isPatientMode) {
            intent = new Intent(this, PatientOverviewActivity.class);
        } else {
            intent = new Intent(this, NokOverviewActivity.class);
        }
        /* To delete the whole stack above an activity and return to this specific activity,
        read more in https://stackoverflow.com/a/14857698*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * This intent started by one of the bottom buttons will lead to the alarm menu.
     *
     * @param view ignore
     */
    public void launchAlarmActivity(View view) {
        Intent intent = new Intent(this, EmergencyActivity.class);
        startActivity(intent);
    }

    /**
     * @return The layout id (R.layout....) that's gonna be the activity view.
     */
    protected abstract int getLayoutID();

    /**
     * @return The reference (R.string....) to the title of the activity
     */
    protected abstract int getLayoutTitleDescriptor();

    /**
     * Please implement this method to be able to display the helptext.
     *
     * @return he reference (R.string....) to the help text of this activity
     */
    protected abstract int getHelpText();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.base_icon_help) {
            String helpText = getString(getHelpText());
            showHelpText(helpText);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays the helpText. Currently implemented as dialog.
     *
     * @param helpText help text to be printed
     */
    private void showHelpText(String helpText) {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(helpText);
        builder.setTitle(getString(R.string.help));

        // 3. Add the ok-Button, but with nulled listener to dismiss the dialog
        builder.setPositiveButton(R.string.ok, null);

        // 4. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Sets if the current user should see the patient activities(true) or the ones for next of kin(false)
     *
     * @param isPatientMode true, if the user should see the patient activities, for next of kin use else.
     */
    public static void setModeIsPatient(boolean isPatientMode) {
        BaseActivity.isPatientMode = isPatientMode;
    }

    /**
     * Logs the complete content of a bundle
     * @param parameters the bundle to be logged
     */
    protected void logParams(Bundle parameters){
        for (String key : parameters.keySet())
        {
            Log.d("QuestBundleContent", key + " = \"" + parameters.get(key) + "\"");
        }
    }

}