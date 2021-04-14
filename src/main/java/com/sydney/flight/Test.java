package com.sydney.flight;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
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


    static Stack<Flight> lines = new Stack<>();

    public static void main(String[] args) {


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


        Flight fab1 = new Flight(0, la, lb, DayOfWeek.MONDAY, LocalTime.of(8, 0), 10, BigDecimal.TEN, 0);
        Flight fab2 = new Flight(1, la, lb, DayOfWeek.MONDAY, LocalTime.of(10, 0), 10, BigDecimal.TEN, 0);
        Flight fbc = new Flight(2, lb, lc, DayOfWeek.MONDAY, LocalTime.of(8, 0), 10, BigDecimal.TEN, 0);
        Flight fbd = new Flight(3, lb, ld, DayOfWeek.MONDAY, LocalTime.of(8, 0), 10, BigDecimal.TEN, 0);
        Flight fde1 = new Flight(4, ld, le, DayOfWeek.MONDAY, LocalTime.of(18, 0), 10, BigDecimal.TEN, 0);
        Flight fde2 = new Flight(5, ld, le, DayOfWeek.MONDAY, LocalTime.of(14, 0), 10, BigDecimal.TEN, 0);
        Flight fef = new Flight(6, le, lf, DayOfWeek.MONDAY, LocalTime.of(8, 0), 10, BigDecimal.TEN, 0);

        List<Flight> fs = new ArrayList<>();
        fs.add(fab1);
        fs.add(fab2);
        fs.add(fbc);
        fs.add(fbd);
        fs.add(fde1);
        fs.add(fde2);
        fs.add(fef);


        Map<String, List<Flight>> fromToMap = fs.stream()
                .collect(Collectors.groupingBy(f -> f.getSourceLocation().getLocationName()));
    }

    public static void search(String from, String to, Map<String, List<Flight>> fromToMap) {



    }

}
