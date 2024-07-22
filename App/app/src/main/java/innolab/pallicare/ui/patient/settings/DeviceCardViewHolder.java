package innolab.pallicare.ui.patient.settings;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import innolab.pallicare.R;

/**
 * This class gets the layout and connects to the needed visual components
 * - extends the ViewHolder to inherit the necessary methods.
 * - implements the onClickListener, to be able to react on a click
 *
 * @author Klaus Schmidt
 */
public class DeviceCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    /**
     * LOG TAG for Logging
     */
    private final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * Main CardView
     */
    MaterialCardView deviceCard;

    /**
     * the textView for the device name
     */
    TextView deviceName;

    /**
     * the textView for the device address
     */
    TextView deviceAddress;

    /**
     * The displayed icon
     */
    ImageView deviceValidIcon;

    /**
     * gets an OnDeviceClickListener to call on an onClick.
     */
    private OnDeviceClickListener onDeviceClickListener;

    /**
     * gets an OnDeviceClickListener to call on an onClick.
     */
    private OnDeviceLongClickListener onDeviceLongClickListener;

    /**
     * Flag indicating, if device was saved to db.
     */
    private boolean savedToDB = false;

    DeviceCardViewHolder(@NonNull View itemView, OnDeviceClickListener onDeviceClickListener, OnDeviceLongClickListener onDeviceLongClickListener) {
        super(itemView);
        deviceName = itemView.findViewById(R.id.device_name);
        deviceAddress = itemView.findViewById(R.id.device_address);
        deviceValidIcon = itemView.findViewById(R.id.device_valid_icon);
        deviceCard = itemView.findViewById(R.id.device_bluetooth_card);
        this.onDeviceClickListener = onDeviceClickListener;
        this.onDeviceLongClickListener = onDeviceLongClickListener;

        //sets the onClickListener to this cardItem
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        //If device is not saved to db, prevent to remove it (default)
        deviceCard.setLongClickable(false);
    }

    /**
     * Adapt GUI and behaviour, when device got saved to DB
     */
    void savedToDB() {
        savedToDB = true;
        deviceCard.setClickable(false);
        deviceCard.setLongClickable(true);
        deviceCard.setStrokeWidth(7);
    }


    @Override
    public void onClick(View v) {
        if (!savedToDB) {
            //If the card is clicked, call the onDeviceClickListener by telling the position of the clicked Device. The onDeviceClickListener is implemented in the ManageDevicesActivity
            onDeviceClickListener.onDeviceClick(getAdapterPosition());
            Log.d(LOG_TAG, "Clicked at item");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (savedToDB) {
            onDeviceLongClickListener.onDeviceLongClick(getAdapterPosition());
            Log.d(LOG_TAG, "LongClicked at item");
        }
        return false;
    }
}
