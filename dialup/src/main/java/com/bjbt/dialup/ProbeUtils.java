package com.bjbt.dialup;

import android.content.Context;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class ProbeUtils {

    protected static void cacheDate(String data, String business_code) {
        FileOutputStream out;
        BufferedWriter writer = null;
        try {
            out = ProbeInitializer.getContext().openFileOutput(business_code, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected static String loadCacheData(String business_code) {
        FileInputStream in;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = ProbeInitializer.getContext().openFileInput(business_code);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    protected static String md5(String string) {

        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    protected static String uploadTypeUrl(String urlType) {
        if (urlType.equals(ProbeInitializer.getContext().getString(R.string.ping_type))) {
            return ProbeConstant.PING_SERVER;
        } else if (urlType.equals(ProbeInitializer.getContext().getString(R.string.dns_type))) {
            return ProbeConstant.DNS_SERVER;
        } else if (urlType.equals(ProbeInitializer.getContext().getString(R.string.trace_type))) {
            return ProbeConstant.TRACE_SERVER;
        } else if (urlType.equals(ProbeInitializer.getContext().getString(R.string.short_type))) {
            return ProbeConstant.SHORT_PROCESS_REPORT_SERVER;
        } else if (urlType.equals(ProbeInitializer.getContext().getString(R.string.long_type))) {
            return ProbeConstant.LONG_PROCESS_REPORT_SERVER;
        } else {
            return ProbeConstant.EXCEPTION_SERVER;
        }
    }
}
