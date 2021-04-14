package com.sydney.flight;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Flight
 *
 * @author zhibin.wang
 * @since 2021/04/12 13:34
 */
public class Flight {

    private Integer flightId;
    private Location sourceLocation;
    private Location destinationLocation;
    private DayOfWeek departureDay;
    private LocalTime departureTime;
    private Integer capacity;
    private BigDecimal ticketPrice;
    private Integer bookedNum;


    public Flight() {
    }

    public Flight(Integer flightId, Location sourceLocation, Location destinationLocation, DayOfWeek departureDay,
                  LocalTime departureTime, Integer capacity, BigDecimal ticketPrice, Integer bookedNum) {
        this.flightId = flightId;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.departureDay = departureDay;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
        this.bookedNum = bookedNum;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Location getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(Location sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public DayOfWeek getDepartureDay() {
        return departureDay;
    }

    public void setDepartureDay(DayOfWeek departureDay) {
        this.departureDay = departureDay;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getBookedNum() {
        return bookedNum;
    }

    public void setBookedNum(Integer bookedNum) {
        this.bookedNum = bookedNum;
    }

    @Override
    public String toString() {
        return "Flight " + flightId +
                "\nDeparture: " + departureDay + " " + departureTime + " " + sourceLocation.getLocationName() +
                "\nArrival: " + destinationLocation.getLocationName() +
                "\nDistance: 0" +
                "\nDuration: 0" +
                "\nTicket Cost: " + ticketPrice +
                "\nPassengers: " + bookedNum + "/" +  capacity;
    }
}
