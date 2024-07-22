package innolab.pallicare.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.db.entities.UserType;
import innolab.pallicare.io.SharedPrefHandler;
import innolab.pallicare.ui.BaseActivity;
import innolab.pallicare.ui.nok.NokOverviewActivity;
import innolab.pallicare.ui.patient.home.PatientOverviewActivity;

/**
 * This class will display a Splash screen with the PalliCare-Logo to check if the user is already logged in to route to an appropiate activity
 *
 * @author Patrick Höfner
 */
//TODO: Patrick use login instead of user&password
//TODO: Patrick Logout Button
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO improve? Maybe Implement in DB with Future?
        //Create and prepopulate the database on first run.
        if (new SharedPrefHandler().isFirstRun(getApplicationContext())) {
            //Load the repository and therefore prepopulate the database.
            PallicareRepository.getInstance(getApplication());
            new Handler().postDelayed(() -> {
                //Wait with routing to let database be prepopulated.
                routeToAppropriatePage();
                finish();
            }, 2000);
        } else {
            routeToAppropriatePage();
            finish();
        }


    }

    /**
     * Checks if the user is already logged-in and routes afterwards to an appropiate activity
     * TODO improve login process
     * TODO similar code in Login activity: login
     *
     * @author Klaus schmidt
     */
    private void routeToAppropriatePage() {
        int userTypeID = new SharedPrefHandler().getLoggedInUserType(getApplicationContext());
        Intent intent;

        //Get the IDs of the two login-able user types patient & nok
        UserType patientUser = PallicareRepository.getInstance(getApplication()).getUserTypeByName("patient");
        final int patientTypeID = patientUser.getUserTypeId();
        UserType nokUser = PallicareRepository.getInstance(getApplication()).getUserTypeByName("nok");
        final int nokTypeID = nokUser.getUserTypeId();

        //Differ actions between the two user types or not logged in.
        if (userTypeID == patientTypeID) {
            BaseActivity.setModeIsPatient(true);
            intent = new Intent(this, PatientOverviewActivity.class);
        } else if (userTypeID == nokTypeID) {
            BaseActivity.setModeIsPatient(false);
            intent = new Intent(this, NokOverviewActivity.class);
        } else {
            intent = new Intent(this, WelcomeMessageActivity.class);
        }
        startActivity(intent);
    }
}
