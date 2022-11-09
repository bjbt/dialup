package com.bjbt.dialup;


public class ProbeController {

    public static void ping(String businessCode, String clientId, String ip, String eventCode, String eventName, String eventType, String apiTime){
        ProbePing.ping(businessCode,clientId,ip,eventCode,eventName,eventType,apiTime);
    }

    public static void dns(String businessCode, String clientId, String ip, String eventCode, String eventName, String eventType, String apiTime){
        ProbeDns.dns(businessCode,clientId,ip,eventCode,eventName,eventType,apiTime);
    }

    public static void trace(String businessCode, String clientId, String ip, String eventCode, String eventName, String eventType, String apiTime){
        ProbeTrace.trace(businessCode,clientId,ip,eventCode,eventName,eventType,apiTime);
    }

    public static void shortProcessReport(String businessCode, String clientId, String eventCode, String eventName, String eventType, String eventValue, String eventStatus, String exceptionCode, String exceptionMsg, String apiTime){
        ProbeShort.shortProcessReport(businessCode,clientId,eventCode,eventName,eventType,eventValue,eventStatus,exceptionCode,exceptionMsg,apiTime);
    }

    public static void longProcessReport(String businessCode, String clientId, String eventCode, String eventName, String eventType, String eventTime, String indexCode, String indexValue, String eventStatus, String exceptionCode, String exceptionMsg, String apiTime){
        ProbeLong.longProcessReport(businessCode,clientId,eventCode,eventName,eventType,eventTime,indexCode,indexValue,eventStatus,exceptionCode,exceptionMsg,apiTime);
    }

    public static void exceptionReport(String businessCode, String clientId, String eventCode, String eventName, String indexCode, String indexValue, String exceptionCode, String exceptionMsg, String apiTime){
        ProbeException.exceptionReport(businessCode,clientId,eventCode,eventName,indexCode,indexValue,exceptionCode,exceptionMsg,apiTime);
    }
}
