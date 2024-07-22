package innolab.pallicare.db.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import innolab.pallicare.db.entities.Questionnaire;

/**
 * data access object (DAO) that specifies database interactions such as queries for "questionnaire" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface QuestionnaireDao {

    @Insert // TODO add onConflict strategy
    long insertQuestionnaireResult(Questionnaire questionnaire);

    @Update
    void updateQuestionnaireAnswers(Questionnaire questionnaire);

    @Query("SELECT * FROM questionnaire WHERE patient_id = :patientId")
    LiveData<List<Questionnaire>> getAllQuestionnairesByPatientId(int patientId);

    // TODO remove? (only for testing)
    @Query("SELECT * FROM questionnaire")
    LiveData<List<Questionnaire>> getAllQuestionnaires();
}