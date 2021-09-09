package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductServiceBean {
    private String status,status_message;
    @SerializedName("results")
    @Expose
    private List<PS_results> ps_resultsList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public List<PS_results> getPs_resultsList() {
        return ps_resultsList;
    }

    public void setPs_resultsList(List<PS_results> ps_resultsList) {
        this.ps_resultsList = ps_resultsList;
    }

    public class PS_results{
        private String productServicesId,name;

        public String getProductServicesId() {
            return productServicesId;
        }

        public void setProductServicesId(String productServicesId) {
            this.productServicesId = productServicesId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
