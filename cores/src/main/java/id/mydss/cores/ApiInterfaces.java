package id.mydss.cores;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterfaces {
    /** -------------------------- Presensi Lokasi -------------------------- **/
    @FormUrlEncoded
    @POST("pointing/data")
    Call<LokasiList> syncLokasi(@Field("sekolah_id") String sekolah_id);

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("auth")
    Call<User> Authenticate(@Body Account account);

  }
