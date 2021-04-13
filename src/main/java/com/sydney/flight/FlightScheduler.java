package com.sydney.flight;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);;
        String command;
        while (true) {
            System.out.println("Input Command:");
            command = scanner.nextLine();
            if ("exit".equals(command.trim())) {
                break;
            }

            // flight addadb dfdasfdsf
            System.out.println("User Command :" + command);
            String[] commands = command.split(" ");
            if (command.length() == 0) {
                System.out.println("Command Is Empty!");
            }
            else {
                runCommand(commands);
            }
        }
    }

    private static void runCommand(String[] commands) {
        StringBuilder cmdBuilder = new StringBuilder();
        for (String command : commands) {
            command = command.trim();
            cmdBuilder.append(command).append(" ");
        }
        String userCommand = cmdBuilder.toString().trim().toLowerCase();
        System.out.println("userCommand : " + userCommand);
        if (userCommand.startsWith("flight")) {
            if (commands[0].equals("flights")) {
                // 处理 flights
                if (commands.length > 1) {
                    System.out.println("command is incorrect");
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
                                DayOfWeek dayOfWeek = getDayOfWeek(commands[2]);
                                if (null == dayOfWeek) {
                                    System.out.println("Command dayOfWeek is incorrect!");
                                    return;
                                }

                                String time = commands[3];
                                if (locations.stream().map(Location::getLocationName).noneMatch(n -> n.equals(commands[4]))) {
                                    System.out.println("Invalid starting location");
                                    return;
                                }
                                // Location startLocation = locations.stream().filter(l -> l.getLocationName().equals(commands[4]))
                                if (locations.stream().map(Location::getLocationName).noneMatch(n -> n.equals(commands[5]))) {
                                    System.out.println("Invalid ending location");
                                    return;
                                }

                                if (isInteger(commands[6])) {
                                    if (Integer.parseInt(commands[6]) < 0) {
                                        System.out.println("Invalid positive integer capacity");
                                        return;
                                    }
                                } else {
                                    System.out.println("capacity is not an integer");
                                    return;
                                }

                                Flight flight = new Flight();
                                flight.setFlightId(flights.stream().map(Flight::getFlightId).max(Integer::compareTo).orElse(-1) + 1);
                                flight.setDepartureDay(dayOfWeek);

                                flight.setDepartureTime(LocalTime.of(Integer.parseInt(time.split(":")[0]), Integer.parseInt(time.split(":")[1])));
                                flight.setCapacity(Integer.parseInt(commands[6]));
                                flight.setBookedNum(0);
                                flights.add(flight);

                                System.out.println("flights : " + flights);

                            } else {
                                System.out.println("arguments is not enough");
                            }
                        } else if (s2.equals("import")) {
                            // flight import
                        } else if (s2.equals("export")) {
                            // flight export
                        }
                    } else {
                        // 处理 flight id remove/reset
                        // flight id remove
                        if (commands.length > 3) {
                            System.out.println("Invalid User Command");
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
                    System.out.println("command is incorrect");
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
                    String s2 = commands[1];
                    if (Arrays.asList("add", "import", "export").contains(s2)) {
                        if ("add".equals(s2)) {
                            if (commands.length == 6) {
                                // location add
                                String locationName = commands[2];
                                boolean isMatch = locations.stream().anyMatch(l -> l.getLocationName().equalsIgnoreCase(locationName));
                                if (isMatch) {
                                    System.out.println(" This location already exists ");
                                    return;
                                }
                                String lat = commands[3];
                                if (!isLegalLatitude(lat)) {
                                    System.out.println("Invalid latitude. It must be a number of degrees between -85 and +85.");
                                    return;
                                }

                                String lon = commands[4];
                                if (!isLegalLongitude(lon)) {
                                    System.out.println("Invalid longitude. It must be a number of degrees between -180 and +180.");
                                    return;
                                }
                                String dc = commands[5];
                                if (!isLegalDemandCoefficient(dc)) {
                                    System.out.println("Invalid demand coefficient. It must be a number between -1 and +1.");
                                    return;
                                }
                                Location location = new Location();
                                location.setLocationName(locationName);
                                location.setLatitude(lat);
                                location.setLongitude(lon);
                                location.setDemandCoefficient(new BigDecimal(dc));
                                locations.add(location);
                                System.out.println("Successfully added location " + locationName);
                            } else {
                                System.out.println("Invalid Location add Arguments.");
                            }
                        } else if ("import".equals(s2)) {
                            // location import
                        } else if ("export".equals(s2)) {
                            // import export
                        }
                    } else {
                        if (commands.length > 2) {
                            System.out.println("Invalid Location Command!");
                        } else {
                            Location location = locations.stream().filter(l -> l.getLocationName().equalsIgnoreCase(s2)).findFirst().orElse(null);
                            System.out.println(location);
                        }

                    }
                }
            }

        } else if (userCommand.startsWith("schedule")) {

        } else if (userCommand.startsWith("departures")) {

        } else if (userCommand.startsWith("arrivals")) {

        } else if (userCommand.startsWith("travel")) {

        } else if (userCommand.startsWith("help")) {

        } else {
            System.out.println("YOUR COMMAND IS INVALID!");
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
            return d.compareTo(new BigDecimal("+180")) <= 0 && d.compareTo(new BigDecimal("-180")) >= 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isLegalLongitude(String longitude){
        try {
            Double.valueOf(longitude);
            BigDecimal d = new BigDecimal(longitude);
            return d.compareTo(new BigDecimal("+85")) <= 0 && d.compareTo(new BigDecimal("-85")) >= 0;
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
        for (Flight f : flights) {
            System.out.println(f.getFlightId() + " " + f.getDepartureDay() + " " + f.getDepartureTime() + " "
                    + f.getSourceLocation() + " --> " + f.getDestinationLocation());
        }
    }

    public static void printLocations(List<Location> locations) {
        System.out.println("Locations (" + locations.size() + "):");
        System.out.println(locations.stream().map(Location::getLocationName).sorted().collect(Collectors.joining(",")));
    }


}
