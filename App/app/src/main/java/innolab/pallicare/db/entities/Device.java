package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * database class that specifies "device" table
 *
 * @author Ulla Sternemann
 */
@Entity(tableName = "device"
//        ,
//        indices = {@Index(value = "device_id", unique = true)}
        , foreignKeys = {@ForeignKey(entity = DeviceType.class, parentColumns = "device_type_id", childColumns = "device_type_id", onDelete = ForeignKey.CASCADE)}
)
public class Device {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "device_id")
    private int deviceId;

    @NonNull
    private String name;

    @NonNull
    @ColumnInfo(name = "hw_address")
    private String hwAddress;

    @NonNull
    private Date timestamp;

    @ColumnInfo(name = "device_type_id")
    private int deviceTypeId;

    public Device(@NonNull String name, @NonNull String hwAddress, @NonNull Date timestamp, int deviceTypeId) {
        this.name = name;
        this.hwAddress = hwAddress;
        this.timestamp = timestamp; // TODO default current timestamp
        this.deviceTypeId = deviceTypeId;
    }

    public int getDeviceId() {
        return this.deviceId;
    }

    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param deviceId ignore
     */
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getDeviceTypeId() {
        return this.deviceTypeId;
    }

    public void setDeviceTypeId(int deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    @NonNull
    public String getHwAddress() {
        return hwAddress;
    }

    public void setHwAddress(@NonNull String hwAddress) {
        this.hwAddress = hwAddress;
    }
}

