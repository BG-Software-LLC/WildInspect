package com.bgsoftware.wildinspect.utils;

import java.util.Collection;

public final class StringUtils {

    public static String format(Collection<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String str : strings)
            stringBuilder.append(", ").append(str);

        return stringBuilder.substring(2);
    }

}
