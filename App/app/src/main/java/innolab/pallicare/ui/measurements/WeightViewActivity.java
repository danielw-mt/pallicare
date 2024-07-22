package innolab.pallicare.ui.measurements;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import innolab.pallicare.R;
import innolab.pallicare.model.WeightOverTime;
import innolab.pallicare.ui.BaseActivity;

/**
 * This class will display an overview of measured weight over time. This is realised mainly by displaying a bar-chart
 * extends {@link BaseActivity} with hidden bottom buttons
 *
 * @author Patrick Höfner
 */

public class WeightViewActivity extends BaseActivity {

    /**
     * Workaround to get data from manual input-activity into the chart
     * //TODO Remove WORKAROUND
     */
    static ArrayList<WeightOverTime> weightOverTimeArrayListWorkaround = new ArrayList<>();
    /**
     * Save the values for weight, where each of them is displayed as a single bar
     */
    ArrayList<BarEntry> barEntryArrayList;
    /**
     * Save the labels for describing each bar
     */
    ArrayList<String> labelsNames;
    /**
     * Declaration of a new barChart object
     */
    BarChart barChart;
    /**
     * Save objects "WeightOverTime" which include a String variable for period like "Month" and an int value for weight
     */
    ArrayList<WeightOverTime> weightOverTimeArrayList = new ArrayList<>();

    /**
     * TabLayout to show data over a certain period of time (week, month, year)
     */
    TabLayout tabLayout;

    /**
     * TextView to display the description "average weight: 80kg" within a cardview
     */
    TextView averageWeight;

    /**
     * TextView to display a text which says if the average-weight is above or below the maximum-value
     */
    TextView minMaxValueText;

    /**
     * Maximum weight the patient should have
     */
    int maxCapacity;

    /**
     * Dummy method to add a new weight for thursday
     * TODO Refactor for general use
     *
     * @param weight the measured weigh
     */
    public static void addNewWeight(int weight) {
        weightOverTimeArrayListWorkaround.add(new WeightOverTime("Do", weight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instantiate objects
        barChart = findViewById(R.id.weightChart);
        barEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();
        averageWeight = findViewById(R.id.textView_average_weight_value);
        minMaxValueText = findViewById(R.id.textView_average_weight_feedback);
        tabLayout = findViewById(R.id.tabs_sufferings);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        displayWeeklyBarChart();
                        break;
                    case 1:
                        displayMonthlyBarChart();
                        break;
                    case 2:
                        displayYearlyBarChart();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Creates initial chart to display data over one week
        displayWeeklyBarChart();
    }

    /**
     * Overriding BaseActivity functions
     */
    @Override
    protected int getLayoutID() {
        return R.layout.activity_weight;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.weight_activity_title;
    }

    /**
     * customize manifold settings for displaying the bar-chart
     * api-documentation: https://weeklycoding.com/mpandroidchart-documentation/
     */
    public void setChartSettings() {

        barChart.setExtraOffsets(0, 10, 0, 10);
        // set names of labels to the xAxis (periods names)
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));

        // set position of labels (periods names)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // set position of labels (periods names)
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(20f);
        xAxis.setLabelCount(labelsNames.size()); //xAxis.setLabelRotationAngle(270);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setTextSize(20f);

        // set animation speed
        barChart.animateY(1500);

        // set and customize Limitline
        maxCapacity = 82;
        LimitLine ll = new LimitLine(maxCapacity, "");
        barChart.getAxisLeft().addLimitLine(ll);
        ll.setLineWidth(4f);
        ll.setTextSize(12f);
        ll.enableDashedLine(15f, 20f, 0f);
        ll.setTextColor(Color.RED);

        // set Legend Position and other Parameters
        Legend legend = barChart.getLegend();
        //TODO: Patrick replace deprecated method. Position Curser at crossed out methods and press STRG+Q to see implementation hints.
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        legend.setTextSize(16f);
        barChart.getDescription().setEnabled(false);
    }

    /**
     * clear arrays to remove old data //TODO remove Workaround, save/load the data instead of delete it
     */
    public void clearArrays() {
        weightOverTimeArrayList.clear();
        barEntryArrayList.clear();
        labelsNames.clear();
    }

