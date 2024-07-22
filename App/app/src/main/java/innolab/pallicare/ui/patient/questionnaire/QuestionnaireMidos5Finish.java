package innolab.pallicare.ui.patient.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;

import innolab.pallicare.R;
import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.db.entities.Questionnaire;
import innolab.pallicare.db.entities.QuestionnaireUserSuffering;
import innolab.pallicare.io.QuestionnaireConstants;
import innolab.pallicare.ui.BaseActivity;

/**
 * This class gives the patient the opportunity to reevaluate his questionnaire data.
 * So he can return to the previous screen or progress and save.
 *
 * @author Klaus Schmidt
 */
public class QuestionnaireMidos5Finish extends BaseActivity {

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
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_finish_quest;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.quest_finish_activity_title;
    }

    /**
     * Saves the patient's data and progresses to the data saved screen.
     *
     * @param view ignore
     */
    public void progressToSavedActivity(View view) {

        try {
            //Extract all information from the intent bundle
            int[] vitalParam = parameters.getIntArray(QuestionnaireConstants.VITAL_PARAM);
            int overallSituation = parameters.getInt(QuestionnaireConstants.OVERALL_SITUATION);
            int filledOutYourself = parameters.getInt(QuestionnaireConstants.FILLED_OUT);
            String additionalDetails = parameters.getString(QuestionnaireConstants.ADDITIONAL_DETAILS);
            int[] customSufferingAnswers = parameters.getIntArray(QuestionnaireConstants.CUSTOM_SUFFERING_ANSWERS);
            ArrayList<String> customSufferingTypes = parameters.getStringArrayList(QuestionnaireConstants.CUSTOM_SUFFERING_TYPES);


            //Check against different errors
            if ((vitalParam == null) || (vitalParam.length != 10)) {
                throw new Error("Internal Problem with vital parameters");
            }
            if ((overallSituation < 0) || (overallSituation > 4)) {
                throw new Error("Internal Problem with overall situation value");
            }
            if ((filledOutYourself < -1) || (filledOutYourself > 4)) {
                throw new Error("Internal Problem with filledOutYourself value");
            }

            //If no errors have been thrown: proceed with saving
            int pain = vitalParam[0];
            int nausea = vitalParam[1];
            int shortnessOfBreath = vitalParam[2];
            int vomit = vitalParam[3];
            int constipation = vitalParam[4];
            int weakness = vitalParam[5];
            int lossOfAppetite = vitalParam[6];
            int fatigue = vitalParam[7];
            int depression = vitalParam[8];
            int anxiety = vitalParam[9];
            int patientID = PallicareRepository.getInstance(getApplication()).getCurrentPatient().getUserId();


            Questionnaire questionnaire = new Questionnaire(pain, nausea, shortnessOfBreath, vomit, constipation, weakness, lossOfAppetite, fatigue, depression, anxiety, overallSituation, filledOutYourself, additionalDetails, new Date(System.currentTimeMillis()), patientID);
            long questionnaireID = PallicareRepository.getInstance(getApplication()).insertQuestionnaireResult(questionnaire);


            //Add custom sufferings of users
            if (customSufferingAnswers != null && customSufferingTypes != null) {
                if (customSufferingAnswers.length == 0 || customSufferingTypes.size() == 0) {
                    throw new Error("There shouldnt be a datatype of zero length.");
                } else if (customSufferingAnswers.length != customSufferingTypes.size()) {
                    throw new Error("customSuffer answer and description should have equal size.");
                } else {
                    int userID = PallicareRepository.getInstance(getApplication()).getCurrentPatient().getUserId();
                    for (int i = 0; i < customSufferingAnswers.length; i++) {
                        QuestionnaireUserSuffering questionnaireUserSuffering = new QuestionnaireUserSuffering(customSufferingTypes.get(i), customSufferingAnswers[i], (int) questionnaireID, userID);
                        PallicareRepository.getInstance(getApplication()).insertQuestionnaireUserSuffering(questionnaireUserSuffering);
                    }
                }
            }


//TODO Save CustomSuffers + Answers
            Intent intent = new Intent(this, QuestionnaireMidos6Saved.class);
            startActivity(intent);
        } catch (Error error) {
            Log.d(LOG_TAG, "questID" + error.getMessage());
            Intent intent = new Intent(this, QuestionnaireMidos7Error.class);
            startActivity(intent);
        }
    }

    @Override
    protected int getHelpText() {
        return R.string.help_quest_5_finish;
    }

}
