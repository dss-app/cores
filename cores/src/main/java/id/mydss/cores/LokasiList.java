package id.mydss.cores;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
public class LokasiList {

    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<LokasiDetail> lokasiLists;

    public LokasiList(String message, ArrayList<LokasiDetail> lokasiLists) {
        this.message = message;
        this.lokasiLists = lokasiLists;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<LokasiDetail> getLokasiLists() {
        return lokasiLists;
    }

    public void setLokasiLists(ArrayList<LokasiDetail> lokasiLists) {
        this.lokasiLists = lokasiLists;
    }
}
