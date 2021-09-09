package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EnquiryViewBean {
    private String status,status_message;
    @SerializedName("enquiryDetails")
    @Expose
    private EnqFormEnqyDetails EnqFormEnqyDetails;
    @SerializedName("feedbackEntryDetails")
    @Expose
    private List<EnqFormfeedbackEntryDetails> EnqFormfeedbackEntryDetailsList;

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

    public EnquiryViewBean.EnqFormEnqyDetails getEnqFormEnqyDetails() {
        return EnqFormEnqyDetails;
    }

    public void setEnqFormEnqyDetails(EnquiryViewBean.EnqFormEnqyDetails enqFormEnqyDetails) {
        EnqFormEnqyDetails = enqFormEnqyDetails;
    }

    public List<EnqFormfeedbackEntryDetails> getEnqFormfeedbackEntryDetailsList() {
        return EnqFormfeedbackEntryDetailsList;
    }

    public void setEnqFormfeedbackEntryDetailsList(List<EnqFormfeedbackEntryDetails> enqFormfeedbackEntryDetailsList) {
        EnqFormfeedbackEntryDetailsList = enqFormfeedbackEntryDetailsList;
    }

    public class EnqFormEnqyDetails{
        private String callType,date,clientType,companyName,location,address,client,mobile,designation,mail,product,
                feedback,followUp,department,employee;

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

        public String getClientType() {
            return clientType;
        }

        public void setClientType(String clientType) {
            this.clientType = clientType;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getFollowUp() {
            return followUp;
        }

        public void setFollowUp(String followUp) {
            this.followUp = followUp;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }
    }

    public class EnqFormfeedbackEntryDetails{
        private String feedBack,feedbackDate;

        public String getFeedBack() {
            return feedBack;
        }

        public void setFeedBack(String feedBack) {
            this.feedBack = feedBack;
        }

        public String getFeedbackDate() {
            return feedbackDate;
        }

        public void setFeedbackDate(String feedbackDate) {
            this.feedbackDate = feedbackDate;
        }
    }
}
