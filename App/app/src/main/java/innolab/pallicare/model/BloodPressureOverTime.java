package innolab.pallicare.model;

/**
 * class which describes the bloodpressure over a period of time (e.g. months and average-weight)
 *
 * @author Patrick Höfner
 */
public class BloodPressureOverTime {
    /**
     * period like day, weekday, month
     */
    String period;

    /**
     * sylostic describes the sylostic value of bloodpressure(single value, average value)
     */
    int systolic;

    /**
     * dialostic describes the dialostic value of bloodpressure(single value, average value)
     */
    int diastolic;


    //TODO Wird durch Klasse von Ulla ersetzt.

    public BloodPressureOverTime(String period, int systolic, int diastolic) {
        this.period = period;
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }
}
