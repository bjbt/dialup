package com.bjbt.dialup;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

class ProbeNetManager {

    private static String requestMethod;
    private static boolean useCaches;
    private static String serverUrl;
    private static int connectTimeout;

    protected ProbeNetManager() {
        init();
    }

    protected void get(String url,Callback callback) {
        serverUrl = url;
        handleData(null, callback);
    }

    protected void get(String url,String params, Callback callback) {
        serverUrl = url;
        handleData(params, callback);
    }

    protected void post(String url,String params, Callback callback) {
        requestMethod = ProbeConstant.POST;
        serverUrl = url;
        handleData(params,callback);
    }

    protected void init() {
        requestMethod = ProbeConstant.GET;
        useCaches = false;
        connectTimeout = 3000;
    }

    private void handleData(final String params,final Callback callback) {
        @SuppressLint("HandlerLeak") final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                callback.onSuccess((String) msg.obj);
            }
        };
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    if (new URL(serverUrl).getProtocol().toLowerCase().equals("https")){
                        submitHttpsData(params,callback, handler);
                    }else {
                        submitHttpData(params,callback,handler);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void submitHttpsData(String params, Callback callback, Handler handler) {
        try {
            URL url = new URL(serverUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            connection.setRequestMethod(requestMethod);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(connectTimeout);
            if (requestMethod.equals(ProbeConstant.POST)) {
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(useCaches);
                connection.setRequestProperty(ProbeConstant.TOKEN, ProbeInitializer.getContext().getString(R.string.t_k));
                connection.setRequestProperty(ProbeConstant.CONTENT_TYPE, ProbeConstant.CONTENT_TYPE_VALUE);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(params.getBytes(StandardCharsets.UTF_8));
            }
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Message message = new Message();
                message.obj = dealResponseResult(inputStream);
                handler.sendMessage(message);
            } else {
                callback.onFailed("Error: response is" + response);
            }
        } catch (Exception e) {
            callback.onFailed("Error: "+e);
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void submitHttpData(String params, Callback callback, Handler handler) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(connectTimeout);
            if (requestMethod.equals(ProbeConstant.POST)) {
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(useCaches);
                connection.setRequestProperty(ProbeConstant.TOKEN, ProbeInitializer.getContext().getString(R.string.t_k));
                connection.setRequestProperty(ProbeConstant.CONTENT_TYPE, ProbeConstant.CONTENT_TYPE_VALUE);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(params.getBytes(StandardCharsets.UTF_8));
            }
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Message message = new Message();
                message.obj = dealResponseResult(inputStream);
                handler.sendMessage(message);
            } else {
                callback.onFailed("Error: response is" + response);
            }
        } catch (Exception e) {
            callback.onFailed("Error: "+e);
            e.printStackTrace();
        }
    }

    private static String dealResponseResult(InputStream inputStream) {
        String resultData;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    protected interface Callback {
        void onSuccess(String res);

        void onFailed(String err);
    }
}
