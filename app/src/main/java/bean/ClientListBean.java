package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientListBean {
    private String status_message,status;
    @SerializedName("clientNames")
    @Expose
    private List<CL_ClientNames> cl_clientNamesList;

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CL_ClientNames> getCl_clientNamesList() {
        return cl_clientNamesList;
    }

    public void setCl_clientNamesList(List<CL_ClientNames> cl_clientNamesList) {
        this.cl_clientNamesList = cl_clientNamesList;
    }

    public class CL_ClientNames{
        private String clientId,companyName;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }
    }
}
