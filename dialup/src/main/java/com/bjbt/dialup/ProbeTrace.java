package com.bjbt.dialup;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class ProbeTrace {

    private static final int MAX_TTL = 30;
    private static final String PING = "PING";
    private static final String FROM_PING = "From";
    private static final String SMALL_FROM_PING = "from";
    private static final String PARENTHESE_OPEN_PING = "(";
    private static final String PARENTHESE_CLOSE_PING = ")";
    private static final String TIME_PING = "time=";
    private static final String EXCEED_PING = "exceed";
    private static final String UNREACHABLE_PING = "100%";
    private static boolean isEmpty = false;
    private static int ttl = 1;
    private static String ipToPing;
    private static float elapsedTime;

    private static final List<ProbeTraceEntity> traces = new ArrayList();
    private static final List<String> traceResult = new ArrayList<>();
    private static final ProbeTraceData probeTraceData = new ProbeTraceData();

    protected static void trace(String businessCode, String clientId, String ip, String eventCode, String eventName, String eventType, String apiTime) {
        probeTraceData.setBusinessCode(businessCode);
        probeTraceData.setClientId(clientId);
        probeTraceData.setEventCode(eventCode);
        probeTraceData.setEventName(eventName);
        probeTraceData.setEventType(eventType);
        probeTraceData.setApiTime(apiTime);
        probeTraceData.setPurposeAddress(ip);
        new ExecuteTraceAsyncTask(MAX_TTL, ip).execute();
    }

    private static List<ProbeTraceEntity> showResultInLog() {
        traceResult.clear();
        for (ProbeTraceEntity container : traces) {
            traceResult.add(container.getIp());
        }
        uploadTraceResult(traceResult.toString().replaceAll("\\[|\\]", ""));
        ttl = 1;
        return traces;
    }

    private static class ExecuteTraceAsyncTask extends AsyncTask<Void, Void, String> {
        ProbeTraceEntity trace;
        private int maxTtl;
        private String url;

        public ExecuteTraceAsyncTask(int maxTtl, String url) {
            this.maxTtl = maxTtl;
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            String res = "";
            try {
                res = launchPing(url);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (res.contains(UNREACHABLE_PING) && !res.contains(EXCEED_PING)) {
                trace = new ProbeTraceEntity("", parseIpFromPing(res),
                        elapsedTime);
            } else {
                trace = new ProbeTraceEntity("", parseIpFromPing(res),
                        ttl == maxTtl ? Float
                                .parseFloat(parseTimeFromPing(res))
                                : elapsedTime);
            }

            traces.add(trace);
            return res;
        }

        private String launchPing(String url) throws IOException {
            Process p;
            String command;
            String format = "ping -c 1 -t %d ";
            command = String.format(format, ttl);
            long startTime = System.nanoTime();
            p = Runtime.getRuntime().exec(command + url);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s + "\n";
                if (s.contains(FROM_PING) || s.contains(SMALL_FROM_PING)) {
                    elapsedTime = (System.nanoTime() - startTime) / 1000000.0f;
                }
            }
            p.destroy();

            if (res.equals("")) {
                isEmpty = true;
            }
            if (ttl == 1) {
                ipToPing = parseIpToPingFromPing(res);
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if (TextUtils.isEmpty(result)) {
                return;
            }
            if (traces.get(traces.size() - 1).getIp().equals(ipToPing)) {
                if (ttl < maxTtl) {
                    ttl = maxTtl;
                    traces.remove(traces.size() - 1);
                    new ExecuteTraceAsyncTask(maxTtl, url).execute();
                } else {
                    showResultInLog();
                }
            } else {
                if (ttl < maxTtl) {
                    ttl++;
                    new ExecuteTraceAsyncTask(maxTtl, url).execute();
                }
            }
            super.onPostExecute(result);
        }
    }

    private static String parseIpFromPing(String ping) {
        String ip = null;
        if (ping.contains(FROM_PING)) {
            int index = ping.indexOf(FROM_PING);

            ip = ping.substring(index + 5);
            if (ip.contains(PARENTHESE_OPEN_PING)) {
                int indexOpen = ip.indexOf(PARENTHESE_OPEN_PING);
                int indexClose = ip.indexOf(PARENTHESE_CLOSE_PING);

                ip = ip.substring(indexOpen + 1, indexClose);
            } else {
                ip = ip.substring(0, ip.indexOf("\n"));
                if (ip.contains(":")) {
                    index = ip.indexOf(":");
                } else {
                    index = ip.indexOf(" ");
                }

                ip = ip.substring(0, index);
            }
        } else {
            int indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
            int indexClose = ping.indexOf(PARENTHESE_CLOSE_PING);
            if (!isEmpty) {
                ip = ping.substring(indexOpen + 1, indexClose);
                isEmpty = false;
            } else {
                uploadTraceResult(null);
            }
        }
        return ip;
    }

    private static String parseIpToPingFromPing(String ping) {
        String ip = "";
        if (ping.contains(PING)) {
            int indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
            int indexClose = ping.indexOf(PARENTHESE_CLOSE_PING);

            ip = ping.substring(indexOpen + 1, indexClose);
        }
        return ip;
    }

    private static String parseTimeFromPing(String ping) {
        String time = "";
        if (ping.contains(TIME_PING)) {
            int index = ping.indexOf(TIME_PING);

            time = ping.substring(index + 5);
            index = time.indexOf(" ");
            time = time.substring(0, index);
        }

        return time;
    }

    private static void uploadTraceResult(String result) {
        probeTraceData.setFactory(ProbeSystemData.deviceBrand);
        probeTraceData.setModel(ProbeSystemData.deviceModel);
        probeTraceData.setOperatingSystem(ProbeSystemData.operatingSystem);
        probeTraceData.setOperatingSystemVersion(ProbeSystemData.systemVersion);
        probeTraceData.setClientId(ProbeUtils.md5(probeTraceData.getClientId()));
        probeTraceData.setAppName(ProbeSystemData.appName);
        probeTraceData.setAppVersion(ProbeSystemData.versionName);
        probeTraceData.setNetFlag(ProbeSystemData.netFlag);
        probeTraceData.setCarrierName(ProbeSystemData.operators);
        probeTraceData.setResult(result);

        if (probeTraceData.getClientId().length() > 16) {
            probeTraceData.setPasswordFlag("2");
        } else {
            probeTraceData.setPasswordFlag("1");
        }

        ProbeUploadData.upload(probeTraceData.toJson(), ProbeInitializer.getContext().getString(R.string.trace_type), probeTraceData.getBusinessCode());
    }
}
