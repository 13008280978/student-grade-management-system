package cn.course.grade.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class DigestTool {
    private DigestTool() {
    }

    public static String sha256Hex(String text) {
        try {
            byte[] raw = MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));
            char[] hex = new char[raw.length * 2];
            char[] table = "0123456789abcdef".toCharArray();
            for (int i = 0; i < raw.length; i++) {
                int value = raw[i] & 0xff;
                hex[i * 2] = table[value >>> 4];
                hex[i * 2 + 1] = table[value & 0x0f];
            }
            return new String(hex);
        } catch (Exception ex) {
            throw new IllegalStateException("密码摘要生成失败", ex);
        }
    }
}
