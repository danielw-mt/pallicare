package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import innolab.pallicare.R;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;

/**
 * Show the introductory information to the second question/part of the MIDOS questionnaire.
 * This refers to M2 which ask for the general condition
 * <p>
 * -extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos2Info extends BaseActivity {
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
        ((TextView) findViewById(R.id.quest_simple_one_textView)).setText(getString(R.string.quest_info_text_second));
        findViewById(R.id.quest_simple_one_button).setOnClickListener(v -> launchMidosThreeQuestionnaire());
    }

    /**
     * This method will launch the third part of the midos questionnaire.
     */
    public void launchMidosThreeQuestionnaire() {
        Intent intent = new Intent(this, QuestionnaireMidos2Rate.class);
        intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
        startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_quest_simple_one;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.quest_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_quest_2_info;
    }
}
