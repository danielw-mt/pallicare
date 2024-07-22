package innolab.pallicare.db.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import innolab.pallicare.db.entities.PermissionType;

/**
 * data access object (DAO) that specifies database interactions such as queries for "permission_type" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface PermissionTypeDao {

    // TODO add default rows of table, because this table will not be modified during runtime, it just defines some things
    // instead of inserting it all at runtime

    // TODO remove? (only for testing)
    @Query("SELECT * FROM permission_type")
    LiveData<List<PermissionType>> getAllPermissionTypes();

    @Insert
    void insertPermissionTypes(PermissionType... permissionTypes);
}
