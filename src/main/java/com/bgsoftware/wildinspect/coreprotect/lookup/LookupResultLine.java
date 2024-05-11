package com.bgsoftware.wildinspect.coreprotect.lookup;

public interface LookupResultLine {

    Type getType();

    enum Type {

        NO_DATA,
        HEADER,
        DATA,
        FOOTER;

    }

}
