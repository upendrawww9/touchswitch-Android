package bizmessage.in.touchswitch.permissionManagerViews;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionManagerActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS = 111;
    private PermissionManagerInstance mPermissionManagerInstance;
    private String[] mPermissionsToRequest;
    private String[] mAllPermissions;
    private boolean mShowMessageOnRationale = false;
    private boolean mShowMessageBeforeRequest = false;
    private String mMessage = "";
    private String mTitle = "";
    private String mNegativeButtonText = "";
    private String mPositiveButtonText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            mPermissionManagerInstance = new PermissionManagerInstance(this);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            if (getIntent().getExtras() != null) {
                mAllPermissions = (String[]) getIntent().getExtras().getSerializable("EXTRA_PERMISSION_LIST");
                mPermissionsToRequest = (String[]) getIntent().getExtras().getSerializable("EXTRA_PERMISSION_TO_REQUEST");
                mShowMessageOnRationale = getIntent().getExtras().getBoolean("EXTRA_IS_SHOW_MESSAGE");
                mMessage = getIntent().getExtras().getString("EXTRA_MESSAGE");
                mTitle = getIntent().getExtras().getString("EXTRA_TITLE");
                mShowMessageBeforeRequest = getIntent().getExtras().getBoolean("EXTRA_IS_SHOW_MESSAGE_BEFORE");
                mNegativeButtonText = getIntent().getExtras().getString("EXTRA_NEGATIVE_BUTTON");
                mPositiveButtonText = getIntent().getExtras().getString("EXTRA_POSITIVE_BUTTON");
            }

            if (mPermissionsToRequest != null && mShowMessageBeforeRequest && mShowMessageOnRationale) {
                boolean showMessage = false;
                for (String aPermission : mPermissionsToRequest) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, aPermission)) {
                        showMessage = true;
                        showDialogForPermissionBefore();
                        break;
                    }
                }
                if (!showMessage) {
                    ActivityCompat.requestPermissions(this, mPermissionsToRequest, MY_PERMISSIONS);
                }
            } else if (mPermissionsToRequest != null && mPermissionsToRequest.length >= 1
                    && !mPermissionsToRequest[0].equalsIgnoreCase("")) {
                ActivityCompat.requestPermissions(this, mPermissionsToRequest, MY_PERMISSIONS);
            } else {
                checkPermissionToReturn();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        checkPermissionToReturn();
    }

    private void checkPermissionToReturn() {
        boolean allGranted = true;
        Permission[] grantResults = new Permission[mAllPermissions.length];
        int count = 0;
        boolean showMessage = false;
        for (String aPermission : mAllPermissions) {
            if (mPermissionManagerInstance.checkPermission(aPermission)) {
                grantResults[count] = Permission.GRANTED;
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, aPermission)) {
                grantResults[count] = Permission.DENIED;
                allGranted = false;
                if (mShowMessageOnRationale && !mShowMessageBeforeRequest) {
                    showMessage = true;
                }
            } else {
                grantResults[count] = Permission.PERMANENTLY_DENIED;
                allGranted = false;
            }
            count++;
        }
        if (!showMessage) {
            PermissionManagerInstance.handleResult(mAllPermissions, grantResults, allGranted);
            finish();
        } else {
            showDialogForPermissionAfter(grantResults);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showDialogForPermissionBefore() {
        new AlertDialog.Builder(this).setTitle(mTitle)
                .setMessage(mMessage)
                .setNegativeButton(mNegativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(mPositiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(PermissionManagerActivity.this, mPermissionsToRequest, MY_PERMISSIONS);
                    }
                })
                .setCancelable(false)
                .show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showDialogForPermissionAfter(final Permission[] grantResults) {
        new AlertDialog.Builder(this).setTitle(mTitle)
                .setMessage(mMessage)
                .setNegativeButton(mNegativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PermissionManagerInstance.handleResult(mAllPermissions, grantResults, false);
                        finish();
                    }
                })
                .setPositiveButton(mPositiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PermissionManagerInstance.handleResult(mAllPermissions, grantResults, false);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }
}
