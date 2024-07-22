package innolab.pallicare.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import innolab.pallicare.db.entities.QuestionnaireUserSuffering;

/**
 * data access object (DAO) that specifies database interactions such as queries for "questionnaire_user_suffering" table
 *
 * @author Ulla Sternemann
 */
@Dao
public interface QuestionnaireUserSufferingDao {

    @Insert
        // TODO add onConflict strategy
    void insert(QuestionnaireUserSuffering questionnaireUserSuffering);

    @Update
    void update(QuestionnaireUserSuffering questionnaireUserSufferings);

    @Query("SELECT * FROM questionnaire_user_suffering WHERE questionnaire_id = :questionnaireId")
    LiveData<List<QuestionnaireUserSuffering>> getAllQuestionnaireUserSufferingsByQuestionnaireId(int questionnaireId);

    // TODO other sufferings per patient, who to jump between tables ? Could be used for analysis of often reported problems
//    @Query("SELECT * FROM other_suffering WHERE questionnaire.patient_id = :patientId")
//    LiveData<List<QuestionnaireUserSuffering>> getAllQuestionnaireUserSufferingsByPatientId(int patientId);

    // TODO remove? (only for testing)
    @Query("SELECT * FROM questionnaire_user_suffering")
    LiveData<List<QuestionnaireUserSuffering>> getAllQuestionnaireUserSufferings();
}
