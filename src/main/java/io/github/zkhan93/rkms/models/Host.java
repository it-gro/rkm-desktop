package io.github.zkhan93.rkms.models;

/**
 * Created by zeeshan on 11/20/2016.
 */
public class Host {
    String name;
    String ip;
    int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Host(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
