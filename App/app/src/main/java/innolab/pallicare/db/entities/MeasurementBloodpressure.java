package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * database class that specifies "measurement_bloodpressure" table
 *
 * @author Ulla Sternemann
 */
@Entity(tableName = "measurement_bloodpressure"
        ,
//        indices = {@Index(value = "measurement_bloodpressure_id", unique = true)},
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "patient_id", onDelete = ForeignKey.CASCADE)
)
public class MeasurementBloodpressure {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "measurement_bloodpressure_id")
    private int measurementBloodpressureId;

    private int systolic;

    private int diastolic;

    @NonNull
    private Date timestamp;

    @ColumnInfo(name = "patient_id")
    private int patientId;

    public MeasurementBloodpressure(int systolic, int diastolic, @NonNull Date timestamp, int patientId) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.timestamp = timestamp; // TODO default current timestamp
        this.patientId = patientId;
    }

    public int getMeasurementBloodpressureId() {
        return this.measurementBloodpressureId;
    }

    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param measurementBloodpressureId ignore
     */
    public void setMeasurementBloodpressureId(int measurementBloodpressureId) {
        this.measurementBloodpressureId = measurementBloodpressureId;
    }

    public int getSystolic() {
        return this.systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return this.diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    @NonNull
    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getPatientId() {
        return this.patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
