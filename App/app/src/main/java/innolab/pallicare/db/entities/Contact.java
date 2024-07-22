package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * database class that specifies "contact" table
 *
 * @author Klaus Schmidt
 */

@Entity(tableName = "contact")

public class Contact {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contact_id")
    private int contactID;

    @NonNull
    private String name;

    @NonNull
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    private String photo;

    public Contact(@NonNull String name, @NonNull String phoneNumber, String photo) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getContactID() {
        return this.contactID;
    }

    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param contactID ignore
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
