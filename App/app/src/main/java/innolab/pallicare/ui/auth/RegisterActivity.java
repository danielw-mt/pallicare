package innolab.pallicare.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

import innolab.pallicare.R;
import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.db.api.RetrofitInstance;
import innolab.pallicare.db.api.entity.RegisterData;
import innolab.pallicare.db.entities.UserType;
import innolab.pallicare.io.SharedPrefHandler;
import innolab.pallicare.ui.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is used to show the different opportunities for registration
 * - extends {@link BaseActivity}
 *
 * @author Insa Suchantke
 */

public class RegisterActivity extends BaseActivity {

    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();
    /**
     * instance of database repository
     */
    PallicareRepository mRepo;

    /**
     * The tab layout to select the type: patient and NoK
     */
    TabLayout tabLayout;

    /**
     * Textfield to enter the  first name
     */
    EditText editTextFirstName;

    /**
     * Textfield to enter the phone number
     */
    EditText editTextPhone;

    /**
     * Textfield to enter the mail
     */
    EditText editTextMail;

    /**
     * Textfield to enter the password
     */
    EditText editTextPwd; // TODO password hash?

    /**
     * Textfield to enter the last name
     */
    EditText editTextLastName;

    /**
     * Temporary stores the userType ID. TODO possible not necessary?
     */
    int userTypeID;


    /**
     * TODO
     * Save the inputted data
     */
    String mail;
    String lastName;
    String firstName;
    String phone;

    /**
     * TODO improve how to save the user TYPE ID, make TYPE IDs permanent?
     * Defines which userType is sent to the API (patient=1, nok=2)
     */
    private int apiTypeID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //These are not needed so far
        hideBottomButtons();
        super.onCreate(savedInstanceState);

        tabLayout = findViewById(R.id.login_tab_layout);
        editTextLastName = findViewById(R.id.editText_lastname);
        editTextPhone = findViewById(R.id.editText_phone);
        editTextMail = findViewById(R.id.editText_mail);
        editTextPwd = findViewById(R.id.editText_pwd);
        editTextFirstName = findViewById(R.id.editText_firstname);
        mRepo = PallicareRepository.getInstance(getApplication());
        Log.d(LOG_TAG, "repo: " + mRepo.toString());
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.register_activity_title;
    }

    /**
     * This intent started by pressing NextOfKin on the screen.
     *
     * @author Klaus Schmidt
     * TODO Handle Errors
     */
    public void launchRegistration(View view) {
        //TODO Daten am Anfang einmal abrufen und speichern. Nicht in jeder Zwischenmethode abrufen.
        if (enteredDataCorrect()) {

            //Try to register at server //TODO remove dummy data
            RegisterData registerData = new RegisterData(mail, firstName, lastName, "male", "2000-02-02", phone, apiTypeID, editTextPwd.getText().toString());

            //TODO add error handling
            Call<RegisterData> call = RetrofitInstance.getInstance().registerUser(registerData);
            call.enqueue(new Callback<RegisterData>() {
                @Override
                public void onResponse(Call<RegisterData> call, Response<RegisterData> response) {
                    if (!response.isSuccessful()) {
                        Log.i("API", "Code: " + response.code());
                        Log.i("API-Body", response.message());
                        Log.i("API-Body", response.raw().toString());
                    } else {
                        Log.i("API-...", "Token" + response.raw());
                        afterSuccessfulRegistration();
                    }
                }

                @Override
                public void onFailure(Call<RegisterData> call, Throwable t) {
                    Log.i("APIFailure", t.getMessage());

                }
            });
        }
    }


    /**
     * If a user was registered successful, proceed here by returning to the login screen and giving the intent the email
     */
    public void afterSuccessfulRegistration() {
        //TODO No data should be saved locally. Should be derived at login from server
        new SharedPrefHandler().saveUserType(getApplicationContext(), userTypeID);

        // TODO return intent with emailadress
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("email", mail);
                            /* To delete the whole stack above an activity and return to this specific activity,
                            read more in https://stackoverflow.com/a/14857698*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    /**
     * Checks if all user entered data is correct. No empty fields, password rules, email pattern correct, ...
     * TODO Added by Klaus, can be implemented by anyone
     *
     * @return if all the data is valid
     */
    private boolean enteredDataCorrect() {

        //At first save everything temporary and if all checks ok save to class variables.

        //Get the userTypeID for the current selected tab.
        if (tabLayout.getSelectedTabPosition() == 0) {
            //TODO when populating the DB always use the same IDs for the types and then use constants?
            UserType patientUser = PallicareRepository.getInstance(getApplication()).getUserTypeByName("patient");
            userTypeID = patientUser.getUserTypeId();
            apiTypeID = 1; //TODO improve. 1 = patient
        } else {
            UserType nokUser = PallicareRepository.getInstance(getApplication()).getUserTypeByName("nok");
            userTypeID = nokUser.getUserTypeId();
            apiTypeID = 2; //TODO improve 2=nok
        }

        firstName = editTextFirstName.getText().toString();

        lastName = editTextLastName.getText().toString();

        phone = editTextPhone.getText().toString();

        mail = editTextMail.getText().toString();

        //TODO INSA: add checks here
        //TODO after checks save local variables to class variables.
        return true;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_register;
    }


}
