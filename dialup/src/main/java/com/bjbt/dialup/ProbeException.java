package com.bjbt.dialup;

class ProbeException {

    private static ProbeExceptionData probeExceptionData;

    protected static void exceptionReport(String businessCode, String clientId, String eventCode, String eventName, String indexCode, String indexValue, String exceptionCode, String exceptionMsg, String apiTime) {
        if (probeExceptionData == null){
            probeExceptionData = new ProbeExceptionData();
        }

        probeExceptionData.setBusinessCode(businessCode);
        probeExceptionData.setClientId(clientId);
        probeExceptionData.setEventName(eventName);
        probeExceptionData.setEventCode(eventCode);
        probeExceptionData.setIndexCode(indexCode);
        probeExceptionData.setIndexValue(indexValue);
        probeExceptionData.setExceptionCode(exceptionCode);
        probeExceptionData.setExceptionMsg(exceptionMsg);
        probeExceptionData.setApiTime(apiTime);

        probeUploadExceptionReport();
    }

    private static void probeUploadExceptionReport() {
        probeExceptionData.setFactory(ProbeSystemData.deviceBrand);
        probeExceptionData.setModel(ProbeSystemData.deviceModel);
        probeExceptionData.setOperatingSystem(ProbeSystemData.operatingSystem);
        probeExceptionData.setOperatingSystemVersion(ProbeSystemData.systemVersion);
        probeExceptionData.setClientId(ProbeUtils.md5(probeExceptionData.getClientId()));
        probeExceptionData.setAppName(ProbeSystemData.appName);
        probeExceptionData.setAppVersion(ProbeSystemData.versionName);
        probeExceptionData.setNetFlag(ProbeSystemData.netFlag);
        probeExceptionData.setCarrierName(ProbeSystemData.operators);

        if (probeExceptionData.getClientId().length() > 16){
            probeExceptionData.setPasswordFlag("2");
        }else {
            probeExceptionData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeExceptionData.toJson(),"-1",probeExceptionData.getBusinessCode());
    }
}
