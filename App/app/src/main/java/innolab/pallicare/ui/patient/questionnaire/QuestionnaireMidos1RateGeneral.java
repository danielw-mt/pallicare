package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.Arrays;

import innolab.pallicare.R;
import innolab.pallicare.db.entities.Questionnaire;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;

//TODO: Klaus, add naming to wiki

/**
 * This activity is used to show the first 10 questions (M1) of the midos questionnaire for the patient
 * - extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos1RateGeneral extends BaseActivity {
//TODO Infos anzeigen. evtl marker nutzen um "prüfung" anzuzeigen?
    /**
     * Number of different choices for the MIDOS Questionnaire, usually 4, but at a single question 5
     */
    private final int MIDOS_ANSWER_POSSIBILITIES = 4;

    /**
     * Bundle, saving information, passed by intents
     */
    Bundle parameters;
    /**
     * Save the number of the current question that is shown
     */
    private int currentQuestionNumber = 0;
    /**
     * Saves the severity for each question received from patient
     */
    private int[] patientAnswers;
    /**
     * TextView to show the suffering at the top
     */
    private TextView textView_suffering;
    /**
     * Stores the suffering type texts from the resources
     */
    private String[] suffering_types;
    /**
     * The button to move one question back in the questionnaire
     */
    private Button button_prev;
    /**
     * Contain the string resources to be shown as answers of the questionnaire
     * 0=none, 1=low, 2=moderate, 3=severe
     */
    private ArrayList<String[]> answerArray = new ArrayList<>();
    /**
     * Each row for an answer possibility is saved (with its whole layout)
     */
    private View[] rows = new View[MIDOS_ANSWER_POSSIBILITIES];
    /**
     * Save references to the TextViews of each answer row
     */
    private TextView[] textViews_answer = new TextView[MIDOS_ANSWER_POSSIBILITIES];
    /**
     * The number of questions which will be asked during this activity. For common MIDOS it's always 10, but there is a second option for custom sufferings.
     */
    private int noOfQuestions;
    /**
     * Flag that shows, if one is in the MIDOS or in the custom suffer state
     */
    private boolean customSuffers = false;
    /**
     * This flag tells (in the custom state) if the answer texts have already been setup
     */
    private boolean setUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*~~~~~~~~Load resources from strings~~~~~~~~~*/

        super.onCreate(savedInstanceState);

        //Save the received intent bundle
        parameters = getIntent().getBundleExtra(QuestionnaireConstants.BUNDLE);

        if (parameters == null) {

            parameters = new Bundle();
            //MIDOS Mode, Load the strings in the resources
            Resources res = getResources();
            suffering_types = res.getStringArray(R.array.quest_suffering_types);

            answerArray.add(res.getStringArray(R.array.quest_answer_none));
            answerArray.add(res.getStringArray(R.array.quest_answer_low));
            answerArray.add(res.getStringArray(R.array.quest_answer_moderate));
            answerArray.add(res.getStringArray(R.array.quest_answer_severe));
        } else {
            logParams(parameters);

            //Change the mode to custom
            customSuffers = true;
            ArrayList<String> chosenSuffers = parameters.getStringArrayList(QuestionnaireConstants.CUSTOM_SUFFERING_TYPES);
            //Convert the arraylist to an array
            //TODO Klaus: change the type of sent / local data structures
            suffering_types = new String[chosenSuffers.size()];
            suffering_types = chosenSuffers.toArray(suffering_types);
            //Just add arrays with each a single string to the answerArrayList
            //TODO Klaus: improve workaround. Find better solution
            answerArray.add(new String[]{"keine"});
            answerArray.add(new String[]{"leichte"});
            answerArray.add(new String[]{"mittlere"});
            answerArray.add(new String[]{"starke"});

        }

        //Derive the number of questions from the number of suffers
        noOfQuestions = suffering_types.length;
        //Create an int array to save the patient answers
        //TODO Klaus: save in database? Local? Intent?
        patientAnswers = new int[noOfQuestions];

        //Don't use preselected default answer. Set to -1, patient has to set it to any value.
        Arrays.fill(patientAnswers, -1);

        /*Load textview for suffering*/
        textView_suffering = findViewById(R.id.quest_multiple_title_textview);

        /*~~~~~~~~Setup previous and next buttons*/

        /*Load next Button and set onClickListener*/
        Button next = findViewById(R.id.quest_multiple_button_next);
        next.setOnClickListener(v -> changeToQuestion(1));

        /*Load button_prev Button and set onClickListener*/
        button_prev = findViewById(R.id.quest_multiple_button_prev);
        button_prev.setOnClickListener(v -> changeToQuestion(-1));


        /*~~~~~~~~SetUp View by adding the rows for the answer possibilities~~~~~~~~~*/
        //Load the linear layout as the placeholder view
        LinearLayout linearL_answers = findViewById(R.id.quest_multiple_linear_answers);

        //Add a view/row for each answer possibility
        for (int i = 0; i < MIDOS_ANSWER_POSSIBILITIES; i++) {
//                Inflate the Layout for a single row

            View row = getLayoutInflater().inflate(R.layout.questionnaire_row_answer, linearL_answers, false);
            //Set the tag - necessary to retrieve the selected option easily
            row.setTag(i);
            //Show the correct icon.
            int drawableID = QuestionnaireMidos2Rate.addDrawableToRowImage(1 + i);
            ((ImageView) row.findViewById(R.id.quest_row_answer_image)).setImageDrawable(getDrawable(drawableID));

            textViews_answer[i] = row.findViewById(R.id.quest_row_answer_textView);
            row.setOnClickListener(v -> markSelectedRow((int) v.getTag()));

            //Save the row, the checkbox and the answer field in the according field
            rows[i] = row;
            //populate the view to the linear layout
            linearL_answers.addView(row);
        }

    }

    /**
     * Highlights the selected row, by marking the checkbox and adding a stroke (by changing its default color)
     *
     * @param i the row to be selected
     */
    private void markSelectedRow(int i) {
        //Save the selected answer for the current question at the right position of the answer array.
        patientAnswers[currentQuestionNumber] = i;

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
     * Load the suffering as well as answer strings and check the radio button belonging to the current question number.
     * It is called after onCreate to set up the first screen with default values, as well as after the return of the FinishActivity to recheck all values.
     * Therefore it is needed to set
     */
    @Override
    protected void onResume() {
        super.onResume();
        //Re/set the currentQuestionNumber to 0 to always start from the first question, especially if returning to this screen.
        currentQuestionNumber = 0;
        setUpNextQuestion();

    }


    /**
     * Restores the default=0 or previously chosen answer for this question.
     */
    private void restoreCheckedRadioButtonIndex() {
        int index = patientAnswers[currentQuestionNumber];
        markSelectedRow(index);
    }

    /**
     * Is called on a button click on button_prev or next. Increases or decreases the currentQuestionNumber according to the step.
     * Makes the view to show the proper content.
     *
     * @param step forward step (+1), backward step (-1)
     */
    private void changeToQuestion(int step) {
        if (patientAnswers[currentQuestionNumber] == -1) {

            //highlight all rows by let their stroke color blink
            highlightAllRows(true);
            new Handler().postDelayed(() -> highlightAllRows(false), 400);
        } else {
            //Refreshes the currentQuestionNumber depending on step
            currentQuestionNumber += step;
            if (currentQuestionNumber < 0) {
                currentQuestionNumber = 0;
                Log.d("Questionnaire", "currentQuestionNumber smaller than 0 should not happen");
            } else if (currentQuestionNumber >= noOfQuestions) {
                currentQuestionNumber = noOfQuestions - 1; // Might be unnecessary TODO Klaus: Depending on return behaviour to this screen.
                //Depending on the state launch different succeeding activities
                //TODO Klaus: Intent Bundle?
                if (customSuffers) {
                    parameters.putIntArray(QuestionnaireConstants.CUSTOM_SUFFERING_ANSWERS, patientAnswers);
                    launchMidosPartTwoInfo();
                } else {
                    parameters.putIntArray(QuestionnaireConstants.VITAL_PARAM, patientAnswers);
                    launchMidosOnePartTwoInfo();
                }
            } else {
                setUpNextQuestion();
            }
        }


    }

    /**
     * Depending on the current currentQuestionNumber the correct texts and buttons for this question will be shown.
     * It induces the correct radio button to be set.
     */
    private void setUpNextQuestion() {
        //Hide Prev Button in first question
        if (currentQuestionNumber == 0) {
            button_prev.setVisibility(View.INVISIBLE);
        } else {
            button_prev.setVisibility(View.VISIBLE);
        }
        restoreCheckedRadioButtonIndex();

        textView_suffering.setText(suffering_types[currentQuestionNumber]);

        //This was implemented as workaround to support the custom mode. In the custom suffer mode always the same answers will be shown, so this needs to be setup only once.
        if (!setUp) {
            //For each question row show the defined texts
            for (int i = 0; i < MIDOS_ANSWER_POSSIBILITIES; i++) {
                textViews_answer[i].setText((answerArray.get(i))[currentQuestionNumber]);
            }
            //If in custom mode, set setUp to true, so the answers wont change anymore
            if (customSuffers) {
                setUp = true;
            }
        }
    }

    /**
     * This intent started by pressing next on the last question leads to the second part of the first phase of midos to input custom suffers
     */
    public void launchMidosOnePartTwoInfo() {
        Intent intent = new Intent(this, QuestionnaireMidos1P2Question.class);
        intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
        startActivity(intent);
    }

    /**
     * This method will launch the info of the second part of the midos questionnaire, to rate the overall constitution
     */
    public void launchMidosPartTwoInfo() {
        Intent intent = new Intent(this, QuestionnaireMidos2Info.class);
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
        return R.string.help_quest_1_rate_general;
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
