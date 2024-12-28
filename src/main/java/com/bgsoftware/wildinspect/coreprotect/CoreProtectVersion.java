package com.bgsoftware.wildinspect.coreprotect;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public enum CoreProtectVersion {

    CORE_PROTECT_12,
    CORE_PROTECT_14("12"),
    CORE_PROTECT_19,
    CORE_PROTECT_20,
    CORE_PROTECT_21,
    CORE_PROTECT_22,
    CORE_PROTECT_23("22");

    private final List<String> supportedVersions;

    CoreProtectVersion(String... additionalSupportedVersions) {
        supportedVersions = new LinkedList<>();
        supportedVersions.add(name().split("_")[2]);
        Collections.addAll(supportedVersions, additionalSupportedVersions);
        supportedVersions.sort(Comparator.reverseOrder());
    }

    public List<String> getSupportedVersions() {
        return supportedVersions;
    }

}
