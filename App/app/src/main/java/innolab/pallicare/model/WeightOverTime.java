package innolab.pallicare.model;

/**
 * class which describes the body-weight over a period of time (months and average-weight)
 *
 * @author Patrick Höfner
 */
public class WeightOverTime {

    /**
     * period like day, weekday, month
     */
    String period;

    /**
     * weight (single value, average value)
     */
    int weight;

    public WeightOverTime(String period, int weight) {
        this.period = period;
        this.weight = weight;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
