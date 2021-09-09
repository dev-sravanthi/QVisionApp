package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.quadsel.qvisionapp.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import bean.EnquiryCanListBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class EnquiryList extends AppCompatActivity {
    String token,login_id,status,enquiryId,callType,date,client,location,contactNumber,followUpDate,employee,resstatus,status_message,isFeedbackEnabled;
    boolean networkAvailability=false;
    RecyclerView recyview_costlist;
    EnquiryCanListAdapter enquiryCanListAdapter;
    List<EnquiryCanListAdapterBean> enquiryCanListAdapterBeanList=new ArrayList<>();
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    Button btn_newenquiryform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Enquiry Candidate List");
        toolbar.setSubtitle("");
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        login_id=i.getStringExtra("login_id");

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(EnquiryList.this);

        if(networkAvailability==true){
            findviewids();
        }else{
            Utility.getAlertNetNotConneccted(EnquiryList.this, "Internet Connection");
        }


    }

    private void findviewids() {

        btn_newenquiryform=findViewById(R.id.btn_newenquiryform);
        btn_newenquiryform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EnquiryList.this,NewEnquiryForm.class);
                i.putExtra("token",token);
                i.putExtra("login_id",login_id);
                startActivity(i);
                finish();
            }
        });

        recyview_costlist=findViewById(R.id.recyview_costlist);
        recyview_costlist.setHasFixedSize(true);
        enquiryCanListAdapter = new EnquiryCanListAdapter(enquiryCanListAdapterBeanList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EnquiryList.this);
        recyview_costlist.setLayoutManager(layoutManager);
        recyview_costlist.addItemDecoration(new DividerItemDecoration(EnquiryList.this, 0));
        recyview_costlist.setItemAnimator(new DefaultItemAnimator());
        recyview_costlist.setAdapter(enquiryCanListAdapter);
        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<EnquiryCanListBean> call= RetrofitClient.getInstance().getApi().
                getenquiry_list(token);
        call.enqueue(new Callback<EnquiryCanListBean>() {

            @Override
            public void onResponse(Call<EnquiryCanListBean> call, Response<EnquiryCanListBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    EnquiryCanListBean enquiryCanListBean=response.body();
                    status=enquiryCanListBean.getStatus();

                    if(status.equals("true")){
                        List<EnquiryCanListBean.EnqListResults> crmEnqResultLists=
                                enquiryCanListBean.getEnqListResultsList();

                        for(int i=0;i<crmEnqResultLists.size();i++){
                            enquiryId=crmEnqResultLists.get(i).getEnquiryId();
                            callType=crmEnqResultLists.get(i).getCallType();
                            date=crmEnqResultLists.get(i).getDate();
                            client=crmEnqResultLists.get(i).getClient();
                            location=crmEnqResultLists.get(i).getLocation();
                            contactNumber=crmEnqResultLists.get(i).getContactNumber();
                            followUpDate=crmEnqResultLists.get(i).getFollowUpDate();
                            employee=crmEnqResultLists.get(i).getEmployee();
                            resstatus=crmEnqResultLists.get(i).getStatus();
                            isFeedbackEnabled=crmEnqResultLists.get(i).getIsFeedbackEnabled();

                            EnquiryCanListAdapterBean enquiryCanListAdapterBean=new EnquiryCanListAdapterBean(enquiryId,callType,date,client,location,contactNumber,followUpDate,
                                    employee,resstatus,isFeedbackEnabled);
                            enquiryCanListAdapterBeanList.add(enquiryCanListAdapterBean);
                        }

                        enquiryCanListAdapter.notifyDataSetChanged();

                    }else{
                        status_message=enquiryCanListBean.getStatus_message();
                        new android.app.AlertDialog.Builder(EnquiryList.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryList.this,MenuScreen.class);
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
                    try {
                        new android.app.AlertDialog.Builder(EnquiryList.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(response.errorBody().string())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryList.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
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
            public void onFailure(Call<EnquiryCanListBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(EnquiryList.this)
                        .setCancelable(false)
                        .setTitle("Info")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(EnquiryList.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();
            }

        });
    }

    public class EnquiryCanListAdapter extends RecyclerView.Adapter<EnquiryCanListAdapter.ViewHolder> {
        private List<EnquiryCanListAdapterBean> enquiryCanListAdapterBeanList;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_sno,text_calltype,text_date,text_client,text_location,text_contactno,text_followupdate,text_employee,text_status;
            public ImageButton img_eye_view;

            public ViewHolder(View view) {
                super(view);
                text_sno = (TextView) view.findViewById(R.id.text_sno);
                text_calltype = (TextView) view.findViewById(R.id.text_calltype);
                text_date = (TextView) view.findViewById(R.id.text_date);
                text_client = (TextView) view.findViewById(R.id.text_client);
                text_location = (TextView) view.findViewById(R.id.text_location);
                text_contactno = (TextView) view.findViewById(R.id.text_contactno);
                text_followupdate = (TextView) view.findViewById(R.id.text_followupdate);
                text_employee = (TextView) view.findViewById(R.id.text_employee);
                text_status = (TextView) view.findViewById(R.id.text_status);
                img_eye_view=(ImageButton)view.findViewById(R.id.img_eye_view);
            }
        }

        public EnquiryCanListAdapter(List<EnquiryCanListAdapterBean> mlist) {
            this.enquiryCanListAdapterBeanList = mlist;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_enquiry_candidate_list, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            EnquiryCanListAdapterBean data = enquiryCanListAdapterBeanList.get(position);
            holder.text_sno.setText(String.valueOf(position+1));
            holder.text_calltype.setText(data.getCallType());
            holder.text_date.setText(data.getDate());
            holder.text_client.setText(data.getClient());
            holder.text_location.setText(data.getLocation());
            holder.text_contactno.setText(data.getContactNumber());
            holder.text_followupdate.setText(data.getFollowUpDate());
            holder.text_employee.setText(data.getEmployee());
            holder.text_status.setText(data.getStatus());
            holder.img_eye_view.setImageResource(R.drawable.view);
            holder.img_eye_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(EnquiryList.this,EnquiryView.class);
                    i.putExtra("token",token);
                    i.putExtra("enquiryId",enquiryId);
                    i.putExtra("login_id",login_id);
                    i.putExtra("feedbackflag",data.isFeedbackEnabled);
                    startActivity(i);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return enquiryCanListAdapterBeanList.size();
        }
    }

    public class EnquiryCanListAdapterBean{
        private String enquiryId,callType,date,client,location,contactNumber,followUpDate,employee,status,isFeedbackEnabled;

        public EnquiryCanListAdapterBean(String enquiryId,String callType,String date,String client,String location,String contactNumber,String followUpDate,String employee,
                                         String status,String isFeedbackEnabled){
            this.enquiryId=enquiryId;
            this.callType=callType;
            this.date=date;
            this.client=client;
            this.location=location;
            this.contactNumber=contactNumber;
            this.followUpDate=followUpDate;
            this.employee=employee;
            this.status=status;
            this.isFeedbackEnabled=isFeedbackEnabled;
        }

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

    public void showBar(){
        builder = new AlertDialog.Builder(EnquiryList.this);
        progressDialog = new ProgressDialog(EnquiryList.this);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(getBaseContext(), MenuScreen.class);
        MainActivity.putExtra("token",token);
        MainActivity.putExtra("login_id",login_id);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        EnquiryList.this.finish();
    }

}