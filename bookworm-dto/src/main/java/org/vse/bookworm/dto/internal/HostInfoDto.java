package org.vse.bookworm.dto.internal;

public class HostInfoDto {
    private String host;
    private int port;
    private int shardNum;

    public String getHost() {
        return host;
    }

    public HostInfoDto setHost(String host) {
        this.host = host;
        return this;
    }

    public int getShardNum() {
        return shardNum;
    }

    public HostInfoDto setShardNum(int shardNum) {
        this.shardNum = shardNum;
        return this;
    }

    public int getPort() {
        return port;
    }

    public HostInfoDto setPort(int port) {
        this.port = port;
        return this;
    }
}
