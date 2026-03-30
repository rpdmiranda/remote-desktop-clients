package com.iiordanov.bVNC;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import com.undatech.remoteClientUi.R;

public class AccessibilityServiceHelper {

    public static boolean isServiceEnabled(Context context) {
        AccessibilityManager am = (AccessibilityManager)
                context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am == null) return false;
        String serviceId = context.getPackageName() + "/"
                + KeyInterceptAccessibilityService.class.getName();
        for (AccessibilityServiceInfo info : am.getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_ALL_MASK)) {
            if (serviceId.equals(info.getId())) {
                return true;
            }
        }
        return false;
    }

    public static void showRequiredDialog(Activity activity, Runnable onDecline) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.accessibility_dialog_title)
                .setMessage(R.string.accessibility_dialog_message)
                .setPositiveButton(R.string.accessibility_dialog_open_settings, (dialog, which) ->
                        activity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)))
                .setNegativeButton(R.string.accessibility_dialog_continue_without, (dialog, which) -> {
                    if (onDecline != null) onDecline.run();
                })
                .setCancelable(false)
                .show();
    }
}
