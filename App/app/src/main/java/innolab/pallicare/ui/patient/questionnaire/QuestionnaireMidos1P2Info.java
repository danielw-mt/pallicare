package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import innolab.pallicare.R;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;


/**
 * This class show an information for the patient that he can enter his other sufferings in the next screen.
 * <p>
 * - extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos1P2Info extends BaseActivity {

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

        ((TextView) findViewById(R.id.quest_simple_one_textView)).setText(getString(R.string.quest_info_text_first_p2_input));
        findViewById(R.id.quest_simple_one_button).setOnClickListener(v -> launchMidosTwoSelectionQuestionnaire());
    }

    /**
     * This method will launch the selection screen of the second part of the midos questionnaire.
     */
    public void launchMidosTwoSelectionQuestionnaire() {
        Intent intent = new Intent(this, QuestionnaireMidos1P2AddSuffers.class);
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
        return R.string.help_quest_1p2_info;
    }
}
