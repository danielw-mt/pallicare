package innolab.pallicare.ui.measurements;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import innolab.pallicare.R;
import innolab.pallicare.model.BloodPressureOverTime;
import innolab.pallicare.ui.BaseActivity;

/**
 * This class will display an overview of measured bloodpressure over time. This is realised mainly by displaying a double-line-chart
 * extends {@link BaseActivity} with hidden bottom buttons
 *
 * @author Patrick Höfner
 */
//TODO Improve line-chart and add functionality for radio-buttons
public class BloodPressureViewActivity extends BaseActivity {

    /**
     * Workaround to get data from manual input-activity into the chart
     * //TODO Remove WORKAROUND
     */
    static ArrayList<BloodPressureOverTime> bloodpressureOverTimeArrayListWorkaround = new ArrayList<>();
    /**
     * Save the sylostic values for bloodpressure, where each of them is displayed as a single point
     */
    ArrayList<Entry> systolicEntryArrayList;
    /**
     * Save the dialostic values for bloodpressure, where each of them is displayed as a single point
     */
    ArrayList<Entry> diastolicEntryArrayList;
    /**
     * Save the labels for describing each data-point in the chart
     */
    ArrayList<String> labelsNames;
    /**
     * Declaration of a new lineChart object
     */
    LineChart lineChart;
    /**
     * Save objects "BloodPressureOverTime" which include a String variable for period like "month" and an int value for weight
     */
    ArrayList<BloodPressureOverTime> bloodpressureOverTimeArrayList = new ArrayList<>();

    /**
     * TabLayout to show data over a certain period of time (week, month, year)
     */
    TabLayout tabLayout;

    /**
     * Maximum blood pressure the patient should have
     */
    int maxSystolic;
    int maxDiastolic;

    /**
     * TextView to display the description "average blood pressure: 128/90" within a cardview
     */
    TextView averageBloodPressure;

    /**
     * TextView to display a text which says if the average-blood pressure is above or below the maximum-value
     */
    TextView minMaxValueText;


    /**
     * Dummy method to add a new bloodpressure for thursday
     * TODO Refactor for general use
     *
     * @param systolic  systolic values
     * @param diastolic diastolic valuess
     */
    public static void addNewBloodPressure(int systolic, int diastolic) {
        bloodpressureOverTimeArrayListWorkaround.add(new BloodPressureOverTime("Do", systolic, diastolic));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // instantiate objects
        tabLayout = findViewById(R.id.tabs_bloodpressure);
        lineChart = findViewById(R.id.bloodpressureChart);
        systolicEntryArrayList = new ArrayList<>();
        diastolicEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();
        averageBloodPressure = findViewById(R.id.textView_average_bloodpressure_value);
        minMaxValueText = findViewById(R.id.textView_average_bloodpressure_feedback);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        displayWeeklyLineChart();
                        break;
                    case 1:
                        displayMonthlyLineChart();
                        break;
                    case 2:
                        displayYearlyLineChart();
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
        displayWeeklyLineChart();

    }

    /**
     * Overriding BaseActivity functions
     */
    @Override
    protected int getLayoutID() {
        return R.layout.activity_blood_pressure;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.bloodPressure_activity_title;
    }

    /**
     * customize manifold settings for displaying the bar-chart
     * api-documentation: https://weeklycoding.com/mpandroidchart-documentation/
     */
    public void setChartSettings() {

        lineChart.setExtraOffsets(0, 10, 40, 10);
        // set names of labels to the xAxis (periods names)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));

        // set position fo labels (periods names)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // set more parameters fo displaying x-Axis
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(20f);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisRight().setDrawGridLines(true);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setTextSize(20f);

        // set Limitline values
        maxSystolic = 130;
        maxDiastolic = 80;

        // set sylostic Limitline
        LimitLine llSystolic = new LimitLine(maxSystolic, "");
        lineChart.getAxisLeft().addLimitLine(llSystolic);
        llSystolic.setLineWidth(4f);
        llSystolic.enableDashedLine(15f, 20f, 0f);
        llSystolic.setTextColor(Color.RED);

        // set dialostic Limitline
        LimitLine llDiastolic = new LimitLine(maxDiastolic, "");
        lineChart.getAxisLeft().addLimitLine(llDiastolic);
        llDiastolic.setLineWidth(4f);
        llDiastolic.enableDashedLine(15f, 20f, 0f);
        llDiastolic.setTextColor(Color.RED);

        // turned off chart description and set legend-settings
        lineChart.getDescription().setEnabled(false);
        Legend legend = lineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        legend.setTextSize(16f);
    }

