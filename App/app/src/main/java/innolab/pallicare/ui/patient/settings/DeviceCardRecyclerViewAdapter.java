package innolab.pallicare.ui.patient.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import innolab.pallicare.R;
import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.db.entities.Device;

/**
 * This adapter creates and holds cards for each element of the given device list.
 *
 * @author Klaus Schmidt
 */
public class DeviceCardRecyclerViewAdapter extends RecyclerView.Adapter<DeviceCardViewHolder> {

    /**
     * Repository to check if DB contains the to be added element already.
     */
    private final PallicareRepository pallicareRepository;

    /**
     * The generic OnDeviceLongClickListener that is called if a device card is longclicked.
     */
    private final OnDeviceLongClickListener onDeviceLongClickListener;

    /**
     * The generic OnDeviceClickListener that is called if a device card is clicked.
     */
    private OnDeviceClickListener onDeviceClickListener;

    /**
     * This list contains all device entries which shall be shown as a card
     */
    private List<Device> deviceList;

    //A OnDeviceClickListener is provided. This is the ManageDevicesActivity class, since it implements this interface.
    DeviceCardRecyclerViewAdapter(List<Device> deviceList, OnDeviceClickListener onDeviceClickListener, OnDeviceLongClickListener onDeviceLongClickListener, PallicareRepository pallicareRepository) {
        this.deviceList = deviceList;
        this.onDeviceClickListener = onDeviceClickListener;
        this.onDeviceLongClickListener = onDeviceLongClickListener;
        this.pallicareRepository = pallicareRepository;
    }

    //Loads the view holder one time. The holder gets a layoutView (the element i.e. a card layout) and connects to all relevant components.
    @NonNull
    @Override
    public DeviceCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_card_bluetooth, parent, false);
        //The DeviceCardViewHolder gets the layout it should use and the onDeviceListener which is passed on. This listener is the ManageDevicesActivity class by default.
        return new DeviceCardViewHolder(layoutView, onDeviceClickListener, onDeviceLongClickListener);
    }


    //This method is called for each element of the deviceList and stores the given information of the array into a card.
    @Override
    public void onBindViewHolder(@NonNull DeviceCardViewHolder holder, int position) {
        if (deviceList != null && position < deviceList.size()) {
            Device device = deviceList.get(position);
            //TODO differ layout of un/supported devices
//            BluetoothCommunication btDevice = BluetoothFactory.createDeviceDriver((Context) onDeviceClickListener, device.getName());
            if (device != null) {
                //If device is already saved to db adapt visualization and behaviour.
                if (pallicareRepository.getAllDeviceHWID().contains(device.getHwAddress())) {
                    holder.savedToDB();
                }
                holder.deviceName.setText(device.getName());
                holder.deviceAddress.setText(device.getHwAddress());

            }
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }


}
