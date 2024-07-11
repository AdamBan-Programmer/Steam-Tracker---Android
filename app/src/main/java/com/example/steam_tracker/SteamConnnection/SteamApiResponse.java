package com.example.steam_tracker.SteamConnnection;

import com.example.steam_tracker.Items.Item;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SteamApiResponse {

    private static ApiQueueStatusEnum queueStatus = ApiQueueStatusEnum.FREE;

    // Sends server request
    public void sendApiRequest(Call<ResponseBody> call, Item observedItem) {
        queueStatus = ApiQueueStatusEnum.IN_PROGRESS;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { //response
                try {
                    queueStatus = ApiQueueStatusEnum.FREE;
                    if(response.isSuccessful()) {
                        byte[] imageData = response.body().bytes();
                        observedItem.setImageBytes(imageData);
                    }
                    else
                    {
                        call.cancel();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                queueStatus = ApiQueueStatusEnum.FREE;
                call.cancel();
            }
        });
    }
}
