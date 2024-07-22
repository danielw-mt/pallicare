package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * database class that specifies "device_type" table
 *
 * @author Ulla Sternemann
 */
@Entity(tableName = "device_type"
//        , indices = {@Index(value = "device_type_id", unique = true)}
        )
public class DeviceType {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "device_type_id")
    private int deviceTypeId;


    @NonNull
    @ColumnInfo(name = "device_type_description")
    private String deviceTypeDescription;


    public DeviceType(@NonNull String deviceTypeDescription) {
        this.deviceTypeDescription = deviceTypeDescription;
    }


    public int getDeviceTypeId() {
        return this.deviceTypeId;
    }


    @NonNull
    public String getDeviceTypeDescription() {
        return this.deviceTypeDescription;
    }


    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param deviceTypeId ignore
     */
    public void setDeviceTypeId(int deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }


    public void setDeviceTypeDescription(@NonNull String deviceTypeDescription) {
        this.deviceTypeDescription = deviceTypeDescription;
    }
}
