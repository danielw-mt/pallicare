package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * database class that specifies "questionnaire" table
 *
 * @author Ulla Sternemann
 */
@Entity(tableName = "questionnaire",
//        indices = {@Index(value = "questionnaire_id", unique = true)},
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "patient_id", onDelete = ForeignKey.CASCADE)
)
public class Questionnaire {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "questionnaire_id")
    private int questionnaireId;

    private int pain;

    private int nausea;

    @ColumnInfo(name = "shortness_of_breath")
    private int shortnessOfBreath;

    private int vomit;

    private int constipation;

    private int weakness;

    @ColumnInfo(name = "loss_of_appetite")
    private int lossOfAppetite;

    private int fatigue;

    private int depression;

    private int anxiety;

    @ColumnInfo(name = "overall_situation")
    private int overallSituation;

    @ColumnInfo(name = "filled_out_yourself")
    private int filledOutYourself;

    @ColumnInfo(name = "additional_details")
    private String additionalDetails;

    @NonNull
    private Date timestamp;

    @ColumnInfo(name = "patient_id") // foreign key
    private int patientId;

    public Questionnaire(int pain, int nausea, int shortnessOfBreath, int vomit, int constipation,
                         int weakness, int lossOfAppetite, int fatigue, int depression, int anxiety,
                         int overallSituation, int filledOutYourself, String additionalDetails, @NonNull Date timestamp, int patientId) {
        this.pain = pain;
        this.nausea = nausea;
        this.shortnessOfBreath = shortnessOfBreath;
        this.vomit = vomit;
        this.constipation = constipation;
        this.weakness = weakness;
        this.lossOfAppetite = lossOfAppetite;
        this.fatigue = fatigue;
        this.depression = depression;
        this.anxiety = anxiety;
        this.overallSituation = overallSituation;
        this.filledOutYourself = filledOutYourself;
        this.additionalDetails = additionalDetails;
        this.timestamp = timestamp; // TODO default current timestamp
        this.patientId = patientId;
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

    public int getPain() {
        return this.pain;
    }

    public void setPain(int pain) {
        this.pain = pain;
    }

    public int getNausea() {
        return this.nausea;
    }

    public void setNausea(int nausea) {
        this.nausea = nausea;
    }

    public int getShortnessOfBreath() {
        return this.shortnessOfBreath;
    }

    public void setShortnessOfBreath(int shortnessOfBreath) {
        this.shortnessOfBreath = shortnessOfBreath;
    }

    public int getVomit() {
        return this.vomit;
    }

    public void setVomit(int vomit) {
        this.vomit = vomit;
    }

    public int getConstipation() {
        return this.constipation;
    }

    public void setConstipation(int constipation) {
        this.constipation = constipation;
    }

    public int getWeakness() {
        return this.weakness;
    }

    public void setWeakness(int weakness) {
        this.weakness = weakness;
    }

    public int getLossOfAppetite() {
        return this.lossOfAppetite;
    }

    public void setLossOfAppetite(int lossOfAppetite) {
        this.lossOfAppetite = lossOfAppetite;
    }

    public int getFatigue() {
        return this.fatigue;
    }

    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }

    public int getDepression() {
        return this.depression;
    }

    public void setDepression(int depression) {
        this.depression = depression;
    }

    public int getAnxiety() {
        return this.anxiety;
    }

    public void setAnxiety(int anxiety) {
        this.anxiety = anxiety;
    }

    public int getOverallSituation() {
        return this.overallSituation;
    }

    public void setOverallSituation(int overallSituation) {
        this.overallSituation = overallSituation;
    }

    public int getFilledOutYourself() {
        return this.filledOutYourself;
    }

    public void setFilledOutYourself(int filledOutYourself) {
        this.filledOutYourself = filledOutYourself;
    }

    public String getAdditionalDetails() {
        return this.additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    @NonNull
    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getPatientId() {
        return this.patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
