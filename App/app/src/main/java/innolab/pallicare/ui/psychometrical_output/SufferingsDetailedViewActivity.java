package innolab.pallicare.ui.psychometrical_output;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import innolab.pallicare.R;
import innolab.pallicare.ui.BaseActivity;

public class SufferingsDetailedViewActivity extends BaseActivity {

    /**
     * TabLayout which contains the selection types (week, month, year) at the top of the screen
     */
    private TabLayout tabLayout;

    /**
     * Icon (Smiley) which visualizes the average value
     */
    private ImageView averageImageView;

    /**
     * TextView which displays the average value as description (e.g. no sufferings)
     */
    private TextView averageText;

    /**
     * Stores all suffering types from string-resources
     */
    private String[] suffering_types;

    /**
     * Save the psychometric data values (strength), where each of them is displayed as a single point
     */
    ArrayList<Entry> psychDataEntryArrayList;

    /**
     * Save the labels for describing each data-point in the chart
     */
    ArrayList<String> labelsNames;

    /**
     * Declaration of a new lineChart object
     */
    LineChart lineChart;

    /**
     * Save objects "PsyochometricDataOverTime" which includes a String variable timestamp and an int value for strength
     */
    ArrayList<PsychometricDataOverTime> psychDataOverTimeArrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //set Toolbar-Title
        setToolbarTitle(getToolbarTitle());

        // instantiate Objects
        lineChart = findViewById(R.id.suffersChart);
        psychDataEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();
        averageImageView = findViewById(R.id.suffers_avg_ImageView);
        tabLayout = findViewById(R.id.tabs_sufferings);
        averageText = findViewById(R.id.textView_average_suffer);

        // initially display weekly line-chart
        displayWeeklyLineChart();

