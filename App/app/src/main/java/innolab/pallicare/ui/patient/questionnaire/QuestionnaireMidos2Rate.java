package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import innolab.pallicare.R;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;

/**
 * Get information about the overall constitution of the patient. This is part M2 of the MIDOS questionnaire.
 * <p>
 * -extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos2Rate extends BaseActivity {

    /**
     * Bundle, saving information, passed by intents
     */
    Bundle parameters;
    /**
     * Saves the row which is currently selected. By default -1, so it is unselected
     */
    private int selectedAnswer = -1;
    /**
     * Each row for an answer possibility is saved (with its whole layout)
     */
    private View[] rows;

    /**
     * Return the predefined image for the iths row
     * 0 = nothing, -> 4 very bad
     *
     * @param i the row the given icon shall be added
     * @return the ID of the drawable
     */
    public static int addDrawableToRowImage(int i) {
        switch (i) {
            case 0:
                return R.drawable.questionnaire_excellent;
            case 1:
                return R.drawable.questionnaire_none;
            case 2:
                return R.drawable.questionnaire_low;
            case 3:
                return R.drawable.questionnaire_moderate;
            default:
                return R.drawable.questionnaire_severe;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Save the received intent bundle
        parameters = getIntent().getBundleExtra(QuestionnaireConstants.BUNDLE);
        logParams(parameters);

        Resources res = getResources();
        String[] condition_answers = res.getStringArray(R.array.quest_overall_condition);

        ((TextView) findViewById(R.id.quest_multiple_title_textview)).setText(res.getString(R.string.quest_text_second));

        /*Load next and back button and set onClickListeners*/
        findViewById(R.id.quest_multiple_button_next).setOnClickListener(v -> {
            if (selectedAnswer == -1) {
                //highlight all rows by let their stroke color blink
                highlightAllRows(true);
                new Handler().postDelayed(() -> highlightAllRows(false), 400);
            } else {
                launchMidosThirdInfo();

            }

        });
        findViewById(R.id.quest_multiple_button_prev).setOnClickListener(v -> finish());

        /*~~~~~~~~SetUp View by adding the rows for the answer possibilities~~~~~~~~~*/
        //Load the linear layout as the placeholder view
        LinearLayout linearL_answers = findViewById(R.id.quest_multiple_linear_answers);

        //Add a view/row for each answer possibility
        rows = new View[condition_answers.length];
        for (int i = 0; i < condition_answers.length; i++) {
            //Inflate the Layout for a single row
            View row = getLayoutInflater().inflate(R.layout.questionnaire_row_answer, linearL_answers, false);
            //Set the tag - necessary to retrieve the selected option easily
            row.setTag(i);

            //The formula 5-length+i is a workaround
            ((ImageView) row.findViewById(R.id.quest_row_answer_image)).setImageDrawable(getDrawable(addDrawableToRowImage(5 - condition_answers.length + i)));

            ((TextView) row.findViewById(R.id.quest_row_answer_textView)).setText(condition_answers[i]);
            row.setOnClickListener(v -> markSelectedRow((int) row.getTag()));

            //Save the row, the checkbox and the answer field in the according field
            rows[i] = row;
            //populate the view to the linear layout
            linearL_answers.addView(row);
        }

        markSelectedRow(selectedAnswer);

    }

    /**
     * Highlights the selected row, by marking the checkbox and adding a stroke (by changing its default color)
     *
     * @param i the row to be selected
     */
    private void markSelectedRow(int i) {
        //Save the selected answer for the current question at the right position of the answer array.
        selectedAnswer = i;

        for (int j = 0; j < rows.length; j++) {
            //Mark only the selected row, all others reset to unchecked
            if (j == i) {
                ((CheckBox) rows[j].findViewById(R.id.quest_row_answer_checkBox)).setChecked(true);
                ((MaterialCardView) rows[j].findViewById(R.id.quest_answer_card)).setStrokeColor(getColor(R.color.color_secondary));

            } else {
                ((CheckBox) rows[j].findViewById(R.id.quest_row_answer_checkBox)).setChecked(false);
                ((MaterialCardView) rows[j].findViewById(R.id.quest_answer_card)).setStrokeColor(getColor(R.color.color_primary));
            }
            //Invalidation is somehow necessary since the MaterialCardView is not refreshing itself???
            rows[j].invalidate();

        }
    }

    /**
     * This method will launch the info of the third part of the midos questionnaire, to decide if one wants to input custom notes
     */
    public void launchMidosThirdInfo() {
        Intent intent = new Intent(this, QuestionnaireMidos3Question.class);
        parameters.putInt(QuestionnaireConstants.OVERALL_SITUATION, selectedAnswer);
        intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
        startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_quest_multiple_answers;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.quest_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_quest_2_rate;
    }

    /**
     * Set the stroke color of all rows to color_error or color_primary
     *
     * @param highlight if true, set the color to color_error, else set to color_primary
     */
    public void highlightAllRows(boolean highlight) {
        int color;
        if (highlight) {
            color = R.color.color_error;
        } else {
            color = R.color.color_primary;
        }
        for (View row : rows) {
            ((MaterialCardView) row.findViewById(R.id.quest_answer_card)).setStrokeColor(getColor(color));
            row.invalidate(); //Needs to be invalidated somehow
        }
    }
}