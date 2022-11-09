package com.bjbt.dialup;

class ProbeConstant {

    public static final String SDK_VERSION = "sdkVersion";
    public static final String VERSION = "1.3.0";
    //数据上报接口
    static final String PING_SERVER = "sdk/ping?sdkType=2&sdkVersion=1.3.0";
    static final String DNS_SERVER = "sdk/dns?sdkType=2&sdkVersion=1.3.0";
    static final String TRACE_SERVER = "sdk/trace?sdkType=2&sdkVersion=1.3.0";
    static final String SHORT_PROCESS_REPORT_SERVER = "sdk/shortProcessReport?sdkType=2&sdkVersion=1.3.0";
    static final String LONG_PROCESS_REPORT_SERVER = "sdk/longProcessReport?sdkType=2&sdkVersion=1.3.0";
    static final String EXCEPTION_SERVER = "sdk/exceptionReport?sdkType=2&sdkVersion=1.3.0";
    //获取地址结果解析key
    static final String DATA = "data";
    static final String ZERO = "0";
    static final String CODE = "code";
    static final String UPLOAD = "upload";
    static final String ONE = "1";
    //缓存失败数据key，value
    static final String CACHE_DATA = "cache_data";
    static final String CACHE_CODE = "cache_code";
    static final String CACHE_FIRST_CODE = "cache_first_code";
    //缓存系统参数key
    static final String BUSINESS_CODE_SYSTEM = "system";
    //网络请求参数
    static final String TOKEN = "token";
    static final String POST = "POST";
    static final String GET = "GET";
    static final String CONTENT_TYPE = "Content-Type";
    static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";
}
