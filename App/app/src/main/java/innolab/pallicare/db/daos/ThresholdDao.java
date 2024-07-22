package innolab.pallicare.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import innolab.pallicare.db.entities.Threshold;

/**
 * data access object (DAO) that specifies database interactions such as queries for "threshold" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface ThresholdDao {

    @Insert
    void insertThresholds(Threshold threshold);

    @Update
    void updateThresholds(Threshold threshold);

    @Query("SELECT * FROM threshold WHERE patient_id = :patientId")
    LiveData<Threshold> getThresholdsByPatientId(int patientId);

    @Query("SELECT max_weight FROM threshold WHERE patient_id = :patientId")
    float getMaxWeightByPatientId(int patientId);

}
