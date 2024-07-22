package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * database class that specifies "permission_type" table
 *
 * @author Ulla Sternemann
 */
@Entity(tableName = "permission_type"
//        , indices = {@Index(value = "permission_type_id", unique = true)}
        )
public class PermissionType {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "permission_type_id")
    private int permissionTypeId;

    @NonNull
    @ColumnInfo(name = "permission_type_description")
    private String permissionTypeDescription;

    public PermissionType(@NonNull String permissionTypeDescription) {
        this.permissionTypeDescription = permissionTypeDescription;
    }

    public int getPermissionTypeId() {
        return this.permissionTypeId;
    }

    public void setPermissionTypeId(int permissionTypeId) {
        this.permissionTypeId = permissionTypeId;
    }

    @NonNull
    public String getPermissionTypeDescription() {
        return this.permissionTypeDescription;
    }

    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param permissionTypeDescription ignore
     */
    public void setPermissionTypeDescription(@NonNull String permissionTypeDescription) {
        this.permissionTypeDescription = permissionTypeDescription;
    }
}
