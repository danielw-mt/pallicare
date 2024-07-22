package innolab.pallicare.ui.patient.settings;

/**
 * Interface which shall be called on a longClick on a device card. Therefore it is implemented by ManageDevicesActivity
 * https://www.youtube.com/watch?v=69C1ljfDvl0
 *
 * @author Klaus Schmidt
 */
public interface OnDeviceLongClickListener {
    void onDeviceLongClick(int position);
}