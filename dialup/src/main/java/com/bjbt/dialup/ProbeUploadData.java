package com.bjbt.dialup;

import java.io.File;

class ProbeUploadData {

    private static ProbeNetManager probeNetManager;
    private static File fileData;
    private static File fileCode;


    protected static void upload(String jsonData, String type, String businessCode) {
        fileData = new File(ProbeInitializer.getContext().getFilesDir(), ProbeConstant.CACHE_DATA);
        fileCode = new File(ProbeInitializer.getContext().getFilesDir(), ProbeConstant.CACHE_CODE);

        if (probeNetManager == null) {
            probeNetManager = new ProbeNetManager();
        }

        if (fileData.exists() && fileCode.exists()) {
            probeUploadData(ProbeUtils.loadCacheData(ProbeConstant.CACHE_DATA), ProbeUtils.loadCacheData(ProbeConstant.CACHE_CODE), businessCode);
        }

        probeUploadData(jsonData, type, businessCode);
    }

    private static void probeUploadData(final String jsonData, final String type, final String businessCode) {
        File file = new File(ProbeInitializer.getContext().getFilesDir(), ProbeInitializer.getBusinessCode());
        String uploadUrl;
        if (file.exists()) {
            if (ProbeUtils.loadCacheData(ProbeInitializer.getBusinessCode()).equals(ProbeConstant.ZERO)) {
                return;
            }
        }

        if (businessCode.equals(ProbeInitializer.getContext().getString(R.string.hdj_code))) {
            uploadUrl = ProbeInitializer.getContext().getString(R.string.upload_server_hdj);
        } else if (ProbeInitializer.getServerType().equalsIgnoreCase("prod")) {
            uploadUrl = ProbeInitializer.getContext().getString(R.string.upload_server_formal);
        } else if (ProbeInitializer.getServerType().equalsIgnoreCase("test")) {
            uploadUrl = ProbeInitializer.getContext().getString(R.string.upload_server_test);
        } else if (ProbeInitializer.getServerType().equalsIgnoreCase("dev")) {
            uploadUrl = ProbeInitializer.getContext().getString(R.string.upload_server_develop);
        } else {
            return;
        }

        probeNetManager.post(uploadUrl + ProbeUtils.uploadTypeUrl(type), jsonData, new ProbeNetManager.Callback() {
            @Override
            public void onSuccess(String res) {
                if (fileData.exists() || fileCode.exists()) {
                    fileData.delete();
                    fileCode.delete();
                }
            }

            @Override
            public void onFailed(String err) {
                ProbeUtils.cacheDate(jsonData, ProbeConstant.CACHE_DATA);
                ProbeUtils.cacheDate(type, ProbeConstant.CACHE_CODE);
                System.out.print(err);
            }
        });
    }
}
