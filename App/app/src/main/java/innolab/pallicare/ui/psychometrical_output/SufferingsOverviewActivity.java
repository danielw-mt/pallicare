package innolab.pallicare.ui.psychometrical_output;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import innolab.pallicare.R;
import innolab.pallicare.ui.BaseActivity;

public class SufferingsOverviewActivity extends BaseActivity {

    /**
     * Stores the suffering type texts from the resources
     */
    private String[] suffering_types;

    /**
     * The number of questions which will be asked during this activity. For common MIDOS it's always 10, but there is a second option for custom sufferings.
     */
    private int noOfQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        suffering_types = res.getStringArray(R.array.quest_suffering_types);
        LinearLayout linlay = findViewById(R.id.container_suffers);

        //Derive the number of questions from the number of suffers
        noOfQuestions = suffering_types.length;

        for (int i = 0; i < noOfQuestions; i++) {
            View row = getLayoutInflater().inflate(R.layout.suffers_overview_row_complaint, linlay, false);
            ((TextView) row.findViewById(R.id.suffers_row_textView)).setText(suffering_types[i]);
            if(i == 0 || i == 4){
                ((ImageView) row.findViewById(R.id.suffers_row_ImageView)).setImageDrawable(getDrawable(R.drawable.questionnaire_moderate));
            }
            else if(i == 2 || i == 5){
                ((ImageView) row.findViewById(R.id.suffers_row_ImageView)).setImageDrawable(getDrawable(R.drawable.questionnaire_low));
            }
            else{
                ((ImageView) row.findViewById(R.id.suffers_row_ImageView)).setImageDrawable(getDrawable(R.drawable.questionnaire_excellent));
            }

            row.setTag(i);
            linlay.addView(row);

            row.setOnClickListener(v -> launchSuffersDetailedActivity((int) v.getTag()));

        }

    }

    public void launchSuffersDetailedActivity(int tag) {
        Intent intent = new Intent(this, SufferingsDetailedViewActivity.class);
        intent.putExtra("Rownumber", tag);
        startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_suffers_overview;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.suffers_overview_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_text_sufferings_overview;
    }
}
