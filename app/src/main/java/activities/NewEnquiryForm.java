package activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.quadsel.qvisionapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.ClientDetailsBean;
import bean.ClientListBean;
import bean.DepartmentListBean;
import bean.EmployeesListBean;
import bean.NewEnquiryFormBean;
import bean.ProductServiceBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class NewEnquiryForm extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    EditText ed_client,ed_address,ed_city,ed_clientname,ed_designation,ed_contactno,ed_emailid,ed_service,ed_feedback,ed_followupdate,ed_account_manager_dept,
            ed_account_manager,ed_dp_date,ed_dp_followupdate,ed_solutions;
    Spinner spin_clienttype,spin_calltype,spin_client,spin_assignto_department,spin_assignto_employee,spin_productservice,spin_sub_prodserv;
    LinearLayout ll_acc_manager_dept,ll_acc_manager,ll_spin_assignto_employee,ll_spin_assignto_department;
    String token,login_id,status,status_message,clientId,companyName,departmentId,deptName,employeeId,empName,
           address,city,clientName,designation,mobileNo,email,cv_deptName,cv_empName,cv_status,cv_status_message,st_spin_clienttype,st_spin_calltype,selected_clienttype,
           selected_calltype,createdBy,new_createdby,st_spinclient,st_ed_dp_date,st_ed_address,st_ed_city,st_ed_clientname,st_ed_designation,st_ed_contactno,st_ed_emailid,
           st_ed_service,st_ed_feedback,st_ed_dp_followupdate,st_spin_assignto_department,st_spin_assignto_employee,st_spin_productservice,st_spin_productservice_id,
           productServicesId,prodserviceName,st_spin_sub_prodserv,st_spin_sub_prodserv_id,st_ed_solutions;
    LinearLayout ll_spin_sub_prodserv,ll_ed_solutions;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    ArrayList<String> list_clientid,list_clientname,list_deptid,list_deptname,list_empid,list_empname,list_prodserv_id,list_prodserv_name;
    boolean networkAvailability=false;
    String department="",selected_dept_code="",selected_employee_code="",selected_client_code="", employee="",dept_code="",client="", prodsericd="",selected_prodserv_code="";
    int spin_dept_pstn=0,spin_emp_pstn=0,spin_client_pstn=0,spin_prodserv_pstn=0;
    private ArrayAdapter<CharSequence> adapter;
    Button btn_submit_nef,btn_dp_date,btn_dp_followupdate;
    ArrayList<String> listsub_ids;
    static final int DATE_DIALOG_ID = 1;
    protected static final String Selected_id = null;
    protected static final String Selected_schema = null;
    private int year = 0,month,day,myear,mmonth,mday;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_enquiry_form);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        login_id=i.getStringExtra("login_id");

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(NewEnquiryForm.this);

        if(networkAvailability==true){
            findviewbyids();
        }else{
            Utility.getAlertNetNotConneccted(NewEnquiryForm.this, "Internet Connection");
        }

        loadJSON_getClientList();
     //   loadJSON_getClientView();
        loadJSON_getDepartmentList();
      //  loadJSON_getEmployeeList();
    }

    private void findviewbyids(){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);

        list_clientid=new ArrayList<>();
        list_clientname=new ArrayList<>();
        list_deptid=new ArrayList<>();
        list_deptname=new ArrayList<>();
        list_empid=new ArrayList<>();
        list_empname=new ArrayList<>();
        list_prodserv_id=new ArrayList<>();
        list_prodserv_name=new ArrayList<>();

        list_clientname.add("-Select-");
        list_deptname.add("-Select-");
        list_empname.add("-Select-");
        list_prodserv_name.add("-Select-");

        spin_calltype=findViewById(R.id.spin_calltype);
        spin_calltype.setOnItemSelectedListener(this);

        spin_client=findViewById(R.id.spin_client);
        spin_client.setOnItemSelectedListener(this);

        spin_assignto_department=findViewById(R.id.spin_assignto_department);
        spin_assignto_department.setOnItemSelectedListener(this);

        spin_clienttype=findViewById(R.id.spin_clienttype);
        spin_clienttype.setOnItemSelectedListener(this);

        spin_assignto_employee=findViewById(R.id.spin_assignto_employee);
        spin_assignto_employee.setOnItemSelectedListener(this);

        spin_productservice=findViewById(R.id.spin_productservice);
        spin_productservice.setOnItemSelectedListener(this);

        ll_acc_manager=findViewById(R.id.ll_acc_manager);
        ll_acc_manager_dept=findViewById(R.id.ll_acc_manager_dept);
        ll_spin_assignto_department=findViewById(R.id.ll_spin_assignto_department);
        ll_spin_assignto_employee=findViewById(R.id.ll_spin_assignto_employee);
        ll_spin_sub_prodserv=findViewById(R.id.ll_spin_sub_prodserv);
        ll_ed_solutions=findViewById(R.id.ll_ed_solutions);

        adapter = ArrayAdapter.createFromResource(NewEnquiryForm.this,
                R.array.clienttype, R.layout.spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_clienttype.setAdapter(adapter);

        spin_clienttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                st_spin_clienttype = spin_clienttype.getSelectedItem().toString();
                if(st_spin_clienttype.equals("Existing")){
                    ll_acc_manager_dept.setVisibility(View.VISIBLE);
                    ll_acc_manager.setVisibility(View.VISIBLE);
                    ll_spin_assignto_employee.setVisibility(View.GONE);
                    ll_spin_assignto_department.setVisibility(View.GONE);
                    selected_clienttype="1";
                }else if(st_spin_clienttype.equals("New")){
                    ll_spin_assignto_department.setVisibility(View.VISIBLE);
                    ll_spin_assignto_employee.setVisibility(View.VISIBLE);
                    ll_acc_manager_dept.setVisibility(View.GONE);
                    ll_acc_manager.setVisibility(View.GONE);
                    selected_clienttype="2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                st_spin_calltype = spin_calltype.getSelectedItem().toString();
                if(st_spin_calltype.equals("Incoming")){
                    selected_calltype="1";
                }else if(st_spin_calltype.equals("Outgoing")){
                    selected_calltype="2";
                }else if(st_spin_calltype.equals("Direct")){
                    selected_calltype="3";
                }
            }
        });

        adapter = ArrayAdapter.createFromResource(NewEnquiryForm.this,
                R.array.calltype, R.layout.spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_calltype.setAdapter(adapter);

        spin_calltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        adapter = ArrayAdapter.createFromResource(NewEnquiryForm.this,
                R.array.product_service, R.layout.spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_productservice.setAdapter(adapter);

        spin_productservice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                st_spin_productservice=spin_productservice.getSelectedItem().toString();
                if(st_spin_productservice.equals("Product")){
                    st_spin_productservice_id="1";
                    ll_spin_sub_prodserv.setVisibility(View.VISIBLE);
                    ll_ed_solutions.setVisibility(View.GONE);
                    loadJSON_get_product_services_list();
                }else if(st_spin_productservice.equals("Services")){
                    st_spin_productservice_id="2";
                    ll_spin_sub_prodserv.setVisibility(View.VISIBLE);
                    ll_ed_solutions.setVisibility(View.GONE);
                    loadJSON_get_product_services_list();
                }else if(st_spin_productservice.equals("Solutions")){
//                    st_spin_productservice_id="3";
                    ll_spin_sub_prodserv.setVisibility(View.GONE);
                    ll_ed_solutions.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ed_address=findViewById(R.id.ed_address);
        ed_city=findViewById(R.id.ed_city);
        ed_clientname=findViewById(R.id.ed_clientname);
        ed_designation=findViewById(R.id.ed_designation);
        ed_contactno=findViewById(R.id.ed_contactno);
        ed_emailid=findViewById(R.id.ed_emailid);
        ed_service=findViewById(R.id.ed_service);
        ed_feedback=findViewById(R.id.ed_feedback);
        ed_followupdate=findViewById(R.id.ed_followupdate);
        ed_account_manager_dept=findViewById(R.id.ed_account_manager_dept);
        ed_account_manager=findViewById(R.id.ed_account_manager);
        ed_dp_followupdate=findViewById(R.id.ed_dp_followupdate);
        ed_dp_date=findViewById(R.id.ed_dp_date);
        showDate(year, month+1, day);
        showFollowUpDate(year,month+1,day);

        btn_submit_nef=findViewById(R.id.btn_submit_nef);
        btn_submit_nef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st_spin_clienttype=spin_clienttype.getSelectedItem().toString().trim();
                if (spin_clienttype.getSelectedItem().equals("-Select-")){
                    Utility.getAlertMsgSelect(NewEnquiryForm.this,"Client Type");
                    return;
                }

                if(selected_clienttype.equals("1")){
                    new_createdby=createdBy;
                }else if(selected_clienttype.equals("2")){
                    new_createdby=selected_employee_code;
                }

                st_spin_calltype=spin_calltype.getSelectedItem().toString().trim();
                if (spin_calltype.getSelectedItem().equals("-Select-")){
                    Utility.getAlertMsgSelect(NewEnquiryForm.this,"Call Type");
                    return;
                }

                st_spin_calltype = spin_calltype.getSelectedItem().toString();
                if(st_spin_calltype.equals("Incoming")){
                    selected_calltype="1";
                }else if(st_spin_calltype.equals("Outgoing")){
                    selected_calltype="2";
                }else if(st_spin_calltype.equals("Direct")){
                    selected_calltype="3";
                }

                st_ed_dp_date=ed_dp_date.getText().toString().trim();
                if (st_spin_calltype.length()==0){
                    Utility.getAlertMsgSelect(NewEnquiryForm.this,"Date");
                    return;
                }

                st_spinclient=spin_client.getSelectedItem().toString().trim();
                if (spin_client.getSelectedItem().equals("-Select-")){
                    Utility.getAlertMsgSelect(NewEnquiryForm.this,"Client");
                    return;
                }

                st_ed_address=ed_address.getText().toString().trim();
                if (st_ed_address.length()==0){
                    Utility.getAlertMsgEnter(NewEnquiryForm.this,"Address");
                    return;
                }

                st_ed_city=ed_city.getText().toString().trim();
                if (st_ed_city.length()==0){
                    Utility.getAlertMsgEnter(NewEnquiryForm.this,"City");
                    return;
                }

                st_ed_clientname=ed_clientname.getText().toString().trim();
                if (st_ed_clientname.length()==0){
                    Utility.getAlertMsgEnter(NewEnquiryForm.this,"Client Name");
                    return;
                }

                st_ed_designation=ed_designation.getText().toString().trim();
                if (st_ed_designation.length()==0){
                    Utility.getAlertMsgEnter(NewEnquiryForm.this,"Designation");
                    return;
                }

                st_ed_contactno=ed_contactno.getText().toString().trim();
                if (st_ed_contactno.length()==0){
                    Utility.getAlertMsgEnter(NewEnquiryForm.this,"Contact No");
                    return;
                }

                st_ed_emailid=ed_emailid.getText().toString().trim();
                if (st_ed_emailid.length()==0){
                    Utility.getAlertMsgEnter(NewEnquiryForm.this,"Email ID");
                    return;
                }

                st_ed_service=ed_service.getText().toString().trim();
                if (st_ed_service.length()==0){
                    Utility.getAlertMsgEnter(NewEnquiryForm.this,"Service");
                    return;
                }

                st_ed_feedback=ed_feedback.getText().toString().trim();
                if (st_ed_feedback.length()==0){
                    Utility.getAlertMsgEnter(NewEnquiryForm.this,"Feedback");
                    return;
                }

                st_ed_dp_followupdate=ed_dp_followupdate.getText().toString().trim();
                if (st_ed_dp_followupdate.length()==0){
                    Utility.getAlertMsgSelect(NewEnquiryForm.this,"Followup Date");
                    return;
                }

                if(selected_clienttype.equals("2")){
                    st_spin_assignto_department=spin_assignto_department.getSelectedItem().toString().trim();
                    if (spin_assignto_department.getSelectedItem().equals("-Select-")){
                        Utility.getAlertMsgSelect(NewEnquiryForm.this,"Department");
                        return;
                    }

                    st_spin_assignto_employee=spin_assignto_employee.getSelectedItem().toString().trim();
                    if (spin_assignto_employee.getSelectedItem().equals("-Select-")){
                        Utility.getAlertMsgSelect(NewEnquiryForm.this,"Employee");
                        return;
                    }
                }

                Toast.makeText(NewEnquiryForm.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });

        btn_dp_date=findViewById(R.id.btn_dp_date);
        btn_dp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        ed_dp_date.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private void showFollowUpDate(int year, int month, int day) {
        ed_dp_followupdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private void setNewEnquiryForm() {
        showBar();

        Call<NewEnquiryFormBean> call= RetrofitClient.getInstance().getApi().setNewEnquiryForm(token,
                selected_calltype,ed_dp_date.getText().toString(),selected_clienttype,client,city,address,clientName,designation,mobileNo,email,ed_service.getText().toString(),
                ed_feedback.getText().toString(),ed_dp_followupdate.getText().toString(),selected_dept_code,selected_employee_code,new_createdby);
        call.enqueue(new Callback<NewEnquiryFormBean>() {
            @Override
            public void onResponse(Call<NewEnquiryFormBean> call, Response<NewEnquiryFormBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    NewEnquiryFormBean crmGetClientList=response.body();

                    status=crmGetClientList.getStatus();

                    if(status.equals("true")){
                        Toast.makeText(NewEnquiryForm.this, "Submitted successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        status_message=crmGetClientList.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<NewEnquiryFormBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }


    private void loadJSON_getClientList() {
        showBar();

        Call<ClientListBean> call= RetrofitClient.getInstance().getApi().getClientList("68d83c7772aebc5c306a2aafcb38e891");
        call.enqueue(new Callback<ClientListBean>() {

            @Override
            public void onResponse(Call<ClientListBean> call, Response<ClientListBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    ClientListBean clientListBean=response.body();

                    status=clientListBean.getStatus();

                    if(status.equals("true")){
                        List<ClientListBean.CL_ClientNames> clientNamesList=clientListBean.getCl_clientNamesList();
                        for(int i=0;i<clientNamesList.size();i++){
                            clientId=clientNamesList.get(i).getClientId();
                            companyName=clientNamesList.get(i).getCompanyName();

                            list_clientid.add(clientId);
                            list_clientname.add(companyName);
                        }

                        ArrayAdapter<String> adapter_spin_client = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_text,list_clientname);
                        spin_client.setAdapter(adapter_spin_client);


                    }else{
                        status_message=clientListBean.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }


                }else{
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ClientListBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }

    private void loadJSON_getClientView() {
        showBar();
//        pending check
        Call<ClientDetailsBean> call= RetrofitClient.getInstance().getApi().getClientDetails("68d83c7772aebc5c306a2aafcb38e891",selected_client_code);
        call.enqueue(new Callback<ClientDetailsBean>() {

            @Override
            public void onResponse(Call<ClientDetailsBean> call, Response<ClientDetailsBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    ClientDetailsBean clientDetailsBean=response.body();

                    cv_status=clientDetailsBean.getStatus();

                    if(cv_status.equals("true")){

                        ClientDetailsBean.CD_ClientDetails cd_clientDetails=clientDetailsBean.getCd_clientDetails();
                        address=cd_clientDetails.getAddress();
                        city=cd_clientDetails.getCity();
                        clientName=cd_clientDetails.getClientName();
                        designation=cd_clientDetails.getDesignation();
                        mobileNo=cd_clientDetails.getMobileNo();
                        email=cd_clientDetails.getEmail();
                        deptName=cd_clientDetails.getDeptName();
                        empName=cd_clientDetails.getEmpName();
                        createdBy=cd_clientDetails.getCreatedBy();

                        if(st_spin_clienttype.equals("Existing")){
                            ed_address.setText(address);
                            ed_city.setText(city);
                            ed_clientname.setText(clientName);
                            ed_designation.setText(designation);
                            ed_contactno.setText(mobileNo);
                            ed_emailid.setText(email);
                            ed_account_manager_dept.setText(deptName);
                            ed_account_manager.setText(empName);
                            ed_account_manager_dept.setEnabled(false);
                            ed_account_manager.setEnabled(false);
                            ed_account_manager_dept.setBackgroundResource(R.drawable.edittext_rounded_bg_grey);
                            ed_account_manager.setBackgroundResource(R.drawable.edittext_rounded_bg_grey);
                        }else{
                            ed_address.setText("");
                            ed_city.setText("");
                            ed_clientname.setText("");
                            ed_designation.setText("");
                            ed_contactno.setText("");
                            ed_emailid.setText("");
                            ed_account_manager_dept.setText("");
                            ed_account_manager.setText("");
                        }

                    }else{
                        cv_status_message=clientDetailsBean.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ClientDetailsBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }

    private void loadJSON_getDepartmentList() {
       // showBar();

        Call<DepartmentListBean> call= RetrofitClient.getInstance().getApi().getDepartmentList("68d83c7772aebc5c306a2aafcb38e891");
        call.enqueue(new Callback<DepartmentListBean>() {

            @Override
            public void onResponse(Call<DepartmentListBean> call, Response<DepartmentListBean> response) {

              //  progressDialog.dismiss();

                if(response.isSuccessful()){
                    DepartmentListBean departmentListBean=response.body();

                    status=departmentListBean.getStatus();

                    if(status.equals("true")){
                        List<DepartmentListBean.DL_Departments> cgdl_departmentsList=departmentListBean.getDl_departmentsList();
                        for(int i=0;i<cgdl_departmentsList.size();i++){
                            departmentId=cgdl_departmentsList.get(i).getDepartmentId();
                            deptName=cgdl_departmentsList.get(i).getDeptName();

                            list_deptid.add(departmentId);
                            list_deptname.add(deptName);
                        }

                        ArrayAdapter<String> adapter_ass_to_dept = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_text,list_deptname);
                        spin_assignto_department.setAdapter(adapter_ass_to_dept);

                    }else{
                        status_message=departmentListBean.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                 //   progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<DepartmentListBean> call, Throwable t) {
             //   progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }

    private void loadJSON_getEmployeeList() {
        showBar();

        Call<EmployeesListBean> call= RetrofitClient.getInstance().getApi().getEmployeeDetails("68d83c7772aebc5c306a2aafcb38e891",
                selected_dept_code);
        call.enqueue(new Callback<EmployeesListBean>() {

            @Override
            public void onResponse(Call<EmployeesListBean> call, Response<EmployeesListBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    EmployeesListBean employeesListBean=response.body();

                    status=employeesListBean.getStatus();

                    if(status.equals("true")){
                        List<EmployeesListBean.EL_EmployeeList> el_employeeLists=employeesListBean.getEl_employeeListList();
                        for(int i=0;i<el_employeeLists.size();i++){
                            employeeId=el_employeeLists.get(i).getEmployeeId();
                            empName=el_employeeLists.get(i).getEmpName();

                            list_empid.add(employeeId);
                            list_empname.add(empName);
                        }

                        ArrayAdapter<String> adapter_ass_to_emp = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_text,list_empname);
                        spin_assignto_employee.setAdapter(adapter_ass_to_emp);

                    }else{
                        status_message=employeesListBean.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }


                }else{
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<EmployeesListBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }

    private void loadJSON_get_product_services_list() {
        showBar();

        Call<ProductServiceBean> call= RetrofitClient.getInstance().getApi().getProductServicesList("68d83c7772aebc5c306a2aafcb38e891",st_spin_productservice_id);
        call.enqueue(new Callback<ProductServiceBean>() {

            @Override
            public void onResponse(Call<ProductServiceBean> call, Response<ProductServiceBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    ProductServiceBean productServiceBean=response.body();

                    status=productServiceBean.getStatus();

                    if(status.equals("true")){
                        List<ProductServiceBean.PS_results> productServiceBeanPs_resultsList=productServiceBean.getPs_resultsList();
                        for(int i=0;i<productServiceBeanPs_resultsList.size();i++){
                            productServicesId=productServiceBeanPs_resultsList.get(i).getProductServicesId();
                            prodserviceName=productServiceBeanPs_resultsList.get(i).getName();

                            list_prodserv_id.add(productServicesId);
                            list_prodserv_name.add(prodserviceName);
                        }

                        spin_sub_prodserv=findViewById(R.id.spin_sub_prodserv);
                        spin_sub_prodserv.setOnItemSelectedListener(NewEnquiryForm.this);

                        ArrayAdapter<String> adapter_spin_client = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_text,list_prodserv_name);
                        spin_sub_prodserv.setAdapter(adapter_spin_client);

                    }else{
                        status_message=productServiceBean.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ProductServiceBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub

        if(parent==spin_client)
        {
            spin_client_pstn=position;
            if(spin_client_pstn>0)
            {
                System.out.println("spin_client_pstn===="+spin_client_pstn);
                selected_client_code=list_clientid.get(spin_client_pstn-1).toString().trim();
                client=list_clientname.get(spin_client_pstn).toString().trim();
                System.out.println("selected_client_code===="+selected_client_code);
                loadJSON_getClientView();
            }
        }

        if(parent==spin_assignto_department)
        {
            spin_dept_pstn=position;
            if(spin_dept_pstn>0)
            {
                selected_dept_code=list_deptid.get(spin_dept_pstn-1).toString().trim();
                department=list_deptname.get(spin_dept_pstn).toString().trim();
                System.out.println("selected_dept_code===="+selected_dept_code);
                loadJSON_getEmployeeList();
            }
        }

        if(parent==spin_assignto_employee)
        {
            spin_emp_pstn=position;
            if(spin_emp_pstn>0)
            {
                selected_employee_code=list_empid.get(spin_emp_pstn-1).toString().trim();
                employee=list_empname.get(spin_emp_pstn).toString().trim();
                System.out.println("selected_employee_code===="+selected_employee_code);
            }
        }

        if(parent==spin_sub_prodserv)
        {
            spin_prodserv_pstn=position;
            if(spin_prodserv_pstn>0)
            {
                selected_prodserv_code=list_prodserv_id.get(spin_prodserv_pstn-1).toString().trim();
                prodsericd=list_prodserv_name.get(spin_prodserv_pstn).toString().trim();
                System.out.println("selected_prodserv_code===="+selected_prodserv_code);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void showBar(){
        builder = new AlertDialog.Builder(NewEnquiryForm.this);
        progressDialog = new ProgressDialog(NewEnquiryForm.this);
        progressDialog.setMessage("Processing Data...");
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(getBaseContext(), EnquiryList.class);
        MainActivity.putExtra("token",token);
        MainActivity.putExtra("login_id",login_id);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        NewEnquiryForm.this.finish();
    }

}