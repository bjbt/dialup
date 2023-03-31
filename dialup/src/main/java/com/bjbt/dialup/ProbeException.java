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
        String netFlag = ProbeSystemParam.getNetFlag(ProbeInitializer.getContext());
        probeExceptionData.setFactory(ProbeSystemParam.getDeviceBrand());
        probeExceptionData.setModel(ProbeSystemParam.getDeviceModel());
        probeExceptionData.setOperatingSystem(ProbeSystemParam.getOperatingSystem());
        probeExceptionData.setOperatingSystemVersion(ProbeSystemParam.getSystemVersion());
        probeExceptionData.setClientId(ProbeUtils.md5(probeExceptionData.getClientId()));
        probeExceptionData.setAppName(ProbeSystemParam.getAppName(ProbeInitializer.getContext()));
        probeExceptionData.setAppVersion(ProbeSystemParam.getVersionName(ProbeInitializer.getContext()));
        probeExceptionData.setNetFlag(netFlag);
        probeExceptionData.setCarrierName(ProbeSystemParam.getOperators(ProbeInitializer.getContext()));

        if (probeExceptionData.getClientId().length() > 16){
            probeExceptionData.setPasswordFlag("2");
        }else {
            probeExceptionData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeExceptionData.toJson(),"-1",probeExceptionData.getBusinessCode());
    }
}
