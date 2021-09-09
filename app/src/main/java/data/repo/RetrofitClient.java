package data.repo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import data.network.ApiInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient retrofitClient;
//    private static final String BASE_URL = "http://115.243.95.118:8081/hrms/hrms_android/";
//    private static final String BASE_URL = "http://192.168.83.1:8080//qvision/qvision_android/";
    private static final String BASE_URL = "http://192.168.201.73/qvision/qvision_android/";
    private Retrofit retrofit;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private RetrofitClient(){
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

    }

    public static synchronized RetrofitClient getInstance(){
        if(retrofitClient==null){
            retrofitClient=new RetrofitClient();
        }
        return retrofitClient;
    }

    public ApiInterface getApi(){
        return retrofit.create(ApiInterface.class);
    }

}