    /**
     * Create dataset for displaying bars and set labels and coloring
     *
     * @param systolicEntries  pass sylostic values for bloodpressure
     * @param diastolicEntries pass dialostic values for bloodpressure
     * @param labelSystolic    pass String for a label which describes the sylostic dataSet (e.g. sylostic)
     * @param labelDialostic   pass String for a label which describes the dialostic dataSet (e.g. dialostic)
     */
    public void createLineDataSets(ArrayList<Entry> systolicEntries, ArrayList<Entry> diastolicEntries, String labelSystolic, String labelDialostic) {
        LineDataSet systolicDataSet = new LineDataSet(systolicEntries, labelSystolic);
        systolicDataSet.setColors(ColorTemplate.getHoloBlue());
        LineDataSet diastolicDataSet = new LineDataSet(diastolicEntries, labelDialostic);
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(systolicDataSet);
        lineDataSets.add(diastolicDataSet);

        systolicDataSet.setDrawValues(false);
        diastolicDataSet.setDrawValues(false);
        //systolicDataSet.setValueTextSize(16f);
        //diastolicDataSet.setValueTextSize(16f);
        systolicDataSet.setLineWidth(5f);
        diastolicDataSet.setLineWidth(5f);
        systolicDataSet.setCircleSize(7f);
        diastolicDataSet.setCircleSize(7f);
        systolicDataSet.setCircleHoleRadius(4f);
        diastolicDataSet.setCircleHoleRadius(4f);
        systolicDataSet.setCircleColor(ColorTemplate.getHoloBlue());


        lineChart.setData(new LineData(lineDataSets));
    }

    /**
     * clear arrays to remove old data //TODO remove Workaround, save/load the data instead of delete it
     */
    public void clearArrays() {
        bloodpressureOverTimeArrayList.clear();
        systolicEntryArrayList.clear();
        diastolicEntryArrayList.clear();
        labelsNames.clear();
    }

