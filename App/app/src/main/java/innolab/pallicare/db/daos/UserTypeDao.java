package innolab.pallicare.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import innolab.pallicare.db.entities.UserType;

/**
 * data access object (DAO) that specifies database interactions such as queries for "user_type" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface UserTypeDao {

    // TODO add default rows of table, because this table will not be modified during runtime, it just defines some things
    // instead of inserting it all at runtime

    // TODO remove? (only for testing)
    @Query("SELECT * FROM user_type")
    LiveData<List<UserType>> getAllUserTypes();

    @Insert
    void insertUserType(UserType userType);

    @Insert
    void insertAllUserTypes(UserType... userTypes);

    @Query("SELECT * FROM user_type WHERE user_type.user_type_description=:userTypeName LIMIT 1")
    UserType getUserTypeByName(String userTypeName);

}
