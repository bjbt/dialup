package com.bjbt.dialup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class ProbeInitializer {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String probeUploadUrl;
    private static String businessCode;
    private static ProbeNetManager probeNetManager = new ProbeNetManager();

    public static Context getContext() {
        return context;
    }

    static String getProbeUploadUrl() {
        return probeUploadUrl;
    }

    static String getBusinessCode() {
        return businessCode;
    }


    public static void initializtion(Context context, String businessCode) {
        ProbeInitializer.context = context;
        ProbeInitializer.businessCode = businessCode;
        ProbeSystemParam.systemParameter(context);
        String sv = ProbeUtils.loadCacheData(ProbeConstant.SDK_VERSION);
        if (!sv.equals(ProbeConstant.VERSION) && !TextUtils.isEmpty(sv)) {
            probeRequestUploadUrl(businessCode);
        }

        String isUpload = ProbeUtils.loadCacheData(businessCode);
        if (!isUpload.equals(ProbeConstant.ZERO)) {
            probeRequestUploadUrl(businessCode);
        }
    }

    private static void probeRequestUploadUrl(final String businessCode) {
        final String serverUrl;
        if ((int) (Math.random() * 100 % 2) == 0) {
            serverUrl = context.getString(R.string.u_s) + "&" + context.getString(R.string.u_e) + "&" + context.getString(R.string.u_c) + businessCode;
        } else {
            serverUrl = context.getString(R.string.u_s) + "&" + context.getString(R.string.u_e) + "&" + context.getString(R.string.u_c) + businessCode;
        }

        probeNetManager.get(serverUrl, new ProbeNetManager.Callback() {
            @Override
            public void onSuccess(String res) {
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getString(ProbeConstant.CODE).equals(ProbeConstant.ZERO)) {
                        probeUploadUrl = object.getString(ProbeConstant.DATA);
                        ProbeUtils.cacheDate(ProbeConstant.VERSION, ProbeConstant.SDK_VERSION);
                        ProbeUtils.cacheDate(object.getString(ProbeConstant.UPLOAD), businessCode);
                        if (!TextUtils.isEmpty(ProbeUtils.loadCacheData(ProbeConstant.CACHE_DATA)) && ProbeUtils.loadCacheData(ProbeInitializer.getBusinessCode()).equals(ProbeConstant.ONE)) {
                            ProbeUploadData.upload(ProbeUtils.loadCacheData(ProbeConstant.CACHE_DATA), ProbeUtils.loadCacheData(ProbeConstant.CACHE_CODE), true);
                        }
                    } else {
                        probeUploadUrl = "";
                    }
                } catch (JSONException e) {
                    probeUploadUrl = "";
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String err) {
                probeUploadUrl = "";
            }
        });
    }
}
