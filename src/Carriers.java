import com.google.gson.annotations.SerializedName;

public class Carriers {
    public enum Carrier{
        @SerializedName("0")
        USPS,
        @SerializedName("1")
        UPS,
        @SerializedName("2")
        FEDEX
    }
}
