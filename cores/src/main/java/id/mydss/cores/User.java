package id.mydss.cores;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User {
    @SerializedName("ID")
    public String ID;
    @SerializedName("username")
    public String username;
    @SerializedName("kode_role")
    public String kode_role;
    @SerializedName("nama_role")
    public String nama_role;
    @SerializedName("modul_installed")
    public String modul_installed;
    @SerializedName("identitas")
    public String identitas;
    @SerializedName("AccessToken")
    public String AccessToken;
    @SerializedName("RefreshToken")
    public String RefreshToken;
    @SerializedName("AccessUUID")
    public String AccessUUID;
    @SerializedName("RefreshUUID")
    public String RefreshUUID;
    @SerializedName("AtExpires")
    public String AtExpires;
    @SerializedName("RtExpires")
    public String RtExpires;

    public User(String ID,
                String username,
                String kode_role,
                String nama_role,
                String modul_installed,
                String identitas,
                String AccessToken,
                String RefreshToken,
                String AccessUUID,
                String RefreshUUID,
                String AtExpires,
                String RtExpires) {
        this.ID = ID;
        this.username = username;
        this.kode_role = kode_role;
        this.nama_role = nama_role;
        this.modul_installed = modul_installed;
        this.identitas = identitas;
        this.AccessToken = AccessToken;
        this.RefreshToken = RefreshToken;
        this.AccessUUID = AccessUUID;
        this.RefreshUUID = RefreshUUID;
        this.AtExpires = AtExpires;
        this.RtExpires = RtExpires;
    }
}
