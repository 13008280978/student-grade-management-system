package cn.course.grade.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class GpaTool {
    private GpaTool() {
    }

    public static BigDecimal point(BigDecimal score) {
        if (score == null || score.compareTo(BigDecimal.valueOf(60)) < 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        BigDecimal raw = score.subtract(BigDecimal.valueOf(50)).divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP);
        if (raw.compareTo(BigDecimal.valueOf(5)) > 0) {
            return BigDecimal.valueOf(5).setScale(2);
        }
        return raw.setScale(2, RoundingMode.HALF_UP);
    }

    public static String level(BigDecimal score) {
        if (score == null) {
            return "N/A";
        }
        int value = score.intValue();
        if (value >= 90) {
            return "A";
        }
        if (value >= 80) {
            return "B";
        }
        if (value >= 70) {
            return "C";
        }
        if (value >= 60) {
            return "D";
        }
        return "F";
    }
}
