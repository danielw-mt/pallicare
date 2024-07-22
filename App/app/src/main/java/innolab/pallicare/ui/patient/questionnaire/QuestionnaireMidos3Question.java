package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import innolab.pallicare.R;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;


/**
 * Show the introductory information to the third part of the MIDOS questionnaire.
 * This refers to M3, asking for custom notes of the patient.
 * <p>
 * -extends {@link BaseActivity}
 *
 * @author Klau Schmidt
 */
public class QuestionnaireMidos3Question extends BaseActivity {
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

        ((TextView) findViewById(R.id.quest_simple_two_textView)).setText(getString(R.string.quest_info_text_third));

        findViewById(R.id.quest_simple_two_no).setOnClickListener(v -> launchMidosFourInfo());
        findViewById(R.id.quest_simple_two_yes).setOnClickListener(v -> launchMidosThree());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_quest_simple_two;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.questionnaire_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_quest_3_question;
    }

    /**
     * This method will launch the third part of the midos questionnaire to input custom notes
     */
    public void launchMidosThree() {
        Intent intent = new Intent(this, QuestionnaireMidos3Input.class);
        intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
        startActivity(intent);
    }

    /**
     * This method will launch the info of the fourth part of the midos questionnaire, to decide if one wants to input custom notes
     */
    public void launchMidosFourInfo() {
        Intent intent = new Intent(this, QuestionnaireMidos4Question.class);
        intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
        startActivity(intent);
    }
}
