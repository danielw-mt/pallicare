package innolab.pallicare.db.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import innolab.pallicare.db.entities.Device;

/**
 * data access object (DAO) that specifies database interactions such as queries for "device" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface DeviceDao {

    @Insert
        // TODO add onConflict strategy
    void insertDevice(Device device);

    @Update
    void updateDevice(Device device);

    @Query("SELECT * FROM device")
    List<Device> getAllDevices();

    @Query("SELECT hw_address FROM device")
    List<String> getAllDeviceHWID();

    @Query("DELETE FROM device WHERE hw_address = :hw_address")
    void deleteDeviceByHWID(String hw_address);
}
