package innolab.pallicare.io;

/**
 * currently unused. kept for later
 */

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

//TODO author & comments
public class DataSaver {

    private static final String LOG_TAG = DataSaver.class.getSimpleName();

    private static final String SEPARATOR = ",";
    private static final String DELIMITER = "\r\n";

    private Context mContext;

    private boolean mFileCreated;
    private File mStorageDirAsFile;
    private String mStorageDirAsPath;
    private File mStorageFile;
    private String mStorageFilePath;
    private String mDataType;
    private String mFileName;

    private BufferedWriter mBufferedWriter;

    public DataSaver(Context context, String dataType, String storageDirPath) {

        mContext = context; // context, also Activity, die DataSaver aufruft, wird uebergeben
        mStorageDirAsPath = storageDirPath;
        mDataType = dataType; // Datenkategorie ohne .csv

        mFileName = mDataType + "_" + getTimeStamp() + ".csv";

        mFileCreated = createStorageFile();
        if (!mFileCreated) {
            Toast.makeText(mContext, "!!!storage file could not be created!!!", Toast.LENGTH_LONG).show();
            System.exit(1);
        }
        mStorageFilePath = mStorageFile.getAbsolutePath();
        Log.d(LOG_TAG, "created file (path): " + mStorageFilePath);

        prepareWriter();
    }

    private boolean createStorageFile() {
        boolean fileCreated = false;

        // external directory should be writable
        try {

            // create new file in directory
            mStorageFile = new File(mStorageDirAsPath + File.separator + mFileName);
            if (mStorageFile.exists()) {
                fileCreated = true;
            } else {
                fileCreated = mStorageFile.createNewFile();
            }
            if (!fileCreated) {
                fileCreated = mStorageFile.exists();
                if (!fileCreated) {
                    Log.d(LOG_TAG, "file could not be created!");
                    return false;
                }
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "exception, file or dir creation didn't worked properly!", e);
            mFileCreated = false;
        }
        return true;
    }

    private void prepareWriter() {
        String header;
        switch (mDataType) {
            case "location":
                header = "timestamp" + SEPARATOR + "latitude" + SEPARATOR + "longitude" + SEPARATOR + "accuracy" + DELIMITER;
                break;
            case "wifi":
                header = "timestamp" + SEPARATOR + "ssid" + SEPARATOR + "detailed state" + DELIMITER;
                break;
            case "steps":
                header = "start time" + SEPARATOR + "end time" + SEPARATOR + "number of steps" + DELIMITER;
                break;
            case "activity":
                header = "start time" + SEPARATOR + "end time" + SEPARATOR + "duration" + SEPARATOR + "activity" + DELIMITER;
                break;
            case "alarm_receiver":
                header = "query timestamp" + DELIMITER;
                break;
            default:
                header = "timestamp" + SEPARATOR + mDataType + DELIMITER;
        }

        FileWriter fw;
        if (mFileCreated) {
            try {
                fw = new FileWriter(mStorageFilePath);
                mBufferedWriter = new BufferedWriter(fw);
                mBufferedWriter.write(header);
                mBufferedWriter.flush();
//                mBufferedWriter.close();

//                Log.d(LOG_TAG, "header ('" + header + "') was written in: " + mFileName);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception in prepare Writer", e);
                mFileCreated = false;
            }
        }
    }

    public void writeData2csv(String data) {
        StringBuilder sb = new StringBuilder();

        sb.append(data);
        sb.append(DELIMITER);
        Log.d(LOG_TAG, data);

        try {

            mBufferedWriter.write(sb.toString());
            mBufferedWriter.flush();
//            mBufferedWriter.close();
            Log.d(LOG_TAG, "data written to .csv (" + mDataType + "): " + sb.toString());
        } catch (IOException e) {
            Log.d(LOG_TAG, "exception while writing data to .csv", e);
        }
    }

    public void closeWriter() {
        try {
            mBufferedWriter.flush();
            mBufferedWriter.close();
        } catch (IOException e) {
            Log.d(LOG_TAG, "exception closing buffered writer", e);
        }
    }

    public String getTimeStamp() {
        return DateFormat.format("dd-MM-yyyy HH:mm:ss", new Date()).toString();
    }
}