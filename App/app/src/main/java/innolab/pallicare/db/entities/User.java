package innolab.pallicare.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * database class that specifies "user" table
 *
 * @author Ulla Sternemann
 */

@Entity(tableName = "user"
        ,
//        indices = {@Index(value = "user_id", unique = true), @Index(value = "mail", unique = true)},
        foreignKeys = @ForeignKey(entity = UserType.class,
                parentColumns = "user_type_id",
                childColumns = "user_type_id",
                onDelete = ForeignKey.CASCADE)
                )
// TODO cascade? or no action, see also other classes)
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int userId;

    @NonNull
    private String surname;

    @NonNull
    private String name;

    @NonNull
    private String mail;

    private int gender;

    @ColumnInfo(name = "date_of_birth")
    private Date dateOfBirth;

    @NonNull
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @NonNull
    @ColumnInfo(name = "created_date")
    private Date createdDate;

    @ColumnInfo(name = "user_type_id")
    private int userTypeId;

    public User(@NonNull String surname, @NonNull String name, @NonNull String mail, int gender,
                @NonNull Date dateOfBirth, @NonNull String phoneNumber, @NonNull Date createdDate, int userTypeId) {
        this.surname = surname;
        this.name = name;
        this.mail = mail;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate; // TODO default current date
        this.userTypeId = userTypeId;
    }

    public int getUserId() {
        return this.userId;
    }

    /**
     * Only needed for autogeneration of primary key. Don't use!
     *
     * @param userId ignore
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @NonNull
    public String getSurname() {
        return this.surname;
    }

    public void setSurname(@NonNull String surname) {
        this.surname = surname;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getMail() {
        return this.mail;
    }

    public void setMail(@NonNull String username) {
        this.mail = username;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @NonNull
    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(@NonNull Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @NonNull
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(@NonNull Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getUserTypeId() {
        return this.userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }
}
