package innolab.pallicare.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import innolab.pallicare.R;
import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.db.api.RetrofitInstance;
import innolab.pallicare.db.api.entity.LoginData;
import innolab.pallicare.db.entities.User;
import innolab.pallicare.db.entities.UserType;
import innolab.pallicare.io.SharedPrefHandler;
import innolab.pallicare.ui.BaseActivity;
import innolab.pallicare.ui.nok.NokOverviewActivity;
import innolab.pallicare.ui.patient.home.PatientOverviewActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Activity for login or moving on to initial registration
 * - implements BaseActivity
 *
 * @author Ulla Sternemann
 */
public class LoginActivity extends BaseActivity {

    /**
     * Stores the userType ID. Is just necessary as login is not yet possible.
     * TODO if login is possible rework then.
     */
    int userTypeID;

    /**
     * TextField to enter the username
     */
    private EditText et_email;
    /**
     * TextField to enter the password
     */
    private EditText et_pwd;

    /**
     * Save the email during login process
     */
    private String email = "";

    /**
     * Save the password during login process
     */
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideBackButton();
        hideBottomButtons(); //Are not needed here
        super.onCreate(savedInstanceState);

        et_email = findViewById(R.id.editText_login_username);
        et_pwd = findViewById(R.id.editText_login_pwd);

        //If this Activity is reached from a successful registration load the email which has already been saved through the intent
        //TODO use static final String
        email = getIntent().getStringExtra("email");
        if (email != null) {
            et_email.setText(email);
        }
    }


    /**
     * if the register button is clicked RegisterActivity is launched
     *
     * @param view the register button (@id/button_register)
     */
    public void launchRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * if the login button is pressed this class decides which overview to load next
     *
     * @param view ignore
     */
    public void login(View view) {
        if (!isEnteredDataValid()) {
            //TODO Show some error
            return;
        }

        LoginData loginData = new LoginData(email, password);

        Call<LoginData> call = RetrofitInstance.getInstance().authorizeUser(loginData);

        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if (!response.isSuccessful()) {
                    Log.i("API", "Code: " + response.code());
                    Log.i("API-Body", response.message());
                    //TODO ERROR Handling
                    showError(view);

                } else {
                    Log.i("API", "Code: " + response.code());
                    if (response.body() == null) {
                        //TODO Errorhandling
                        showError(view);

                    } else {
                        String usertoken = response.body().getKey();
                        Log.i("API-Body", usertoken);
                        if (usertoken != null) {
                            //TODO request user data by server and save it to DB
                            new SharedPrefHandler().saveUserToken(getApplicationContext(), usertoken);
                            new SharedPrefHandler().saveUserID(getApplicationContext(), response.body().getUser());

                            saveUserDataToDatabase();
                            launchCorrectOverview();
                        } else {
                            showError(view);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                Log.i("APIFailure", t.getMessage());
                //TODO error handling
                showError(view);
            }
        });
    }

    private void showError(View view) {
        Snackbar.make(view, "Anmeldung fehlgeschlagen!", Snackbar.LENGTH_LONG)
                .setTextColor(getColor(R.color.color_on_primary))
                .setBackgroundTint(getColor(R.color.color_secondary_variant))
                .show();
    }

    /**
     * Launches the correct activity for the logged in user
     * TODO dont use userTypeID (remove it), get information from DB
     */
    private void launchCorrectOverview() {
        //Get the IDs of the two login-able user types patient & nok
        UserType patientUser = PallicareRepository.getInstance(getApplication()).getUserTypeByName("patient");
        int patientTypeID = patientUser.getUserTypeId();
        UserType nokUser = PallicareRepository.getInstance(getApplication()).getUserTypeByName("nok");
        int nokTypeID = nokUser.getUserTypeId();

        //Differ actions between the two user types or not logged in.
        Intent intent;
        if (userTypeID == patientTypeID) {
            BaseActivity.setModeIsPatient(true); //TODO might be unnecessary
            intent = new Intent(this, PatientOverviewActivity.class);
            //Clean the appstack and only keep the new PatientOverviewActivity
            finishAffinity();
            startActivity(intent);
        } else if (userTypeID == nokTypeID) {
            BaseActivity.setModeIsPatient(false);
            intent = new Intent(this, NokOverviewActivity.class);
            //Clean the appstack and only keep the new NokOverviewActivity
            finishAffinity();
            startActivity(intent);
        } else {
            throw new RuntimeException("Systemfehler");
        }
    }

    /**
     * TODO implement
     * Check if user has entered data which is a email & a non empty password
     *
     * @return
     */
    private boolean isEnteredDataValid() {

        password = et_pwd.getText().toString().trim();
        email = et_email.getText().toString().trim();
        return (!password.isEmpty() && !email.isEmpty());
    }

    /**
     * Save the user data received from the server to the database
     */
    private void saveUserDataToDatabase() {
        //TODO remove connection via SharedPrefHandler
        //TODO neuer Table mit userCredentials? Der linkt die UserID des angemeldeten Benutzers und seine anmeldeDaten z.B. Token am Server
        userTypeID = new SharedPrefHandler().getLoggedInUserType(getApplicationContext());
        //TODO remove this workaround and determine userType 100% by logindata
        {
            if (userTypeID == -1) {
                userTypeID = PallicareRepository.getInstance(getApplication()).getUserTypeByName("patient").getUserTypeId();
            }
            new SharedPrefHandler().saveUserType(getApplicationContext(), userTypeID);
        }
        User newUser = new User("", "", "", 0, new Date(System.currentTimeMillis()), "", new Date(System.currentTimeMillis()), userTypeID);
        PallicareRepository.getInstance(getApplication()).insertUser(newUser);
        Toast.makeText(this, "Sie haben sich erfolgreich registriert, Sie können sich jetzt anmelden", Toast.LENGTH_LONG).show();
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.login_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_login;
    }
}
