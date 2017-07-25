package com.example.taye.ProjectALC.network;

import com.example.taye.ProjectALC.model.ItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by TAYE on 21/07/2017.
 */

public interface APIService {
    @GET("search/users?q=language:java+location:lagos")
    Call<ItemResponse> getItems();
}

