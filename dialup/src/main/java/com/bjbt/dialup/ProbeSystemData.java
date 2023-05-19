package com.bjbt.dialup;

class ProbeSystemData {

    protected static String systemVersion;
    protected static String deviceModel;
    protected static String deviceBrand;
    protected static String operatingSystem;
    protected static String operators;
    protected static String netFlag;
    protected static String appName;
    protected static String versionName;


    public void setSystemVersion(String systemVersion) {
        ProbeSystemData.systemVersion = systemVersion;
    }

    public void setDeviceModel(String deviceModel) {
        ProbeSystemData.deviceModel = deviceModel;
    }

    public void setDeviceBrand(String deviceBrand) {
        ProbeSystemData.deviceBrand = deviceBrand;
    }

    public void setOperatingSystem(String operatingSystem) {
        ProbeSystemData.operatingSystem = operatingSystem;
    }

    public void setOperators(String operators) {
        ProbeSystemData.operators = operators;
    }

    public void setNetFlag(String netFlag) {
        ProbeSystemData.netFlag = netFlag;
    }

    public void setAppName(String appName) {
        ProbeSystemData.appName = appName;
    }

    public void setVersionName(String versionName) {
        ProbeSystemData.versionName = versionName;
    }
}
