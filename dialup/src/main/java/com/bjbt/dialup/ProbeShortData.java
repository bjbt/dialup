package com.bjbt.dialup;

import org.json.JSONException;
import org.json.JSONObject;

class ProbeShortData {
    private String factory;
    private String model;
    private String operatingSystem;
    private String operatingSystemVersion;
    private String token;
    private String businessCode;
    private String passwordFlag;
    private String clientId;
    private String eventSeq;
    private String appName;
    private String appVersion;
    private String netFlag;
    private String carrierName;
    private String eventCode;
    private String eventName;
    private String eventType;
    private String eventValue;
    private String eventStatus;
    private String exceptionCode;
    private String exceptionMsg;
    private String apiTime;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getOperatingSystemVersion() {
        return operatingSystemVersion;
    }

    public void setOperatingSystemVersion(String operatingSystemVersion) {
        this.operatingSystemVersion = operatingSystemVersion;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getPasswordFlag() {
        return passwordFlag;
    }

    public void setPasswordFlag(String passwordFlag) {
        this.passwordFlag = passwordFlag;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getEventSeq() {
        return eventSeq;
    }

    public void setEventSeq(String eventSeq) {
        this.eventSeq = eventSeq;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getNetFlag() {
        return netFlag;
    }

    public void setNetFlag(String netFlag) {
        this.netFlag = netFlag;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getApiTime() {
        return apiTime;
    }

    public void setApiTime(String apiTime) {
        this.apiTime = apiTime;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("factory", getFactory());
            jsonObject.put("model", getModel());
            jsonObject.put("operatingSystem", getOperatingSystem());
            jsonObject.put("operatingSystemVersion", getOperatingSystemVersion());
            jsonObject.put("token", ProbeInitializer.getContext().getString(R.string.t_k));
            jsonObject.put("businessCode", getBusinessCode());
            jsonObject.put("passwordFlag", getPasswordFlag());
            jsonObject.put("clientId", getClientId());
            jsonObject.put("eventSeq", getEventSeq());
            jsonObject.put("eventCode", getEventCode());
            jsonObject.put("appName", getAppName());
            jsonObject.put("appVersion", getAppVersion());
            jsonObject.put("netFlag", getNetFlag());
            jsonObject.put("carrierName", getCarrierName());
            jsonObject.put("eventName", getEventName());
            jsonObject.put("eventType", getEventType());
            jsonObject.put("eventValue", getEventValue());
            jsonObject.put("eventStatus", getEventStatus());
            jsonObject.put("exceptionCode", getExceptionCode());
            jsonObject.put("exceptionMsg", getExceptionMsg());
            jsonObject.put("apiTime", getApiTime());
            jsonObject.put("sdkType", "2");
            jsonObject.put("sdkVersion", ProbeConstant.VERSION);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
