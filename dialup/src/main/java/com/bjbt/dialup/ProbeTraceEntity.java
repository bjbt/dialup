package com.bjbt.dialup;

class ProbeTraceEntity {

    private String ip;
    private float time;

    public ProbeTraceEntity(String s, String s1, float elapsedTime) {
        ip=s1;
        this.time=elapsedTime;
    }


    public String getIp() {
        return ip;
    }

}
