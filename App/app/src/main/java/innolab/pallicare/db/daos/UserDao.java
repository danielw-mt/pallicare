package innolab.pallicare.db.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import innolab.pallicare.db.entities.User;

/**
 * data access object (DAO) that specifies database interactions such as queries for "user" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface UserDao {

    @Insert // TODO add onConflict strategy
    void insertUser(User user);

    @Query("DELETE FROM user")
    void deleteAllUsers();

    @Query("SELECT * FROM user WHERE user_id = :userId")
    LiveData<User> getUserById(int userId);

    // TODO remove? (only for testing)
    @Query("SELECT * FROM user ORDER BY surname ASC")
    LiveData<List<User>> getAllUsersAlphabetically();

    // TODO remove? (only for testing)
    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user WHERE user.user_type_id = :patientTypeID ORDER BY user.user_type_id ASC LIMIT 1")
    User getMainPatient(int patientTypeID);
}
