package data.network;

import bean.ClientDetailsBean;
import bean.ClientListBean;
import bean.DepartmentListBean;
import bean.EmployeesListBean;
import bean.EnquiryCanListBean;
import bean.EnquiryViewBean;
import bean.InsertFeedbackBean;
import bean.LoginDataBean;
import bean.NewEnquiryFormBean;
import bean.ProductServiceBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login/login.php")
    Call<LoginDataBean> checkLogin(
            @Field("userName")String userName,
            @Field("password")String password
    );

    @FormUrlEncoded
    @POST("crm/enquiry_list.php")
    Call<EnquiryCanListBean> getenquiry_list(
            @Field("token")String token
    );

    @FormUrlEncoded
    @POST("crm/enquiry_view.php")
    Call<EnquiryViewBean> getenquiry_view(
            @Field("token")String token,
            @Field("enquiryId")String enquiryId
    );

    @FormUrlEncoded
    @POST("crm/get_client_list.php")
    Call<ClientListBean> getClientList(
            @Field("token")String token
    );

    @FormUrlEncoded
    @POST("crm/get_client_details.php")
    Call<ClientDetailsBean> getClientDetails(
            @Field("token")String token,
            @Field("clientId")String clientId
    );

    @FormUrlEncoded
    @POST("crm/get_department_list.php")
    Call<DepartmentListBean> getDepartmentList(
            @Field("token")String token
    );

    @FormUrlEncoded
    @POST("crm/get_employee_list.php")
    Call<EmployeesListBean> getEmployeeDetails(
            @Field("token")String token,
            @Field("departmentId")String departmentId
    );

    @FormUrlEncoded
    @POST("crm/get_product_services_list.php")
    Call<ProductServiceBean> getProductServicesList(
            @Field("token")String token,
            @Field("mappingId")String mappingId
    );

    @FormUrlEncoded
    @POST("crm/add_enquiry.php")
    Call<NewEnquiryFormBean> setNewEnquiryForm(
            @Field("token")String token,
            @Field("callType")String callType,
            @Field("date")String date,
            @Field("clientType")String clientType,
            @Field("companyName")String companyName,
            @Field("location")String location,
            @Field("address")String address,
            @Field("client")String client,
            @Field("designation")String designation,
            @Field("mobile")String mobile,
            @Field("email")String email,
            @Field("product")String product,
            @Field("feedback")String feedback,
            @Field("followUp")String followUp,
            @Field("department")String department,
            @Field("employeeId")String employeeId,
            @Field("createdBy")String createdBy
    );

    @FormUrlEncoded
    @POST("crm/insert_feedback.php")
    Call<InsertFeedbackBean> insertFeedback(
            @Field("token")String token,
            @Field("enquiryId")String enquiryId,
            @Field("feedBack")String feedBack,
            @Field("feedBackDate")String feedBackDate,
            @Field("createdBy")String createdBy
    );

}