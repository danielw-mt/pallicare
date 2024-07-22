package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import innolab.pallicare.R;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;

/**
 * The user can enter custom notes in this screen. This is part M3 of the MIDOS questionnaire.
 * <p>
 * -extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos3Input extends BaseActivity {

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

        findViewById(R.id.btn_qst_m3input_next).setOnClickListener(v -> launchMidosFourInfo());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_questionnaire_midos3_input;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.questionnaire_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_quest_3_input;
    }

    /**
     * This method will launch the info of the fourth part of the midos questionnaire, to decide if one wants to input custom notes
     */
    public void launchMidosFourInfo() {

        String userNotes = ((EditText) findViewById(R.id.et_qst_m3input_notes)).getText().toString();
        if(!userNotes.trim().isEmpty()){
            parameters.putString(QuestionnaireConstants.ADDITIONAL_DETAILS, userNotes);
        }
        Intent intent = new Intent(this, QuestionnaireMidos4Question.class);
        intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
        startActivity(intent);
    }
}
