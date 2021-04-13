package com.sydney.flight;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import java.util.*;

/**
 * @Author: xianchao.han
 * @Date: 2021/4/12 10:55
 */

public class Demo {

    public static void main(String[] args) {
        //instruct
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Berlin", "52.5", "13.15"));
        locations.add(new Location("NewYork", "52.5", "13.15"));
        locations.add(new Location("Perth", "52.5", "13.15"));
        locations.add(new Location("bejing", "52.5", "13.15"));
        locations.add(new Location("shanghai", "52.5", "13.15"));
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(0 + "", 0 + "", 1 + ""));
        flights.add(new Flight(1 + "", 1 + "", 2 + ""));
        flights.add(new Flight(2 + "", 2 + "", 3 + ""));
        flights.add(new Flight(3 + "", 3 + "", 4 + ""));

        flights.add(new Flight(4 + "", 0 + "", 5 + ""));
        flights.add(new Flight(5 + "", 5 + "", 4 + ""));

        String fromId = 0 + "";
        String endId = 4 + "";
        List<Flight> flights1 = new ArrayList<>();
//        flight add sunday 20:00 Berlin NewYork 250
        Map<String, List<Flight>> map = new HashMap<>();

        for (Flight flight : flights) {
            String to = flight.getTo();
            if (to.equals(endId)) {
                List<Flight> flightGroup = new ArrayList<>();
                flights1.add(flight);
                flightGroup.add(flight);
                map.put(flight.getId(), flightGroup);
            }
        }
        getList(flights, flights1, fromId, map);
        System.out.println(map);

//        Map<String, Object> resMap = new HashMap<>();
//        Map<String, Object> instructMap = new HashMap<String, Object>() {{
//            put("add", resMap);
//            put("get", resMap);
//            put("delete", resMap);
//            put("all", resMap);
//        }};
//
//        Scanner input = new Scanner(System.in);
//        while (true) {
//            String next = input.nextLine();
//            Object result = "";
//            String[] s = next.split(" ");
//            if (instructMap.containsKey(s[0])) {
//                Map<String, Object> o = (Map<String, Object>) instructMap.get(s[0]);
//                if ("add".equals(s[0])) {
//                    o.put(s[1], s[2]);
//                    result = "success";
//                } else if ("get".equals(s[0])) {
//                    result = o.get(s[1]);
//                } else if ("delete".equals(s[0])) {
//                    o.remove(s[1]);
//                    result = "success";
//                } else if ("all".equals(s[0])) {
//                    result = o;
//                }
//            }
//            System.out.println(result);
//        }
    }

    /**
     * @param flights  所有的航班信息
     * @param flights1
     */
    private static void getList(List<Flight> flights, List<Flight> flights1, String fromId, Map<String, List<Flight>> map) {
        String endId;
        List<Flight> flights2 = new ArrayList<>();
        for (Flight flightNew : flights1) {
            endId = flightNew.getFrom();
            for (Flight flight : flights) {
                String to = flight.getTo();
                if (to.equals(endId)) {
                    List<Flight> flights3 = map.get(flightNew.getId());
                    flights2.add(flight);
                    flights3.add(flight);
                    map.put(flight.getId(), flights3);
                    map.remove(flightNew.getId());
                }
            }
        }
        if (!CollectionUtils.isEmpty(flights2)) {
            getList(flights, flights2, fromId, map);
        }
    }
}

@Data
@AllArgsConstructor
class Flight {
    private String id;
    //    private String week;
//    private String time;
    private String from;
    private String to;
//    private String place;

}
@Data
class Location {
    private Long id;
    private String name;
    private String jin;
    private String wei;

    public Location(String name, String jin, String wei) {
        this.name = name;
        this.jin = jin;
        this.wei = wei;
    }
}