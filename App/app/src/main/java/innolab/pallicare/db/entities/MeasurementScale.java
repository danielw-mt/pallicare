package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * database class that specifies "measurement_scale" table
 *
 * @author Ulla Sternemann
 */
@Entity(tableName = "measurement_scale",
//        indices = {@Index(value = "measurement_scale_id", unique = true)},
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "patient_id", onDelete = ForeignKey.CASCADE))
//        ,
//        indices = {@Index(value = "measurement_scale_id", unique = true)}

public class MeasurementScale {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "measurement_scale_id")
    private int measurementScaleId;

    private float weight;

    private float fat;

    private float muscle;

    private float water;

    @NonNull
    private Date timestamp;

    @ColumnInfo(name = "patient_id")
    private int patientId;

    public MeasurementScale(float weight, float fat, float muscle, float water, @NonNull Date timestamp, int patientId) {
        this.weight = weight;
        this.fat = fat;
        this.muscle = muscle;
        this.water = water;
        this.timestamp = timestamp; // TODO default current timestamp
        this.patientId = patientId;
    }

    public int getMeasurementScaleId() {
        return this.measurementScaleId;
    }

    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param measurementScaleId ignore
     */
    public void setMeasurementScaleId(int measurementScaleId) {
        this.measurementScaleId = measurementScaleId;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getFat() {
        return this.fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getMuscle() {
        return this.muscle;
    }

    public void setMuscle(float muscle) {
        this.muscle = muscle;
    }

    public float getWater() {
        return this.water;
    }

    public void setWater(float water) {
        this.water = water;
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

    @Override
    @NonNull
    public String toString() {
        return "measurement_scale:id: " + measurementScaleId + " | weight: " + weight + " | fat: " + fat + " | muscle: "
                + muscle + " | water: " + water + " | timestamp: " + timestamp + " | patient_id: " + patientId + "\n";
    }
}
