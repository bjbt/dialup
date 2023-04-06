package com.bjbt.dialup;

public class ProbeConstant {

    //数据上报接口
    public static final String PING_SERVER = "sdk/ping?sdkType=2&sdkVersion=1.3.3";
    public static final String DNS_SERVER = "sdk/dns?sdkType=2&sdkVersion=1.3.3";
    public static final String TRACE_SERVER = "sdk/trace?sdkType=2&sdkVersion=1.3.3";
    public static final String SHORT_PROCESS_REPORT_SERVER = "sdk/shortProcessReport?sdkType=2&sdkVersion=1.3.3";
    public static final String LONG_PROCESS_REPORT_SERVER = "sdk/longProcessReport?sdkType=2&sdkVersion=1.3.3";
    public static final String EXCEPTION_SERVER = "sdk/exceptionReport?sdkType=2&sdkVersion=1.3.3";

    //获取地址结果解析key
    public static final String ZERO = "0";
    public static final String CODE = "code";
    public static final String UPLOAD = "upload";

    //缓存失败数据key，value
    public static final String CACHE_DATA = "cache_data";
    public static final String CACHE_CODE = "cache_code";

    //缓存系统参数key
    public static final String BUSINESS_CODE_SYSTEM = "system";

    //网络请求参数
    public static final String TOKEN = "token";
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";
}
