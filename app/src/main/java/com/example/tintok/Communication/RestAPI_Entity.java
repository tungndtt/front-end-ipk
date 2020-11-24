package com.example.tintok.Communication;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class RestAPI_Entity {
    public interface LoginService{
        @Headers({
                "Accept: application/json",
                "Content-type:text/plain",
                "User-Agent: mApp"
        })
        @POST("/login")
        Call<StatusResponseEntity> login(@Body String data);
    }


    public interface SignUpService{
        @Headers({
                "Accept: application/plaintext",
                "Content-type:text/plain",
                "User-Agent: mApp"
        })
        @POST("/sign_up")
        Call<StatusResponseEntity> sign_up(@Body String data);
    }

    public interface GetKeyService{
        @Headers({
                "Accept: application/plaintext",
                "Content-type:text/plain",
                "User-Agent: mApp"
        })
        @POST("/PublicKeyRequest")
        Call<KeyResponseEntiy> requestKey(@Body String data);
    }

    public interface RestApiListener{
        public void onSuccess(AbstractResponseEntity response);
        public void onFailure();
    }




    public class StatusResponseEntity extends AbstractResponseEntity{
        @SerializedName("status")
        public boolean status;
        @SerializedName("token")
        public String mToken;
        @SerializedName("reason")
        public String reason;
    }

    public class KeyResponseEntiy extends AbstractResponseEntity{
        @SerializedName("serverKey")
        public  String serverKey;
    }
    public abstract class AbstractResponseEntity{

    }
}
