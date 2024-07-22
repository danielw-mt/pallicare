package innolab.pallicare.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.session.MediaSession;

/**
 * This class is more or less persistent and is used to I/O with the SharedPreferences
 * TODO implement as Singleton?
 * @author Klaus Schmidt
 */
public class SharedPrefHandler {

    /**
     * The file name of the Shared-Pref-File
     */
    private static final String PREF_NAME = "settings_pallicare";

    /**
     * References the usertype. -1 not defined (not logged in), 0=Patient (logged in), 1=NoK (logged in)
     */
    private static final String USER_TYPE = "usertype";

    private static final String FIRST_RUN = "first_run";

    private static final String USERTOKEN = "token";

    private static final String USERID = "user_id";

    /**
     * Retrieves the user type to decide which overview screen to start.
     * Currently the usertype is just dummy saved in the shared preferences on registering.
     * //TODO request the user type from the database
     *
     * @param context context of the activity calling this method. retrieve by getApplicationContext()
     */
    public int getLoggedInUserType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(USER_TYPE, -1);
    }


    /**
     * Saves the selected userType in the preferences
     *
     * @param context  context of the activity calling this method. retrieve by getApplicationContext()
     * @param userType the userType 0:patient, 1:nok, -1 not logged in, else undefined
     */
    public void saveUserType(Context context, int userType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_TYPE, userType);
        editor.apply();
    }

    /**
     * Saves the usertoken received from the server
     *
     * @param context  context of the activity calling this method. retrieve by getApplicationContext()
     * @param usertoken the token required to post to the api
     */
    public void saveUserToken(Context context, String usertoken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERTOKEN, usertoken);
        editor.apply();
    }

    /**
     * Retrieves the user token to communicate with the server
     * //TODO save the token to the DB
     *
     * @param context context of the activity calling this method. retrieve by getApplicationContext()
     */
    public String getLoggedInUserToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERTOKEN, "");
    }


    /**
     * Logs the user out, by setting the saved user type to -1
     * TODO Should do more and clean up all data, etc
     * TODO delete all DB data except prepopulation.
     *
     * @param context context of the activity calling this method. retrieve by getApplicationContext()
     */
    public void logout(Context context) {
        saveUserType(context, -1);
    }

    /**
     * Returns, if the app is run for the first time.
     *
     * @param context context of the activity calling this method. retrieve by getApplicationContext()
     * @return if the app is run for the first time
     */
    public boolean isFirstRun(Context context) {
        //TODO Klaus: if it is the first run prepopulate the DB
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(FIRST_RUN, true)) {
            sharedPreferences.edit().putBoolean(FIRST_RUN, false).apply();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Save the userid of the current user to the shared data
     * TODO to database? together with the token.
     * @param context context of the activity calling this method. retrieve by getApplicationContext()
     * @param userID the userID of the currently logged in user
     */
    public void saveUserID(Context context, int userID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USERID, userID);
        editor.apply();
    }

    /**
     * Retrieve the userid of the current logged in user
     *
     * @param context context of the activity calling this method. retrieve by getApplicationContext()
     * @return the userID of the currently logged in user
     */
    public int getLoggedInUserID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(USERID, -1);
    }
}
