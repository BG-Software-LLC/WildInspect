package com.bgsoftware.wildinspect.coreprotect.lookup;

public class HeaderResultLine implements LookupResultLine {

    private final int x;
    private final int y;
    private final int z;

    public HeaderResultLine(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Type getType() {
        return Type.HEADER;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

}
