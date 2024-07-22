package innolab.pallicare.db.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import innolab.pallicare.db.entities.MeasurementBloodpressure;

/**
 * data access object (DAO) that specifies database interactions such as queries for "measurement_bloodpressure" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface MeasurementBloodpressureDao {

    @Insert // TODO add onConflict strategy
    void insertMeasurementBloodpressure(MeasurementBloodpressure measurementBloodpressure);

    @Update
    void updateMeasurementBloodpressure(MeasurementBloodpressure measurementBloodpressure);

    @Query("SELECT * FROM measurement_bloodpressure WHERE patient_id = :patientId")
    LiveData<List<MeasurementBloodpressure>> getAllBloodpressureMeasurementsByPatientId(int patientId);

    // TODO remove? (only for testing)
    @Query("SELECT * FROM measurement_bloodpressure")
    LiveData<List<MeasurementBloodpressure>> getAllBloodpressureMeasurements();
}
