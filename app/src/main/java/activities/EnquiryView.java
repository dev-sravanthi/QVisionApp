package activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quadsel.qvisionapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.EnquiryViewBean;
import bean.InsertFeedbackBean;
import bean.NewEnquiryFormBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class EnquiryView extends AppCompatActivity {
    EditText ed_calltype,ed_date,ed_clienttype,ed_companyname,ed_location,ed_address,ed_clientname,ed_contactno,ed_designation,ed_emailid,ed_service,ed_feedback,
            ed_followupdate,ed_department,ed_employeename;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    boolean networkAvailability=false;
    String token,enquiryId,login_id,status,status_message,callType,date,clientType,companyName,location,address,client,mobile,designation,mail,product,
            feedback,followUp,department,employee,fe_feedback,fe_feedbackDate,feedbackflag;
    Button btn_addfeedback;
    List<EnqFormViewAdapterBean> list=new ArrayList<>();
    EnqFormViewAdapter enqFormViewAdapter;
    RecyclerView rv_feedbackentrydetails;
    CardView cv_feedackentrydetails;
    private View popupInputDialogView = null;
    private EditText ed_feedback_popup,ed_feedback_follwupdate;
    private Button btn_foolowup_date,btn_submit_feedback,btn_cancel;
    private String st_ed_feedback_popup,st_ed_feedback_follwupdate;
    private int year = 0,month,day,myear,mmonth,mday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry_view);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        enquiryId=i.getStringExtra("enquiryId");
        login_id=i.getStringExtra("login_id");
        feedbackflag=i.getStringExtra("feedbackflag");

        System.out.println("token===="+token+" ===== "+enquiryId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Enquiry Form");
        toolbar.setSubtitle("");
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(EnquiryView.this);

        if(networkAvailability==true){
            findviewbyids();
        }else {
            Utility.getAlertNetNotConneccted(EnquiryView.this, "Internet Connection");
        }
    }

    private void findviewbyids(){
        cv_feedackentrydetails=findViewById(R.id.cv_feedackentrydetails);
        ed_calltype=findViewById(R.id.ed_calltype);
        ed_date=findViewById(R.id.ed_date);
        ed_clienttype=findViewById(R.id.ed_clienttype);
        ed_companyname=findViewById(R.id.ed_companyname);
        ed_location=findViewById(R.id.ed_location);
        ed_address=findViewById(R.id.ed_address);
        ed_clientname=findViewById(R.id.ed_clientname);
        ed_contactno=findViewById(R.id.ed_contactno);
        ed_designation=findViewById(R.id.ed_designation);
        ed_emailid=findViewById(R.id.ed_emailid);
        ed_service=findViewById(R.id.ed_service);
        ed_feedback=findViewById(R.id.ed_feedback);
        ed_followupdate=findViewById(R.id.ed_followupdate);
        ed_department=findViewById(R.id.ed_department);
        ed_employeename=findViewById(R.id.ed_employeename);

        if(feedbackflag.equals("true")){
            btn_addfeedback=findViewById(R.id.btn_addfeedback);
            btn_addfeedback.setVisibility(View.VISIBLE);
            btn_addfeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(EnquiryView.this, "popup form", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EnquiryView.this);
                    alertDialogBuilder.setTitle("Feedback Entry");
                    alertDialogBuilder.setIcon(R.drawable.feedback_popupicon);
                    alertDialogBuilder.setCancelable(false);
                    initPopupViewControls();
                    alertDialogBuilder.setView(popupInputDialogView);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    btn_foolowup_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(999);
                        }
                    });

                    btn_submit_feedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            st_ed_feedback_popup=ed_feedback_popup.getText().toString();
                            if(st_ed_feedback_popup.equals("") && st_ed_feedback_popup.length()==0){
                                Toast.makeText(EnquiryView.this, "Enter Feedback", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            st_ed_feedback_follwupdate=ed_feedback_follwupdate.getText().toString();
                            if(st_ed_feedback_follwupdate.equals("") && st_ed_feedback_follwupdate.length()==0){
                                Toast.makeText(EnquiryView.this, "Select Followup Date", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            System.out.println(token+"========"+enquiryId+"========"+st_ed_feedback_popup+"====="+st_ed_feedback_follwupdate+"====="+login_id);
                            Toast.makeText(EnquiryView.this, "Feedback Entered Successfully", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });
        }else if(feedbackflag.equals("false")){
            btn_addfeedback=findViewById(R.id.btn_addfeedback);
            btn_addfeedback.setVisibility(View.GONE);
        }

        rv_feedbackentrydetails=findViewById(R.id.rv_feedbackentrydetails);
        rv_feedbackentrydetails.setHasFixedSize(true);
        enqFormViewAdapter = new EnqFormViewAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EnquiryView.this);
        rv_feedbackentrydetails.setLayoutManager(layoutManager);
        rv_feedbackentrydetails.addItemDecoration(new DividerItemDecoration(EnquiryView.this, LinearLayoutManager.VERTICAL));
        rv_feedbackentrydetails.setItemAnimator(new DefaultItemAnimator());
        rv_feedbackentrydetails.setAdapter(enqFormViewAdapter);

        loadJSON();
    }

    private void insertFeedback(){
        showBar();

        Call<InsertFeedbackBean> call= RetrofitClient.getInstance().getApi().insertFeedback(token,enquiryId,st_ed_feedback_popup,st_ed_feedback_follwupdate,login_id);
        call.enqueue(new Callback<InsertFeedbackBean>() {
            @Override
            public void onResponse(Call<InsertFeedbackBean> call, Response<InsertFeedbackBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    InsertFeedbackBean crmGetClientList=response.body();

                    status=crmGetClientList.getStatus();

                    if(status.equals("true")){
                        Toast.makeText(EnquiryView.this, "Submitted successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        status_message=crmGetClientList.getStatus_message();
                        new android.app.AlertDialog.Builder(EnquiryView.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryView.this,MenuScreen.class);
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

                        new android.app.AlertDialog.Builder(EnquiryView.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryView.this,MenuScreen.class);
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
            public void onFailure(Call<InsertFeedbackBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(EnquiryView.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(EnquiryView.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }

    private void initPopupViewControls() {
        LayoutInflater layoutInflater = LayoutInflater.from(EnquiryView.this);
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_feedback_dialog, null);
        ed_feedback_popup = (EditText) popupInputDialogView.findViewById(R.id.ed_feedback_popup);
        ed_feedback_follwupdate = (EditText) popupInputDialogView.findViewById(R.id.ed_feedback_follwupdate);
        btn_foolowup_date=popupInputDialogView.findViewById(R.id.btn_foolowup_date);
        btn_submit_feedback=popupInputDialogView.findViewById(R.id.btn_submit_feedback);
        btn_cancel=popupInputDialogView.findViewById(R.id.btn_cancel);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
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
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        ed_feedback_follwupdate.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }


    private void loadJSON() {

        showBar();
        Call<EnquiryViewBean> call= RetrofitClient.getInstance().getApi().
                getenquiry_view(token,enquiryId);
        call.enqueue(new Callback<EnquiryViewBean>() {

            @Override
            public void onResponse(Call<EnquiryViewBean> call, Response<EnquiryViewBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    EnquiryViewBean enquiryViewBean=response.body();
                    status=enquiryViewBean.getStatus();

                    if(status.equals("true")){
                        EnquiryViewBean.EnqFormEnqyDetails enqFormEnqyDetails=enquiryViewBean.getEnqFormEnqyDetails();

                        callType=enqFormEnqyDetails.getCallType();
                        date=enqFormEnqyDetails.getDate();
                        clientType=enqFormEnqyDetails.getClientType();
                        companyName=enqFormEnqyDetails.getCompanyName();
                        location=enqFormEnqyDetails.getLocation();
                        address=enqFormEnqyDetails.getAddress();
                        client=enqFormEnqyDetails.getClient();
                        mobile=enqFormEnqyDetails.getMobile();
                        designation=enqFormEnqyDetails.getDesignation();
                        mail=enqFormEnqyDetails.getMail();
                        product=enqFormEnqyDetails.getProduct();
                        feedback=enqFormEnqyDetails.getFeedback();
                        followUp=enqFormEnqyDetails.getFollowUp();
                        department=enqFormEnqyDetails.getDepartment();
                        employee=enqFormEnqyDetails.getEmployee();

                        ed_calltype.setText(callType);
                        ed_date.setText(date);
                        ed_clienttype.setText(clientType);
                        ed_companyname.setText(companyName);
                        ed_location.setText(location);
                        ed_address.setText(address);
                        ed_clientname.setText(client);
                        ed_contactno.setText(mobile);
                        ed_designation.setText(designation);
                        ed_emailid.setText(mail);
                        ed_service.setText(product);
                        ed_feedback.setText(feedback);
                        ed_followupdate.setText(followUp);
                        ed_department.setText(department);
                        ed_employeename.setText(employee);

                        if (enquiryViewBean.getEnqFormfeedbackEntryDetailsList()!=null){
                            cv_feedackentrydetails.setVisibility(View.VISIBLE);
                            List<EnquiryViewBean.EnqFormfeedbackEntryDetails> enqFormfeedbackEntryDetails=
                                    enquiryViewBean.getEnqFormfeedbackEntryDetailsList();
                            for(int i=0;i<enqFormfeedbackEntryDetails.size();i++){
                                fe_feedback=enqFormfeedbackEntryDetails.get(i).getFeedBack();
                                fe_feedbackDate=enqFormfeedbackEntryDetails.get(i).getFeedbackDate();

                                EnqFormViewAdapterBean enqFormViewAdapterBean=new EnqFormViewAdapterBean(fe_feedback,fe_feedbackDate);
                                list.add(enqFormViewAdapterBean);
                            }
                            enqFormViewAdapter.notifyDataSetChanged();
                        }else{
                            cv_feedackentrydetails.setVisibility(View.GONE);
                        }

                    }else{
                        status_message=enquiryViewBean.getStatus_message();
                        Utility.showMessageDialogue(EnquiryView.this,status_message,"Info");
                        new android.app.AlertDialog.Builder(EnquiryView.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryView.this,EnquiryList.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                    try {
                        progressDialog.dismiss();
                        new android.app.AlertDialog.Builder(EnquiryView.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(response.errorBody().string())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryView.this,EnquiryList.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<EnquiryViewBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(EnquiryView.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(EnquiryView.this,EnquiryList.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();
            }

        });

    }

    public class EnqFormViewAdapter extends RecyclerView.Adapter<EnqFormViewAdapter.ViewHolder> {
        private List<EnqFormViewAdapterBean> enqFormViewAdapterBeanslist;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_feedback,text_feedbackdate;

            public ViewHolder(View view) {
                super(view);
                text_feedback = (TextView) view.findViewById(R.id.text_feedback);
                text_feedbackdate = (TextView) view.findViewById(R.id.text_feedbackdate);
            }
        }

        public EnqFormViewAdapter(List<EnqFormViewAdapterBean> mlist)
        {
            this.enqFormViewAdapterBeanslist = mlist;
            this.context=context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_enquiryformview, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            EnqFormViewAdapterBean data = enqFormViewAdapterBeanslist.get(position);

            holder.text_feedback.setText(data.getFeedback());
            holder.text_feedbackdate.setText(data.getFeedback_date());

        }

        @Override
        public int getItemCount() {
            return enqFormViewAdapterBeanslist.size();
        }
    }

    public class EnqFormViewAdapterBean {
        private String feedback,feedback_date;

        public EnqFormViewAdapterBean(String feedback,String feedback_date) {
            this.feedback = feedback;
            this.feedback_date = feedback_date;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getFeedback_date() {
            return feedback_date;
        }

        public void setFeedback_date(String feedback_date) {
            this.feedback_date = feedback_date;
        }
    }

    public void showBar(){
        builder = new AlertDialog.Builder(EnquiryView.this);
        progressDialog = new ProgressDialog(EnquiryView.this);
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
        EnquiryView.this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}