package me.jj97181818.dangongcha;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetRequest {
    @GET("/v1/info/{city}?ver=3")
    Call<Infos> getCall (@Path("city") String city);
}
