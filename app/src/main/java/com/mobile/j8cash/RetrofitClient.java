package com.mobile.j8cash;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.utils.Constants;
import com.pixplicity.easyprefs.library.Prefs;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    public static Retrofit getInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    String token = Prefs.getString(AccountManager.TOKEN, "");

                    Request.Builder builder = chain.request().newBuilder();
                    builder.addHeader("Accept", "application/json");
                    builder.addHeader("Authorization", "Bearer " + token);

                    Request request = builder.build();
                    Response response = chain.proceed(request);

//                    if(response.code() == 403 || response.code() == 401){
//                        Prefs.remove(AccountManager.TOKEN);
//                        Context cxt = KLAEatsApplication.getContext();
//                        Intent intent = new Intent(cxt, LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.putExtra("MainActivity", 202);
//                        cxt.startActivity(intent);
//                    }

                    return response;
                }).build();



        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

//        gsonBuilder.registerTypeAdapter(User.class, (JsonDeserializer<User>) (json, typeOfT, context) -> {
//            JsonObject j = json.getAsJsonObject();
//            Gson g = new Gson();
//            User u = g.fromJson(json, User.class);
//            u.setId(j.get("id").getAsLong());
//            return u;
//        });

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(client)
                .build();
    }

    public static Retrofit getRaveAPI() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {

                    Request.Builder builder = chain.request().newBuilder();
                    builder.addHeader("Accept", "application/json");
                    builder.addHeader("Authorization", "Bearer " + Constants.RAVE_SEC_KEY);

                    Request request = builder.build();
                    Response response = chain.proceed(request);

//                    if(response.code() == 403 || response.code() == 401){
//                        Prefs.remove(AccountManager.TOKEN);
//                        Context cxt = KLAEatsApplication.getContext();
//                        Intent intent = new Intent(cxt, LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.putExtra("MainActivity", 202);
//                        cxt.startActivity(intent);
//                    }

                    return response;
                }).build();



        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);


        return new Retrofit.Builder()
                .baseUrl(Constants.RAVE_WITHDRAW_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(client)
                .build();
    }

}
