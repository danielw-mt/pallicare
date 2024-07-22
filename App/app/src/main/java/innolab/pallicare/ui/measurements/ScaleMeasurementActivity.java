package innolab.pallicare.ui.measurements;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

import innolab.pallicare.R;
import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.db.entities.MeasurementScale;
import innolab.pallicare.openScale.BluetoothBeurerSanitas;
import innolab.pallicare.openScale.ScaleMeasurement;
import innolab.pallicare.ui.BaseActivity;
import innolab.pallicare.util.Util;

/**
 * This class is used for triggering the BLE Communication process.
 * It replaces Openscale.java from the openscale project to get rid of boilerplate code.
 * A device driver is created and Bluetooth Permissions are checked.
 * A Bluetooth Callback handler is used for debugging the connection.
 *
 * @author Daniel Wagner, refactored by Klaus Schmidt
 */
public class ScaleMeasurementActivity extends BaseActivity {
    /**
     * constant to differentiate between different system requests
     */
    private final int ENABLE_BLUETOOTH_REQUEST = 100;
    /**
     * counts how many valid, not too fluctuating measurements are being taken in one piece
     * It starts with 0, as it is also the counter for the progressBar
     */
    int progressCount = 0;
    /**
     * The last transmitted weight. The current weight is allowed to vary ~0.5 from the last weight to increase the progressCount
     */
    float lastWeight = 0.0f;
    /**
     * This is a dummy BluetoothMac Adress. Since it is not possible for Android > 6.0 to extract the MAC Adresses easily anymore, I use this one.
     * The address is needed to search for BLE devices.
     */
    String hwAddress = "C8:B2:1E:C2:A6:F8";
    /**
     * The progressBar to show an indicator how long it takes for the scale to get ready.
     */
    ProgressBar progressBar;
    /**
     * Button to quit the weighing process.
     */
    Button quitButton;
    /**
     * textview for displaying hints and the final weight
     */
    private TextView displayWeightTextView;
    /**
     * Handler for dealing with a change of the BT_STATUS of the Device driver.
     * When Retrieving a measurement it can be stored from here. All the other changes in
     * BT Status are just shown for debugging purposes.
     */
    private final Handler callbackBtHandler = new Handler() {
        /**
         * Depending on message that gets sent from BluetoothBeurerSanitas different actions are performed.
         * @param msg the message from the driver
         */
        @Override
        public void handleMessage(Message msg) {
            BluetoothBeurerSanitas.BT_STATUS btStatus = BluetoothBeurerSanitas.BT_STATUS.values()[msg.what];
            switch (btStatus) {
                case RETRIEVE_SCALE_DATA:

                    //Set the progress bar to 100%
                    progressBar.setProgress(progressBar.getMax());

                    //Play notification sound
                    Util.vibrate(getApplicationContext());
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Called on stable measurement
                    ScaleMeasurement scaleBtData = (ScaleMeasurement) msg.obj;
                    displayWeightTextView.setText(String.format(getResources().getString(R.string.info_scale_weight_saved), Float.toString(scaleBtData.getWeight())));
                    int userID = PallicareRepository.getInstance(getApplication()).getCurrentPatient().getUserId();
                    PallicareRepository.getInstance(getApplication()).insertMeasurementScale(new MeasurementScale(scaleBtData.getWeight(), 0, 0, 0, new Date(System.currentTimeMillis()), userID));
                    PallicareRepository.getInstance(getApplication()).saveWeightToServer(scaleBtData.getWeight(), getApplicationContext());

                    quitButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
//                    OpenScale openScale = OpenScale.getInstance();
//                    if (prefs.getBoolean("mergeWithLastMeasurement", true)) {
//                        List<ScaleMeasurement> scaleMeasurementList = openScale.getScaleMeasurementList();
//                        if (!scaleMeasurementList.isEmpty()) {
//                            ScaleMeasurement lastMeasurement = scaleMeasurementList.get(0);
//                            scaleBtData.merge(lastMeasurement);
//                        }
//                    }
                    Log.d("Device Measurement Act", scaleBtData.toString());
//                    openScale.addScaleData(scaleBtData, true);
                    break;
                case INIT_PROCESS:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_bluetooth_init), Toast.LENGTH_SHORT).show();
                    Log.d("Device Measurement Act", "Bluetooth initializing");
                    displayWeightTextView.setText(getResources().getString(R.string.info_bluetooth_connect_scale));
                    break;
                case CONNECTION_LOST:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_bluetooth_connection_lost), Toast.LENGTH_SHORT).show();
                    Log.d("Device Measurement Act", "Bluetooth connection lost");
                    displayWeightTextView.setText(getResources().getString(R.string.info_bluetooth_connection_lost));
                    setErrorMessageOnButton();
                    break;
                case NO_DEVICE_FOUND:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_bluetooth_no_device), Toast.LENGTH_SHORT).show();
                    Log.e("Device Measurement Act", "No Bluetooth device found");
                    displayWeightTextView.setText(getResources().getString(R.string.info_bluetooth_no_device));
                    setErrorMessageOnButton();

                    break;
                case CONNECTION_RETRYING:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_bluetooth_no_device_retrying), Toast.LENGTH_SHORT).show();
                    Log.e("Device Measurement Act", "No Bluetooth device found retrying");
                    displayWeightTextView.setText(getResources().getString(R.string.info_bluetooth_no_device));
                    setErrorMessageOnButton();

                    break;
                case CONNECTION_ESTABLISHED:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_bluetooth_connection_successful), Toast.LENGTH_SHORT).show();
                    Log.d("Device Measurement Act", "Bluetooth connection successful established");
                    displayWeightTextView.setText(getResources().getString(R.string.please_weigh_yourself_hint));
                    break;
                case CONNECTION_DISCONNECT:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_bluetooth_connection_disconnected), Toast.LENGTH_SHORT).show();
                    Log.d("Device Measurement Act", "Bluetooth connection successful disconnected");
                    displayWeightTextView.setText(getResources().getString(R.string.info_bluetooth_connection_disconnected));
                    break;
                case UNEXPECTED_ERROR:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_bluetooth_connection_error) + ": " + msg.obj, Toast.LENGTH_SHORT).show();
                    Log.e("Device Measurement Act", "Bluetooth unexpected error: %s" + msg.obj);
                    displayWeightTextView.setText(getResources().getString(R.string.info_bluetooth_connection_error));
                    setErrorMessageOnButton();

                    break;
                case SCALE_MESSAGE:
