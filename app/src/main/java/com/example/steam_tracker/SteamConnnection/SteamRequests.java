package com.example.steam_tracker.SteamConnnection;

import com.example.steam_tracker.Items.Item;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SteamRequests {

    public void sendImageRequest(Item observedItem)
    {
        SteamRequests retrofitConfigController = new SteamRequests();
        SteamApiResponse responseController = new SteamApiResponse();
        SteamApiServiceInterface apiCfg = retrofitConfigController.getDefaultRetrofitConfig().create(SteamApiServiceInterface.class);
        responseController.sendApiRequest(apiCfg.getSteamItemImage(observedItem.getImageUrl()),observedItem);
    }

    public static Retrofit getDefaultRetrofitConfig()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://community.akamai.steamstatic.com/economy/image/") // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
