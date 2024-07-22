package innolab.pallicare.db.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import innolab.pallicare.db.entities.MeasurementScale;

/**
 * data access object (DAO) that specifies database interactions such as queries for "measurement_scale" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface MeasurementScaleDao {

    @Insert // TODO add onConflict strategy
    void insertMeasurementScale(MeasurementScale measurementScale);

    @Update
    void updateMeasurementScale(MeasurementScale measurementScale);

    @Query("SELECT * FROM measurement_scale WHERE patient_id = :patientId")
    LiveData<List<MeasurementScale>> getAllScaleMeasurementsByPatientId(int patientId);

    // TODO remove? (only for testing)
    @Query("SELECT * FROM measurement_scale")
    LiveData<List<MeasurementScale>> getAllScaleMeasurements();
}
