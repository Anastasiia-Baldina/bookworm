package org.vse.bookworm.service.properties;

public class SessionProperties {
    private int maxLoginCodeSize;
    private int inactivationIntervalSec;

    public int getInactivationIntervalSec() {
        return inactivationIntervalSec;
    }

    public void setInactivationIntervalSec(int inactivationIntervalSec) {
        this.inactivationIntervalSec = inactivationIntervalSec;
    }

    public int getMaxLoginCodeSize() {
        return maxLoginCodeSize;
    }

    public void setMaxLoginCodeSize(int maxLoginCodeSize) {
        this.maxLoginCodeSize = maxLoginCodeSize;
    }
}
