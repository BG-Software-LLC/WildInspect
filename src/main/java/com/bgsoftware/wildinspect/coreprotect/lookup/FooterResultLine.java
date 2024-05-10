package com.bgsoftware.wildinspect.coreprotect.lookup;

public class FooterResultLine implements LookupResultLine {

    private final int page;

    public FooterResultLine(int page) {
        this.page = page;
    }

    @Override
    public Type getType() {
        return Type.FOOTER;
    }

    public int getPage() {
        return page;
    }

}
