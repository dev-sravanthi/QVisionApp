package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EnquiryCanListBean {
    private String status,status_message;
    @SerializedName("results")
    @Expose
    private List<EnqListResults> enqListResultsList;

    public List<EnqListResults> getEnqListResultsList() {
        return enqListResultsList;
    }

    public void setEnqListResultsList(List<EnqListResults> enqListResultsList) {
        this.enqListResultsList = enqListResultsList;
    }

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

    public class EnqListResults{
        private String enquiryId,callType,date,client,location,contactNumber,followUpDate,employee,status,isFeedbackEnabled;

        public String getIsFeedbackEnabled() {
            return isFeedbackEnabled;
        }

        public void setIsFeedbackEnabled(String isFeedbackEnabled) {
            this.isFeedbackEnabled = isFeedbackEnabled;
        }

        public String getEnquiryId() {
            return enquiryId;
        }

        public void setEnquiryId(String enquiryId) {
            this.enquiryId = enquiryId;
        }

        public String getCallType() {
            return callType;
        }

        public void setCallType(String callType) {
            this.callType = callType;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getFollowUpDate() {
            return followUpDate;
        }

        public void setFollowUpDate(String followUpDate) {
            this.followUpDate = followUpDate;
        }

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
