package com.bjbt.dialup;

import android.text.TextUtils;

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
        String netFlag = ProbeSystemParam.getNetFlag(ProbeInitializer.getContext());
        if (probeLongData.getEventType().equals("1")) {
            probeLongData.setEventSeq(String.valueOf(System.currentTimeMillis()));
            ProbeUtils.cacheDate(String.valueOf(System.currentTimeMillis()), ProbeInitializer.getContext().getString(R.string.l_s_t));
        } else {
            if (TextUtils.isEmpty(ProbeUtils.loadCacheData(ProbeInitializer.getContext().getString(R.string.l_s_t)))) {
                probeLongData.setEventSeq(String.valueOf(System.currentTimeMillis()));
            } else {
                probeLongData.setEventSeq(ProbeUtils.loadCacheData(ProbeInitializer.getContext().getString(R.string.l_s_t)));
            }
        }

        probeLongData.setFactory(ProbeSystemParam.getDeviceBrand());
        probeLongData.setModel(ProbeSystemParam.getDeviceModel());
        probeLongData.setOperatingSystem(ProbeSystemParam.getOperatingSystem());
        probeLongData.setOperatingSystemVersion(ProbeSystemParam.getSystemVersion());
        probeLongData.setClientId(ProbeUtils.md5(probeLongData.getClientId()));
        probeLongData.setAppName(ProbeSystemParam.getAppName(ProbeInitializer.getContext()));
        probeLongData.setAppVersion(ProbeSystemParam.getVersionName(ProbeInitializer.getContext()));
        probeLongData.setNetFlag(netFlag);
        probeLongData.setCarrierName(ProbeSystemParam.getOperators(ProbeInitializer.getContext()));

        if (probeLongData.getClientId().length() > 16){
            probeLongData.setPasswordFlag("2");
        }else {
            probeLongData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeLongData.toJson(),ProbeInitializer.getContext().getString(R.string.l_t),false);
    }
}
