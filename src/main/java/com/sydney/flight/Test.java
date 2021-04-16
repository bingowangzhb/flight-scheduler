package com.sydney.flight;


import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Test
 *
 * @author zhibin.wang
 * @since 2021/04/12 15:46
 */
public class Test {

    public static void main(String[] args) {
        LocalTime lt1 = LocalTime.of(4, 0);
        LocalTime lt2 = LocalTime.of(12, 0);

        long b = ChronoUnit.MINUTES.between(lt2, lt1);
        System.out.println("b = " + b);
        System.out.println("b = " + (b < 60));


        List<String> list = new ArrayList<>(Arrays.asList("ab", "abc", "abcd", "de", "ef"));

        List<String> ls = list.stream()
                .filter(s -> s.startsWith("a"))
                .filter(s -> s.length() > 3)
                .collect(Collectors.toList());
        System.out.println("ls = " + ls);
    }

    private static void s4() {
        LocalTime departureTime = LocalTime.of(22, 0);
        LocalTime arrivalTime = LocalTime.of(2, 0);

        Duration d1 = Duration.between(arrivalTime, departureTime);
        Duration d2 = Duration.between(departureTime, arrivalTime);
    }

    private static void s3() {
        DayOfWeek departureDay = DayOfWeek.SUNDAY;
        LocalTime departureTime = LocalTime.of(20, 0);

        System.out.println("departureDay = " + departureDay);
        System.out.println("departureTime = " + departureTime);
        // 2
        DayOfWeek arrivalDay = departureDay.plus(1);
        LocalTime arrivalTime = departureTime.plusHours(4);

        System.out.println("arrivalDay = " + arrivalDay);
        System.out.println("arrivalTime = " + arrivalTime);
        System.out.println("arrivalTime = " + arrivalTime.isAfter(departureTime));
    }

    private static void s2() {
        int distance = 3387;
        int hour = distance / 720;
        int m = distance % 720;


        int mins = (distance % 720) / 12;

        System.out.println("hour = " + hour);
        System.out.println("m = " + m);
        System.out.println("mins = " + mins);
    }

    private static void ss() {
        // 1. from to list
        // 从list中过滤出from的list2，然后从list中移除list2作为新的list

        // 遍历list2 去一个元素判断目的地是否等于to
        // 如果等于则存储该flight

        // 重复步骤1

        Location la = new Location("A", "100", "20", BigDecimal.ZERO);
        Location lb = new Location("B", "100", "20", BigDecimal.ZERO);
        Location lc = new Location("C", "100", "20", BigDecimal.ZERO);
        Location ld = new Location("D", "100", "20", BigDecimal.ZERO);
        Location le = new Location("E", "100", "20", BigDecimal.ZERO);
        Location lf = new Location("F", "100", "20", BigDecimal.ZERO);

        // A-B-C-E
        Flight fab1 = new Flight(0, la, lb, DayOfWeek.MONDAY, LocalTime.of(8, 0), 10, BigDecimal.TEN, 0);
        Flight fab2 = new Flight(1, la, lb, DayOfWeek.MONDAY, LocalTime.of(10, 0), 10, BigDecimal.TEN, 0);

        Flight fbc = new Flight(2, lb, lc, DayOfWeek.MONDAY, LocalTime.of(14, 0), 10, BigDecimal.TEN, 0);
        Flight fbd = new Flight(3, lb, ld, DayOfWeek.MONDAY, LocalTime.of(14, 0), 10, BigDecimal.TEN, 0);

        Flight fce = new Flight(7, lc, le, DayOfWeek.MONDAY, LocalTime.of(20, 0), 10, BigDecimal.TEN, 0);

        Flight fde1 = new Flight(4, ld, le, DayOfWeek.MONDAY, LocalTime.of(18, 0), 10, BigDecimal.TEN, 0);
        Flight fde2 = new Flight(5, ld, le, DayOfWeek.MONDAY, LocalTime.of(15, 0), 10, BigDecimal.TEN, 0);

        Flight fef = new Flight(6, le, lf, DayOfWeek.MONDAY, LocalTime.of(8, 0), 10, BigDecimal.TEN, 0);

        List<Flight> fs = new ArrayList<>();
        fs.add(fab1);
        fs.add(fab2);
        fs.add(fbc);
        fs.add(fbd);
        fs.add(fce);
        fs.add(fde1);
        fs.add(fde2);
        fs.add(fef);


        DayOfWeek dayOfWeek1 = DayOfWeek.MONDAY;
        DayOfWeek dayOfWeek2 = DayOfWeek.TUESDAY;
        DayOfWeek dayOfWeek3 = DayOfWeek.WEDNESDAY;
        DayOfWeek dayOfWeek4 = DayOfWeek.THURSDAY;
        DayOfWeek dayOfWeek5 = DayOfWeek.FRIDAY;
        DayOfWeek dayOfWeek6 = DayOfWeek.SATURDAY;
        DayOfWeek dayOfWeek7 = DayOfWeek.SUNDAY;

        System.out.println("dayOfWeek1.compareTo(dayOfWeek2) = " + dayOfWeek1.compareTo(dayOfWeek2));
        System.out.println("dayOfWeek1.compareTo(dayOfWeek7) = " + dayOfWeek1.compareTo(dayOfWeek7));
        System.out.println("dayOfWeek4.compareTo(dayOfWeek2) = " + dayOfWeek4.compareTo(dayOfWeek3));
        System.out.println("dayOfWeek4.compareTo(dayOfWeek4) = " + dayOfWeek4.compareTo(dayOfWeek4));


        LocalTime l1 = LocalTime.of(8, 0);
        LocalTime l2 = LocalTime.of(9, 0);
        LocalTime l3 = LocalTime.of(13, 0);
        LocalTime l4 = LocalTime.of(13, 10);


        System.out.println("l1.compareTo(l2) = " + l1.compareTo(l2));
        System.out.println("l3.compareTo(l4) = " + l3.compareTo(l4));


        List<List<Flight>> lines = search("A", "E", fs);
        System.out.println("lines.size() = " + lines.size());
        System.out.println("lines = " + lines);
    }

