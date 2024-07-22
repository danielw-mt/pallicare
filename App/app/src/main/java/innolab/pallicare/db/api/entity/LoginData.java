package innolab.pallicare.db.api.entity;

/**
 * Used by the API to authorize a user at the server and receive the token
 */
public class LoginData {

    /**
     * The email of the user to login
     */
    private String email;

    /**
     * The password of the user
     */
    private String password;

    /**
     * This key isn't set for the request, but returns the user token
     */
    private String key;

    /**
     * This key isn't set for the request, but returns the user id at the server
     */
    private int user;

    /**
     * Default constructor
     *
     * @param email The email of the user to login
     * @param pwd   The password of the user
     */
    public LoginData(String email, String pwd) {
        this.email = email;
        this.password = pwd;
    }

    public int getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {
        return key;
    }

    public String getPwd() {
        return password;
    }

}
