package id.mydss.cores;
import com.google.gson.annotations.SerializedName;
public class LokasiDetail {
    @SerializedName("id")
    private int id;
    @SerializedName("nama_lokasi")
    private String nama_lokasi;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public LokasiDetail(int id, String nama_lokasi, String latitude, String longitude) {
        this.id = id;
        this.nama_lokasi = nama_lokasi;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_lokasi() {
        return nama_lokasi;
    }

    public void setNama_lokasi(String nama_lokasi) {
        this.nama_lokasi = nama_lokasi;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
