package com.example.gamerate.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // para comunicarse con el 'localhost' de tu ordenador
    // Cambiamos tu IP de antes por el atajo del emulador
    private static final String BASE_URL = "http://35.153.36.108/api/";
    private static Retrofit retrofit = null;

    public static IApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Traduce JSON a objetos Java solo
                    .build();
        }
        return retrofit.create(IApiService.class);
    }
}