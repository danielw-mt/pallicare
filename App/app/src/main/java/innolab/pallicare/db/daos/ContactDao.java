package innolab.pallicare.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import innolab.pallicare.db.entities.Contact;

/**
 * data access object (DAO) that specifies database interactions such as queries for "contact" table
 *
 * @author Klaus Schmidt
 */
@Dao
public interface ContactDao {

    @Insert
        // TODO add onConflict strategy
    void insertContact(Contact Contact);

    @Query("DELETE FROM Contact")
    void deleteAllContacts();

    @Delete
    void deleteContact(Contact contact);

    @Query("SELECT * FROM Contact ORDER BY name ASC")
    LiveData<List<Contact>> getAllContactsAlphabetically();

}
