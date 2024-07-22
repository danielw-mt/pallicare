package innolab.pallicare.ui.patient.emergency;

/**
 * Interface which shall be called on a click on a contact card. Therefore it is implemented by the emergency class
 * https://www.youtube.com/watch?v=69C1ljfDvl0
 *
 * @author Klaus Schmidt
 */
public interface OnContactClickListener {
    void onContactClick(int position);
}