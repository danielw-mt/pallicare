package innolab.pallicare.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * database class that specifies "permission" table
 *
 * @author Ulla Sternemann
 */
@Entity(tableName = "permission"
        ,
//        indices = {@Index(value = "permission_id", unique = true)},
        foreignKeys = {
                @ForeignKey(entity = PermissionType.class, parentColumns = "permission_type_id", childColumns = "permission_write"),
                @ForeignKey(entity = PermissionType.class, parentColumns = "permission_type_id", childColumns = "permission_read_biometrical"),
                @ForeignKey(entity = PermissionType.class, parentColumns = "permission_type_id", childColumns = "permission_read_psychometrical"),
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "patient_id"),
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "requester_id")}
                )
public class Permission {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "permission_id")
    private int permissionId;

    @ColumnInfo(name = "permission_write")
    private int permissionWrite;

    @ColumnInfo(name = "permission_read_biometrical")
    private int permissionReadBiometrical;

    @ColumnInfo(name = "permission_read_psychometrical")
    private int permissionReadPsychometrical;

    @ColumnInfo(name = "patient_id")
    private int patientId;

    @ColumnInfo(name = "requester_id")
    private int requesterId;

    // TODO do i need a constructor (all foreign keys)? Klaus@Ulla Yeah, I think so.

    public Permission(int permissionWrite, int permissionReadBiometrical, int permissionReadPsychometrical, int patientId, int requesterId) {
        this.permissionWrite = permissionWrite;
        this.permissionReadBiometrical = permissionReadBiometrical;
        this.permissionReadPsychometrical = permissionReadPsychometrical;
        this.patientId = patientId;
        this.requesterId = requesterId;
    }

    public int getPermissionId() {
        return this.permissionId;
    }


    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param permissionId ignore
     */
    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public int getPermissionWrite() {
        return this.permissionWrite;
    }

    public void setPermissionWrite(int permissionWrite) {
        this.permissionWrite = permissionWrite;
    }

    public int getPermissionReadBiometrical() {
        return this.permissionReadBiometrical;
    }

    public void setPermissionReadBiometrical(int permissionReadBiometrical) {
        this.permissionReadBiometrical = permissionReadBiometrical;
    }

    public int getPermissionReadPsychometrical() {
        return this.permissionReadPsychometrical;
    }

    public void setPermissionReadPsychometrical(int permissionReadPsychometrical) {
        this.permissionReadPsychometrical = permissionReadPsychometrical;
    }

    public int getPatientId() {
        return this.patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getRequesterId() {
        return this.requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }
}
