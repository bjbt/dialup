package com.bjbt.dialup;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class ProbePing {

    private static List<String> pingData;
    private static ProbePingData probePingData;
    private static final ProbeSystemData systemData = new ProbeSystemData();

    protected static void ping(String businessCode, String clientId, String ip, String eventCode, String eventName, String eventType, String apiTime) {
        probeInitPingData();

        probePingData.setBusinessCode(businessCode);
        probePingData.setClientId(clientId);
        probePingData.setEventCode(eventCode);
        probePingData.setEventName(eventName);
        probePingData.setEventType(eventType);
        probePingData.setApiTime(apiTime);
        probePingData.setPurposeAddress(ip);

        probeCheckPingClient(ip);
    }

    private static void probeInitPingData() {
        if (pingData == null){
            pingData = new ArrayList<>();
        }

        if (probePingData == null){
            probePingData = new ProbePingData();
        }
    }

    private static List<String> probeCheckPingClient(String pingIp) {


        pingData.clear();
        String pingResult;
        String resultLine;
        Process process = null;
        BufferedReader successReader = null;
        String command = "ping -c 3 " + pingIp;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                pingData.add("Fail:process is null");
                return pingData;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((resultLine = successReader.readLine()) != null) {
                pingData.add(successReader.readLine());
                if (resultLine.contains("packet loss")) {
                    pingData.add(resultLine);
                }
            }
            pingResult = pingData.toString().replaceAll("\\[|\\]", "");
            uploadPingResult(pingResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return pingData;
    }


    private static void uploadPingResult(String result) {
        probePingData.setFactory(systemData.getDeviceBrand());
        probePingData.setModel(systemData.getDeviceModel());
        probePingData.setOperatingSystem(systemData.getOperatingSystem());
        probePingData.setOperatingSystemVersion(systemData.getSystemVersion());
        probePingData.setClientId(ProbeUtils.md5(probePingData.getClientId()));
        probePingData.setAppName(systemData.getAppName());
        probePingData.setAppVersion(systemData.getVersionName());
        probePingData.setNetFlag(systemData.getNetFlag());
        probePingData.setCarrierName(systemData.getOperators());
        probePingData.setResult(result);

        if (probePingData.getClientId().length() > 16){
            probePingData.setPasswordFlag("2");
        }else {
            probePingData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probePingData.toJson(),ProbeInitializer.getContext().getString(R.string.ping_type),probePingData.getBusinessCode());
    }
}
