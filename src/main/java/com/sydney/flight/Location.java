package com.sydney.flight;

import java.math.BigDecimal;

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
}
