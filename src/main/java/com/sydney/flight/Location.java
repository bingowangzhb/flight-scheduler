package com.sydney.flight;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Location
 *
 * @author zhibin.wang
 * @since 2021/04/12 13:34
 */
public class Location {

    private String locationName;
    private String latitude;
    private String longitude;
    private BigDecimal demandCoefficient;

    public Location() {
    }

    public Location(String locationName, String latitude, String longitude, BigDecimal demandCoefficient) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.demandCoefficient = demandCoefficient;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getDemandCoefficient() {
        return demandCoefficient;
    }

    public void setDemandCoefficient(BigDecimal demandCoefficient) {
        this.demandCoefficient = demandCoefficient;
    }

    @Override
    public String toString() {
        return "Location: " + locationName + "\nLatitude: " + latitude + "\nLongitude: " + longitude + "\nDemand:" + demandCoefficient;
    }

    public int getDistance(Location l) {
        // 地球半径
        double r = 6371;

        double lat1 = Double.parseDouble(this.getLatitude());
        double lat2 = Double.parseDouble(l.getLatitude());

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(Double.parseDouble(l.getLongitude()) - Double.parseDouble(this.getLongitude()));

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return new BigDecimal(Double.toString(r * c)).setScale(0, RoundingMode.HALF_UP).intValue();
    }
}
