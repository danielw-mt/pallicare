package innolab.pallicare.db.api;

import innolab.pallicare.db.api.entity.LoginData;
import innolab.pallicare.db.api.entity.RegisterData;
import innolab.pallicare.db.api.entity.ScaleMeasurementData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * API Handler to communicate with the server
 */
public interface JsonAPI {

    /**
     * Tries to authorize a user at the server.
     *
     * @param loginData the password and email of the user
     * @return a LoginData object with the token in the key variable
     */
    @POST("/api/v1/login/")
    Call<LoginData> authorizeUser(@Body LoginData loginData);

    /**
     * Tries to create a new user at the server.
     *
     * @param registerData TODO
     * @return TODO
     */
    @POST("/api/v1/registration/")
    Call<RegisterData> registerUser(@Body RegisterData registerData);

    /**
     * Uploads a new ScaleMeasurement to the server
     *
     * @param header    "token <token>"
     * @param weight
     * @param fat
     * @param muscle
     * @param water
     * @param timestamp
     * @param patient
     * @return ignored
     * TODO fix, to user measurement data object
     */
    @POST("/api/v1/MeasurementScale/")
    @FormUrlEncoded
    Call<ScaleMeasurementData> uploadMeasurementData(@Header("authorization") String header,
                                                     @Field("weight") float weight,
                                                     @Field("fat") float fat,
                                                     @Field("muscle") float muscle,
                                                     @Field("water") float water,
                                                     @Field("timestamp") String timestamp,
                                                     @Field("patient") int patient);
}