    public static List<List<Flight>> search(String from, String to, List<Flight> allFlights) {
        Map<String, List<Flight>> fromToMap = allFlights.stream()
                .collect(Collectors.groupingBy(f -> f.getSourceLocation().getLocationName()));

        if (!fromToMap.containsKey(from)) {
            return new ArrayList<>();
        }
        List<List<Flight>> lines = new ArrayList<>();
        // A
        for (Flight f0 : fromToMap.getOrDefault(from, new ArrayList<>(0))) {
            if (isMatch(f0, to)) {
                List<Flight> flights = new ArrayList<>();
                flights.add(f0);
                lines.add(flights);
            } else {
                List<Flight> f1s = fromToMap.getOrDefault(f0.getDestinationLocation().getLocationName(), new ArrayList<>(0));
                List<Flight> availableF1s = f1s.stream()
                        .filter(f -> f.isAfter(f0))
                        .filter(f -> f.getBookedNum() < f.getCapacity())
                        .collect(Collectors.toList());
                for (Flight stopover1 : availableF1s) {
                    if (isMatch(stopover1, to)) {
                        List<Flight> flights = new ArrayList<>();
                        flights.add(f0);
                        flights.add(stopover1);
                        lines.add(flights);
                    } else {
                        List<Flight> stopover1s = fromToMap.getOrDefault(stopover1.getDestinationLocation().getLocationName(), new ArrayList<>(0));
                        List<Flight> availableStopover1s = stopover1s.stream()
                                .filter(f -> f.isAfter(stopover1))
                                .filter(f -> f.getBookedNum() < f.getCapacity())
                                .collect(Collectors.toList());

                        for (Flight stopover2 : availableStopover1s) {
                            if (isMatch(stopover2, to)) {
                                List<Flight> flights = new ArrayList<>();
                                flights.add(f0);
                                flights.add(stopover1);
                                flights.add(stopover2);
                                lines.add(flights);
                            } else {
                                List<Flight> stopover2s = fromToMap.getOrDefault(stopover2.getDestinationLocation().getLocationName(), new ArrayList<>(0));
                                List<Flight> availableStopover2s = stopover2s.stream()
                                        .filter(f -> f.isAfter(stopover2))
                                        .filter(f -> f.getBookedNum() < f.getCapacity())
                                        .collect(Collectors.toList());
                                for (Flight stopover3 : availableStopover2s) {
                                    if (isMatch(stopover3, to)) {
                                        List<Flight> flights = new ArrayList<>();
                                        flights.add(f0);
                                        flights.add(stopover1);
                                        flights.add(stopover2);
                                        flights.add(stopover3);
                                        lines.add(flights);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return lines;
    }

    public static boolean isMatch(Flight f, String to) {
        return f.getDestinationLocation().getLocationName().equals(to) && f.getBookedNum() < f.getCapacity();
    }
}




