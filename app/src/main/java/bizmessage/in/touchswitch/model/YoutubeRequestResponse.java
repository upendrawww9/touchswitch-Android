
package bizmessage.in.touchswitch.model;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.io.Serializable;
        import java.util.List;

public class YoutubeRequestResponse implements Serializable {

    @SerializedName("esps")
    @Expose
    private List<Object> esps = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    public List<Object> getEsps() {
        return esps;
    }

    public void setEsps(List<Object> esps) {
        this.esps = esps;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public class Esp implements Serializable{

        @SerializedName("id")
        @Expose
        private String desc;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}