package com.framgia.englishforkids.util;

/**
 * Created by GIAKHANH on 1/2/2017.
 */
public class Convert {
    public static String convertTime(int duration) {
        if (duration < 0) return "";
        return (duration / 3600 > 0) ?
            String.format("%d:%d:%d", duration / 3600, duration % 3600 / 60,
                duration % 3600 % 60) : String.format("%d:%d", duration / 60, duration % 60);
    }
}
