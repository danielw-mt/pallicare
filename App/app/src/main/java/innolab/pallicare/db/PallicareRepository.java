package innolab.pallicare.db;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import innolab.pallicare.db.api.RetrofitInstance;
import innolab.pallicare.db.api.entity.ScaleMeasurementData;
import innolab.pallicare.db.converters.Converters;
import innolab.pallicare.db.daos.ContactDao;
import innolab.pallicare.db.daos.DeviceDao;
import innolab.pallicare.db.daos.DeviceTypeDao;
import innolab.pallicare.db.daos.MeasurementBloodpressureDao;
import innolab.pallicare.db.daos.MeasurementScaleDao;
import innolab.pallicare.db.daos.PermissionDao;
import innolab.pallicare.db.daos.PermissionTypeDao;
import innolab.pallicare.db.daos.QuestionnaireDao;
import innolab.pallicare.db.daos.QuestionnaireUserSufferingDao;
import innolab.pallicare.db.daos.ThresholdDao;
import innolab.pallicare.db.daos.UserDao;
import innolab.pallicare.db.daos.UserTypeDao;
import innolab.pallicare.db.entities.Contact;
import innolab.pallicare.db.entities.Device;
import innolab.pallicare.db.entities.DeviceType;
import innolab.pallicare.db.entities.MeasurementBloodpressure;
import innolab.pallicare.db.entities.MeasurementScale;
import innolab.pallicare.db.entities.Permission;
import innolab.pallicare.db.entities.PermissionType;
import innolab.pallicare.db.entities.Questionnaire;
import innolab.pallicare.db.entities.QuestionnaireUserSuffering;
import innolab.pallicare.db.entities.Threshold;
import innolab.pallicare.db.entities.User;
import innolab.pallicare.db.entities.UserType;
import innolab.pallicare.io.SharedPrefHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PallicareRepository {


    private static final String LOG_TAG = PallicareRepository.class.getSimpleName();

    /**
     * The repository itself. Used for the singleton.
     */
    private static PallicareRepository sRepositoryInstance;

    /**
     * The used database.
     */
    private PallicareRoomDatabase mPallicareRoomDatabase;
    /**
     * All the DAOs required for the different local tables
     */
    private DeviceDao mDeviceDao;
    private DeviceTypeDao mDeviceTypeDao;
    private MeasurementBloodpressureDao mMeasurementBloodpressureDao;
    private MeasurementScaleDao mMeasurementScaleDao;
    private QuestionnaireUserSufferingDao mQuestionnaireUserSufferingDao;
    private PermissionDao mPermissionDao;
    private PermissionTypeDao mPermissionTypeDao;
    private QuestionnaireDao mQuestionnaireDao;
    private ThresholdDao mThresholdDao;
    private UserDao mUserDao;
    private UserTypeDao mUserTypeDao;
    private ContactDao mContactDao;

    private PallicareRepository(Application application) {

        mPallicareRoomDatabase = PallicareRoomDatabase.getDatabase(application);

        mDeviceDao = mPallicareRoomDatabase.getDeviceDao();
        mDeviceTypeDao = mPallicareRoomDatabase.getDeviceTypeDao();
        mMeasurementBloodpressureDao = mPallicareRoomDatabase.getMeasurementBloodpressureDao();
        mMeasurementScaleDao = mPallicareRoomDatabase.getMeasurementScaleDao();
        mQuestionnaireUserSufferingDao = mPallicareRoomDatabase.getQuestionnaireUserSufferingDao();
        mPermissionDao = mPallicareRoomDatabase.getPermissionDao();
        mPermissionTypeDao = mPallicareRoomDatabase.getPermissionTypeDao();
        mQuestionnaireDao = mPallicareRoomDatabase.getQuestionnaireDao();
        mThresholdDao = mPallicareRoomDatabase.getThresholdDao();
        mUserDao = mPallicareRoomDatabase.getUserDao();
        mUserTypeDao = mPallicareRoomDatabase.getUserTypeDao();
        mContactDao = mPallicareRoomDatabase.getContactDao();
    }

    public static PallicareRepository getInstance(Application application) {
        if (sRepositoryInstance == null) {
            synchronized (PallicareRepository.class) {
                if (sRepositoryInstance == null) {
                    sRepositoryInstance = new PallicareRepository(application);
                }
            }
        }
        return sRepositoryInstance;
    }

    /**
     * Saves the weight from the user input to the server
     * TODO if no internet-> somehow queue and do later.
     * TODO add the parameter to the method
     * TODO add return type for possible errors / throw error
     */
    public void saveWeightToServer(float weight, Context context) {

        String usertoken = new SharedPrefHandler().getLoggedInUserToken(context);
        int userID = new SharedPrefHandler().getLoggedInUserID(context);
        Call<ScaleMeasurementData> call = RetrofitInstance.getInstance().uploadMeasurementData("token " + usertoken, weight, 0.0f, 0.0f, 0.0f, Converters.dateToStringAPI(new Date(System.currentTimeMillis())), userID);
        call.enqueue(new Callback<ScaleMeasurementData>() {
            @Override
            public void onResponse(Call<ScaleMeasurementData> call, Response<ScaleMeasurementData> response) {
                if (!response.isSuccessful()) {
                    Log.i("API", "Code: " + response.code());
                    Log.i("API-Body", response.message());
                    Log.i("API-Body", response.headers().toString());
                    Log.i("API-Body", response.raw().toString());
                } else {
                    Log.i("API-...", "Token" + response.raw());
                }
            }

            @Override
            public void onFailure(Call<ScaleMeasurementData> call, Throwable t) {
                Log.i("APIFailure", t.getMessage());

            }
        });
    }

    /**
     * @return the current patient
     */
    public User getCurrentPatient() {
        int patientTypeID = getUserTypeByName("patient").getUserTypeId();
        Future<User> x = PallicareRoomDatabase.EXECUTORS.submit(() -> mUserDao.getMainPatient(patientTypeID));
        try {
            return x.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Device> getAllDevices() {
        Future<List<Device>> x = PallicareRoomDatabase.EXECUTORS.submit(() -> mDeviceDao.getAllDevices());
        try {
            return x.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getAllDeviceHWID() {
        Future<List<String>> x = PallicareRoomDatabase.EXECUTORS.submit(() -> mDeviceDao.getAllDeviceHWID());
        try {
            return x.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<List<Contact>> getAllContacts() {
        return mContactDao.getAllContactsAlphabetically();
    }

    public LiveData<List<DeviceType>> getAllDeviceTypes() {
        return mDeviceTypeDao.getAllDeviceTypes();
    }

    public LiveData<List<MeasurementBloodpressure>> getAllBloodpressureMeasurementsByPatientId(int patientId) {
        return mMeasurementBloodpressureDao.getAllBloodpressureMeasurementsByPatientId(patientId);
    }

    public LiveData<List<MeasurementBloodpressure>> getAllMeasurementBloodpressure() {
        return mMeasurementBloodpressureDao.getAllBloodpressureMeasurements();
    }

    public LiveData<List<MeasurementScale>> getAllScaleMeasurementsByPatientId(int patientId) {
        return mMeasurementScaleDao.getAllScaleMeasurementsByPatientId(patientId);
    }

    public LiveData<List<MeasurementScale>> getAllMeasurementScale() {
        return mMeasurementScaleDao.getAllScaleMeasurements();
    }

    public LiveData<List<QuestionnaireUserSuffering>> getAllQuestionnaireUserSufferings() {
        return mQuestionnaireUserSufferingDao.getAllQuestionnaireUserSufferings();
    }

    public LiveData<List<QuestionnaireUserSuffering>> getAllQuestionnaireUserSufferingsByQuestionnaireId(int questionnaireId) {
        return mQuestionnaireUserSufferingDao.getAllQuestionnaireUserSufferingsByQuestionnaireId(questionnaireId);
    }

    public LiveData<List<Permission>> getAllPermissions() {
        return mPermissionDao.getAllPermissions();
    }

    public LiveData<List<Permission>> getAllPermissionsByPatientId(int patientId) {
        return mPermissionDao.getAllPermissionsByPatientId(patientId);
    }

    public LiveData<List<Permission>> getAllPermissionsByRequestorId(int requestorId) {
        return mPermissionDao.getAllPermissionsByRequestorId(requestorId);
    }

    public LiveData<List<PermissionType>> getAllPermissionTypes() {
        return mPermissionTypeDao.getAllPermissionTypes();
    }

    public LiveData<List<Questionnaire>> getAllQuestionnaires() {
        return mQuestionnaireDao.getAllQuestionnaires();
    }

    public LiveData<List<Questionnaire>> getAllQuestionnairesByPatientId(int patientId) {
        return mQuestionnaireDao.getAllQuestionnairesByPatientId(patientId);
    }

    public LiveData<List<User>> getAllUsers() {
        return mUserDao.getAllUsers();
    }

    public LiveData<User> getUserById(int userId) {
        return mUserDao.getUserById(userId);
    }

    public LiveData<List<User>> getAllUsersAlphabetically() {
        return mUserDao.getAllUsersAlphabetically();
    }

    public LiveData<List<UserType>> getAllUserTypes() {
        return mUserTypeDao.getAllUserTypes();
    }

    public LiveData<Threshold> getThresholdByPatientId(int patientId) {
        return mThresholdDao.getThresholdsByPatientId(patientId);
    }

    /**
     * Clears all tables of the DB and repopulates it.
     * Use with care. Since it is executed as ASyncTask any modifications directly following the execution of this method might be done before clearDB is ready
     */
    public void clearDB() {
        mPallicareRoomDatabase.clearDB();
    }

    // -------- adding new data ---------
    //TODO change all to AsyncTask? see 6.1.5: https://codelabs.developers.google.com/codelabs/android-training-livedata-viewmodel/#7
    //TODO Add all insert methods
    public void addBloodpressureMeasurement(MeasurementBloodpressure m) {
        PallicareRoomDatabase.EXECUTORS.execute(() -> {
            mMeasurementBloodpressureDao.insertMeasurementBloodpressure(m);
            Log.d(LOG_TAG, "Bloodpressure measurement added");

        });
    }

    public void deleteDeviceByHWID(String hw_address) {
        PallicareRoomDatabase.EXECUTORS.execute(() -> {
            mDeviceDao.deleteDeviceByHWID(hw_address);
            Log.d(LOG_TAG, "Device deleted");

        });
    }

    public long insertQuestionnaireResult(Questionnaire questionnaire) {
        Future<Long> x = PallicareRoomDatabase.EXECUTORS.submit(() -> mQuestionnaireDao.insertQuestionnaireResult(questionnaire));
        try {
            return x.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void insertQuestionnaireUserSuffering(QuestionnaireUserSuffering questionnaireUserSuffering) {
        PallicareRoomDatabase.EXECUTORS.execute(() -> {
            mQuestionnaireUserSufferingDao.insert(questionnaireUserSuffering);
            Log.d(LOG_TAG, "QuestionnaireUserSuffering added");

        });
    }

    public void insertContact(Contact contact) {
        PallicareRoomDatabase.EXECUTORS.execute(() -> {
            mContactDao.insertContact(contact);
            Log.d(LOG_TAG, "Contact added");

        });
    }

    public void insertMeasurementScale(MeasurementScale measurementScale) {
        PallicareRoomDatabase.EXECUTORS.execute(() -> {
            mMeasurementScaleDao.insertMeasurementScale(measurementScale);
            Log.d(LOG_TAG, "Scale measurement added");

        });
    }

    public void insertUser(User user) {
        PallicareRoomDatabase.EXECUTORS.execute(() -> {
            mUserDao.insertUser(user);
            Log.d(LOG_TAG, "User added");
        });
    }

    public void insertUserType(UserType userType) {
        PallicareRoomDatabase.EXECUTORS.execute(() -> {
            mUserTypeDao.insertUserType(userType);
            Log.d(LOG_TAG, "UserType added");
        });
    }

    public void insertDeviceType(DeviceType deviceType) {
        PallicareRoomDatabase.EXECUTORS.execute(() -> {
            mDeviceTypeDao.insertDeviceType(deviceType);
            Log.d(LOG_TAG, "DeviceType added");
        });
    }


    public void insertDevice(Device device) {
        PallicareRoomDatabase.EXECUTORS.execute(() -> {
            mDeviceDao.insertDevice(device);
            Log.d(LOG_TAG, String.format("Device added: %s", device.getName()));
        });
    }


    /**
     * @param userTypeName any userTypeName
     * @return the row of the user with the specified description
     */
    public UserType getUserTypeByName(String userTypeName) {
        Future<UserType> x = PallicareRoomDatabase.EXECUTORS.submit(() -> mUserTypeDao.getUserTypeByName(userTypeName));
        try {
            return x.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param deviceTypeName any deviceTypeName
     * @return the device DB entry with the specified description
     */
    public DeviceType getDeviceTypeByName(String deviceTypeName) {
        Future<DeviceType> x = PallicareRoomDatabase.EXECUTORS.submit(() -> mDeviceTypeDao.getDeviceTypeByName(deviceTypeName));
        try {
            return x.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
