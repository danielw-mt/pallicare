package innolab.pallicare.ui.patient.settings;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.welie.blessed.BluetoothCentral;
import com.welie.blessed.BluetoothCentralCallback;
import com.welie.blessed.BluetoothPeripheral;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import innolab.pallicare.R;
import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.db.entities.Device;
import innolab.pallicare.openScale.BluetoothFactory;
import innolab.pallicare.ui.BaseActivity;


/**
 * This class is used to discover and connect to devices for the first time so they can be
 * accessed later.
 *
 * @author Daniel Wagner, refactored by Klaus Schmidt
 */
public class ManageDevicesActivity extends BaseActivity implements OnDeviceClickListener, OnDeviceLongClickListener {
    /**
     * Constants for permission requests
     * TODO Ulla is his number fixed or arbitrary? Maybe you can delete this comment and write me in slack.
     */
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1111;
    /**
     * Constant to identify enable bluetooth request in onActivityResult
     */
    private static final int ENABLE_BLUETOOTH_REQUEST = 102;

    /**
     * The RecyclerView containing the discovered devices
     */
    RecyclerView recyclerView;

    /**
     * Runnable to count up the progress bar during scan and terminate bluetooth scanning process at the end.
     */
    ProgressCounterRunnable progressCounterRunnable;

    /**
     * The RecycleView Adapter. It needs to be notified on a DataSetChange.
     */
    DeviceCardRecyclerViewAdapter adapter;
    /**
     * ProgressBar to show progress of BLE device search
     */
    private ProgressBar progressBar;
    /**
     * List to save the discovered bluetooth devices
     */
    private List<Device> deviceList = new ArrayList<>();
    /**
     * Central class to connect and communicate with bluetooth peripherals.
     */
    private BluetoothCentral central;
    /**
     * List to save the hardwareAddresses of found devices. Used to prevent duplicates
     */
    private ArrayList<String> foundDeviceAddresses = new ArrayList<>();
    /**
     * This method just calls the onDeviceFound methods every time a device is found, copied from openScale
     */
    private final BluetoothCentralCallback bluetoothCentralCallback = new BluetoothCentralCallback() {
        @Override
        public void onDiscoveredPeripheral(BluetoothPeripheral peripheral, ScanResult scanResult) {
            onDeviceFound(scanResult);
        }
    };

    /**
     * Checks whether the ACCESS_FINE_LOCATION permission is granted. If not start permission request.
     * access fine location must be enabled to connect to bluetooth devices.
     * <p>
     * No need to request: Manifest.permission.BLUETOOTH, as it is a normal permission which does not need runtime permission.
     * See https://stackoverflow.com/a/36784732
     *
     * @return yes, if all permissions have already been granted. No if any of the permissions was not granted.
     * @author Klaus Schmidt
     */
    private boolean isFineLocationPermissionGranted() {
        return (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Handle result of permission requests: FINE LOCATION, BLUETOOTH
     * Sets the according flag to true / false
     *
     * @author Daniel Wagner, refactored by Klaus Schmidt
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                Log.d(LOG_TAG, "permission ACCESS FINE LOCATION was granted");
            } else {
                // permission was denied
                Log.d(LOG_TAG, "permission ACCESS FINE LOCATION was NOT granted");
                //show dialog and close window.
                alertAndFinish(true);
            }
        } else {
            Log.d(LOG_TAG, "param requestCode (in void onRequestPermissionsResult) was not as expected: " + requestCode);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_manage_device;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.descriptor_manage_devices;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_add_device;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * When the activity is started it checks whether the app has location permission.
         * Location permission is only used to find the device for the first time
         * (depending on the phone).
         */
        progressBar = findViewById(R.id.progressbar_scan_devices);

        setUpRecyclerView();

        //if FINE LOCATION Permission is not granted request permission after dialog
        if (!isFineLocationPermissionGranted()) {
            final AlertDialog.Builder permissionRequestDialog = new AlertDialog.Builder(this);
            permissionRequestDialog.setTitle(getResources().getString(R.string.permission_request));
            permissionRequestDialog.setMessage(getResources().getString(R.string.fine_request));
            permissionRequestDialog.setPositiveButton(android.R.string.ok, null);
            permissionRequestDialog.setOnDismissListener(dialog -> requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION));
            permissionRequestDialog.show();
        }

