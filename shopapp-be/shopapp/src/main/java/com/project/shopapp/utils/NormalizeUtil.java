package com.project.shopapp.utils;

public class NormalizeUtil {

    // Chuẩn hóa prefix đường dẫn API
    public static String normalizePrefix(String p) {
        if (p == null || p.isEmpty()) return "/";
        String s = p.trim();
        if (!s.startsWith("/")) s = "/" + s;
        if (s.length() > 1 && s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }


    // Chuẩn hóa đường dẫn path
    public static String normalizePath(String path) {
        if (path == null || path.isEmpty()) return "/";
        String p = path.trim();
        if (!p.startsWith("/")) p = "/" + p;
        if (p.length() > 1 && p.endsWith("/")) p = p.substring(0, p.length() - 1);
        return p;
    }
}
