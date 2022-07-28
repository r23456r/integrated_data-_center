package com.idc.common.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getFullUUID() {
        return UUID.randomUUID().toString();
    }
}
