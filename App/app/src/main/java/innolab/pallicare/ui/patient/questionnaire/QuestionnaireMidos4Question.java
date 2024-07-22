package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import innolab.pallicare.BuildConfig;
import innolab.pallicare.R;
import innolab.pallicare.db.entities.Questionnaire;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;

/**
 * Show the introductory information to the fourth question/part of the MIDOS questionnaire.
 * This refers to M4, if the patient did the survey on his own.
 * <p>
 * -extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos4Question extends BaseActivity {

    /**
     * Bundle, saving information, passed by intents
     */
    Bundle parameters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Save the received intent bundle
        parameters = getIntent().getBundleExtra(QuestionnaireConstants.BUNDLE);
        logParams(parameters);

        //Set the correct text and onClickListener as this layout is reused.
        ((TextView) findViewById(R.id.quest_simple_two_textView)).setText(getString(R.string.quest_info_text_fourth));
        findViewById(R.id.quest_simple_two_yes).setOnClickListener(v -> launchFinishQuestionnaire());

        findViewById(R.id.quest_simple_two_no).setOnClickListener(v -> launchMidosFourQuestionnaire());

    }

    /**
     * This method will launch the fourth parth of the midos questionnaire
     */
    public void launchMidosFourQuestionnaire() {
        Intent intent = new Intent(this, QuestionnaireMidos4Rate.class);
        intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
        startActivity(intent);
    }

    /**
     * This method will launch the finish activity.
     */
    public void launchFinishQuestionnaire() {
        Intent intent = new Intent(this, QuestionnaireMidos5Finish.class);
        parameters.putInt(QuestionnaireConstants.FILLED_OUT, -1);
        intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
        startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_quest_simple_two;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.quest_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_quest_4_info;
    }
}
