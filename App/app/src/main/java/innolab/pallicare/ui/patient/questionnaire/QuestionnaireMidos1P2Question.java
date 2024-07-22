package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import innolab.pallicare.R;
import innolab.pallicare.db.entities.Questionnaire;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;

/**
 * Show the introductory information to the second question/part of the MIDOS questionnaire.
 * These are the 11th and 12th question of M1, both asking for other sufferings.
 * <p>
 * -extends {@link BaseActivity}
 *
 * @author Klau Schmidt
 */
public class QuestionnaireMidos1P2Question extends BaseActivity {
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
        ((TextView) findViewById(R.id.quest_simple_two_textView)).setText(getString(R.string.quest_info_text_first_p2));

        findViewById(R.id.quest_simple_two_no).setOnClickListener(v -> launchMidosThreeInfo());
        findViewById(R.id.quest_simple_two_yes).setOnClickListener(v -> launchMidosTwoSelectionInfo());
    }

    /**
     * This method will launch the info of the third part of the midos questionnaire.
     */
    public void launchMidosThreeInfo() {
        Intent intent = new Intent(this, QuestionnaireMidos2Info.class);
        intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
        startActivity(intent);
    }

    /**
     * This method will launch the input info of the second part of the midos questionnaire.
     */
    public void launchMidosTwoSelectionInfo() {
        Intent intent = new Intent(this, QuestionnaireMidos1P2Info.class);
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
        return R.string.help_quest_1p2_question;
    }
}
