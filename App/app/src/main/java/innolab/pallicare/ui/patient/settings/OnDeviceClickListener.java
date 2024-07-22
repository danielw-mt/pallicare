package innolab.pallicare.ui.patient.settings;

/**
 * Interface which shall be called on a click on a device card. Therefore it is implemented by ManageDevicesActivity
 * https://www.youtube.com/watch?v=69C1ljfDvl0
 *
 * @author Klaus Schmidt
 */
public interface OnDeviceClickListener {
    void onDeviceClick(int position);
}