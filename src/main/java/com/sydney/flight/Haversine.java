package com.sydney.flight;

/**
 * Haversine
 *
 * @author zhibin.wang
 * @since 2021/04/12 14:25
 */
public class Haversine {
    // In kilometers
    public static final double R = 6371;

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
    public static void main(String[] args) {
        System.out.println(haversine(52.5, 13.15, 40.7, -74.26));
    }
}
