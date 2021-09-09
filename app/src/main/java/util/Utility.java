package util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.quadsel.qvisionapp.R;

public class Utility {

    //Alert Messages
    public static void getAlertNetNotConneccted(Context context, String errMsg) {

        showAlertDialog(context, errMsg + "", "Please Check Your "
                + errMsg, false);
    }
    public static void getAlertMsgSelect(Context context, String errMsg) {

        showAlertDialog(context, errMsg + " not selected", "Please Select "
                + errMsg, false);
    }
    public static void getAlertRevisit_not_allowed(Context context, String errMsg) {

        showAlertDialog(context,"Info", " "
                + errMsg, false);
    }
    public static void getAlertMsgVAlidMobileNo(Context context, String errMsg) {

        showAlertDialog(context, errMsg + " not valid", "Please Enter Valid "
                + errMsg, false);
    }

    public static void getAlertMsgEnter(Context context, String errMsg) {

        showAlertDialog(context, errMsg + " empty", "Please Enter "
                + errMsg, false);
    }
    public static void getAlertMsgEnter_TechnicalIssue(Context context, String errMsg) {

        showAlertDialog(context, "Technical Issue", "Oops! There is some "
                + errMsg, false);
    }
    public static void getAlertMsgEnter_Transaction_fail(Context context, String errMsg) {

        showAlertDialog(context, "Transaction Failed..", "Oops! Your "
                + errMsg, false);
    }
    public static void getAlertMsgEnter_NetworkIssue(Context context, String errMsg) {

        showAlertDialog(context, "Network Issue", ""
                + errMsg, false);
    }
    public static void getAlertRevisit_data_fail(Context context, String errMsg) {

        showAlertDialog(context, "Info", "You entered wrong MRDNO / Mobile Number..Please check it..", false);
    }
    public static void getAlertMsgEnter_Registartion_fail(Context context, String errMsg) {

        showAlertDialog(context, "Info", "Registration Failed.."
                + errMsg, false);
    }
    public static void getAlertMsg_not_avail(Context context, String errMsg) {

        showAlertDialog(context, "Info", ""
                + errMsg, false);
    }
    public static void showAlertDialog(Context context, String title,
                                       String message, Boolean status) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        if (status != null)
            builder.setIcon((status) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        final AlertDialog alert = builder.create();

        alert.show();

    }


    public static void showMessageDialogue(Context context, String messageTxt, String argTitle) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(argTitle)
                .setMessage(messageTxt)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;

    }


}
