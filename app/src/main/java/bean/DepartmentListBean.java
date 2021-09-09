package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentListBean {
    private String status_message,status;
    @SerializedName("departments")
    @Expose
    private List<DL_Departments> dl_departmentsList;

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

    public List<DL_Departments> getDl_departmentsList() {
        return dl_departmentsList;
    }

    public void setDl_departmentsList(List<DL_Departments> dl_departmentsList) {
        this.dl_departmentsList = dl_departmentsList;
    }

    public class DL_Departments{
        private String departmentId,deptName;

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }
    }
}
