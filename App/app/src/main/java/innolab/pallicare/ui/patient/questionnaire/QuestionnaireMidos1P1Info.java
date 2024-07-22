package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import innolab.pallicare.R;
import innolab.pallicare.ui.BaseActivity;

/**
 * Show the introductory information to the first question/part of the MIDOS questionnaire.
 * These are the first 10 questions of M1.
 * <p>
 * -extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos1P1Info extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the correct text and onClickListener as this layout is reused.
        ((TextView) findViewById(R.id.quest_simple_one_textView)).setText(getString(R.string.quest_info_text_first));
        findViewById(R.id.quest_simple_one_button).setOnClickListener(v -> launchMidosOneQuestionnaire());
    }

    /**
     * This method will launch the first part of the midos questionnaire.
     */
    public void launchMidosOneQuestionnaire() {
        Intent intent = new Intent(this, QuestionnaireMidos1RateGeneral.class);
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
        return R.string.help_quest_1p1_info;
    }
}
