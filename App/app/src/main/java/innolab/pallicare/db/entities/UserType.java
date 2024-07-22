package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * database class that specifies "user_type" table
 *
 * @author Ulla Sternemann
 */
@Entity(tableName = "user_type"
//        , indices = {@Index(value = "user_type_id", unique = true)}
)
public class UserType {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_type_id")
    private int userTypeId;

    @NonNull
    @ColumnInfo(name = "user_type_description")
    private String userTypeDescription;

    public UserType(@NonNull String userTypeDescription) {
        this.userTypeDescription = userTypeDescription;
    }

    public int getUserTypeId() {
        return this.userTypeId;
    }

    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param userTypeId ignore
     */
    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    @NonNull
    public String getUserTypeDescription() {
        return this.userTypeDescription;
    }

    public void setUserTypeDescription(@NonNull String userTypeDescription) {
        this.userTypeDescription = userTypeDescription;
    }
}
