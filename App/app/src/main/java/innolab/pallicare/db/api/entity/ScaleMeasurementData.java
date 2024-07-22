package innolab.pallicare.db.api.entity;

public class ScaleMeasurementData {


    private float weight;

    private float fat;

    private float muscle;

    private float water;

    private String timestamp;

    /**
     * the patient ID
     */
    private int patient;

    public ScaleMeasurementData(float weight, float fat, float muscle, float water, String timestamp, int patient) {
        this.weight = weight;
        this.fat = fat;
        this.muscle = muscle;
        this.water = water;
        this.timestamp = timestamp;
        this.patient = patient;
    }

    public int getPatientId() {
        return patient;
    }

    public void setPatientId(int patientId) {
        this.patient = patientId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public float getMuscle() {
        return muscle;
    }

    public void setMuscle(float muscle) {
        this.muscle = muscle;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


}
