package innolab.pallicare.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

//exportSchema should only be set to true, if we are using some kind of migration. It is true by default.
//Entities are the classes which belong to the database. Only their tables will be created.
@Database(entities = {Device.class, DeviceType.class, MeasurementBloodpressure.class,
        MeasurementScale.class, Permission.class, PermissionType.class, Questionnaire.class,
        QuestionnaireUserSuffering.class, Threshold.class, User.class, UserType.class, Contact.class},
        version = 11, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PallicareRoomDatabase extends RoomDatabase {

    private static final String LOG_TAG = PallicareRoomDatabase.class.getSimpleName();

    private static final String DATABASE_NAME = "pallicare_database.db";
    /**
     * Use Executors with number of threads instead of AsyncTask. TODO change to AsyncTask?
     */
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    /**
     * The only database instance which will be used. Implemented as singleton.
     */
    private static volatile PallicareRoomDatabase INSTANCE;

    private static RoomDatabase.Callback CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }

        //TODO test me
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            new PopulateDbAsync(INSTANCE).execute();
//        }
    };

    /**
     * creates instance of a room database. Prepopulation is only done on first write.
     * TODO create migration strategy https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929. Use exportSchema therefore
     *
     * @param context ?
     * @return instance of PallicareRoomDatabase
     */
    static PallicareRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PallicareRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PallicareRoomDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration() // DestructiveMigration. Recreate empty tables.
                            .addCallback(CALLBACK).build();
                    Log.d(LOG_TAG, "database was created " + INSTANCE);
                }
            }
            //Run a useless query to prepopulate the database
            INSTANCE.query("select 1", null);

        }
        return INSTANCE;
    }

    /**
     * Clears all tables of the DB and repopulates it.
     * Use with care. Since it is executed as ASyncTask any modifications directly following the execution of this method might be done before clearDB is ready
     */
    void clearDB() {
        if (INSTANCE != null) {
            new PopulateDbAsync(INSTANCE).execute();
        }
    }

    //The DAOs working with the database
    public abstract DeviceDao getDeviceDao();

    public abstract DeviceTypeDao getDeviceTypeDao();

    public abstract MeasurementBloodpressureDao getMeasurementBloodpressureDao();

    public abstract MeasurementScaleDao getMeasurementScaleDao();

    public abstract PermissionDao getPermissionDao();

    public abstract PermissionTypeDao getPermissionTypeDao();

    public abstract QuestionnaireDao getQuestionnaireDao();

    public abstract QuestionnaireUserSufferingDao getQuestionnaireUserSufferingDao();

    public abstract ThresholdDao getThresholdDao();

    public abstract UserDao getUserDao();

    public abstract UserTypeDao getUserTypeDao();

    public abstract ContactDao getContactDao();

    /**
     * AsyncTask Class to prepopulate the Database.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        /**
         * Load the required Daos
         */
        private final UserTypeDao userTypeDao;
        private final DeviceTypeDao deviceTypeDao;
        private final PermissionTypeDao permissionTypeDao;
        private PallicareRoomDatabase instance;

        PopulateDbAsync(PallicareRoomDatabase instance) {
            this.instance = instance;
            userTypeDao = instance.getUserTypeDao();
            deviceTypeDao = instance.getDeviceTypeDao();
            permissionTypeDao = instance.getPermissionTypeDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            instance.clearAllTables();

            UserType userTypePatient = new UserType("patient");
            UserType userTypeDoc = new UserType("doctor");
            UserType userTypeNok = new UserType("nok");
            userTypeDao.insertAllUserTypes(userTypePatient, userTypeDoc, userTypeNok);

            DeviceType deviceTypeScale = new DeviceType("scale");
            DeviceType deviceTypeBp = new DeviceType("bloodpressure");
            deviceTypeDao.insertDeviceTypes(deviceTypeScale, deviceTypeBp);

            PermissionType permissionTypeUndefined = new PermissionType("undefined");
            PermissionType permissionTypeGranted = new PermissionType("granted");
            PermissionType permissionTypeDenied = new PermissionType("denied");
            PermissionType permissionTypeRevoked = new PermissionType("revoked");
            permissionTypeDao.insertPermissionTypes(permissionTypeUndefined, permissionTypeGranted,
                    permissionTypeDenied, permissionTypeRevoked);

            Log.d(LOG_TAG, "DATABASE PREPOPULATED");
            return null;
        }
    }
}
