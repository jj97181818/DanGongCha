package me.jj97181818.dangongcha.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetRequest {
    @GET("/v1/info/{city}?ver=3")
    Call<Infos> getInfo(@Path("city") String city);

    @GET("/v1/time/{city}/{route}?ver=3")
    Call<Times> getTime(@Path("city") String city, @Path("route") String route);
}
