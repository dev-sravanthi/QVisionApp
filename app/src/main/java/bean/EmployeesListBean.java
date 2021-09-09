package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeesListBean {
    private String status_message,status;
    @SerializedName("results")
    @Expose
    private List<EL_EmployeeList> el_employeeListList;

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

    public List<EL_EmployeeList> getEl_employeeListList() {
        return el_employeeListList;
    }

    public void setEl_employeeListList(List<EL_EmployeeList> el_employeeListList) {
        this.el_employeeListList = el_employeeListList;
    }

    public class EL_EmployeeList{
        private String employeeId,empName;

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }
    }
}
