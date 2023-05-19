package com.bjbt.dialup;


import java.io.File;

class ProbeLong {

    private static ProbeLongData probeLongData;

    protected static void longProcessReport(String businessCode, String clientId, String eventCode, String eventName, String eventType, String eventTime, String indexCode, String indexValue, String eventStatus, String exceptionCode, String exceptionMsg, String apiTime) {
        if (probeLongData == null){
            probeLongData = new ProbeLongData();
        }

        probeLongData.setBusinessCode(businessCode);
        probeLongData.setClientId(clientId);
        probeLongData.setEventType(eventType);
        probeLongData.setEventName(eventName);
        probeLongData.setEventTime(eventTime);
        probeLongData.setEventCode(eventCode);
        probeLongData.setIndexCode(indexCode);
        probeLongData.setIndexValue(indexValue);
        probeLongData.setExceptionCode(exceptionCode);
        probeLongData.setExceptionMsg(exceptionMsg);
        probeLongData.setEventStatus(eventStatus);
        probeLongData.setApiTime(apiTime);

        probeUploadLongProcessReport();
    }

    private static void probeUploadLongProcessReport() {
        if (probeLongData.getEventType().equals("1")) {
            long currentTime = System.currentTimeMillis();
            probeLongData.setEventSeq(String.valueOf(currentTime));
            ProbeUtils.cacheDate(String.valueOf(currentTime), probeLongData.getBusinessCode()+"longProcessReport"+probeLongData.getEventCode());
        } else {
            File longSeq = new File(ProbeInitializer.getContext().getFilesDir(),probeLongData.getBusinessCode()+"longProcessReport"+probeLongData.getEventCode());
            if (longSeq.exists()){
                probeLongData.setEventSeq(ProbeUtils.loadCacheData(probeLongData.getBusinessCode()+"longProcessReport"+probeLongData.getEventCode()));
            }else {
                probeLongData.setEventSeq(String.valueOf(System.currentTimeMillis()));
            }
        }

        probeLongData.setFactory(ProbeSystemData.deviceBrand);
        probeLongData.setModel(ProbeSystemData.deviceModel);
        probeLongData.setOperatingSystem(ProbeSystemData.operatingSystem);
        probeLongData.setOperatingSystemVersion(ProbeSystemData.systemVersion);
        probeLongData.setClientId(ProbeUtils.md5(probeLongData.getClientId()));
        probeLongData.setAppName(ProbeSystemData.appName);
        probeLongData.setAppVersion(ProbeSystemData.versionName);
        probeLongData.setNetFlag(ProbeSystemData.netFlag);
        probeLongData.setCarrierName(ProbeSystemData.operators);

        if (probeLongData.getClientId().length() > 16){
            probeLongData.setPasswordFlag("2");
        }else {
            probeLongData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeLongData.toJson(),ProbeInitializer.getContext().getString(R.string.long_type),probeLongData.getBusinessCode());
    }
}
