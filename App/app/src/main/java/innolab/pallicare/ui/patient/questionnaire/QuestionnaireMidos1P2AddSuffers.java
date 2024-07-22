package innolab.pallicare.ui.patient.questionnaire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import innolab.pallicare.R;
import innolab.pallicare.db.entities.Questionnaire;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;

/**
 * In this class the patient can enter all his other sufferings not mentioned in Midos so far.
 * <p>
 * - extends {@link BaseActivity}
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos1P2AddSuffers extends BaseActivity {

    //TODO Klaus: use recycler view?
    //TODO Klaus: edit / delete entry?
    //TODO Klaus: Refactor

    /**
     * EditText to enter new suffers
     */
    EditText editText;
    /**
     * LinearLayout to show rows with custom suffers
     */
    LinearLayout linearLayout;
    /**
     * Saves all suffers entered by the patient
     */
    private List<String> suffers = new ArrayList<>();
    /**
     * List to save all rows, which have been created for suffers
     */
    private List<View> rows = new ArrayList<>();

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

        linearLayout = findViewById(R.id.quest_select_linlay_suffer);
        editText = findViewById(R.id.quest_select_edittext_suffer);

        findViewById(R.id.quest_select_button_add_suffer).setOnClickListener(v -> tryToAddNewSuffer());

        findViewById(R.id.quest_select_next).setOnClickListener(v -> launchRatingActivity());
    }

    /**
     * This method is called by a click on the next button, it gathers all selected sufferings and sends them to the rating activity
     */
    private void launchRatingActivity() {
        //Save all chosen suffers
        ArrayList<String> chosenSuffers = new ArrayList<>();
        //Iterate over all views and add selected ones to the list
        for (View row : rows) {
            boolean state = ((CheckBox) row.findViewById(R.id.quest_row_answer_checkBox)).isChecked();
            if (state) {
                String chosenSuffer = ((TextView) row.findViewById(R.id.quest_row_answer_textView)).getText().toString();
                chosenSuffers.add(chosenSuffer);
            }
        }

        //Proceed only if at least one element is selected
        if (chosenSuffers.isEmpty()) {
            //if no suffer was selected return to screen to choose if one has suffers.
            Intent intent = new Intent(this, QuestionnaireMidos1P2Question.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
            startActivity(intent);
        } else if (chosenSuffers.size() > 2) {
            Snackbar.make(findViewById(R.id.base_button_alert), getString(R.string.quest_m1p2_select_max_2), Snackbar.LENGTH_LONG)
                    .setTextColor(getColor(R.color.color_on_primary))
                    .setBackgroundTint(getColor(R.color.color_secondary_variant))
                    .show();
        } else {
            //attach the key value pair using putExtra to this intent
            Intent intent = new Intent(this, QuestionnaireMidos1RateGeneral.class);
            parameters.putStringArrayList(QuestionnaireConstants.CUSTOM_SUFFERING_TYPES, chosenSuffers);
            intent.putExtra(QuestionnaireConstants.BUNDLE, parameters);
            startActivity(intent);
        }

    }

    /**
     * Try to add a new suffer to the list.
     */
    private void tryToAddNewSuffer() {
        //Get the current String
        String string = editText.getText().toString();

        //If the string is not null and not ""
        if (isStringOK(string)) {
            //Trim it for unnecessary whitespace
            string = string.trim();
            //Check if it was already saved
            if (!isAlreadySaved(string)) {
                //If not, add it and reset the layout
                addString(string);
                cleanUpLayout();
            } else {
                Log.d("KLAUS", "is already added" + suffers.toString());
                //TODO Klaus: Fehlerbehandlung?
            }
        } else {
            Log.d("KLAUS", "this is no valid string");
        }

    }

    /**
     * Reset / CleanUp the layout.
     */
    private void cleanUpLayout() {
        editText.setText("");

        //Hide the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //Keep the keyboard hidden on onResume
        editText.clearFocus();

    }

    /**
     * Add the string to the layout
     *
     * @param suffer the new suffering which is not yet in the list
     */
    private void addString(String suffer) {
        suffers.add(suffer);
        //Inflate layout
        View row = getLayoutInflater().inflate(R.layout.questionnaire_row_answer, linearLayout, false);

        //Hide the image container
        row.findViewById(R.id.quest_row_answer_image).setVisibility(View.GONE);

        //Set the Text of the suffering
        ((TextView) row.findViewById(R.id.quest_row_answer_textView)).setText(suffer);

        //Set independent onClickListener to select each row on its own.
        row.setOnClickListener(v -> {
            boolean state = ((CheckBox) v.findViewById(R.id.quest_row_answer_checkBox)).isChecked();
            ((CheckBox) v.findViewById(R.id.quest_row_answer_checkBox)).setChecked(!state);

            int color = state ? R.color.color_primary : R.color.color_secondary;
            ((MaterialCardView) v.findViewById(R.id.quest_answer_card)).setStrokeColor(getColor(color));
        });

        //add to layout
        linearLayout.addView(row);
        rows.add(row);
    }

    /**
     * Check if the string is valid
     *
     * @param string any string
     * @return true if the string is not null and not trimmed emptys
     */
    private boolean isStringOK(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        } else return !string.trim().isEmpty();
    }

    /**
     * Check if the given string is already saved
     *
     * @param string any string
     * @return if it is already saved
     */
    public boolean isAlreadySaved(String string) {
        return suffers.contains(string);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_quest_suffer_selection;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.quest_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_quest_1p2_add_suffers;
    }
}
