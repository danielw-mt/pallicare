package innolab.pallicare.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * database class that specifies "threshold" table
 * contains min, max biometric values for each patient, which are added in the doctors web app
 *
 * @author Ulla Sternemann
 */

@Entity(tableName = "threshold",
//        indices = {@Index(value = "threshold_id", unique = true)},
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "patient_id", onDelete = ForeignKey.CASCADE)})
public class Threshold {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "threshold_id")
    private int thresholdId;

    @ColumnInfo(name = "min_systolic")
    private float minSystolic;

    @ColumnInfo(name = "max_systolic")
    private float maxSystolic;

    @ColumnInfo(name = "min_diastolic")
    private float minDiastolic;

    @ColumnInfo(name = "max_diastolic")
    private float maxDiastolic;

    @ColumnInfo(name = "min_weight")
    private float minWeight;

    @ColumnInfo(name = "max_weight")
    private float maxWeight;

//    @ColumnInfo(name = "min_water") // currently without water measurement, if this is added -> also add it in constructor and getter and setter
//    private float minWater;
//
//    @ColumnInfo(name = "max_water")
//    private float maxWater;

    @ColumnInfo(name = "patient_id")
    private int patientId;

    public Threshold(float minSystolic, float maxSystolic, float minDiastolic, float maxDiastolic,
                     float minWeight, float maxWeight, int patientId) {
        this.minSystolic = minSystolic;
        this.maxSystolic = maxSystolic;
        this.minDiastolic = minDiastolic;
        this.maxDiastolic = maxDiastolic;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.patientId = patientId;
    }

    public int getThresholdId() {
        return this.thresholdId;
    }

    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param thresholdId ignore
     */
    public void setThresholdId(int thresholdId) {
        this.thresholdId = thresholdId;
    }

    public float getMinSystolic() {
        return this.minSystolic;
    }

    public void setMinSystolic(float minSystolic) {
        this.minSystolic = minSystolic;
    }

    public float getMaxSystolic() {
        return this.maxSystolic;
    }

    public void setMaxSystolic(float maxSystolic) {
        this.maxSystolic = maxSystolic;
    }

    public float getMinDiastolic() {
        return this.minDiastolic;
    }

    public void setMinDiastolic(float minDiastolic) {
        this.minDiastolic = minDiastolic;
    }

    public float getMaxDiastolic() {
        return this.maxDiastolic;
    }

    public void setMaxDiastolic(float maxDiastolic) {
        this.maxDiastolic = maxDiastolic;
    }

    public float getMinWeight() {
        return this.minWeight;
    }

    public void setMinWeight(float minWeight) {
        this.minWeight = minWeight;
    }

    public float getMaxWeight() {
        return this.maxWeight;
    }

    public void setMaxWeight(float maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getPatientId() {
        return this.patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
