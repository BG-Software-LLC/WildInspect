package com.bgsoftware.wildinspect.utils;

public final class StringUtils {

    public static String format(String[] strings){
        StringBuilder stringBuilder = new StringBuilder();

        for(String str : strings)
            stringBuilder.append(", ").append(str);

        return stringBuilder.substring(2);
    }

}