    /**
     * notify the barChart-object that the displayed data has been changed to refresh the graph
     */
    public void notifyDataChanged() {
        barChart.getData().notifyDataChanged();
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    /**
     * display weight over one week (Mo, Di, Mi, Do, Fr, Sa, So)
     */
    public void displayWeeklyBarChart() {
        clearArrays();
        fillWeeklyWeightList();
        fillBarEntryArrayList(weightOverTimeArrayList);
        createBarDataSet(barEntryArrayList, "Ø Tägliches Gewicht ");
        setChartSettings();
        setAverageWeightCard();
        notifyDataChanged();
    }

    /**
     * display weight over one month(1., 2., 3., ... , 31.)
     */
    public void displayMonthlyBarChart() {
        clearArrays();
        fillMonthlyWeightList();
        fillBarEntryArrayList(weightOverTimeArrayList);
        createBarDataSet(barEntryArrayList, "Ø Wöchentliches Gewicht");
        setChartSettings();
        setAverageWeightCard();
        notifyDataChanged();
    }

    /**
     * display weight over one year(Jan, Feb, Mär)
     */
    public void displayYearlyBarChart() {
        clearArrays();
        fillYearlyWeightList();
        fillBarEntryArrayList(weightOverTimeArrayList);
        createBarDataSet(barEntryArrayList, "Ø Monatliches Gewicht / Durchschnitt");
        setChartSettings();
        setAverageWeightCard();
        notifyDataChanged();
    }

    /**
     * This method calculates the average weight
     */
    public int calculateAverageWeight() {
        double addedValues = 0;
        for (int i = 0; i < weightOverTimeArrayList.size(); i++) {
            addedValues = addedValues + weightOverTimeArrayList.get(i).getWeight();
        }
        return (int) Math.round(addedValues / weightOverTimeArrayList.size());
    }

    /**
     * This method checks if the calculated average value for weight is below the maximum value of the patient
     */
    public boolean checkAverageIsBelowMaxValue(){
        return calculateAverageWeight() < maxCapacity;
    }

    /**
     * This method sets the values, which are displayed within the card view for average
     */
    public void setAverageWeightCard() {
        String average = calculateAverageWeight() + getString(R.string.kg);
        averageWeight.setText(average);
        if(checkAverageIsBelowMaxValue()){
            minMaxValueText.setText(getString(R.string.belowMaximum));
        }
        else{
            minMaxValueText.setText(getString(R.string.overMaximum));
        }
    }
    /**
     * loop over the ArrayList to add Entries for bars and labels
     *
     * @param weightOverTimeArrayList pass ArrayList which contains WeightOvertimeObjects (each object is basically one bar)
     */
    public void fillBarEntryArrayList(ArrayList<WeightOverTime> weightOverTimeArrayList) {
        for (int i = 0; i < weightOverTimeArrayList.size(); i++) {
            String period = weightOverTimeArrayList.get(i).getPeriod();
            int weight = weightOverTimeArrayList.get(i).getWeight();
            barEntryArrayList.add(new BarEntry(i, weight));
            labelsNames.add(period);
        }
    }

    /**
     * Create dataset for displaying bars and set labels and coloring
     *
     * @param barEntries   pass values for weight
     * @param datasetLabel pass String for a label which describes the whole dataset (e.g. monthly weight-average)
     */
    public void createBarDataSet(ArrayList<BarEntry> barEntries, String datasetLabel) {
        BarDataSet barDataSet = new BarDataSet(barEntries, datasetLabel);
        barDataSet.setColors(ColorTemplate.getHoloBlue());
        barDataSet.setDrawValues(false);
        //barDataSet.setValueTextSize(12f);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
    }

    /**
     * add dummy data
     * TODO load data from database
     */
    private void fillYearlyWeightList() {
        weightOverTimeArrayList.add(new WeightOverTime("Jan-Mär", 75));
        weightOverTimeArrayList.add(new WeightOverTime("Apr-Jun", 77));
        weightOverTimeArrayList.add(new WeightOverTime("Jul-Sep", 80));
        weightOverTimeArrayList.add(new WeightOverTime("Okt-Dez", 82));
    }

    /**
     * add dummy data
     * TODO load data from database
     */
    private void fillMonthlyWeightList() {
        weightOverTimeArrayList.add(new WeightOverTime("1.-7.", 75));
        weightOverTimeArrayList.add(new WeightOverTime("8.-15.", 77));
        weightOverTimeArrayList.add(new WeightOverTime("16.-23.", 80));
        weightOverTimeArrayList.add(new WeightOverTime("24.-31.", 85));
    }

    /**
     * add dummy data
     * TODO load data from database
     */
    private void fillWeeklyWeightList() {
        weightOverTimeArrayList.add(new WeightOverTime("Do", 85));
        weightOverTimeArrayList.add(new WeightOverTime("Fr", 83));
        weightOverTimeArrayList.add(new WeightOverTime("Sa", 82));
        weightOverTimeArrayList.add(new WeightOverTime("So", 90));
        weightOverTimeArrayList.add(new WeightOverTime("Mo", 75));
        weightOverTimeArrayList.add(new WeightOverTime("Di", 77));
        weightOverTimeArrayList.add(new WeightOverTime("Mi", 80));
        weightOverTimeArrayList.addAll(weightOverTimeArrayListWorkaround); //TODO Remove WORKAROUND
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_weight_view;
    }
}

