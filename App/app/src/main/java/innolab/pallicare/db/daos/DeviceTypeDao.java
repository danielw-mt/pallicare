package innolab.pallicare.db.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import innolab.pallicare.db.entities.DeviceType;

/**
 * data access object (DAO) that specifies database interactions such as queries for "device_type" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface DeviceTypeDao {

    @Insert // TODO add onConflict strategy
    void insertDeviceType(DeviceType deviceType);

    @Update
    void updateDeviceType(DeviceType deviceType);

    // TODO remove? (only for testing)
    @Query("SELECT * FROM device_type")
    LiveData<List<DeviceType>> getAllDeviceTypes();

    @Insert
    void insertDeviceTypes(DeviceType... deviceTypes);

    @Query("SELECT * FROM device_type WHERE device_type.device_type_description=:deviceTypeName LIMIT 1")
    DeviceType getDeviceTypeByName(String deviceTypeName);
}
