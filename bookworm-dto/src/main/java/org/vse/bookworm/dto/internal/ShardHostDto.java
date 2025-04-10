package org.vse.bookworm.dto.internal;

public class ShardHostDto {
    private String host;
    private int port;
    private int shardNum;

    public String getHost() {
        return host;
    }

    public ShardHostDto setHost(String host) {
        this.host = host;
        return this;
    }

    public int getShardNum() {
        return shardNum;
    }

    public ShardHostDto setShardNum(int shardNum) {
        this.shardNum = shardNum;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ShardHostDto setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return "HostInfoDto{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", shardNum=" + shardNum +
                '}';
    }
}
