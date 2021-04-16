package com.sydney.flight;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * FlightScheduler
 *
 * @author zhibin.wang
 * @since 2021/04/12 13:34
 */
public class FlightScheduler {

    /**
     * locations
     */
    private static List<Location> locations = new ArrayList<>();

    /**
     * flights
     */
    private static List<Flight> flights = new ArrayList<>();

    /**
     * 无效命令提示
     */
    private final static String INVALID_COMMAND = "Invalid command. Type 'help' for a list of commands.";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);;
        String command;
        while (true) {
            System.out.print("User: ");
            command = scanner.nextLine();
            if ("exit".equalsIgnoreCase(command.trim())) {
                break;
            }
            command = command.replace("\t", " ");
            System.out.println("User Command :" + command);
            String[] commands = command.split(" ");
            if (command.length() == 0) {
                System.out.println(INVALID_COMMAND);
            }
            else {
                runCommand(commands);
            }
        }
    }

    /**
     * 运行用户输入的命令
     * @param cmd cmd
     */
    private static void runCommand(String[] cmd) {
        StringBuilder cmdBuilder = new StringBuilder();
        StringBuilder sourceBuilder = new StringBuilder();
        for (String command : cmd) {
            command = command.trim();
            if (command.equals("")) {
                continue;
            }
            cmdBuilder.append(command).append(" ");
            sourceBuilder.append(command).append(" ");
        }

        String userCommand = cmdBuilder.toString().trim().toLowerCase();
        String sourceCommand = sourceBuilder.toString().trim();
        String[] sourceCommands = sourceCommand.split(" ");
        String[] commands = userCommand.split(" ");
        if (userCommand.startsWith("flight")) {
            if (commands[0].equals("flights")) {
                // 处理 flights
                if (commands.length > 1) {
                    System.out.println(INVALID_COMMAND);
                }
                else {
                   printFlights(flights);
                }
            }
            else {
                if (commands.length == 1) {
                    System.out.println("Invalid Flight ID");
                } else {
                    // 处理 flight add/import/export
                    String s2 = commands[1];
                    if (Arrays.asList("add", "import", "export").contains(s2)) {
                        if (s2.equals("add")) {
                            if (commands.length == 7) {
                                String startingLocationName = sourceCommands[4];
                                String endingLocationName = sourceCommands[5];
                                String day = commands[2];
                                String time = commands[3];
                                String dc = commands[6];
                                addFlight(startingLocationName, endingLocationName, day, time, dc, "0");
                            } else {
                                System.out.println("Usage: FLIGHT ADD <departure time> <from> <to> <capacity>");
                            }
                        } else if (s2.equals("import")) {
                            // flight import
                            // location import
                            if (commands.length == 3) {
                                String fileName = sourceCommands[2];
                                List<String[]> lines = readerCsv(fileName);
                                if (null != lines && lines.size() > 0) {
                                    importFlights(lines);
                                }
                            } else {
                                System.out.println("Invalid Flight import Arguments.");
                            }
                        } else if (s2.equals("export")) {
                            // flight export
                        }
                    } else {
                        // 处理 flight id remove/reset
                        // flight id remove
                        if (commands.length > 3) {
                            System.out.println(INVALID_COMMAND);
                        } else {
                            if (isInteger(commands[1])) {
                                if (commands.length == 2) {
                                    // search flight by id
                                    Flight flight = flights.stream().filter(f -> f.getFlightId().equals(Integer.parseInt(commands[1]))).findFirst().orElse(null);
                                    System.out.println("flight : " + flight);
                                } else {
                                    if (commands[2].equals("remove")) {
                                        flights.removeIf(f -> f.getFlightId().equals(Integer.parseInt(commands[1])));
                                        System.out.println("flights : " + flights);
                                    } else if (commands[2].equals("reset")) {
                                        flights.forEach(f -> {
                                            if (f.getFlightId().equals(Integer.parseInt(commands[1]))) {
                                                f.setBookedNum(0);
                                                f.setTicketPrice(BigDecimal.ZERO);

                                                System.out.println("flight is reset");
                                            }
                                        });
                                    } else {
                                        System.out.println("command is incorrect!");
                                    }
                                }
                            } else {
                                System.out.println("Invalid Command");
                            }
                        }
                    }
                }

            }
        } else if (userCommand.startsWith("location")) {
            // locations
            // location add
            // location name
            // location export、import
            if ("locations".equals(commands[0])) {
                // 处理 locations
                if (commands.length > 1) {
                    System.out.println("Invalid Command");
                }
                else {
                    printLocations(locations);
                }
            } else {
                if (commands.length == 1) {
                    System.out.println("Usage:\\nLOCATION <name>\\nLOCATION ADD \n" +
                            "<name> <latitude> <longitude> \n" +
                            "<demand_coefficient>\\nLOCATION \n" +
                            "IMPORT/EXPORT <filename>");
                } else {
                    String s2 = sourceCommands[1];
                    if (Arrays.asList("add", "import", "export").contains(s2)) {
                        if ("add".equals(s2)) {
                            if (commands.length == 6) {
                                // location add
                                String locationName = sourceCommands[2];
                                String lat = commands[3];
                                String lon = commands[4];
                                String dc = commands[5];
                                addLocation(locationName, lat, lon, dc);
                            } else {
                                System.out.println("Invalid Location add Arguments.");
                            }
                        } else if ("import".equals(s2)) {
                            // location import
                            if (commands.length == 3) {
                                String fileName = sourceCommands[2];
                                List<String[]> lines = readerCsv(fileName);
                                if (null != lines && lines.size() > 0) {
                                    importLocation(lines);
                                }
                            } else {
                                System.out.println("Invalid Location import Arguments.");
                            }
                        } else if ("export".equals(s2)) {
                            // import export
                        }
                    } else {
                        if (commands.length > 2) {
                            System.out.println("Invalid Command!");
                        } else {
                            Location location = locations.stream().filter(l -> l.getLocationName().equals(s2)).findFirst().orElse(null);
                            if (null == location) {
                                System.out.println("Invalid location name.");
                            } else {
                                System.out.println(location);
                            }
                        }

                    }
                }
            }

        } else if (userCommand.startsWith("schedule")) {

        } else if (userCommand.startsWith("departures")) {

        } else if (userCommand.startsWith("arrivals")) {

        } else if (userCommand.startsWith("travel")) {
            if (commands.length == 1) {
                if (userCommand.equals("travel")) {
                    System.out.println("Usage: TRAVEL <from> <to> [cost/duration/stopovers/layover/flight_time]");
                } else {
                    System.out.println(INVALID_COMMAND);
                }
            } else if (commands.length > 6 || commands.length < 4) {
                System.out.println(INVALID_COMMAND);
            } else {
                String from = sourceCommands[1];
                if (!isLocationFound(from)) {
                    System.out.println("Starting location not found.");
                    return;
                }
                String to = sourceCommands[2];
                if (!isLocationFound(to)) {
                    System.out.println("Ending location not found.");
                    return;
                }
                String sort;
                if (commands.length == 4) {
                    // 默认排序
                    sort = "duration";
                }
                else {
                    sort = commands[4];
                    if (!Arrays.asList("cost", "duration", "stopovers", "layover", "flight_time").contains(sort)) {
                        System.out.println("Invalid sorting property: must be either cost, duration, stopovers, layover, or flight_time.");
                        return;
                    }
                }

                searchFlights(from, to, sort);
            }
        } else if (userCommand.startsWith("help")) {
            // 打印帮助信息
        } else {
            System.out.println("");
        }
    }

    private static boolean addFlight(String startingLocationName, String endingLocationName,
                                     String day, String time, String capacity, String bookedNum) {
        DayOfWeek departureDay = getDayOfWeek(day);
        if (null == departureDay) {
            System.out.println("Invalid departure time. Use the format <day_of_week> <hour:minute>, with 24h time.");
            return false;
        }
        // checktime
        String[] times = time.split(":");
        LocalTime departureTime;
        if (times.length != 2) {
            System.out.println("Invalid departure time. Use the format <day_of_week> <hour:minute>, with 24h time.");
            return false;
        } else {
            if (!isValidTime(times)) {
                System.out.println("Invalid departure time. Use the format <day_of_week> <hour:minute>, with 24h time.");
                return false;
            } else {
                departureTime = LocalTime.of(Integer.parseInt(times[0]), Integer.parseInt(times[1]));
            }
        }

        // String startingLocationName = sourceCommands[4];
        if (!locations.isEmpty() && locations.stream().map(Location::getLocationName).noneMatch(n -> n.equals(startingLocationName))) {
            System.out.println("Invalid starting location." + startingLocationName);
            return false;
        }

        // String endingLocationName = sourceCommands[5];
        if (!locations.isEmpty() && locations.stream().map(Location::getLocationName).noneMatch(n -> n.equals(endingLocationName))) {
            System.out.println("Invalid ending location." + endingLocationName);
            return false;
        }

        if (startingLocationName.equals(endingLocationName)) {
            System.out.println("Source and destination cannot be the same place.");
            return false;
        }

        Location startingLocation = locations.stream().filter(l -> l.getLocationName().equals(startingLocationName)).findFirst().orElse(null);
        Location endingLocation = locations.stream().filter(l -> l.getLocationName().equals(endingLocationName)).findFirst().orElse(null);

        int distance = startingLocation.getDistance(endingLocation);

        // 计算到达时间
        int hours = distance / 720;
        int minutes = distance % 720 / 12;

        DayOfWeek arrivalDay = departureDay.plus(hours / 24);
        LocalTime arrivalTime = departureTime.plusHours(hours % 24).plusMinutes(minutes);

        if (arrivalTime.isBefore(departureTime)) {
            arrivalDay = arrivalDay.plus(1);
        }

        final DayOfWeek finalArrivalDay = arrivalDay;
        // 校验是否有航班冲突
        // 起飞冲突
        boolean isDepartureConflicting = flights.stream()
                .anyMatch(f -> f.getSourceLocation().getLocationName().equals(startingLocationName)
                        && f.getDepartureDay().compareTo(departureDay) == 0
                        && isTimeConflict(f.getDepartureTime(), departureTime));
        // 起飞冲突
        if (isDepartureConflicting) {
            List<Flight> conflictingFlights = flights.stream()
                    .filter(f -> f.getSourceLocation().getLocationName().equals(startingLocationName))
                    .filter(f -> f.getDepartureDay().compareTo(departureDay) == 0)
                    .filter(f -> isTimeConflict(f.getDepartureTime(), departureTime))
                    .filter(f -> f.getDepartureTime().isAfter(departureTime))
                    .collect(Collectors.toList());
            if (conflictingFlights.isEmpty()) {
                conflictingFlights = flights.stream()
                        .filter(f -> f.getSourceLocation().getLocationName().equals(startingLocationName))
                        .filter(f -> f.getDepartureDay().compareTo(departureDay) == 0)
                        .filter(f -> isTimeConflict(f.getDepartureTime(), departureTime))
                        .filter(f -> f.getDepartureTime().isBefore(departureTime))
                        .collect(Collectors.toList());
            }

            System.out.println("Scheduling conflict! This flight clashes with Flight " + conflictingFlights.get(0).getFlightId()
                    + " departing from " + conflictingFlights.get(0).getSourceLocation().getLocationName()
                    + " on " + conflictingFlights.get(0).getDepartureDay() + " " + conflictingFlights.get(0).getDepartureTime());
            return false;
        }

        // 到达冲突
        boolean isArrivalConflicting = flights.stream()
                .anyMatch(f -> f.getDestinationLocation().getLocationName().equals(endingLocationName)
                        && f.getDepartureDay().compareTo(finalArrivalDay) == 0
                        && isTimeConflict(f.getDepartureTime(), arrivalTime));
        // 到达冲突
        if (isArrivalConflicting) {
            List<Flight> conflictingFlights = flights.stream()
                    .filter(f -> f.getDestinationLocation().getLocationName().equals(endingLocationName))
                    .filter(f -> f.getDepartureDay().compareTo(finalArrivalDay) == 0)
                    .filter(f -> isTimeConflict(f.getDepartureTime(), arrivalTime))
                    .filter(f -> f.getDepartureTime().isAfter(arrivalTime))
                    .collect(Collectors.toList());
            if (conflictingFlights.isEmpty()) {
                conflictingFlights = flights.stream()
                        .filter(f -> f.getDestinationLocation().getLocationName().equals(endingLocationName))
                        .filter(f -> f.getDepartureDay().compareTo(finalArrivalDay) == 0)
                        .filter(f -> isTimeConflict(f.getDepartureTime(), arrivalTime))
                        .filter(f -> f.getDepartureTime().isBefore(arrivalTime))
                        .collect(Collectors.toList());
            }

            System.out.println("Scheduling conflict! This flight clashes with Flight " + conflictingFlights.get(0).getFlightId()
                    + " arriving at " + conflictingFlights.get(0).getSourceLocation().getLocationName()
                    + " on " + conflictingFlights.get(0).getDepartureDay() + " " + conflictingFlights.get(0).getDepartureTime());
            return false;
        }


        if (isInteger(capacity)) {
            if (Integer.parseInt(capacity) < 0) {
                System.out.println("Invalid positive integer capacity.");
                return false;
            }
        } else {
            System.out.println("Invalid integer capacity.");
            return false;
        }

        int flightId = flights.stream().map(Flight::getFlightId).max(Integer::compareTo).orElse(-1) + 1;
        Flight flight = new Flight();
        flight.setFlightId(flightId);
        flight.setDepartureDay(departureDay);
        flight.setDepartureTime(departureTime);
        flight.setSourceLocation(startingLocation);
        flight.setDestinationLocation(endingLocation);
        flight.setDuration(hours * 60 + minutes);
        flight.setArrivalDay(arrivalDay);
        flight.setArrivalTime(arrivalTime);
        flight.setDistance(distance);
        flight.setCapacity(Integer.parseInt(capacity));
        flight.setBookedNum(0);
        flight.setTicketPrice(getTicketPrice(flight, Integer.parseInt(bookedNum)));
        flights.add(flight);

        System.out.println("Successfully added Flight " + flightId + ".");
        return true;
    }

    /**
     * 判定两个时间是否冲突，时间间隔是否在60分钟内
     * @param lt1 lt1
     * @param lt2 lt2
     * @return boolean
     */
    private static boolean isTimeConflict(LocalTime lt1, LocalTime lt2){
        long l = ChronoUnit.MINUTES.between(lt1, lt2);
        l = l > 0 ? l : -l;
        return l < 60;
    }



    private static void importFlights(List<String[]> lines) {
        boolean success;
        int successSize = 0;
        int failureSize = 0;
        for (String[] line : lines) {
            String day = line[0].split(" ")[0];
            String time = line[0].split(" ")[1];
            success = addFlight(line[1], line[2], day, time, line[3], line[4]);
            if (success) {
                successSize++;
            } else {
                failureSize++;
            }
        }

        System.out.println("Imported " + successSize + " flights.");
        if (failureSize == 1) {
            System.out.println("1 line was invalid.");
        } else if (failureSize > 1) {
            System.out.println(failureSize + " lines were invalid.");
        }

    }

    private static void importLocation(List<String[]> lines) {
        boolean success;
        int successSize = 0;
        int failureSize = 0;
        for (String[] line : lines) {
            success = addLocation(line[0], line[1], line[2], line[3]);
            if (success) {
                successSize++;
            } else {
                failureSize++;
            }
        }

        System.out.println("Imported " + successSize + " locations.");
        if (failureSize == 1) {
            System.out.println("1 line was invalid.");
        } else if (failureSize > 1) {
            System.out.println(failureSize + " lines were invalid.");
        }
    }

    private static List<String[]> readerCsv(String fileName) {
        List<String[]> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
            String line = null;
            while((line = br.readLine()) != null){
                result.add(line.split(","));
            }

            return result;
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        return null;
    }


    private static boolean addLocation(String locationName, String lat, String lon, String dc) {
        boolean isMatch = locations.stream().anyMatch(l -> l.getLocationName().equals(locationName));
        if (isMatch) {
            System.out.println(" This location already exists ");
            return false;
        }

        if (!isLegalLatitude(lat)) {
            System.out.println("Invalid latitude. It must be a number of degrees between -85 and +85.");
            return false;
        }


        if (!isLegalLongitude(lon)) {
            System.out.println("Invalid longitude. It must be a number of degrees between -180 and +180.");
            return false;
        }

        if (!isLegalDemandCoefficient(dc)) {
            System.out.println("Invalid demand coefficient. It must be a number between -1 and +1.");
            return false;
        }

        Location location = new Location();
        location.setLocationName(locationName);
        location.setLatitude(lat);
        location.setLongitude(lon);
        location.setDemandCoefficient(new BigDecimal(dc));
        locations.add(location);
        System.out.println("Successfully added location " + locationName);
        return true;
    }

    private static boolean isValidTime(String[] times) {
        try {
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);

            if (hour < 0 || hour > 24) {
                return false;
            }

            return minute >= 0 && minute <= 59;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static DayOfWeek getDayOfWeek(String dayOfWeek) {
        try {
            return DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean isLegalLatitude(String latitude){
        try {
            Double.valueOf(latitude);

            BigDecimal d = new BigDecimal(latitude);
            return d.compareTo(new BigDecimal("85")) <= 0 && d.compareTo(new BigDecimal("-85")) >= 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isLegalLongitude(String longitude){
        try {
            Double.valueOf(longitude);
            BigDecimal d = new BigDecimal(longitude);
            return d.compareTo(new BigDecimal("180")) <= 0 && d.compareTo(new BigDecimal("-180")) >= 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isLegalDemandCoefficient(String dc){
        try {
            Double.valueOf(dc);
            BigDecimal d = new BigDecimal(dc);
            return d.compareTo(new BigDecimal("+1")) <= 0 && d.compareTo(new BigDecimal("-1")) >= 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void printFlights(List<Flight> flights) {
        System.out.println("Flights:");
        System.out.println("-------------------------------------------------------");
        System.out.println("ID Departure Arrival Source --> Destination");
        System.out.println("-------------------------------------------------------");

        // 排序输出
        for (Flight f : flights) {
            System.out.println(f.getFlightId() + " " + f.getDepartureDay() + " " + f.getDepartureTime() + " "
                    + f.getSourceLocation().getLocationName() + " --> " + f.getDestinationLocation().getLocationName());
        }
    }

    /**
     * 打印Locations
     * @param locations locations
     */
    public static void printLocations(List<Location> locations) {
        System.out.println("Locations (" + locations.size() + "):");
        System.out.println(locations.stream().map(Location::getLocationName).sorted().collect(Collectors.joining(",")));
    }

    /**
     * 计算票价
     * @param flight 航班
     * @param bookNum 订票数量
     * @return BigDecimal
     */
    public static BigDecimal getTicketPrice(Flight flight, int bookNum) {
        return new BigDecimal(Integer.toString(flight.getDistance()))
                .multiply(new BigDecimal("30")
                        .add(new BigDecimal("4")
                                .multiply(flight.getDestinationLocation().getDemandCoefficient()
                                        .subtract(flight.getSourceLocation().getDemandCoefficient()))))
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }


    public static boolean isLocationFound(String locationName) {
        return locations.stream().anyMatch(l -> l.getLocationName().equals(locationName));
    }

    /**
     * 查询航班
     * @param from 出发地
     * @param to 目的地
     * @param sort sort
     */
    public static void searchFlights(String from, String to, String sort) {
        Map<String, List<Flight>> fromToMap = flights.stream()
                .collect(Collectors.groupingBy(f -> f.getSourceLocation().getLocationName()));

        if (!fromToMap.containsKey(from)) {
            System.out.println("Sorry, no flights with 3 or less stopovers are available from " + from + " to " + to + ".");
        }
        List<List<Flight>> lines = new ArrayList<>();
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

                    if (stopover1.getDestinationLocation().getLocationName().equals(from)) {
                        continue;
                    }

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

                            if (stopover2.getDestinationLocation().getLocationName().equals(stopover1.getSourceLocation().getLocationName())) {
                                continue;
                            }

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

        printTravelFlights(from, to, lines, sort);
    }

    /**
     * 排序打印航班
     * @param from from
     * @param to to
     * @param lines 航班线路
     * @param sort 排序方式
     */
    private static void printTravelFlights(String from, String to, List<List<Flight>> lines, String sort) {

        if (lines.isEmpty()) {
            System.out.println("Sorry, no flights with 3 or less stopovers are available from " + from + " to " + to + ".");
            return;
        }

        // cost > duration
        // duration > cost
        // stopovers > duration
        // layover > duration > cost
        // flight_time > duration > cost

        List<List<Flight>> orderedFlights = null;
        if ("cost".equals(sort)) {
            orderedFlights = lines.stream()
                    .sorted(getComparator("cost").thenComparing(getComparator("duration")))
                    .collect(Collectors.toList());
        } else if ("duration".equals(sort)) {
            orderedFlights = lines.stream()
                    .sorted(getComparator("duration").thenComparing(getComparator("cost")))
                    .collect(Collectors.toList());
        } else if ("stopovers".equals(sort)) {
            orderedFlights = lines.stream()
                    .sorted(getComparator("stopovers").thenComparing(getComparator("duration")))
                    .collect(Collectors.toList());
        } else if ("layover".equals(sort)) {
            orderedFlights = lines.stream()
                    .sorted(getComparator("layover")
                            .thenComparing(getComparator("duration"))
                            .thenComparing(getComparator("cost")))
                    .collect(Collectors.toList());
        } else {
            orderedFlights = lines.stream()
                    .sorted(getComparator("flight_time")
                            .thenComparing(getComparator("duration"))
                            .thenComparing(getComparator("cost")))
                    .collect(Collectors.toList());
        }

        for (List<Flight> flights : orderedFlights) {
            System.out.println("Legs: " + (flights.size() - 1));

            int totalDuration = getTotalDuration(flights);
            System.out.println("Total Duration: " + (totalDuration / 60) + "h " + (totalDuration % 60) + "m");
            System.out.println("Total Cost: $" + getTotalCost(flights));

            System.out.println("-------------------------------------------------------------");
            System.out.println("ID Cost Departure Arrival Source --> Destination");
            System.out.println("-------------------------------------------------------------");

            for (Flight flight : flights) {
                System.out.println(flight.getFlightId() + " $" + flight.getTicketPrice() + " " + flight.getArrivalDay()
                        + " " + flight.getArrivalTime() + " " + flight.getSourceLocation().getLocationName() + "-->"
                        + flight.getDestinationLocation().getLocationName());
            }
        }
    }

    /**
     * 构建排序器
     * @param sort sort
     * @return Comparator
     */
    public static Comparator<List<Flight>> getComparator(String sort){
        Comparator<List<Flight>> comparator = (flights1, flights2) -> {
            if ("cost".equals(sort)) {
                // 机票价格
                BigDecimal flight1sCost = getTotalCost(flights1);
                BigDecimal flight2sCost = getTotalCost(flights2);
                return flight1sCost.compareTo(flight2sCost);
            } else if ("duration".equals(sort)) {
                // 起始站起飞时间 - 终点站降落时间
                Integer flight1Duration = getTotalDuration(flights1);
                Integer flight2Duration = getTotalDuration(flights2);
                return flight1Duration.compareTo(flight2Duration);
            } else if ("stopovers".equals(sort)) {
                // 中转站
                Integer stopovers1Size = flights1.size();
                Integer stopovers2Size = flights2.size();
                return stopovers1Size.compareTo(stopovers2Size);
            } else if ("layover".equals(sort)) {
                // 停留时间
                Integer layoverTime1 = getTotalLayoverDuration(flights1);
                Integer layoverTime2 = getTotalLayoverDuration(flights2);
                return layoverTime1.compareTo(layoverTime2);
            } else {
                // flight_time
                // 飞行时间
                Integer flightTime1 = getTotalFlightTime(flights1);
                Integer flightTime2 = getTotalFlightTime(flights2);
                return flightTime1.compareTo(flightTime2);
            }
        };

        return comparator;
    }



    /**
     * 获取总的飞行时间
     * @param flights flights
     * @return BigDecimal
     */
    public static Integer getTotalFlightTime(List<Flight> flights) {
        return flights.stream()
                .map(Flight::getDuration)
                .reduce(0, Integer::sum);
    }

    /**
     * 获取总的机票费用
     * @param flights flights
     * @return BigDecimal
     */
    public static BigDecimal getTotalCost(List<Flight> flights) {
        return flights.stream()
                .map(Flight::getTicketPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 获取总的等待时间
     * @param flights flights
     * @return int
     */
    public static Integer getTotalLayoverDuration(List<Flight> flights) {

        if (flights.size() == 1) {
             return 0;
        }

        int layoverTime = 0;
        DayOfWeek departureDay;
        LocalTime departureTime;
        DayOfWeek arrivalDay;
        LocalTime arrivalTime;
        for (int i = 0; i < flights.size() - 1; i++) {
            Flight current = flights.get(i);
            Flight next = flights.get(i + 1);
            arrivalDay = current.getArrivalDay();
            arrivalTime = current.getArrivalTime();


            departureDay = next.getDepartureDay();
            departureTime = next.getArrivalTime();

            departureDay = departureDay.getValue() < arrivalDay.getValue() ? departureDay.plus(7) : departureDay;
            int days = departureDay.getValue() - arrivalDay.getValue();

            departureTime = departureTime.compareTo(arrivalTime) < 0 ? departureTime.plusMinutes(days * 24 * 60) : departureTime;
            long minutes = ChronoUnit.MINUTES.between(arrivalTime, departureTime);
            layoverTime += (int) (days * 24 + minutes);
        }

        return layoverTime;
    }

    /**
     * 获取总的持续时间
     * @param flights flights
     * @return int
     */
    public static int getTotalDuration(List<Flight> flights) {
        Flight start;
        Flight end;
        if (flights.size() == 1) {
             start = flights.get(0);
             end = flights.get(0);
        } else {
             start = flights.get(0);
             end = flights.get(flights.size() - 1);
        }

        DayOfWeek departureDay = start.getDepartureDay();
        LocalTime departureTime = start.getDepartureTime();

        DayOfWeek arrivalDay = end.getArrivalDay();
        LocalTime arrivalTime = end.getArrivalTime();

        int days = 0;
        if (flights.size() > 1) {
            // 1 - 7 6
            // 4 - 1 1
            arrivalDay = arrivalDay.getValue() < departureDay.getValue() ? arrivalDay.plus(7) : arrivalDay;
            days = arrivalDay.getValue() - departureDay.getValue();
        }

        // 08:00 - 14:00
        // 20:00 - 01:00
        arrivalTime = arrivalTime.compareTo(departureTime) < 0 ? arrivalTime.plusMinutes(days * 24 * 60) : arrivalTime;
        long minutes = ChronoUnit.MINUTES.between(arrivalTime, departureTime);

        return (int) (minutes > 0 ? minutes : -minutes);
    }



    public static boolean isMatch(Flight f, String to) {
        return f.getDestinationLocation().getLocationName().equals(to) && f.getBookedNum() < f.getCapacity();
    }

}
