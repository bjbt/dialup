package com.bjbt.dialup;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;


class ProbeSystemParam {

    protected static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    protected static String getDeviceModel() {
        return Build.MODEL;
    }

    protected static String getDeviceBrand() {
        return Build.BRAND;
    }

    protected static String getOperatingSystem() {
        String s = Build.MANUFACTURER.toLowerCase();
        if (s.contains("huawei")) {
            return "EMUI";
        } else if (s.contains("meizu")) {
            return "FLYME";
        } else if (s.contains("xiaomi")) {
            return "MIUI";
        } else if (s.contains("oppo")) {
            return "Color OS";
        } else if (s.contains("vivo")) {
            return "Funtouch OS";
        } else if (s.contains("samsung")) {
            return "Bada";
        } else if (s.contains("smartisan")) {
            return "Smartisan OS";
        } else if (s.contains("lenovo")) {
            return "VIBE UI";
        } else {
            return "OTHER";
        }
    }

    protected static String getOperators(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (networkOperator != null && !networkOperator.isEmpty()) {
                if (networkOperator.equals("46000") || networkOperator.equals("46002") || networkOperator.equals("46007")) {
                    return "中国移动";
                } else if (networkOperator.equals("46001")) {
                    return "中国联通";
                } else if (networkOperator.equals("46003")) {
                    return "中国电信";
                }
            }
        }
        return "未知运营商";
    }

    public static String getNetFlag(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return "2";
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return "1";
            } else {
                return "网络未连接";
            }
        }
        return "网络未连接";
    }

    protected static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(packageManager.getApplicationLabel(packageInfo.applicationInfo));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "获取APP Name失败";
    }

    protected static String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    protected static void systemParameter(Context context) {
        ProbeSystemData systemData = new ProbeSystemData();
        systemData.setSystemVersion(getSystemVersion());
        systemData.setDeviceModel(getDeviceModel());
        systemData.setDeviceBrand(getDeviceBrand());
        systemData.setOperatingSystem(getOperatingSystem());
        systemData.setOperators(getOperators(context));
        systemData.setNetFlag(getNetFlag(context));
        systemData.setAppName(getAppName(context));
        systemData.setVersionName(getVersionName(context));
    }
}
