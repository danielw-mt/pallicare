package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * database class that specifies "questionnaire_user_suffering" table
 *
 * @author Ulla Sternemann
 */
@Entity(tableName = "questionnaire_user_suffering"
        ,
//        indices = {@Index(value = "saved_answer_id", unique = true)},
        foreignKeys = {
                @ForeignKey(entity = Questionnaire.class, parentColumns = "questionnaire_id", childColumns = "questionnaire_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "patient_id", onDelete = ForeignKey.CASCADE)
        }
)
public class QuestionnaireUserSuffering {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "saved_answer_id")
    private int userSufferingId;

    @NonNull
    @ColumnInfo(name = "user_suffering_description")
    private String userSufferingDescription;

    private int rating;

    @ColumnInfo(name = "questionnaire_id")
    private int questionnaireId;

    @ColumnInfo(name = "patient_id") // foreign key
    private int patientId;

    public QuestionnaireUserSuffering(@NonNull String userSufferingDescription, int rating, int questionnaireId, int patientId) {
        this.userSufferingDescription = userSufferingDescription;
        this.rating = rating;
        this.questionnaireId = questionnaireId;
        this.patientId = patientId;
    }

    public int getUserSufferingId() {
        return this.userSufferingId;
    }

    public void setUserSufferingId(int userSufferingId) {
        this.userSufferingId = userSufferingId;
    }

    @NonNull
    public String getUserSufferingDescription() {
        return this.userSufferingDescription;
    }

    public void setUserSufferingDescription(@NonNull String userSufferingDescription) {
        this.userSufferingDescription = userSufferingDescription;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getQuestionnaireId() {
        return this.questionnaireId;
    }

    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param questionnaireId ignore
     */
    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
