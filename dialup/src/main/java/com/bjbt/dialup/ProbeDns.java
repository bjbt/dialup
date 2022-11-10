package com.bjbt.dialup;

import java.net.InetAddress;

class ProbeDns {

    private static ProbeDNSEntity dnsNetInfoEntity;
    private static ProbeDnsData probeDnsData;

    protected static void dns(String businessCode, String clientId, String ip, String eventCode, String eventName, String eventType, String apiTime) {
        probeInitDnsData();

        probeDnsData.setBusinessCode(businessCode);
        probeDnsData.setClientId(clientId);
        probeDnsData.setEventCode(eventCode);
        probeDnsData.setEventName(eventName);
        probeDnsData.setEventType(eventType);
        probeDnsData.setApiTime(apiTime);

        probeCheckConnectOfDNS(ip);
    }

    private static void probeInitDnsData() {
        if (dnsNetInfoEntity == null){
            dnsNetInfoEntity = new ProbeDNSEntity();
        }

        if (probeDnsData == null){
            probeDnsData = new ProbeDnsData();
        }
    }

    protected static ProbeDNSEntity probeCheckConnectOfDNS(final String hostname) {
        String result;
        try {
            dnsNetInfoEntity.setIps(null);
            DNSParse parse = new DNSParse(hostname);
            Thread thread = new Thread(parse);
            thread.start();
            thread.join(3 * 1000);
            InetAddress resCode = parse.get();
            if (resCode != null) {
                result = resCode.getHostAddress();
                dnsNetInfoEntity.setIps(resCode.getHostAddress());
            } else {
                result = null;
            }
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        uploadDNSResult(result);
        return dnsNetInfoEntity;
    }

    private static class DNSParse implements Runnable {
        private String hostname;
        private InetAddress address;

        public DNSParse(String hostname) {
            this.hostname = hostname;
        }

        public void run() {
            try {
                set(InetAddress.getByName(hostname));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void set(InetAddress address) {
            this.address = address;
        }

        public synchronized InetAddress get() {
            return address;
        }
    }

    private static void uploadDNSResult(String result) {
        String netFlag = ProbeSystemParam.getNetFlag(ProbeInitializer.getContext());
        probeDnsData.setFactory(ProbeSystemParam.getDeviceBrand());
        probeDnsData.setModel(ProbeSystemParam.getDeviceModel());
        probeDnsData.setOperatingSystem(ProbeSystemParam.getOperatingSystem());
        probeDnsData.setOperatingSystemVersion(ProbeSystemParam.getSystemVersion());
        probeDnsData.setClientId(ProbeUtils.md5(probeDnsData.getClientId()));
        probeDnsData.setAppName(ProbeSystemParam.getAppName(ProbeInitializer.getContext()));
        probeDnsData.setAppVersion(ProbeSystemParam.getVersionName(ProbeInitializer.getContext()));
        probeDnsData.setNetFlag(netFlag);
        probeDnsData.setCarrierName(ProbeSystemParam.getOperators(ProbeInitializer.getContext()));
        probeDnsData.setResult(result);

        if (probeDnsData.getClientId().length() > 16){
            probeDnsData.setPasswordFlag("2");
        }else {
            probeDnsData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeDnsData.toJson(),ProbeInitializer.getContext().getString(R.string.d_t),false);
    }
}
