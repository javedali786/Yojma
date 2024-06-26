package com.tv.uscreen.yojmatv.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.tv.uscreen.yojmatv.R;


public class PermissionUtil {
    private final String[] galleryPermissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };

    private final String[] cameraPermissions = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };

    public static void showPermissionDialog(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDatePickerStyle);
        builder.setTitle("Runtime Permission");
        builder.setMessage(msg);
        builder.setPositiveButton(mContext.getString(R.string.profile), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
            intent.setData(uri);
            (mContext).startActivity(intent);
        });

        builder.setNegativeButton(mContext.getString(R.string.noti_title), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    public String[] getGalleryPermissions() {
        return galleryPermissions;
    }

    public String[] getCameraPermissions() {
        return cameraPermissions;
    }

    public boolean verifyPermissions(Context context, String[] grantResults) {
        for (String result : grantResults) {
            if (ActivityCompat.checkSelfPermission(context, result) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean checkMarshMellowPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

}