//                    String toastMessage = String.format(getResources().getString(msg.arg1), msg.obj);
//                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();

                    //Get current weight as float
                    float currWeight = Float.valueOf(msg.obj.toString());

                    //If the current weight is in an valid frame/window regarding the last frame (+- ~0.5) it will be counted as increase of the strepsize.
                    if ((currWeight <= lastWeight + 0.05f) && (currWeight >= lastWeight - 0.05f)) {
                        progressCount++;
                    } else {
                        progressCount = 1;
                        lastWeight = currWeight;
                    }

                    //Refresh the visual progressbar.
                    progressBar.setProgress(progressCount);

                    displayWeightTextView.setText(getResources().getString(R.string.please_stand_still_hint));
            }
        }
    };

    /**
     * storing driver object during measurement process
     */
    private BluetoothBeurerSanitas scaleDeviceDriver;

    /**
     * If an error was raised during the weighing process change the button text.
     */
    private void setErrorMessageOnButton() {
        quitButton.setVisibility(View.VISIBLE);
        quitButton.setText(R.string.scale_problem_retry);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_device_measurements;
    }


    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.descriptor_device_measurements;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_add_measurement;
    }

    /**
     * Making sure textview with hint is accessable and empty
     *
     * @param savedInstanceState ignore
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayWeightTextView = findViewById(R.id.tv_weight_display_text);
        quitButton = findViewById(R.id.btn_save_measurement_button);
        progressBar = findViewById(R.id.progressBar_weighting_process);

        start_retrieving_process();
    }

    /**
     * This method checks whether BT is enabled and connecting is possible.
     * After that a connection is invoked and the weighing process is started.
     */
    public void start_retrieving_process() {
        //Check if hardware adress is correct. If it is not, abort and show error message.
        if (!BluetoothAdapter.checkBluetoothAddress(hwAddress)) {
            displayWeightTextView.setText(R.string.no_such_address);
            return;
        }

        //Check if bluetooth is enabled. If not start intent to do so, and listen to onActivityResult to proceed. If it is already enabled, connect with scale
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        if (!bluetoothManager.getAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST);
            return;
        }

        //Only if bluetooth is already enabled (or via onActivityResult)
        connectScale();

    }

    /**
     * Connects to scale.
     */
    private void connectScale() {
        //If bluetooth is enabled connect to scale
        if (!connectToBluetoothDevice("BF 700", hwAddress, callbackBtHandler)) {
            //setBluetoothStatusIcon(R.drawable.ic_bluetooth_connection_lost);
            displayWeightTextView.setText(String.format("BF700 %s", getResources().getString(R.string.label_bt_device_no_support)));
        }
    }

    /**
     * Creates an Openscale Driver (BT Communication) object which can be used to connect to the device with.
     * The address is essential for connecting. A callback Handler is registered for debugging purposes and
     * adding a measurement to a database (later on).
     *
     * @param deviceName        e.g. "BF700"
     * @param hwAddress         e.g. "C8:B2:1E:C2:A6:F8"
     * @param callbackBtHandler Message Handler
     * @return <code>false</code> if driver object could not be created;
     * <code>true</code> if connection was successful.
     */
    public boolean connectToBluetoothDevice(String deviceName, String hwAddress, Handler callbackBtHandler) {
        Log.d("Device Measurement Act", "Trying to connect to bluetooth device [%s] (%s)" + hwAddress + deviceName);
        displayWeightTextView.setText(getResources().getString(R.string.info_bluetooth_connect_scale));
        disconnectFromBluetoothDevice();
        scaleDeviceDriver = new BluetoothBeurerSanitas(getApplicationContext(), BluetoothBeurerSanitas.DeviceType.BEURER_BF710);
        scaleDeviceDriver.registerCallbackHandler(callbackBtHandler);
        scaleDeviceDriver.connect(hwAddress);
        return true;
    }

    /**
     * The created Device driver is used to disconnect and destroyed.
     *
     * @return <code>false</code> if there is no Device to disconnect from
     * <code>true</code> if disconnecting was successful
     */
    public boolean disconnectFromBluetoothDevice() {
        if (scaleDeviceDriver == null) {
            return false;
        }
        Log.d("Device measurement act", "Disconnecting from bluetooth device");
        scaleDeviceDriver.disconnect();
        scaleDeviceDriver = null;
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If the request bluetooth intent is started this method will receive the intent result, i.e if the permission was granted or not.
        if (requestCode == ENABLE_BLUETOOTH_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                //Connect to scale if request was granted.
                connectScale();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //Otherwise display notification
                displayWeightTextView.setText(R.string.info_bluetooth_please_enable);
            }
        }
    }
}
