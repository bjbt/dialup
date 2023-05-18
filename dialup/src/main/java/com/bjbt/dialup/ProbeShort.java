package com.bjbt.dialup;


import java.io.File;

class ProbeShort {

    private static ProbeShortData probeShortData;
    private static final ProbeSystemData systemData = new ProbeSystemData();

    protected static void shortProcessReport(String businessCode, String clientId, String eventCode, String eventName, String eventType, String eventValue, String eventStatus,
                                             String exceptionCode, String exceptionMsg, String apiTime) {
        if (probeShortData == null) {
            probeShortData = new ProbeShortData();
        }

        probeShortData.setBusinessCode(businessCode);
        probeShortData.setClientId(clientId);
        probeShortData.setEventCode(eventCode);
        probeShortData.setEventType(eventType);
        probeShortData.setEventName(eventName);
        probeShortData.setEventValue(eventValue);
        probeShortData.setEventStatus(eventStatus);
        probeShortData.setExceptionCode(exceptionCode);
        probeShortData.setExceptionMsg(exceptionMsg);
        probeShortData.setApiTime(apiTime);

        probeUploadProcessReport();
    }

    protected static void probeUploadProcessReport() {
        if (probeShortData.getEventType().equals("1")) {
            long currentTime = System.currentTimeMillis();
            probeShortData.setEventSeq(String.valueOf(currentTime));
            ProbeUtils.cacheDate(String.valueOf(currentTime), probeShortData.getBusinessCode() + "shortProcessReport" + probeShortData.getEventCode());
        } else {
            File shortSeq = new File(ProbeInitializer.getContext().getFilesDir(), probeShortData.getBusinessCode() + "shortProcessReport" + probeShortData.getEventCode());
            if (shortSeq.exists()) {
                probeShortData.setEventSeq(ProbeUtils.loadCacheData(probeShortData.getBusinessCode() + "shortProcessReport" + probeShortData.getEventCode()));
            } else {
                probeShortData.setEventSeq(String.valueOf(System.currentTimeMillis()));
            }
        }

        probeShortData.setFactory(systemData.getDeviceBrand());
        probeShortData.setModel(systemData.getDeviceModel());
        probeShortData.setOperatingSystem(systemData.getOperatingSystem());
        probeShortData.setOperatingSystemVersion(systemData.getSystemVersion());
        probeShortData.setClientId(ProbeUtils.md5(probeShortData.getClientId()));
        probeShortData.setAppName(systemData.getAppName());
        probeShortData.setAppVersion(systemData.getVersionName());
        probeShortData.setNetFlag(systemData.getNetFlag());
        probeShortData.setCarrierName(systemData.getOperators());

        if (probeShortData.getClientId().length() > 16) {
            probeShortData.setPasswordFlag("2");
        } else {
            probeShortData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeShortData.toJson(), ProbeInitializer.getContext().getString(R.string.short_type), probeShortData.getBusinessCode());
    }
}
