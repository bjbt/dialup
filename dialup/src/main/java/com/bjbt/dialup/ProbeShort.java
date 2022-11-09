package com.bjbt.dialup;

import android.text.TextUtils;

class ProbeShort {


    private static ProbeShortData probeShortData;

    protected static void shortProcessReport(String businessCode, String clientId, String eventCode, String eventName, String eventType, String eventValue, String eventStatus,
                                             String exceptionCode, String exceptionMsg, String apiTime) {
        if (probeShortData == null){
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
        String netFlag = ProbeSystemParam.getNetFlag(ProbeInitializer.getContext());
        if (probeShortData.getEventType().equals("1")) {
            probeShortData.setEventSeq(String.valueOf(System.currentTimeMillis()));
            ProbeUtils.cacheDate(String.valueOf(System.currentTimeMillis()), ProbeInitializer.getContext().getString(R.string.s_s_t));
        } else {
            if (TextUtils.isEmpty(ProbeUtils.loadCacheData(ProbeInitializer.getContext().getString(R.string.s_s_t)))) {
                probeShortData.setEventSeq(String.valueOf(System.currentTimeMillis()));
            } else {
                probeShortData.setEventSeq(ProbeUtils.loadCacheData(ProbeInitializer.getContext().getString(R.string.s_s_t)));
            }
        }

        probeShortData.setFactory(ProbeSystemParam.getDeviceBrand());
        probeShortData.setModel(ProbeSystemParam.getDeviceModel());
        probeShortData.setOperatingSystem(ProbeSystemParam.getOperatingSystem());
        probeShortData.setOperatingSystemVersion(ProbeSystemParam.getSystemVersion());
        probeShortData.setClientId(ProbeUtils.md5(probeShortData.getClientId()));
        probeShortData.setAppName(ProbeSystemParam.getAppName(ProbeInitializer.getContext()));
        probeShortData.setAppVersion(ProbeSystemParam.getVersionName(ProbeInitializer.getContext()));
        probeShortData.setNetFlag(netFlag);
        probeShortData.setCarrierName(ProbeSystemParam.getOperators(ProbeInitializer.getContext()));

        if (probeShortData.getClientId().length() > 16){
            probeShortData.setPasswordFlag("2");
        }else {
            probeShortData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeShortData.toJson(),ProbeInitializer.getContext().getString(R.string.s_t),false);

    }
}
