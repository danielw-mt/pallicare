package innolab.pallicare.ui.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import innolab.pallicare.R;

/**
 * Show the information for all the required permissions and request them if not yet done.
 *
 * @author Insa Suchantke, refactored by Klaus Schmidt
 */
public class PermissionInformationActivity extends AppCompatActivity {

    private static final String LOG_TAG = PermissionInformationActivity.class.getSimpleName();

    /**
     * constants for permission requests
     */
    private static final int REQUEST_PHONE = 111;
    private static final int REQUEST_BLUETOOTH = 222;
    private static final int REQUEST_READ_WRITE_STORAGE = 333;
    private static final int REQUEST_FINE_LOCATION = 444;
    /**
     * The layout screens which will are presented through the sliding
     */
    int[] permission_screens;
    /**
     * The button to go to the next screen
     */
    Button bu_next;
    /**
     * The view pager leading through all screens and enables swiping
     */
    ViewPager viewPager;
    /**
     * The adapter to react on the interaction with the viewPager.
     */
    MyViewPagerAdapter myvpAdapter;
    /**
     * saves if call phone was granted (true if granted)
     */
    private boolean mPhonePermissionGranted;
    /**
     * saves if storage read and write permissions were granted (true if both read and write permission were granted)
     */
    private boolean mStoragePermissionsGranted;
    /**
     * saves if bluetooth normal and admin permissions were granted (true if both permissions were granted)
     */
    private boolean mBluetoothPermissionsGranted;
    /**
     * saves if access fine location was granted (true is it was granted)
     */
    private boolean mLocationPermissionGranted;
    /**
     * The listener to listen on interaction with the viewPager.
     */
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageSelected(int position) {
            //In screen 1 request permission 1 which just belongs to screen 0. //TODO?
            requestCurrentPermission(position);
            //If in last screen
            if (position == permission_screens.length - 1) {
                checkAllPermissionsFinal();
                bu_next.setText(getString(R.string.accepted));
            } else {
                bu_next.setText(getString(R.string.quest_btn_next));
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
//ignored
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
//ignored
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);

        //Setup the different to be shown screens.
        permission_screens = new int[]{
                R.layout.screen_permission_bluetooth,
                R.layout.screen_permission_storage,
                R.layout.screen_permission_call_phone,
                R.layout.screen_permission_overview
        };

        viewPager = findViewById(R.id.view_pager);

        bu_next = findViewById(R.id.next);
        //Set the default text
        bu_next.setText(getString(R.string.quest_btn_next));

        myvpAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myvpAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        //Connect the tablayout (bottom indicators) with the view pager
        ((TabLayout) findViewById(R.id.tab_layout)).setupWithViewPager(viewPager, true);
    }

    /**
     * Check for all permissions granted in the last screen to prevent manual modification.
     */
    private boolean checkAllPermissionsFinal() {
        TextView overview_state = findViewById(R.id.tv_permission_overview_text);

        boolean isAllPermissionsGranted = checkPermissions(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE
        );

        if (isAllPermissionsGranted) {
            //TODO Klaus: In Strings auslagern oder stylen.
            overview_state.setText("Alle Berechtigungen wurden gewährt. Sie können die App nun nutzen.");
        } else {
            overview_state.setText("Bitte stellen Sie sicher, dass die App alle Berechtigungen hat, um korrekt zu funktionieren.");
        }
        //TODO Klaus: In zwei methoden unterteilen. Evtl eine statische für den komplett check, weil wir das öfter brauchen?
        return isAllPermissionsGranted;
    }

    /**
     * If the next/accepted button is pressed
     *
     * @param v ignore
     */
    public void onNextButton(View v) {
        int i = getItem(+1);
        //if not yet in last screen set it
        if (i < permission_screens.length) {
            viewPager.setCurrentItem(i);

        } else {
            //If all permissions got granted proceed to app. Else return to first screen.
            if (checkAllPermissionsFinal()) {
                //Request permissions and proceed to login Activity
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                viewPager.setCurrentItem(0);
            }
        }

    }

