package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import innolab.pallicare.R;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;

/**
 * Get information why the patient was not able to enter the data on his own. This is the fourth question/part of the MIDOS questionnaire (M4)
 * <p>
 * -extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos4Rate extends BaseActivity {
    //TODO Klaus: how many answers are allowed here? @TOBI?

    /**
     * Bundle, saving information, passed by intents
     */
    Bundle parameters;
    /**
     * Each row for an answer possibility is saved (with its whole layout)
     */
    private View[] rows;
    /**
     * Saves the row which is currently selected. By default -1, so it is unselected
     */
    private int selectedAnswer = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*~~~~~~~~Load resources from strings~~~~~~~~~*/
        super.onCreate(savedInstanceState);

        //Save the received intent bundle
        parameters = getIntent().getBundleExtra(QuestionnaireConstants.BUNDLE);
        logParams(parameters);

        Resources res = getResources();
        String[] reasons_submission_not_possible = res.getStringArray(R.array.quest_submission_not_possible);

        /*~~~~~~~~SetUp View by adding the rows for the answer possibilities~~~~~~~~~*/

        //Load the linear layout as the placeholder view
        LinearLayout linearL_answers = findViewById(R.id.quest_multiple_linear_answers);

        //Add a view/row for each answer possibility
        rows = new View[reasons_submission_not_possible.length];
        for (int i = 0; i < reasons_submission_not_possible.length; i++) {
            //Inflate the Layout for a single row
            View row = getLayoutInflater().inflate(R.layout.questionnaire_row_answer, linearL_answers, false);
            //Set the tag - necessary to retrieve the selected option easily
            row.setTag(i);

            //Style textView
            ((TextView) row.findViewById(R.id.quest_row_answer_textView)).setText(reasons_submission_not_possible[i]);
            ((TextView) row.findViewById(R.id.quest_row_answer_textView)).setTextSize(20);

            //Hide the image placeholder
            row.findViewById(R.id.quest_row_answer_image).setVisibility(View.GONE);
            row.setOnClickListener(v -> markSelectedRow((int) v.getTag()));

            //Save the row, the checkbox and the answer field in the according field
            rows[i] = row;
            //populate the view to the linear layout
            linearL_answers.addView(row);
        }

        /*~~~~~~~~SetUp other view components ~~~~~~~~~*/
        //Set the main text of the activity
        ((TextView) findViewById(R.id.quest_multiple_title_textview)).setTextSize(30);
        ((TextView) findViewById(R.id.quest_multiple_title_textview)).setText(getString(R.string.quest_text_fourth));

        findViewById(R.id.quest_multiple_button_prev).setOnClickListener(v -> finish());
        findViewById(R.id.quest_multiple_button_next).setOnClickListener(v -> {
            //If an answer is selected
            if (selectedAnswer != -1) {
                launchFinishQuestionnaire();
            } else {
                //highlight all rows by let their stroke color blink
                highlightAllRows(true);
                new Handler().postDelayed(() -> highlightAllRows(false), 400);
            }
        });
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
     * This method will launch the finish activity.
     */
    public void launchFinishQuestionnaire() {
        Intent intent = new Intent(this, QuestionnaireMidos5Finish.class);
        //Save the information on who filled the questionnaire to the intent bundle
        parameters.putInt(QuestionnaireConstants.FILLED_OUT, selectedAnswer);
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
        return R.string.help_quest_4_rate;
    }
}
