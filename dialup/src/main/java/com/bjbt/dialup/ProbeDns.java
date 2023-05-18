package com.bjbt.dialup;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

class ProbeDns {

    private static final ProbeSystemData systemData = new ProbeSystemData();
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
        probeDnsData.setPurposeAddress(ip);

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

    protected static void probeCheckConnectOfDNS(final String hostname) {
        List<String> result = new ArrayList<>();
        try {
            dnsNetInfoEntity.setIps(null);
            DNSParse parse = new DNSParse(hostname);
            Thread thread = new Thread(parse);
            thread.start();
            thread.join(3 * 1000);
            InetAddress[] resCode = parse.get();
            if (resCode != null) {
                for (InetAddress address : resCode) {
                    result.add(address.getHostAddress());
                    dnsNetInfoEntity.setIps(address.getHostAddress());
                }
            } else {
                result = null;
            }
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        assert result != null;
        uploadDNSResult(result.toString().replaceAll("[\\[\\]]", ""));
    }

    private static class DNSParse implements Runnable {
        private final String hostname;
        private InetAddress[] address;

        public DNSParse(String hostname) {
            this.hostname = hostname;
        }

        public void run() {
            try {
                set(InetAddress.getAllByName(hostname));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void set(InetAddress[] address) {
            this.address = address;
        }

        public synchronized InetAddress[] get() {
            return address;
        }
    }

    private static void uploadDNSResult(String result) {
        probeDnsData.setFactory(systemData.getDeviceBrand());
        probeDnsData.setModel(systemData.getDeviceModel());
        probeDnsData.setOperatingSystem(systemData.getOperatingSystem());
        probeDnsData.setOperatingSystemVersion(systemData.getSystemVersion());
        probeDnsData.setClientId(ProbeUtils.md5(probeDnsData.getClientId()));
        probeDnsData.setAppName(systemData.getAppName());
        probeDnsData.setAppVersion(systemData.getVersionName());
        probeDnsData.setNetFlag(systemData.getNetFlag());
        probeDnsData.setCarrierName(systemData.getOperators());
        probeDnsData.setResult(result);

        if (probeDnsData.getClientId().length() > 16){
            probeDnsData.setPasswordFlag("2");
        }else {
            probeDnsData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeDnsData.toJson(),ProbeInitializer.getContext().getString(R.string.dns_type),probeDnsData.getBusinessCode());
    }
}
