package com.bjbt.dialup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class ProbeInitializer {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String businessCode;
    private static String serverType;
    private static final ProbeNetManager probeNetManager = new ProbeNetManager();

    protected static Context getContext() {
        return context;
    }

    protected static String getBusinessCode() {
        return businessCode;
    }

    protected static String getServerType() {
        return serverType;
    }

    public static void initializtion(Context context, String businessCode, String serverType) {
        System.setProperty(context.getString(R.string.ipv_6_k), context.getString(R.string.ipv_6_y));
        ProbeInitializer.context = context;
        ProbeInitializer.businessCode = businessCode;
        ProbeInitializer.serverType = serverType;
        ProbeSystemParam.systemParameter(context);

        File isUploadFile = new File(ProbeInitializer.getContext().getFilesDir(), businessCode);
        if (isUploadFile.exists()){
            String isUpload = ProbeUtils.loadCacheData(businessCode);
            if (!ProbeConstant.ZERO.equals(isUpload)) {
                probeRequestUploadUrl();
            }
        }

        File svFile = new File(ProbeInitializer.getContext().getFilesDir(), ProbeInitializer.getContext().getString(R.string.sdk_version));
        if (svFile.exists()){
            String sv = ProbeUtils.loadCacheData(ProbeInitializer.getContext().getString(R.string.sdk_version));
            if (!ProbeInitializer.getContext().getString(R.string.version_code).equals(sv)) {
                probeRequestUploadUrl();
            }
        }else {
            probeRequestUploadUrl();
        }
    }

    private static void probeRequestUploadUrl() {
        String serverUrl;
        if (businessCode.equals(context.getString(R.string.hdj_code))) {
            serverUrl = context.getString(R.string.get_server_hdj);
        } else if (serverType.equalsIgnoreCase("prod")) {
            serverUrl = context.getString(R.string.get_server_formal);
        } else if (serverType.equalsIgnoreCase("test")) {
            serverUrl = context.getString(R.string.get_server_test);
        } else if (serverType.equalsIgnoreCase("dev")) {
            serverUrl = context.getString(R.string.get_server_develop);
        } else {
            return;
        }

        probeNetManager.get(serverUrl + businessCode, new ProbeNetManager.Callback() {
            @Override
            public void onSuccess(String res) {
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getString(ProbeConstant.CODE).equals(ProbeConstant.ZERO)) {
                        ProbeUtils.cacheDate(context.getString(R.string.version_code), context.getString(R.string.sdk_version));
                        ProbeUtils.cacheDate(object.getString(ProbeConstant.UPLOAD), businessCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String err) {
                System.out.print(err);
            }
        });
    }
}
