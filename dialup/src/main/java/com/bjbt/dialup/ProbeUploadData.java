package com.bjbt.dialup;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;

class ProbeUploadData {

    private static ProbeNetManager probeNetManager;

    protected static void upload(String jsonData, String type, boolean isGetFailed) {
        if (TextUtils.isEmpty(ProbeInitializer.getProbeUploadUrl())) {
            ProbeUtils.cacheDate(jsonData, ProbeConstant.CACHE_DATA);
            ProbeUtils.cacheDate(type, ProbeConstant.CACHE_CODE);
            return;
        }

        if (probeNetManager == null) {
            probeNetManager = new ProbeNetManager();
        }

        if (!TextUtils.isEmpty(ProbeUtils.loadCacheData(ProbeConstant.CACHE_DATA)) && ProbeUtils.loadCacheData(ProbeInitializer.getBusinessCode()).equals(ProbeConstant.ONE)) {
            probeUploadData(ProbeUtils.loadCacheData(ProbeConstant.CACHE_DATA), ProbeUtils.loadCacheData(ProbeConstant.CACHE_CODE));
        }

        if (!isGetFailed) {
            probeUploadData(jsonData, type);
        }
    }

    private static void probeUploadData(final String jsonData, final String type) {
        if (ProbeUtils.loadCacheData(ProbeInitializer.getBusinessCode()).equals(ProbeConstant.ONE)) {
            probeNetManager.post(ProbeInitializer.getProbeUploadUrl() + ProbeUtils.uploadTypeUrl(type), jsonData, new ProbeNetManager.Callback() {
                @Override
                public void onSuccess(String res) {
                    File fileData = new File(ProbeInitializer.getContext().getFilesDir(), ProbeConstant.CACHE_DATA);
                    File fileCode = new File(ProbeInitializer.getContext().getFilesDir(), ProbeConstant.CACHE_CODE);
                    if (fileData.exists() || fileCode.exists() || fileCode.isFile() || fileData.isFile()) {
                        fileData.delete();
                        fileCode.delete();
                    }
                }

                @Override
                public void onFailed(String err) {
                    ProbeUtils.cacheDate(jsonData, ProbeConstant.CACHE_DATA);
                    ProbeUtils.cacheDate(type, ProbeConstant.CACHE_CODE);
                }
            });
        }
    }
}
