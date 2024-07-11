package com.example.steam_tracker.SteamConnnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SteamApiServiceInterface {
    @GET("{dynamicPath}")
    Call<ResponseBody> getSteamItemImage(@Path("dynamicPath") String imageUrl);
}