    /**
     * notify the lineChart-object that the displayed data has been changed to refresh the graph
     */
    public void notifyDataChanged() {
        lineChart.getData().notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    /**
     * display blood pressure over one week (Mo, Di, Mi, Do, Fr, Sa, So)
     */
    public void displayWeeklyLineChart(){
        clearArrays();
        fillWeeklyBloodPressureList();
        fillSylosticAndDialosticEntryArrayList(bloodpressureOverTimeArrayList);
        createLineDataSets(systolicEntryArrayList, diastolicEntryArrayList, "systolisch", "diastolisch");
        setChartSettings();
        setAverageBloodPressureCard();
        notifyDataChanged();
    }

    /**
     * display blood pressure over one month(1., 2., 3., ... , 31.)
     */
    public void displayMonthlyLineChart(){
        clearArrays();
        fillMonthlyBloodPressureList();
        fillSylosticAndDialosticEntryArrayList(bloodpressureOverTimeArrayList);
        createLineDataSets(systolicEntryArrayList, diastolicEntryArrayList, "systolisch", "diastolisch");
        setChartSettings();
        setAverageBloodPressureCard();
        notifyDataChanged();
    }

    /**
     * display weight over one year(Jan, Feb, Mär)
     */
    public void displayYearlyLineChart(){
        clearArrays();
        fillYearlyBloodPressureList();
        fillSylosticAndDialosticEntryArrayList(bloodpressureOverTimeArrayList);
        createLineDataSets(systolicEntryArrayList, diastolicEntryArrayList, "systolisch", "diastolisch");
        setChartSettings();
        setAverageBloodPressureCard();
        notifyDataChanged();
    }

    /**
     * loop over the ArrayList to add Entries for lines and labels
     *
     * @param bloodPressureOverTimeArrayList pass ArrayList which contains bloodpressureOvertimeObjects (each object is basically one data-point)
     */
    public void fillSylosticAndDialosticEntryArrayList(ArrayList<BloodPressureOverTime> bloodPressureOverTimeArrayList) {
        for (int i = 0; i < bloodPressureOverTimeArrayList.size(); i++) {
            String period = bloodPressureOverTimeArrayList.get(i).getPeriod();
            int systolic = bloodPressureOverTimeArrayList.get(i).getSystolic();
            int diastolic = bloodpressureOverTimeArrayList.get(i).getDiastolic();
            systolicEntryArrayList.add(new Entry(i, systolic));
            diastolicEntryArrayList.add(new Entry(i, diastolic));
            labelsNames.add(period);
        }
    }

    /**
     * This method calculates the average blood pressure for systolic values
     */
    public int calculateAverageBloodPressureSystolic() {
        double addedValuesSystolic = 0;
        double addedValuesDiastolic = 0;
        for (int i = 0; i < bloodpressureOverTimeArrayList.size(); i++) {
            addedValuesSystolic = addedValuesSystolic + bloodpressureOverTimeArrayList.get(i).getSystolic();
            addedValuesDiastolic = addedValuesDiastolic + bloodpressureOverTimeArrayList.get(i).getDiastolic();
        }
        return (int) Math.round(addedValuesSystolic / bloodpressureOverTimeArrayList.size());
    }

    /**
     * This method calculates the average blood pressure for diastolic values
     */
    public int calculateAverageBloodpressureDiastolic(){
        double addedValuesDiastolic = 0;
        for (int i = 0; i < bloodpressureOverTimeArrayList.size(); i++) {
            addedValuesDiastolic = addedValuesDiastolic + bloodpressureOverTimeArrayList.get(i).getDiastolic();
        }
        return (int) Math.round(addedValuesDiastolic / bloodpressureOverTimeArrayList.size());
    }

    /**
     * This method checks if the calculated average value for blood pressure is below the maximum value of the patient
     */
    public boolean checkAverageIsBelowMaxValue(){
        return calculateAverageBloodPressureSystolic() < maxSystolic && calculateAverageBloodpressureDiastolic() < maxDiastolic;
    }

    /**
     * This method sets the values, which are displayed within the card view for average
     */
    public void setAverageBloodPressureCard() {
        String average = calculateAverageBloodPressureSystolic() + "/" + calculateAverageBloodpressureDiastolic();
        averageBloodPressure.setText(average);
        if(checkAverageIsBelowMaxValue()){
            minMaxValueText.setText(R.string.belowMaximum);
        }
        else{
            minMaxValueText.setText(R.string.overMaximum);
        }
    }

    /**
     * add dummy data
     * TODO load data from database
     */
    private void fillWeeklyBloodPressureList() {
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Do", 133, 83));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Fr", 127, 80));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Sa", 125, 76));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("So", 120, 74));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Mo", 120, 80));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Di", 125, 83));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Mi", 115, 75));
        bloodpressureOverTimeArrayList.addAll(bloodpressureOverTimeArrayListWorkaround); //TODO Remove WORKAROUND

    }

    private void fillMonthlyBloodPressureList() {
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("1.-7.", 133, 83));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("8.-15.", 127, 80));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("16.-23.", 125, 76));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("24.-31.", 120, 74));

    }

    private void fillYearlyBloodPressureList() {
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Jan-Mär", 133, 83));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Apr-Jun", 127, 80));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Jul-Sep", 120, 70));
        bloodpressureOverTimeArrayList.add(new BloodPressureOverTime("Okt-Dez", 124, 72));
        bloodpressureOverTimeArrayList.addAll(bloodpressureOverTimeArrayListWorkaround); //TODO Remove WORKAROUND

    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_blood_pressure_view;
    }
}