        //Load devices saved to db and prepare for search
        resetRecyclerView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Program flow arrives here if the bluetooth enable intent (from prepareBluetoothScan) is finished.
        //If the request bluetooth intent is started this method will receive the intent result, i.e if the permission was granted or not.
        //If the permission was granted the bluetooth discovery will be started, otherwise a message will be shown.

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_BLUETOOTH_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                //Connect to scale if request was granted.
                startBluetoothDiscovery();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //Otherwise display notification
                alertAndFinish(false);
            }
        }
    }

    /**
     * Show an alert, that the activity is not fully functional because the permission / setting was not allowed.
     *
     * @param finish if true finish -> leave the activity
     * @author Klaus Schmidt
     */
    public void alertAndFinish(boolean finish) {
        final AlertDialog.Builder activityInfunctionalDialog = new AlertDialog.Builder(this);
        activityInfunctionalDialog.setTitle(getResources().getString(R.string.limited_functionality_title));
        activityInfunctionalDialog.setMessage(getResources().getString(R.string.limited_functionality_message));
        activityInfunctionalDialog.setPositiveButton(android.R.string.ok, null);
        if (finish) {
            activityInfunctionalDialog.setOnDismissListener(dialog -> finish());
        }
        activityInfunctionalDialog.show();
    }

    /**
     * This method first checks whether BT is turned on. If it isn't turned it starts a dialog to enable bluetooth.
     * If this was denied do nothing. If it was accepted proceed in onActivityResults
     * It bluetooth already was turned on, start searching.
     *
     * @param view is used because this is an onClick method called when pressing the "add devices"
     *             button
     * @author Klaus Schmidt
     */
    public void prepareBluetoothScan(View view) {
        //Prevent that the search is run by two threads simultaneously.
        if (progressCounterRunnable == null || progressCounterRunnable.hasStopped()) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            if (!bluetoothManager.getAdapter().isEnabled()) {

                final AlertDialog.Builder bluetoothEnableDialog = new AlertDialog.Builder(this);
                bluetoothEnableDialog.setTitle(getResources().getString(R.string.permission_request));
                bluetoothEnableDialog.setMessage(getResources().getString(R.string.bluetooth_request));
                bluetoothEnableDialog.setPositiveButton(android.R.string.ok, null);
                bluetoothEnableDialog.setOnDismissListener(dialog -> {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST);
                });
                bluetoothEnableDialog.show();
            } else {
                startBluetoothDiscovery();
            }
        }

    }

    /**
     * Stops the BLE discovery. Code from openCale BluetoothSettingsActivity.java
     *
     * @author Klaus Schmidt
     */
    private void stopBluetoothDiscovery() {
        Log.d("ManageDevicesActivity: ", "stopped BT Discovery");
        if (central != null) {
            central.stopScan();
        }
    }

    /**
     * The phone is registered as a BLE central. The BLESSED library is used to scan for BLE
     * peripherals with the central object. A new thread is started to render the progress bar.
     * It runs for 100*30 ms. When this time is over the progressbar is set invisible and thread is closed.
     *
     * @author Klaus Schmidt
     */
    private void startBluetoothDiscovery() {
        //If a search was already started stop the last Runnable at first.
        if (progressCounterRunnable != null && !progressCounterRunnable.hasStopped()) {
            progressCounterRunnable.stopCounting();
        }

        resetRecyclerView();

        //Set manually to 0 if the scan is triggered multiple times.
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);

        //The next three lines are from openScale - BluetoothSettingsActivity.java
        //Start the search
        Log.d("ManageDevicesActivity", "Starting BT LE Scan");
        central = new BluetoothCentral(getApplicationContext(), bluetoothCentralCallback, new Handler(Looper.getMainLooper()));
        central.scanForPeripherals();

        progressCounterRunnable = new ProgressCounterRunnable();
        new Thread(progressCounterRunnable).start();
    }

    /**
     * If a device is found that is already in the foundDeviceAddresses nothing happens.
     * Every device can only be in there once.
     *
     * @author Klaus Schmidt
     */
    private void onDeviceFound(final ScanResult bleScanResult) {
        BluetoothDevice bluetoothDevice = bleScanResult.getDevice();

        //if bluetoothDevice is already in hashmap don't show it
        if (bluetoothDevice.getName() == null) {
            Log.d(LOG_TAG, String.format("BLE %s device has no name. Cannot process.", bluetoothDevice.getAddress()));
            return;
        } else if (foundDeviceAddresses.contains(bluetoothDevice.getAddress())) {
            Log.d(LOG_TAG, String.format("Device %s is already added", bluetoothDevice.getName()));
            return;
        }

        /*TODO improve that in adapter BlueFac does not need to be called anymore? e.g. Refactor DeviceEntity to have driver and device name? What is device/driver name needed for? driverName can be derived by deviceName.
            bluetoothDevice.getName() => BF700
            BluetoothFactory.createDeviceDriver(getApplicationContext(), bluetoothDevice.getName()).driverName() =>Beurer BF710;
        */
        //Only add, if bluetoothDevice is supported
        if (BluetoothFactory.createDeviceDriver(getApplicationContext(), bluetoothDevice.getName()) != null) {
            foundDeviceAddresses.add(bluetoothDevice.getAddress());

            //TODO add more device type later
            int deviceTypeID = PallicareRepository.getInstance(getApplication()).getDeviceTypeByName("scale").getDeviceTypeId();
            Device device = new Device(bluetoothDevice.getName(), bluetoothDevice.getAddress(), new Date(System.currentTimeMillis()), deviceTypeID);
            deviceList.add(device);
            adapter.notifyDataSetChanged();
            findViewById(R.id.tv_manage_devices_empty).setVisibility(View.GONE);


        }

    }


    //Implemented from the OnDeviceClickListener.
    @Override
    public void onDeviceClick(int position) {
        //If the clicked item HWID is not saved in the DB yet, add the device.
        List<String> currentDeviceHWID = PallicareRepository.getInstance(getApplication()).getAllDeviceHWID();
        if (!currentDeviceHWID.contains(deviceList.get(position).getHwAddress())) {
            DeviceCardViewHolder deviceCardViewHolder = (DeviceCardViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            if (deviceCardViewHolder != null) {
                //TODO change if more device types supported
                Device device = deviceList.get(position);
                if (device != null) {
                    PallicareRepository.getInstance(getApplication()).insertDevice(device);
                }
                deviceCardViewHolder.savedToDB();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Stop the bluetooth search on pressing back.
        if (progressCounterRunnable != null && !progressCounterRunnable.hasStopped()) {
            progressCounterRunnable.stopCounting();
        }
        finish();
    }

    /**
     * Sets up the recyclerView to show the cards
     *
     * @author Klaus Schmidt
     */
    private void setUpRecyclerView() {
        // Set up the RecyclerView, which can show many entries from a list
        recyclerView = findViewById(R.id.bluetoothdevice_recycler_view);
        recyclerView.setHasFixedSize(true);
        //Set the Layout window as a grid, spanCount is the number of rows or columns (depending on orientation)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Add decorator (spacing between items)
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(getDrawable(R.drawable.linearlayoutdivider)));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    /**
     * Resets the recyclerView to prevent strange bugs with recyclerView adapter. It appeared that even the list was cleared and the adapter was notified old settings like disabled clickability have been stored.
     * Now the recyclerView is completely reset by instantiating a new one..
     *
     * @author Klaus Schmidt
     */
    private void resetRecyclerView() {
        /*This adapter is used to connect the information of the device list and the recycler view.
         It creates a new DeviceCardRecyclerViewAdapter and sets itself as onDeviceListener, as it implements this interface*/
        deviceList.clear();

        //Clear list to be able to readd the bluetooth devices to the deviceList.
        foundDeviceAddresses.clear();

        //Load devices from database
        List<Device> databaseDevices = PallicareRepository.getInstance(getApplication()).getAllDevices();
        List<String> databaseDeviceHWIDs = PallicareRepository.getInstance(getApplication()).getAllDeviceHWID();
        if (databaseDevices != null && databaseDeviceHWIDs != null) {
            deviceList.addAll(databaseDevices);
            foundDeviceAddresses.addAll(databaseDeviceHWIDs);
        }


        adapter = new DeviceCardRecyclerViewAdapter(
                deviceList, this, this, PallicareRepository.getInstance(getApplication()));
        recyclerView.setAdapter(adapter);

        onEmptyDeviceList();

    }

    @Override
    public void onDeviceLongClick(int position) {
        //Delete from DB
        PallicareRepository.getInstance(getApplication()).deleteDeviceByHWID(deviceList.get(position).getHwAddress());

        //Delete from current data.
        foundDeviceAddresses.remove(position);
        deviceList.remove(position);

        //Adapt view
        adapter.notifyDataSetChanged();
        onEmptyDeviceList();
    }

    /**
     * Call this if there are currently no devices to adapt view.
     */
    private void onEmptyDeviceList() {
        if (deviceList.isEmpty()) {
            findViewById(R.id.tv_manage_devices_empty).setVisibility(View.VISIBLE);
        }
    }

    //TODO Gerät in DB. Diese Activity öffnen. Suche starten. Gerät löschen. Was passiert? onBind oder programmierfehler?

    /**
     * This class represents the runnable which counts during the search process.
     * It terminates the search process if it counted till 100.
     * It counts 100*50 ms.
     *
     * @author Klaus Schmidt
     */
    class ProgressCounterRunnable implements Runnable {

        /**
         * Flag to indicate whether the Runnable should stop
         */
        boolean stopNow = false;

        /**
         * @return whether the thread shall be stopped / is already stopped now.
         */
        boolean hasStopped() {
            return stopNow;
        }

        /**
         * Can be used to stop the Runnable
         */
        synchronized void stopCounting() {
            stopNow = true;
        }

        @Override
        public void run() {
            //Counts from 1-100 and sets the progress bar to this number
            for (int i = 1; i <= 400; ++i) {
                progressBar.setProgress(i);
                try {
                    //100 * 50 ms
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //If the Runnable is stopped break the loop immediately.
                if (stopNow) {
                    break;
                }
            }

            ManageDevicesActivity.this.stopBluetoothDiscovery();
            //To prevent that UI Code is executed at NON-UI-thread use View.post. Hide the progress bar.
            progressBar.post(() -> progressBar.setVisibility(View.GONE));
            //Stop counting in manual mode.
            stopCounting();
        }
    }
}
