package com.gxdcnjq.sharedbikesmis.utils;

public class MapUtil {
    public static double[] bd09ToWgs84(double bdLon, double bdLat) {
        double[] wgs84 = new double[2];
        double x = bdLon - 0.0065, y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        wgs84[0] = z * Math.cos(theta);
        wgs84[1] = z * Math.sin(theta);
        return wgs84;
    }

    public static double[] wgs84ToBd09(double wgLon, double wgLat) {
        double[] bd09 = new double[2];
        double x = wgLon, y = wgLat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        bd09[0] = z * Math.cos(theta) + 0.0065;
        bd09[1] = z * Math.sin(theta) + 0.006;
        return bd09;
    }
}