        // onClickListener for TabLayout to select week, month or year as a time-period to display the chart
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
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sufferings_detailed_view;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.suffers_detailed_view_activity_title;
    }

    /**
     * This method catches the Intent which contains the rownumber from SufferingsOverviewActivity and returns the appropiate title
     */
    protected String getToolbarTitle(){
        Intent intent = getIntent(); // gets the previously created intent
        int rowNumber= intent.getIntExtra("Rownumber", 0);
        Resources res = getResources();
        suffering_types = res.getStringArray(R.array.quest_suffering_types);
        return suffering_types[rowNumber];
    }

    @Override
    protected int getHelpText() {
        return R.string.default_help_not_implemented;
    }

    public void setChartSettings() {
        // set names of labels to the xAxis (periods names)
        lineChart.setExtraOffsets(0, 10, 40, 10);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));

        //String[] values = new String[] {"keine", "leichte", "mittlere", "schwere"};
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        //Set parameters for diyplaying y-Axis
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setLabelCount(3);
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(3);
        yAxis.setValueFormatter(new YAxisValueFormatter());
        yAxis.setTextSize(20f);

        // set position fo labels (periods names)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // set more parameters fo displaying x-Axis
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(20f);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisRight().setDrawGridLines(true);

        // turned off chart description and legend
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
    }

    /**
     * display sufferings over one month within chart(1., 2., 3., ... , 31.)
     */
    public void displayMonthlyLineChart() {
        clearArrays();
        fillMonthlySufferingsList();
        fillLineEntryArrayList(psychDataOverTimeArrayList);
        createLineDataSet(psychDataEntryArrayList, "Durchschnitt");
        setChartSettings();
        setAverageSufferingCard(calculateAverageSuffering());
        notifyDataChanged();

    }

    /**
     * display sufferings over one year within chart(Jan, Feb, Mär ...)
     */
    public void displayYearlyLineChart() {
        clearArrays();
        fillYearlySufferingsList();
        fillLineEntryArrayList(psychDataOverTimeArrayList);
        createLineDataSet(psychDataEntryArrayList, "Durchschnitt");
        setChartSettings();
        setAverageSufferingCard(calculateAverageSuffering());
        notifyDataChanged();
    }

    /**
     * display sufferings over one week within chart(Mo, Di, Mi ...)
     */
    public void displayWeeklyLineChart() {
        clearArrays();
        fillWeeklySufferingsList();
        fillLineEntryArrayList(psychDataOverTimeArrayList);
        createLineDataSet(psychDataEntryArrayList, "Durchschnitt");
        setChartSettings();
        setAverageSufferingCard(calculateAverageSuffering());
        notifyDataChanged();
    }

    /**
     * clear arrays to remove old data //TODO remove Workaround, save/load the data instead of delete it
     */
    public void clearArrays() {
        psychDataOverTimeArrayList.clear();
        psychDataEntryArrayList.clear();
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
     * This method calculates the average of the suffering-values over a certain period of time (e.g. average of pain over one week)
     */
    public int calculateAverageSuffering() {
        double addedValues = 0;
        for (int i = 0; i < psychDataOverTimeArrayList.size(); i++) {
            addedValues = addedValues + psychDataOverTimeArrayList.get(i).strength;
        }
        return (int) Math.round(addedValues / psychDataOverTimeArrayList.size());
    }

    /**
     * This method sets the parameters for displaying the average value within the card view
     */
    public void setAverageSufferingCard(int average) {
        switch (average) {
            case 0:
                averageImageView.setImageDrawable(getDrawable(R.drawable.questionnaire_excellent));
                averageText.setText(getString(R.string.no_suffering));
                break;
            case 1:
                averageImageView.setImageDrawable(getDrawable(R.drawable.questionnaire_low));
                averageText.setText(getString(R.string.light_suffering));
                break;
            case 2:
                averageImageView.setImageDrawable(getDrawable(R.drawable.questionnaire_moderate));
                averageText.setText(getString(R.string.medium_suffering));
                break;
            case 3:
                averageImageView.setImageDrawable(getDrawable(R.drawable.questionnaire_severe));
                averageText.setText(getString(R.string.high_suffering));
                break;
            default:
                averageImageView.setImageDrawable(getDrawable(R.drawable.questionnaire_excellent));
        }

    }

    /**
     * This method loops over the ArrayList to add Entries for bars and labels
     *
     * @param psychDataOverTimeArrayList pass ArrayList which contains WeightOvertimeObjects (each object is basically one bar)
     */
    public void fillLineEntryArrayList(ArrayList<PsychometricDataOverTime> psychDataOverTimeArrayList) {
        for (int i = 0; i < psychDataOverTimeArrayList.size(); i++) {
            String timestamp = psychDataOverTimeArrayList.get(i).getTimestamp();
            int weight = psychDataOverTimeArrayList.get(i).getStrength();
            psychDataEntryArrayList.add(new Entry(i, weight));
            labelsNames.add(timestamp);
        }
    }

    /**
     * Tis method creates the dataset for displaying values within the charts and set labels and coloring
     *
     * @param psychometricEntries pass sylostic values for bloodpressure
     * @param labelPsychometric   pass String for a label which describes the sylostic dataSet (e.g. sylostic)
     */
    public void createLineDataSet(ArrayList<Entry> psychometricEntries, String labelPsychometric) {
        LineDataSet psychometricDataSet = new LineDataSet(psychometricEntries, labelPsychometric);
        psychometricDataSet.setColors(ColorTemplate.getHoloBlue());

        psychometricDataSet.setValueTextSize(16f);
        psychometricDataSet.setLineWidth(5f);

        psychometricDataSet.setCircleSize(7f);
        psychometricDataSet.setDrawValues(false);
        psychometricDataSet.setCircleHoleRadius(4f);

        LineData lineData = new LineData(psychometricDataSet);
        lineData.setDrawValues(false);

        lineChart.setData(lineData);
    }

    /**
     * add dummy data
     * TODO load data from database
     */

    private void fillYearlySufferingsList() {
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Jan-Mär", 0));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Apr-Jun", 1));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Jul-Sep", 0));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Okt-Dez", 1));

    }

    private void fillMonthlySufferingsList() {
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("1.-7.", 1));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("8.-15.", 2));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("16.-23.", 3));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("23.-31.", 2));

    }

    private void fillWeeklySufferingsList() {
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Mo", 1));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Di", 1));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Mi", 2));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Do", 2));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Fr", 3));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("Sa", 2));
        psychDataOverTimeArrayList.add(new PsychometricDataOverTime("So", 2));

    }
}

/**
 * This class is an value formatter which transforms the suffering values (0,1,2,3) to string values to display them in the chart
 */
class YAxisValueFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String strength;
        switch ((int) value) {
            case 0:
                strength = "keine";
                break;
            case 1:
                strength = "leichte";

                break;
            case 2:
                strength = "mittlere";

                break;
            case 3:
                strength = "starke";
                break;
            default:
                strength = null;
        }
        return strength;
    }
}

/**
 * This class represents a data object for one suffering including timestamp and strength-value
 // TODO fetch this object from database later on
 */
class PsychometricDataOverTime {
    String timestamp;
    int strength;

    protected PsychometricDataOverTime(String timestamp, int strength) {
        this.timestamp = timestamp;
        this.strength = strength;
    }

    protected String getTimestamp() {
        return timestamp;
    }


    protected int getStrength() {
        return strength;
    }
}