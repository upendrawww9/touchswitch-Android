package bizmessage.in.touchswitch.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Locale;

import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;

public class Utility {

    public static int DIALOG_LOGOUT_IDENTIFIER = 1;
    public static int DIALOG_DOWNLOAD_REPORT_IDENTIFIER = 2;
    public static int DIALOG_OFFLINE_IDENTIFIER = 1;
    public static int DIALOG_UPDATE_IDENTIFIER = 3;

    Context mconContext;
    private static Dialog popupWindow;

    public Utility(Context mconContext) {
        super();
        this.mconContext = mconContext;
    }

    public static final java.text.DateFormat dateFormatMMMMdd = new SimpleDateFormat("MMMM dd", Locale.getDefault());
    public static final java.text.DateFormat dateFormatddMMMyyhhmmaa = new SimpleDateFormat("dd MMM''yy | hh:mm aa", Locale.getDefault());
    public static final java.text.DateFormat DATEFORMATddMMMyyy = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public static final java.text.DateFormat DATEFORMATddMMyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    public static final java.text.DateFormat dateFormatddMMyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    public static final java.text.DateFormat dateFormathhmma = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public static void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, pxToDp(context, 70));
        toast.show();
    }

    public static boolean isNetworkAvailable(Context context) {
       // boolean isgoogle= isInternetAvailable();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }




    public static void hideSoftKeyboard(Activity mActivity) {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mActivity.getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showProgress(final Context context) {
        try {
            if (!((Activity) context).isFinishing()) {

                View layout = LayoutInflater.from(context).inflate(R.layout.layout_popup_loading, null);
                popupWindow = new Dialog(context, R.style.ProgressDialog);
                popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popupWindow.setContentView(layout);
                popupWindow.setCancelable(false);

                if (!((Activity) context).isFinishing()) {
                    popupWindow.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgress() {
        try {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void showToast(final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(OnlookApplication.getInstance(), message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, pxToDp(OnlookApplication.getInstance(), 70));
                toast.show();
            }
        });
    }

    public static void showToastLong(final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(OnlookApplication.getInstance(), message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, pxToDp(OnlookApplication.getInstance(), 70));
                toast.show();
            }
        });
    }

    public static void showError(final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(OnlookApplication.getInstance(), message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, pxToDp(OnlookApplication.getInstance(), 70));
                toast.show();
            }
        });
    }

    public static void showErrorLong(final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(OnlookApplication.getInstance(), message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, pxToDp(OnlookApplication.getInstance(), 70));
                toast.show();
            }
        });
    }

    public static void showSnackBar(View view,String message){
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(view.getResources().getColor(R.color.colorPrimaryDark));
        TextView textView = snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(view.getResources().getColor(R.color.white));
        snackbar.show();
    }

    public static int pxToDp(Context context, float px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion + "-" + release + "";
    }

    public static void showDialog(Context context,
                                  String dialogTitle, String dialogMessage, String textPositiveButton,
                                  String textNegativeButton,
                                  final DialogButtonClickListener buttonClickListener,
                                  final int dialogIdentifier, final Object data) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(dialogMessage);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton(textPositiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id1) {
                dialog.dismiss();
                if (buttonClickListener == null) {
                    dialog.dismiss();
                } else {
                    buttonClickListener.onPositiveButtonClicked(dialogIdentifier, data);
                }
            }
        });

        if (textNegativeButton != null) {
            alertDialogBuilder.setNegativeButton(textNegativeButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id1) {
                    dialog.dismiss();
                    if (buttonClickListener == null) {
                        dialog.dismiss();
                    } else {
                      //  buttonClickListener.onNegativeButtonClicked(dialogIdentifier);
                    }
                }
            });
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        //	alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        if (!((Activity) context).isFinishing()) {
            alertDialog.show();
        }

    }
}
