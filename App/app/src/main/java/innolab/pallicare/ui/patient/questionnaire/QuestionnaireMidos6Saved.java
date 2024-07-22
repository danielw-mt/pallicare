package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import innolab.pallicare.R;
import innolab.pallicare.ui.BaseActivity;
import innolab.pallicare.ui.patient.home.PatientOverviewActivity;

/**
 * This activity will show a very simple screen to inform the patient that his data got saved successfully.
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos6Saved extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Hide the BackButton, as this option isn't desired here
        hideBackButton();
        super.onCreate(savedInstanceState);
        ((TextView) findViewById(R.id.quest_simple_one_textView)).setText(getString(R.string.quest_finished_text));
        findViewById(R.id.quest_simple_one_button).setOnClickListener(v -> returnToOverviewActivity());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_quest_simple_one;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.quest_saved_activity_title;
    }

    /**
     * This method will return to the Overview activity. It deletes the whole stack (with questionnaire activity and FinishActivity) above the PatientOverviewActivity.
     */
    public void returnToOverviewActivity() {
        Intent intent = new Intent(this, PatientOverviewActivity.class);
        /* To delete the whole stack above an activity and return to this specific activity,
        read more in https://stackoverflow.com/a/14857698*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Override the backButton, so it is not possible to return to the previous (FinishActivity) screen.
     */
    @Override
    public void onBackPressed() {
        returnToOverviewActivity();
    }

    @Override
    protected int getHelpText() {
        return R.string.help_quest_6_saved;
    }
}