    /**
     * Get the current + ith item
     *
     * @param i the distance from the current screen
     * @return the number of the chosen screen
     */
    @SuppressWarnings("SameParameterValue")
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    public void requestCurrentPermission(int i) {

        switch (i) {
            case 0:
                //TODO noch mit permissions zusammenfügen.
                mBluetoothPermissionsGranted = checkPermissions(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN);
                if (!mBluetoothPermissionsGranted) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                            REQUEST_BLUETOOTH);
                }
                break;
            case 1:
                mLocationPermissionGranted = checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
                if (!mLocationPermissionGranted) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                }
                break;
            case 2:
                mStoragePermissionsGranted = checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (!mStoragePermissionsGranted) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_READ_WRITE_STORAGE);
                }
                break;
            case 3:
                mPhonePermissionGranted = checkPermissions(Manifest.permission.CALL_PHONE);
                if (!mPhonePermissionGranted) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE);
                }
                break;
            default:
                Log.d(LOG_TAG, "permission code wrong?");
        }
    }

    /**
     * Handles what should be done if a single permission is denied.
     * Currently return to previous screen.
     */
    private void onPermissionDeny() {
        //if not granted reset to last screen.
        viewPager.setCurrentItem(getItem(-1));
    }

    /**
     * checks if given permissions are granted
     *
     * @param permission array with all permissions which should be checked as strings in form of
     *                   Manifest.permission.xxx, can be one or multiple
     * @return true if all passed permissions are already granted, false as soon as one is not granted
     */
    private boolean checkPermissions(@NonNull String... permission) {
        // checks all permissions individually
        boolean b = true;
        for (String p : permission) {
            b = b && ContextCompat.checkSelfPermission(this, p)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return b;
    }

    /**
     * handles results from permission requests
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_BLUETOOTH:
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(LOG_TAG, "permission BLUETOOTH and BLUETOOTH_ADMIN was granted");
                    mBluetoothPermissionsGranted = true;
                } else {
                    onPermissionDeny();
                    // permission was denied
                    Log.d(LOG_TAG, "permission BLUETOOTH and BLUETOOTH_ADMIN was NOT granted");
                    mBluetoothPermissionsGranted = false;
                }
                break;
            case REQUEST_READ_WRITE_STORAGE:
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(LOG_TAG, "permission READ_- and WRITE_EXTERNAL_STORAGE was granted");
                    mStoragePermissionsGranted = true;
                } else {
                    onPermissionDeny();


                    // permission was denied
                    Log.d(LOG_TAG, "permission READ_- and WRITE_EXTERNAL_STORAGE was NOT granted");
                    mStoragePermissionsGranted = false;
                }
                break;
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    Log.d(LOG_TAG, "ACCESS_FINE_LOCATION permission was granted");
                } else {
                    onPermissionDeny();

                    mLocationPermissionGranted = false;
                    Log.d(LOG_TAG, "ACCESS_FINE_LOCATION permission was NOT granted");
                }
                break;
            case REQUEST_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhonePermissionGranted = true;

                    //Update last screen
                    checkAllPermissionsFinal();

                    Log.d(LOG_TAG, "CALL_PHONE permission was granted");
                } else {
                    onPermissionDeny();

                    mPhonePermissionGranted = false;
                    Log.d(LOG_TAG, "CALL_PHONE permission was NOT granted");
                }
                break;
            default:
                Log.d(LOG_TAG, "param requestCode (from void onRequestPermissionsResult) was not as expected: " + requestCode);
        }
    }

    /**
     * Adapter to inflate the layout and react to interaction
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        @SuppressWarnings("FieldCanBeLocal")
        private LayoutInflater inflater;

        MyViewPagerAdapter() {
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup container, int position) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(permission_screens[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return permission_screens.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        @Override
        public boolean isViewFromObject(@NotNull View v, @NotNull Object object) {
            return v == object;
        }
    }
}