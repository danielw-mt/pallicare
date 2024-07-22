package innolab.pallicare.db.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton to get access to the API
 */
public class RetrofitInstance {

    /**
     * The Base URL of our server
     */
    private static final String BASE_URL = "https://702902f2.ngrok.io";

    /**
     * The indirect singleton object for the JsonAPI
     */
    private static JsonAPI jsonAPI;

    /**
     * The singleton instance
     */
    private static RetrofitInstance instance;

    /**
     * Verhindere die Erzeugung des Objektes über andere Methoden
     */
    private RetrofitInstance() {
    }

    /**
     * Create the singleton and return the jsonAPI object
     *
     * @return jsonAPI object
     */
    public static JsonAPI getInstance() {

        if (RetrofitInstance.instance == null) {
            RetrofitInstance.instance = new RetrofitInstance();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            jsonAPI = retrofit.create(JsonAPI.class);
        }
        return jsonAPI;
    }


}
