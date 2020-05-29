package id.mydss.cores;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }


}
