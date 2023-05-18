package com.bjbt.dialup;

class ProbeException {

    private static final ProbeSystemData systemData = new ProbeSystemData();
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
        probeExceptionData.setFactory(systemData.getDeviceBrand());
        probeExceptionData.setModel(systemData.getDeviceModel());
        probeExceptionData.setOperatingSystem(systemData.getOperatingSystem());
        probeExceptionData.setOperatingSystemVersion(systemData.getSystemVersion());
        probeExceptionData.setClientId(ProbeUtils.md5(probeExceptionData.getClientId()));
        probeExceptionData.setAppName(systemData.getAppName());
        probeExceptionData.setAppVersion(systemData.getVersionName());
        probeExceptionData.setNetFlag(systemData.getNetFlag());
        probeExceptionData.setCarrierName(systemData.getOperators());

        if (probeExceptionData.getClientId().length() > 16){
            probeExceptionData.setPasswordFlag("2");
        }else {
            probeExceptionData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeExceptionData.toJson(),"-1",probeExceptionData.getBusinessCode());
    }
}
