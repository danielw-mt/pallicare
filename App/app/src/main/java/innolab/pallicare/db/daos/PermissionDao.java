package innolab.pallicare.db.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import innolab.pallicare.db.entities.Permission;

/**
 * data access object (DAO) that specifies database interactions such as queries for "permission" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface PermissionDao {

    @Insert // TODO add onConflict strategy
    void insertPermission(Permission permission);

    @Update
    void updatePermission(Permission permission);

    @Query("SELECT * FROM permission WHERE patient_id = :patientId")
    LiveData<List<Permission>> getAllPermissionsByPatientId(int patientId);

    @Query("SELECT * FROM permission WHERE requester_id = :requesterId")
    LiveData<List<Permission>> getAllPermissionsByRequestorId(int requesterId);

    // TODO remove? (only for testing)
    @Query("SELECT * FROM permission")
    LiveData<List<Permission>> getAllPermissions();
}
