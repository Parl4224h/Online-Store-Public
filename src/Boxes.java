import com.google.gson.annotations.SerializedName;

public class Boxes {
    public enum Box{
        @SerializedName("0")
        SMALL,
        @SerializedName("1")
        MEDIUM,
        @SerializedName("2")
        LARGE
    }
}
