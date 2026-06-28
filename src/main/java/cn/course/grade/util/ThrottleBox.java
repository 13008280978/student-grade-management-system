package cn.course.grade.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ThrottleBox {
    private static final int LIMIT = 5;
    private static final long SPAN = 10 * 60 * 1000L;
    private static final ConcurrentMap<String, Window> WINDOWS = new ConcurrentHashMap<>();

    private ThrottleBox() {
    }

    public static boolean locked(String key) {
        Window window = WINDOWS.get(key);
        if (window == null) {
            return false;
        }
        if (System.currentTimeMillis() > window.expiresAt) {
            WINDOWS.remove(key);
            return false;
        }
        return window.count >= LIMIT;
    }

    public static void bad(String key) {
        long now = System.currentTimeMillis();
        WINDOWS.compute(key, (k, old) -> {
            if (old == null || now > old.expiresAt) {
                return new Window(1, now + SPAN);
            }
            return new Window(old.count + 1, old.expiresAt);
        });
    }

    public static void pass(String key) {
        WINDOWS.remove(key);
    }

    private static final class Window {
        private final int count;
        private final long expiresAt;

        private Window(int count, long expiresAt) {
            this.count = count;
            this.expiresAt = expiresAt;
        }
    }
}
